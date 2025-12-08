package com.ouat.myOrders.controller;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ouat.myOrders.CommonConstant.CommonConstant;
import com.ouat.myOrders.demandStoreAPIResponse.DemandStoreAPIResponse;
import com.ouat.myOrders.demandStoreAPIResponse.DemandStoreAPIResponseHelper;
import com.ouat.myOrders.exception.BusinessProcessException;
import com.ouat.myOrders.interceptor.DemandStoreLoginRequired;
import com.ouat.myOrders.request.OrderDetailRequest;
import com.ouat.myOrders.response.MessageDetail;
import com.ouat.myOrders.response.MessageType;
import com.ouat.myOrders.response.MyOrders;
import com.ouat.myOrders.response.OrderConfirmation;
import com.ouat.myOrders.response.OrderDetailResponse;
import com.ouat.myOrders.service.MyOrderService;
import com.ouat.myOrders.vo.CustomerDetailVO;

/*
 * 
 * author : sourabh singh
 * 
 */
@RestController
@DemandStoreLoginRequired
@CrossOrigin
public class MyOrdersController {
	public static final Logger LOGGER = LoggerFactory.getLogger(MyOrdersController.class);
	@Autowired
	DemandStoreAPIResponseHelper  demandStoreAPIResponseHelper;
	@Autowired
	MyOrderService myOrderService;
	@GetMapping("/my-orders/{pageNo}")
	public ResponseEntity<DemandStoreAPIResponse> orderList(@PathVariable Integer pageNo, HttpServletRequest httpServletRequest) throws BusinessProcessException{
		List<MessageDetail> messageDetailList = new ArrayList<MessageDetail>();
 		CustomerDetailVO customerDetail = (CustomerDetailVO) httpServletRequest.getAttribute(com.ouat.myOrders.interceptor.RequestHeaders.CUSTOMER_DETAIL);
		LOGGER.info("order list request successfully receaved from customer id : {}",customerDetail.getCustomerId());
		MyOrders myOrders = myOrderService.orderListDetail(customerDetail.getCustomerId(),pageNo);
 		if( myOrders==null || myOrders.getOrderListDetailResponse() == null || myOrders.getOrderListDetailResponse().isEmpty()) {
 			    LOGGER.error("orde list is empty for the customer id : {}", customerDetail.getCustomerId());
 			  demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, "Your order list is empty.",messageDetailList);
 			  return ResponseEntity.ok( demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null));           
 		}else {
 			   LOGGER.info("  successfully build order list with my orders : {} of customer_id : {}", myOrders,customerDetail.getCustomerId());
			  demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, "your order list is ready some item may be delay then its expected time",messageDetailList);
			  return ResponseEntity.ok( demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, myOrders));           
 		}
 	}
	@PostMapping("/order-detail")
    public ResponseEntity<DemandStoreAPIResponse> orderDetail(@RequestBody OrderDetailRequest orderDetailRequest, HttpServletRequest httpServletRequest) throws BusinessProcessException{
		List<MessageDetail> messageDetailList = new ArrayList<MessageDetail>();
		CustomerDetailVO customerDetail = (CustomerDetailVO) httpServletRequest.getAttribute(com.ouat.myOrders.interceptor.RequestHeaders.CUSTOMER_DETAIL);	
		LOGGER.info("order detail request successfully receaved from customer id : {}",customerDetail.getCustomerId());
		OrderDetailResponse orderDetailResponse = myOrderService.orderDetail(customerDetail.getCustomerId().intValue(), orderDetailRequest.getOrderId(), orderDetailRequest.getOrderNumber());
		if(null == orderDetailResponse || null == orderDetailResponse.getOrderNumber() || null == orderDetailResponse.getOrderItem()) {
			 LOGGER.error("orde detail is empty for the customer id : {}", customerDetail);
			  demandStoreAPIResponseHelper.setMessageDetailList(MessageType.ERROR,CommonConstant.INVALID_REQUEST,messageDetailList);
			  return ResponseEntity.ok( demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null));           
 		}
		else {
			  LOGGER.error("orde detail is successfullyexecuted for the customer id : {}", customerDetail);
			  demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, "check your order detail",messageDetailList);
			  return ResponseEntity.ok( demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, orderDetailResponse));           
		}
	}
	@PostMapping("/order-confirmation")
	public ResponseEntity<DemandStoreAPIResponse> orderConfirmation(@RequestBody OrderDetailRequest orderDetailRequest, HttpServletRequest httpServletRequest) throws BusinessProcessException{
		List<MessageDetail> messageDetailList = new ArrayList<>();
		CustomerDetailVO customerDetail = (CustomerDetailVO) httpServletRequest.getAttribute(com.ouat.myOrders.interceptor.RequestHeaders.CUSTOMER_DETAIL);	
		LOGGER.info("order detail request successfully receaved from customer id : {}",customerDetail.getCustomerId());
		LOGGER.info("ORDER_PLACED_DEVICE | {} ", httpServletRequest.getHeader(com.ouat.myOrders.interceptor.RequestHeaders.DEVICE_TYPE));
		LOGGER.info("ORDER_PLACED_DEVICE | Order Id {} ", orderDetailRequest.getOrderId());
		OrderDetailResponse orderDetailResponse = myOrderService.orderDetail(customerDetail.getCustomerId().intValue(), orderDetailRequest.getOrderId(), orderDetailRequest.getOrderNumber());
		if(null == orderDetailResponse || null == orderDetailResponse.getOrderNumber() || null == orderDetailResponse.getOrderItem()) {
			 LOGGER.error("orde detail is empty for the customer id : {}", customerDetail);
			  demandStoreAPIResponseHelper.setMessageDetailList(MessageType.ERROR,CommonConstant.INVALID_REQUEST,messageDetailList);
			  return ResponseEntity.ok( demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null));           
		} else {
			  LOGGER.error("orde detail is successfullyexecuted for the customer id : {}", customerDetail);
			  demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, "Your Order is Confirmed",messageDetailList);
			  OrderConfirmation orderConfirmation = myOrderService.buildOrderConfirmation(orderDetailResponse, customerDetail);
 			  return ResponseEntity.ok( demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, orderConfirmation));           
		}
	}
}
