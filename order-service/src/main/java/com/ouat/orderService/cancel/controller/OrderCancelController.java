package com.ouat.orderService.cancel.controller;

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

import com.ouat.orderService.cancel.request.CancelOrderRequest;
import com.ouat.orderService.cancel.service.OrderCancelService;
import com.ouat.orderService.exception.BusinessProcessException;
import com.ouat.orderService.interceptor.DemandStoreLoginRequired;
import com.ouat.orderService.interceptor.RequestHeaders;
import com.ouat.orderService.response.CustomerDetailVO;
import com.ouat.orderService.response.DemandStoreAPIResponse;

@RestController
@CrossOrigin
@RequestMapping("/cancel")
@DemandStoreLoginRequired
public class OrderCancelController {
	
	@Autowired
	OrderCancelService orderCancelService;
	
	@GetMapping("/reason")
	public ResponseEntity<DemandStoreAPIResponse> reason()throws BusinessProcessException{
		return orderCancelService.getReason("C");
	}
	
	@PostMapping("/process")
	public ResponseEntity<DemandStoreAPIResponse> cancellationItems(@RequestBody CancelOrderRequest cancelOrderRequest, HttpServletRequest request) throws BusinessProcessException{
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		return orderCancelService.processItemForCancellation(cancelOrderRequest, customerDetail);
	}
	
	@GetMapping("/v1/reason/{orderNumber}")
	public ResponseEntity<DemandStoreAPIResponse> reasonRefundType(@PathVariable String orderNumber)throws BusinessProcessException{
		return orderCancelService.getReasonRefundType("C", orderNumber);
	}
}
