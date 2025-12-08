package com.ouat.orderService.rto.service;

import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ouat.orderService.cancel.dto.OrderCancelReturnRecordDto;
import com.ouat.orderService.cancel.dto.OrderItemDTO;
import com.ouat.orderService.cancel.dto.RecordType;
import com.ouat.orderService.cancel.dto.RefundType;
import com.ouat.orderService.cancel.repository.OrderCancelRepository;
import com.ouat.orderService.client.EmailAlertType;
import com.ouat.orderService.client.EmailNotificationServiceClient;
import com.ouat.orderService.constants.CommonConstant;
import com.ouat.orderService.exception.BusinessProcessException;
import com.ouat.orderService.response.CustomerDetailVO;
import com.ouat.orderService.response.DemandStoreAPIResponse;
import com.ouat.orderService.response.MessageDetail;
import com.ouat.orderService.response.MessageType;
import com.ouat.orderService.returnExchangeCancelAlert.service.ReturnCancelExchangeAlert;
import com.ouat.orderService.rto.request.ReturnItemRequest;

@Service
public class OrderReturnService {
	
    @Autowired
	OrderCancelRepository orderCancelRepository;
	
   @Autowired
   EmailNotificationServiceClient emailNotificationServiceClient;
   
   @Autowired
   ReturnCancelExchangeAlert returnCancelExchangeAlert;

	@Transactional
	public ResponseEntity<DemandStoreAPIResponse> processItemForReturn(ReturnItemRequest returnOrderRequest, CustomerDetailVO customerDetail) throws BusinessProcessException {
		Long customerId = orderCancelRepository.getCustomerIdByOrderItemId(returnOrderRequest.getOrderItemId(), "return");
		if (customerId == null || !customerDetail.getCustomerId().equals(customerId)) {
			throw new BusinessProcessException(CommonConstant.USER_ACCOUNT_MISMATCHED,
					CommonConstant.FAILURE_STATUS_CODE);
		}
		OrderItemDTO orderItemDTO = orderCancelRepository.getOrderItemDetail(returnOrderRequest.getOrderItemId());

		if(!orderItemDTO.getSku().equals(returnOrderRequest.getSku())) {
			throw new BusinessProcessException(CommonConstant.INVALID_SKU, CommonConstant.SUCCESS_STATUS_CODE);
		}
		OrderCancelReturnRecordDto orderCancelReturnRecordDto = buildOrderCancelRecord(orderItemDTO, returnOrderRequest);
		if(orderCancelRepository.saveOrderCancelReturnRecord(orderCancelReturnRecordDto, true)>0) {
			orderCancelRepository.updateOrderItemStatus(returnOrderRequest.getOrderItemId(), 6);
			String message = buildMessage(orderCancelReturnRecordDto, returnOrderRequest);
			
			if(customerDetail.getEmail()!=null) {
				emailNotificationServiceClient.sendOrderReturnCancelAlert(returnCancelExchangeAlert.buildReturnExchangeCancelRequest(returnOrderRequest.getOrderItemId(),returnOrderRequest.getSku(),customerDetail, EmailAlertType.RETURN));
			}
			return ResponseEntity.ok(new DemandStoreAPIResponse(true, Arrays.asList(new MessageDetail(MessageType.INFO, message, null, null)), CommonConstant.SUCCESS_STATUS_CODE, 0));
		} else {
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG_WITH_ORDER_RETURN, CommonConstant.SUCCESS_STATUS_CODE);
		}
	}
	private String buildMessage(OrderCancelReturnRecordDto orderCancelReturnRecordDto, ReturnItemRequest returnOrderRequest) {
		StringBuilder msg = new StringBuilder(CommonConstant.ORDER_RETURNED);
		if (returnOrderRequest.getRefundType() == RefundType.CREDIT) {
			msg.append(String.format(" refund amount %s will be credit in your wallet in next 1-2 working days.", orderCancelReturnRecordDto.getRefundAmount()));
		} else if (returnOrderRequest.getRefundType() == RefundType.OPM) {
			msg.append(String.format(" refund amount %s will be credited in your account in next 3-5 working days.", orderCancelReturnRecordDto.getRefundAmount()));
		}
		return msg.toString();
	}

	/**
	 * @param orderItemDTO
	 * @param returnOrderRequest
	 * @return
	 */
	private OrderCancelReturnRecordDto buildOrderCancelRecord(OrderItemDTO orderItemDTO,
			ReturnItemRequest returnOrderRequest) {
		OrderCancelReturnRecordDto orderCancelReturnRecordDto = new OrderCancelReturnRecordDto();
		orderCancelReturnRecordDto.setReasonId(returnOrderRequest.getReasonId());
		orderCancelReturnRecordDto.setOrderId(orderItemDTO.getOrderId());
		orderCancelReturnRecordDto.setOrderItemId(returnOrderRequest.getOrderItemId());
		orderCancelReturnRecordDto.setQty(orderItemDTO.getQty());
		orderCancelReturnRecordDto.setRequestedDate(CommonConstant.DATE_FORMATTER.format(new Date()));
		Double totalPaid = orderItemDTO.getOrderItemCreditApplied() + orderItemDTO.getOrderItemPayable() - orderItemDTO.getShippingCharges();
		
		if ("ONLINE".equals(orderItemDTO.getPaymentMethod()) || "COD".equals(orderItemDTO.getPaymentMethod())) {
			orderCancelReturnRecordDto.setRefundRequired(true);
			orderCancelReturnRecordDto.setRefundAmount(totalPaid);
		} else if("OWP".equals(orderItemDTO.getPaymentMethod())) {
			if(orderItemDTO.getOrderItemCreditApplied() > 0.0) {
				orderCancelReturnRecordDto.setRefundAmount(orderItemDTO.getOrderItemCreditApplied());
				orderCancelReturnRecordDto.setRefundRequired(true);
			} else {
				orderCancelReturnRecordDto.setRefundRequired(false);
			}
		}
		
		orderCancelReturnRecordDto.setRecordType(RecordType.valueOf("RETURN"));
		orderCancelReturnRecordDto.setRefundType(returnOrderRequest.getRefundType());
		return orderCancelReturnRecordDto;
	}

	
}
