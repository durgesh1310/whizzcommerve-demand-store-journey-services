package com.ouat.orderService.exchange.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ouat.orderService.client.EmailAlertType;
import com.ouat.orderService.client.EmailNotificationServiceClient;
import com.ouat.orderService.client.EmailSMSServiceClient;
import com.ouat.orderService.constants.CommonConstant;
import com.ouat.orderService.exception.BusinessProcessException;
import com.ouat.orderService.exchange.dto.OrderCancelReturnRecordDto;
import com.ouat.orderService.exchange.dto.OrderDetailDto;
import com.ouat.orderService.exchange.dto.PlaceExchangeOrderDto;
import com.ouat.orderService.exchange.repository.PlaceExchangeOrderDetailRepository;
import com.ouat.orderService.exchange.request.PlaceExchangeOrderRequest;
import com.ouat.orderService.response.CustomerDetailVO;
import com.ouat.orderService.response.DemandStoreAPIResponse;
import com.ouat.orderService.response.DemandStoreAPIResponseHelper;
import com.ouat.orderService.response.MessageDetail;
import com.ouat.orderService.response.MessageType;
import com.ouat.orderService.returnExchangeCancelAlert.service.ReturnCancelExchangeAlert;

@Service
public class PlaceExchangeOrderService {

	@Autowired
	private DemandStoreAPIResponseHelper demandStoreAPIResponseHelper;

	@Autowired
	EmailSMSServiceClient emailSMSServiceClient;

	@Autowired
	EmailNotificationServiceClient emailNotificationServiceCient;
	
	@Autowired 
	ReturnCancelExchangeAlert  returnCancelExchangeAlert;


	@Autowired
	private PlaceExchangeOrderDetailRepository exchangeOrderDetailRepository;

	public static final Logger LOGGER = LoggerFactory.getLogger(PlaceExchangeOrderService.class);


	@Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = { BusinessProcessException.class })
	public DemandStoreAPIResponse processOrderItemForExchange(PlaceExchangeOrderRequest exchangeOrderRequest,
			Long customerIdToken, CustomerDetailVO customerDetail) throws BusinessProcessException {
		OrderDetailDto orderDetailDto = exchangeOrderDetailRepository.exchangeOrderDetail(exchangeOrderRequest);
		Long customerId = orderDetailDto.getCustomerId();								
		if (customerId == null || !(customerIdToken.equals(customerId))) {
			throw new BusinessProcessException(CommonConstant.USER_ACCOUNT_MISMATCHED,
					CommonConstant.FAILURE_STATUS_CODE);
		}

		List<MessageDetail> messageDetailList = new ArrayList<>();
		
		
		/*
		 * OrderDto orderDTO = buildOrderDTO(orderDetailDto);
		 * LOGGER.info("inserting into order table with orderDTO : {}", orderDTO); Long
		 * orderId = (long)
		 * exchangeOrderDetailRepository.insertIntoOrderTable(orderDTO); if (orderId ==
		 * 0) { LOGGER.info("orders.order table not updated successfully"); throw new
		 * BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG,
		 * CommonConstant.FAILURE_STATUS_CODE); } OrderItemDTO orderItemDTO =
		 * buildOrderItemDto(exchangeOrderRequest, orderDetailDto, orderId); LOGGER.
		 * info("inserting order item into orderItem table with orderitemDTO : {}",
		 * orderItemDTO); Long orderItemId = (long)
		 * exchangeOrderDetailRepository.insertIntoOrderItemTable(orderItemDTO); if
		 * (orderItemId == 0) {
		 * LOGGER.info("orders.order_item table not updated successfully"); throw new
		 * BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG,
		 * CommonConstant.FAILURE_STATUS_CODE); } OrderShippmentDetailDTO
		 * orderShippmentDetailDTO = buildOrderShippmentDetailDTO(orderDetailDto,
		 * orderId); LOGGER.
		 * info("Inserting into ordershipmentdetail table with shipment DTO : {} ",
		 * orderShippmentDetailDTO); int orderShippmentTableRowEffected =
		 * exchangeOrderDetailRepository
		 * .insertIntoOrderShippmentDetail(orderShippmentDetailDTO); if
		 * (orderShippmentTableRowEffected == 0) {
		 * LOGGER.info("Inserting into ordershipmentdetail table not done successfully"
		 * ); throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG,
		 * CommonConstant.FAILURE_STATUS_CODE); }
		 */		OrderCancelReturnRecordDto orderCancelReturnRecordDto = buildOrderCancelReturnRecord(orderDetailDto,
				exchangeOrderRequest);
		LOGGER.info("Inserting into orderCancelReturnRecord table with OrderCancelReturnRecord DTO : {} ",
				orderCancelReturnRecordDto);
		int orderCancelReturnTableRowEffected = exchangeOrderDetailRepository
				.insertIntoOrderCancelRecord(orderCancelReturnRecordDto);
		if (orderCancelReturnTableRowEffected == 0) {
			LOGGER.info("Inserting into orderCancelReturnRecord table not done successfully");
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);

		}

		PlaceExchangeOrderDto exchangeOrderDto = buildOrderExchangeOrder(orderDetailDto, exchangeOrderRequest);
		LOGGER.info("Inserting into orderCancelReturnRecord table with ExchangeOrder DTO : {} ", exchangeOrderDto);
		int exchangeOrderTableRowEffected = exchangeOrderDetailRepository
				.inserIntoExchangeOrderDetail(exchangeOrderDto);
		exchangeOrderDetailRepository.updateOrderItemStatus(exchangeOrderRequest.getOrderItemId(), 10);
		if (exchangeOrderTableRowEffected == 0) {
			LOGGER.info("Inserting into exchangeOrder table not done successfully");
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);

		}
		
		if(customerDetail.getEmail()!=null) {
			emailNotificationServiceCient.sendOrderReturnCancelAlert(returnCancelExchangeAlert.buildReturnExchangeCancelRequest(orderDetailDto.getOrderItemId(),orderDetailDto.getSku(),customerDetail, EmailAlertType.EXCHNAGE));
		}
		demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.EXCHANGED_PLACED,
				messageDetailList);
		return demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG,
				CommonConstant.SUCCESS_STATUS_CODE,
				buildOrderExchangeOrder(orderDetailDto, exchangeOrderRequest));

	}


	/*
	 * private OrderDto buildOrderDTO(OrderDetailDto orderDetailDto) throws
	 * BusinessProcessException { OrderDto orderDto = new OrderDto();
	 * orderDto.setCustomerId(orderDetailDto.getCustomerId());
	 * orderDto.setOrderStatusId(ORDER_RECEIVED_ID);
	 * orderDto.setTotalAmount(orderDetailDto.getTotalAmount());
	 * orderDto.setOrderPayable(orderDetailDto.getOrderPayable());
	 * orderDto.setPlatformOfferedDiscount(orderDetailDto.getPlatformOfferDiscount()
	 * ); orderDto.setPromoDiscount(orderDetailDto.getPromoDiscount());
	 * orderDto.setCreditApplied(orderDetailDto.getCreditApplied());
	 * orderDto.setShippingCharges(orderDetailDto.getShippingCharges());
	 * orderDto.setOrderType("DemandStore");
	 * orderDto.setPaymentMethod(orderDetailDto.getPaymentMethod());
	 * orderDto.setPromocode(orderDetailDto.getPromoCode());
	 * orderDto.setPlatform(orderDetailDto.getPlatform());
	 * 
	 * return orderDto; }
	 * 
	 * private OrderItemDTO buildOrderItemDto(PlaceExchangeOrderRequest
	 * exchangeOrderRequest, OrderDetailDto orderDetailDto, Long orderId) throws
	 * BusinessProcessException { OrderItemDTO orderItemDTO = new OrderItemDTO();
	 * 
	 * orderItemDTO.setOrderId(orderId);
	 * orderItemDTO.setSku(exchangeOrderRequest.getSku());
	 * orderItemDTO.setQty(orderDetailDto.getQty());
	 * orderItemDTO.setOrderItemTotalAmount(orderDetailDto.getOrderItemTotalAmount()
	 * ); orderItemDTO.setOrderItemPayable(orderDetailDto.getOrderItemPayable());
	 * orderItemDTO.setOrderItemPlatformOfferedDiscount(orderDetailDto.
	 * getOrderItemPlatformOfferedDiscount());
	 * orderItemDTO.setOrderItemPromoDiscount(orderDetailDto.
	 * getOrderItemPromoDiscount());
	 * orderItemDTO.setOrderItemCreditApplied(orderDetailDto.
	 * getOrderItemCreditApplied());
	 * orderItemDTO.setVendorPrice(orderDetailDto.getVendorPrice());
	 * orderItemDTO.setOuatPrice(orderDetailDto.getOuatMargin());
	 * orderItemDTO.setOrderStatusId(ORDER_RECEIVED_ID);
	 * orderItemDTO.setOrderCreatedBy("DemandStore");
	 * orderItemDTO.setUpdatedBy("ExchangeOrderService");
	 * orderItemDTO.setIsReturnable(orderDetailDto.getIsReturnable());
	 * orderItemDTO.setIsExchangable(false); int edd = 0; try { edd =
	 * Integer.parseInt(orderDetailDto.getEdd()); } catch (Exception e) { edd = 1; }
	 * orderItemDTO.setEstimatedShippedDate(calculateEstimatedShippedAndDeliveryDate
	 * ((edd == 0) ? 1 : edd)); orderItemDTO.setEstimatedDeliveryDate(
	 * calculateEstimatedShippedAndDeliveryDate(((edd == 0) ? 1 : edd) + 4));
	 * orderItemDTO.setOrderItemShippingCharges(orderDetailDto.
	 * getOrderItemShippingCharges()); return orderItemDTO; }
	 * 
	 * public java.sql.Date calculateEstimatedShippedAndDeliveryDate(Integer edd) {
	 * java.sql.Date now = new java.sql.Date(new java.util.Date().getTime());
	 * LOGGER.info("setting shipped date or delivery delevry date for the edd : {}",
	 * edd); java.sql.Date eddDate = new java.sql.Date(now.getTime() + (edd * 24 *
	 * 60 * 60 * 1000)); LOGGER.
	 * info("shipped date or delivery delevry date  set : {} for the edd : {}",
	 * eddDate, edd); return eddDate;
	 * 
	 * }
	 * 
	 * private OrderShippmentDetailDTO buildOrderShippmentDetailDTO(OrderDetailDto
	 * orderDetailDto, Long orderId) throws BusinessProcessException {
	 * OrderShippmentDetailDTO orderShippmentDetailDTO = new
	 * OrderShippmentDetailDTO();
	 * orderShippmentDetailDTO.setAddressId(orderDetailDto.getAddressId());
	 * orderShippmentDetailDTO.setOrderId(orderId); return orderShippmentDetailDTO;
	 * }
	  */ 
	  private OrderCancelReturnRecordDto
	  buildOrderCancelReturnRecord(OrderDetailDto orderDetailDto,
	  PlaceExchangeOrderRequest exchangeOrderRequest) { OrderCancelReturnRecordDto
	  orderCancelReturnRecordDto = new OrderCancelReturnRecordDto();
	  orderCancelReturnRecordDto.setOrderId(orderDetailDto.getOrderId());
	  orderCancelReturnRecordDto.setOrderItemId(exchangeOrderRequest.getOrderItemId
	  ()); orderCancelReturnRecordDto.setQty(orderDetailDto.getQty());
	  orderCancelReturnRecordDto.setReasonId(exchangeOrderRequest.getReasonId());
	  orderCancelReturnRecordDto.setCreatedBy("ExchangeOrderService");
	  
	  return orderCancelReturnRecordDto; }
	 
	private PlaceExchangeOrderDto buildOrderExchangeOrder(OrderDetailDto orderDetailDto,
			PlaceExchangeOrderRequest exchangeOrderRequest) throws BusinessProcessException {
		PlaceExchangeOrderDto exchangeOrderDto = new PlaceExchangeOrderDto();
		exchangeOrderDto.setOrderItemId(orderDetailDto.getOrderItemId());
	//	exchangeOrderDto.setExchangeOrderItemId();
		exchangeOrderDto.setSku(exchangeOrderRequest.getSku());
		return exchangeOrderDto;
	}

}
