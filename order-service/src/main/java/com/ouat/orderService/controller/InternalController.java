package com.ouat.orderService.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ouat.orderService.client.CourierResponse;
import com.ouat.orderService.exception.BusinessProcessException;
import com.ouat.orderService.exception.DownStreamException;
import com.ouat.orderService.request.ShipmentDetailsRequest;
import com.ouat.orderService.response.PostShippmentDetailResponse;
import com.ouat.orderService.service.Label;

@RestController
@RequestMapping("/internal")
public class InternalController {
	public Logger LOGGER = LoggerFactory.getLogger(InternalController.class);

	@Autowired
	private Label labels;
 
	@GetMapping("/orders/labels")
	public ResponseEntity<String> getLabels(@RequestParam String orderItemIds)
			throws BusinessProcessException, FileNotFoundException {
		 
		return ResponseEntity.ok(labels.getLabels(orderItemIds));
	}
	
	//written by mudit (humara cute SDE)
	@PostMapping("/orders/labels")
	public ResponseEntity<PostShippmentDetailResponse> shipmentDetails(@RequestBody ShipmentDetailsRequest shipmentDetailsRequest) throws BusinessProcessException{
		return ResponseEntity.ok(labels.postShipmentDetail(shipmentDetailsRequest));
	}

	@GetMapping("/courierDetails")
	public ResponseEntity<CourierResponse> courierDetails(
			@RequestParam(required = true, name = "orderItemIds") String orderItemIds)
			throws BusinessProcessException, IOException, DownStreamException, ParseException {
		return ResponseEntity.ok(labels.courierDetails(orderItemIds));
	}

}
