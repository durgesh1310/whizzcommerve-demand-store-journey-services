package com.ouat.customerService.customer.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ouat.customerService.customer.request.AddGuestUserRequest;
import com.ouat.customerService.customer.request.CustomerRequest;
import com.ouat.customerService.customer.service.CustomerCreditService;
import com.ouat.customerService.customer.service.CustomerService;
import com.ouat.customerService.exception.BusinessProcessException;
import com.ouat.customerService.response.DemandStoreAPIResponse;

@RestController
@CrossOrigin
@RequestMapping("/internal")
public class InternalCustomerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InternalCustomerController.class);

	
	@Autowired
	private CustomerService customerService;
	
	
	@GetMapping("{customerId}/address/{addressId}")
	public ResponseEntity<DemandStoreAPIResponse> findAddress(@PathVariable Integer customerId, @PathVariable Integer addressId, HttpServletRequest request) throws BusinessProcessException {
		return customerService.findAddressByAddressId(customerId, addressId);
	}
	
	@GetMapping("{customerId}/address")
	public ResponseEntity<DemandStoreAPIResponse> findAddress(@PathVariable Integer customerId) throws BusinessProcessException {
		return customerService.findAddress(customerId);
	}
	
	@Autowired
	CustomerCreditService customerCreditService;
	
	/**
	 * This end point is designed for internal service
	 * @param customerId
	 * @return
	 */
	@GetMapping("/credit/{customerId}")
	public ResponseEntity<DemandStoreAPIResponse> fetchCredits(@PathVariable Integer customerId) {
		return customerCreditService.fetchCredits(customerId);
	}
	
	//TODO : this code is for the guest user 
	@PostMapping("/add-guest-user")
 	public ResponseEntity<DemandStoreAPIResponse> addAddressForGuest(@RequestBody AddGuestUserRequest addGuestUserRequest, HttpServletRequest request) throws BusinessProcessException {
		LOGGER.info("guest user reuqest has been recieved with request body : {}", addGuestUserRequest);
		return customerService.addCustomerForGuestCheckout(addGuestUserRequest);
     }
	//this code is for place on be half of customer
	@PostMapping("/add-customer")
	public ResponseEntity<DemandStoreAPIResponse> addCustomer(@RequestBody CustomerRequest customerRequest) throws BusinessProcessException {
		return customerService.customerCareAddCustomer(customerRequest);
	}
}
