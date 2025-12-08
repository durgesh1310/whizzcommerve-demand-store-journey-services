package com.ouat.customerService.customer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ouat.customerService.customer.request.CustomerCreditHistory;
import com.ouat.customerService.customer.service.CustomerCreditService;
import com.ouat.customerService.response.DemandStoreAPIResponse;

@RestController
@CrossOrigin
public class CustomerCreditInternalController {
	
	@Autowired
	CustomerCreditService customerCreditService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerCreditInternalController.class);
	
	/**
	 * This end point is designed for internal service
	 * @param customerId
	 * @return
	 */
	@PostMapping("/credit/history")
	public ResponseEntity<DemandStoreAPIResponse> addCreditInHistory(@RequestBody CustomerCreditHistory customerCreditHistory) {
		LOGGER.info("Credit History Request : {} ", customerCreditHistory.toString());
		return customerCreditService.addInHistory(customerCreditHistory);
	}

}
