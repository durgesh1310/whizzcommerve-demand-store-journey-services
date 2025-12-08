package com.ouat.orderService.cancel.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ouat.orderService.cancel.dto.CancelReturnReasonDTO;
import com.ouat.orderService.cancel.dto.CancelReturnReasonRefundDTO;
import com.ouat.orderService.cancel.dto.OrderCancelReturnRecordDto;
import com.ouat.orderService.cancel.dto.OrderItemDTO;
import com.ouat.orderService.cancel.dto.RecordType;
import com.ouat.orderService.cancel.dto.RefundType;
import com.ouat.orderService.cancel.repository.OrderCancelRepository;
import com.ouat.orderService.cancel.request.CancelOrderRequest;
import com.ouat.orderService.client.DLTTemplateType;
import com.ouat.orderService.client.EmailAlertType;
import com.ouat.orderService.client.EmailNotificationServiceClient;
import com.ouat.orderService.client.EmailSMSServiceClient;
import com.ouat.orderService.client.InventoryServiceClient;
import com.ouat.orderService.client.MessageRequest;
import com.ouat.orderService.client.SkuAndQty;
import com.ouat.orderService.constants.CommonConstant;
import com.ouat.orderService.exception.BusinessProcessException;
import com.ouat.orderService.exchange.request.OrderExchangeRequest;
import com.ouat.orderService.response.CustomerDetailVO;
import com.ouat.orderService.response.DemandStoreAPIResponse;
import com.ouat.orderService.response.MessageDetail;
import com.ouat.orderService.response.MessageType;
import com.ouat.orderService.returnExchangeCancelAlert.service.ReturnCancelExchangeAlert;

@Service
public class OrderCancelService {
	
	@Autowired
	OrderCancelRepository orderCancelRepository;
	
	@Autowired
	EmailSMSServiceClient emailSMSServiceClient;

	@Autowired
	EmailNotificationServiceClient emailNotificationServiceCient;
	
	@Autowired 
	ReturnCancelExchangeAlert  returnCancelExchangeAlert;
	
	@Autowired
	InventoryServiceClient inventoryServiceClient;
	
	public ResponseEntity<DemandStoreAPIResponse> getReason(String type) throws BusinessProcessException {
		List<CancelReturnReasonDTO> cancelReturnReason = orderCancelRepository.getReason(type);
		if (cancelReturnReason.isEmpty()) {
			throw new BusinessProcessException(CommonConstant.NO_REASON_FOUND, CommonConstant.FAILURE_STATUS_CODE);
		}
		return ResponseEntity.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, cancelReturnReason));
	}
	@Transactional
	public ResponseEntity<DemandStoreAPIResponse> processItemForCancellation(CancelOrderRequest cancelOrderRequest,
			CustomerDetailVO customerDetail) throws BusinessProcessException {
		Long customerId = orderCancelRepository.getCustomerIdByOrderItemId(cancelOrderRequest.getOrderItemId(), "cancel");
		if (customerId == null || !customerDetail.getCustomerId().equals(customerId)) {
			throw new BusinessProcessException(CommonConstant.USER_ACCOUNT_MISMATCHED,
					CommonConstant.FAILURE_STATUS_CODE);
		}
		OrderItemDTO orderItemDTO = orderCancelRepository.getOrderItemDetail(cancelOrderRequest.getOrderItemId());
		if(!orderItemDTO.getSku().equals(cancelOrderRequest.getSku())) {
			throw new BusinessProcessException(CommonConstant.INVALID_SKU, CommonConstant.SUCCESS_STATUS_CODE);
		}
		OrderCancelReturnRecordDto orderCancelReturnRecordDto = buildOrderCancelRecord(orderItemDTO, cancelOrderRequest);
		if(orderCancelRepository.saveOrderCancelReturnRecord(orderCancelReturnRecordDto, false)>0) {
			orderCancelRepository.updateOrderItemStatus(cancelOrderRequest.getOrderItemId(), 8); // 8 is CANCELLED
			String message = buildMessage(orderCancelReturnRecordDto, cancelOrderRequest);
			if (customerDetail.getMobile() != null) {
				MessageRequest messageRequest = new MessageRequest();
				messageRequest.setDltTemplateType(DLTTemplateType.ORDER_CANCEL);
				messageRequest.setMobileNumber(customerDetail.getMobile());
				messageRequest.setContent(String.format(CommonConstant.ORDER_CANCEL_SMS, "", orderItemDTO.getOrderNumber()));
				emailSMSServiceClient.sendSMS(messageRequest);
			}
			if(customerDetail.getEmail()!=null) {
				emailNotificationServiceCient.sendOrderReturnCancelAlert(returnCancelExchangeAlert.buildReturnExchangeCancelRequest( cancelOrderRequest.getOrderItemId(),  cancelOrderRequest.getSku(),customerDetail, EmailAlertType.CANCEL));
			}
			inventoryServiceClient.updateInventory(buildRequestBodyForInventory(orderItemDTO));
			return ResponseEntity.ok(new DemandStoreAPIResponse(true, Arrays.asList(new MessageDetail(MessageType.INFO, message, null, null)), CommonConstant.SUCCESS_STATUS_CODE, 0));
		} else {
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG_WITH_ORDER_CANCEL, CommonConstant.SUCCESS_STATUS_CODE);
		}
 	}
	private List<SkuAndQty> buildRequestBodyForInventory(OrderItemDTO orderItemDTO) {
		List<SkuAndQty> skuAndQtyList = new ArrayList<>();
		SkuAndQty request = new SkuAndQty(orderItemDTO.getSku(), orderItemDTO.getQty());
		skuAndQtyList.add(request);
		return skuAndQtyList;
		
	}
	private String buildMessage(OrderCancelReturnRecordDto orderCancelReturnRecordDto, CancelOrderRequest cancelOrderRequest) {
		StringBuilder msg = new StringBuilder(CommonConstant.ORDER_CANCELLED);
		if(orderCancelReturnRecordDto.isRefundRequired() && orderCancelReturnRecordDto.getRefundAmount()>0.0) {
			if (cancelOrderRequest.getRefundType() == RefundType.CREDIT) {
				msg.append(String.format("refund amount %s will be credit in your wallet in next 1-2 working days.", orderCancelReturnRecordDto.getRefundAmount()));
			} else if (cancelOrderRequest.getRefundType() == RefundType.OPM) {
				msg.append(String.format("refund amount %s will be credited in your account in next 3-5 working days.", orderCancelReturnRecordDto.getRefundAmount()));
			}
		}
		return msg.toString();
	}
	/**
	 * @param orderItemDTO
	 * @param cancelOrderRequest
	 * @return
	 */
	private OrderCancelReturnRecordDto buildOrderCancelRecord(OrderItemDTO orderItemDTO,
			CancelOrderRequest cancelOrderRequest) {
		OrderCancelReturnRecordDto orderCancelReturnRecordDto = new OrderCancelReturnRecordDto();
		orderCancelReturnRecordDto.setReasonId(cancelOrderRequest.getReasonId());
		orderCancelReturnRecordDto.setOrderId(orderItemDTO.getOrderId());
		orderCancelReturnRecordDto.setOrderItemId(cancelOrderRequest.getOrderItemId());
		orderCancelReturnRecordDto.setQty(orderItemDTO.getQty());
		orderCancelReturnRecordDto.setRequestedDate(CommonConstant.DATE_FORMATTER.format(new Date()));
		Double totalPaid = orderItemDTO.getOrderItemCreditApplied() + orderItemDTO.getOrderItemPayable();
		if ("ONLINE".equals(orderItemDTO.getPaymentMethod())) {
			orderCancelReturnRecordDto.setRefundRequired(true);
			orderCancelReturnRecordDto.setRefundAmount(totalPaid);
		} else if ("COD".equals(orderItemDTO.getPaymentMethod())) {
			if(orderItemDTO.getOrderItemCreditApplied() > 0.0) {
				orderCancelReturnRecordDto.setRefundAmount(orderItemDTO.getOrderItemCreditApplied());
				orderCancelReturnRecordDto.setRefundRequired(true);
			} else {
				orderCancelReturnRecordDto.setRefundRequired(false);
			}
		} else if("OWP".equals(orderItemDTO.getPaymentMethod())) {
			if(orderItemDTO.getOrderItemCreditApplied() > 0.0) {
				orderCancelReturnRecordDto.setRefundAmount(orderItemDTO.getOrderItemCreditApplied());
				orderCancelReturnRecordDto.setRefundRequired(true);
			} else {
				orderCancelReturnRecordDto.setRefundRequired(false);
			}
		}
		orderCancelReturnRecordDto.setRecordType(RecordType.valueOf("CANCELLED"));
		orderCancelReturnRecordDto.setRefundType(cancelOrderRequest.getRefundType());
		return orderCancelReturnRecordDto;
	}
	@Transactional
	public ResponseEntity<DemandStoreAPIResponse> processItemForExchange(OrderExchangeRequest orderExchangeRequest,
			Long customerIdToken, CustomerDetailVO customerDetailVo) throws BusinessProcessException {
		Long customerId = orderCancelRepository.getCustomerIdByOrderItemId(orderExchangeRequest.getOrderItemId(), "exchange");
		if (customerId == null || ! (customerIdToken.equals(customerId))) {
			throw new BusinessProcessException(CommonConstant.USER_ACCOUNT_MISMATCHED,
					CommonConstant.FAILURE_STATUS_CODE);
		}
		OrderItemDTO orderItemDTO = orderCancelRepository.getOrderItemDetail(orderExchangeRequest.getOrderItemId());
		if(!orderItemDTO.getSku().equals(orderExchangeRequest.getSku())) {
			throw new BusinessProcessException(CommonConstant.INVALID_SKU, CommonConstant.SUCCESS_STATUS_CODE);
		}
		OrderCancelReturnRecordDto orderCancelReturnRecordDto = buildOrderExchangeRecord(orderItemDTO, orderExchangeRequest);
		if(orderCancelRepository.saveOrderCancelReturnRecord(orderCancelReturnRecordDto, false)>0) {
			orderCancelRepository.updateOrderItemStatus(orderExchangeRequest.getOrderItemId(), 10); // 10 is exchanged raised
			if(customerDetailVo.getEmail()!=null) {
				emailNotificationServiceCient.sendOrderReturnCancelAlert(returnCancelExchangeAlert.buildReturnExchangeCancelRequest(orderExchangeRequest.getOrderItemId(),orderExchangeRequest.getSku(),customerDetailVo, EmailAlertType.EXCHNAGE));
			}
			return ResponseEntity.ok(new DemandStoreAPIResponse(true, Arrays.asList(new MessageDetail(MessageType.INFO, CommonConstant.EXCHANGED_PLACED, null, null)), CommonConstant.SUCCESS_STATUS_CODE, null));
		} else {
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG_WITH_ORDER_EXCHANGE, CommonConstant.SUCCESS_STATUS_CODE);
		}
	}
	/**
	 * @param orderItemDTO
	 * @param orderExchangeRequest
	 * @return
	 */
	private OrderCancelReturnRecordDto buildOrderExchangeRecord(OrderItemDTO orderItemDTO, OrderExchangeRequest orderExchangeRequest) {
		OrderCancelReturnRecordDto orderCancelReturnRecordDto = new OrderCancelReturnRecordDto();
		orderCancelReturnRecordDto.setReasonId(orderExchangeRequest.getReasonId());
		orderCancelReturnRecordDto.setOrderId(orderItemDTO.getOrderId());
		orderCancelReturnRecordDto.setOrderItemId(orderExchangeRequest.getOrderItemId());
		orderCancelReturnRecordDto.setQty(orderItemDTO.getQty());
		orderCancelReturnRecordDto.setRequestedDate(CommonConstant.DATE_FORMATTER.format(new Date()));
		orderCancelReturnRecordDto.setRefundRequired(false);
		orderCancelReturnRecordDto.setRecordType(RecordType.valueOf("EXCHANGE"));
		orderCancelReturnRecordDto.setRefundType(RefundType.NA);
		return orderCancelReturnRecordDto;
	}
	
	public ResponseEntity<DemandStoreAPIResponse> getReasonRefundType(String type, String orderNumber) throws BusinessProcessException {
		CancelReturnReasonRefundDTO newCancelReturnDTO = new CancelReturnReasonRefundDTO();
		String paymentMethod = orderCancelRepository.paymentMethod(orderNumber);
		if(type == "C") {
			if(paymentMethod.equalsIgnoreCase("ONLINE")) {
				newCancelReturnDTO.setIsPopupRequired(true);
			}else {
				newCancelReturnDTO.setIsPopupRequired(false);
			}
		}else if(type == "R") {
			newCancelReturnDTO.setIsPopupRequired(true);
		}else {
			newCancelReturnDTO.setIsPopupRequired(false);
		}
			
		List<CancelReturnReasonDTO> cancelReturnReason = orderCancelRepository.getReason(type);
		if (cancelReturnReason.isEmpty()) {
			throw new BusinessProcessException(CommonConstant.NO_REASON_FOUND, CommonConstant.FAILURE_STATUS_CODE);
		}
		
		newCancelReturnDTO.setReasons(cancelReturnReason);
		return ResponseEntity.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, newCancelReturnDTO));
	}
}
