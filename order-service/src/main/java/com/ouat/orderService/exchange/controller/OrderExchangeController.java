package com.ouat.orderService.exchange.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ouat.orderService.cancel.service.OrderCancelService;
import com.ouat.orderService.exception.BusinessProcessException;
import com.ouat.orderService.exchange.request.OrderExchangeRequest;
import com.ouat.orderService.exchange.request.PlaceExchangeOrderRequest;
import com.ouat.orderService.exchange.service.OrderExchangeService;
import com.ouat.orderService.exchange.service.PlaceExchangeOrderService;
import com.ouat.orderService.interceptor.DemandStoreLoginRequired;
import com.ouat.orderService.interceptor.RequestHeaders;
import com.ouat.orderService.response.CustomerDetailVO;
import com.ouat.orderService.response.DemandStoreAPIResponse;

@RestController
@CrossOrigin
@RequestMapping("/exchange")
@DemandStoreLoginRequired
public class OrderExchangeController {

	

	@Autowired
	private PlaceExchangeOrderService exchangeOrderService;
	
	
	
	@Autowired
	OrderCancelService orderCancelService;
	
	@Autowired
	OrderExchangeService orderExchangeService;

	@GetMapping("/reason")
	public ResponseEntity<DemandStoreAPIResponse> reason() throws BusinessProcessException {
		return orderCancelService.getReason("E");
	}

	@PostMapping("/process")
	public ResponseEntity<DemandStoreAPIResponse> exchangeItems(@RequestBody OrderExchangeRequest exchangeOrderRequest,
			HttpServletRequest request) throws BusinessProcessException {
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		return orderCancelService.processItemForExchange(exchangeOrderRequest, customerDetail.getCustomerId(),
				customerDetail);
	}

	@PostMapping("/items/{orderItemId}")
	public ResponseEntity<DemandStoreAPIResponse> exchangeItem(@PathVariable Long orderItemId, HttpServletRequest request)
			throws BusinessProcessException {
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		return orderExchangeService.getOrderItemId(orderItemId, customerDetail.getCustomerId());
	}
	
	@PostMapping("/v2/process")
	public ResponseEntity<DemandStoreAPIResponse> exchangeOrderItem (@RequestBody PlaceExchangeOrderRequest exchangeOrderRequest, HttpServletRequest request)
			throws BusinessProcessException {
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
	return ResponseEntity.ok(exchangeOrderService.processOrderItemForExchange (exchangeOrderRequest, customerDetail.getCustomerId(),
			customerDetail));
	
	}
	
	@GetMapping("v1/reason/{orderNumber}")
	public ResponseEntity<DemandStoreAPIResponse> reasonRefundType(@PathVariable String orderNumber) throws BusinessProcessException {
		return orderCancelService.getReasonRefundType("E", orderNumber);
	}

}
