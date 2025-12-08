package com.ouat.customerService.customer.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ouat.customerService.constants.CommonConstant;
import com.ouat.customerService.customer.request.AddGuestUserRequest;
import com.ouat.customerService.customer.request.CustomerAddressRequest;
import com.ouat.customerService.customer.request.CustomerRequest;
import com.ouat.customerService.customer.request.EmailAddRequest;
import com.ouat.customerService.customer.service.CustomerCreditService;
import com.ouat.customerService.customer.service.CustomerService;
import com.ouat.customerService.exception.BusinessProcessException;
import com.ouat.customerService.interceptor.DemandStoreLoginRequired;
import com.ouat.customerService.interceptor.RequestHeaders;
import com.ouat.customerService.interceptor.SessionRequired;
import com.ouat.customerService.response.CustomerDetailVO;
import com.ouat.customerService.response.DemandStoreAPIResponse;

@RestController
@SessionRequired
@CrossOrigin
@RequestMapping("/customers")
public class CustomerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	CustomerCreditService customerCreditService;
	
	@PostMapping
	public ResponseEntity<DemandStoreAPIResponse> addCustomer(@RequestBody CustomerRequest customerRequest, HttpServletRequest request) throws BusinessProcessException {
		LOGGER.info("Add Customer Request : {} ", customerRequest);
		return customerService.addCustomer(customerRequest, request.getHeader(RequestHeaders.USER_CLIENT));
	}
	
	@PutMapping
	@DemandStoreLoginRequired
	public ResponseEntity<DemandStoreAPIResponse> updateCustomer(@RequestBody AddGuestUserRequest addGuestUserRequest, HttpServletRequest request) throws BusinessProcessException {
		LOGGER.info("Update Customer Request : {} ", addGuestUserRequest);
		return customerService.addCustomerForGuestCheckout(addGuestUserRequest);
	}
	
	@PutMapping("/update-email")
	@DemandStoreLoginRequired
	public ResponseEntity<DemandStoreAPIResponse> updateCustomer(@RequestBody EmailAddRequest emailAddRequest, HttpServletRequest request) throws BusinessProcessException {
		LOGGER.info("Update Customer Request : {} ", emailAddRequest);
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		if(0 == customerDetail.getCustomerId()) {
			throw new BusinessProcessException(CommonConstant.ANOTHER_USER_TOKEN, CommonConstant.FAILURE_STATUS_CODE);
		}
		return customerService.updateCustomer(emailAddRequest, customerDetail.getCustomerId());
	}
	
	
	@GetMapping("/email/{email}")
	@DemandStoreLoginRequired
	public ResponseEntity<DemandStoreAPIResponse> findCustomerDetailByEmail(@PathVariable String email) throws BusinessProcessException {
		return customerService.findCustomerDetailByEmail(email);
	}
	
	@GetMapping("/is-new-customer")
	public ResponseEntity<DemandStoreAPIResponse> findCustomerDetailByMoileAndEmail(@RequestParam(name="email") String email, @RequestParam(name="mobile") String mobile) throws BusinessProcessException {
		return customerService.findCustomerDetailByMoileAndEmail(email, mobile);
	}
	
	@GetMapping("/mobile/{mobile}")
	@DemandStoreLoginRequired
	public ResponseEntity<DemandStoreAPIResponse> findCustomerDetailByMobile(@PathVariable String mobile) throws BusinessProcessException {
		return customerService.findCustomerDetailByMobile(mobile);
	}
	
	@PostMapping("/add-address")
	@DemandStoreLoginRequired
	public ResponseEntity<DemandStoreAPIResponse> addAddress(@RequestBody CustomerAddressRequest customerAddressRequest, HttpServletRequest request) throws BusinessProcessException {
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		if(0 == customerDetail.getCustomerId().intValue()) {
			throw new BusinessProcessException(CommonConstant.ANOTHER_USER_TOKEN, CommonConstant.FAILURE_STATUS_CODE);
		}
		customerAddressRequest.setCustomerId(customerDetail.getCustomerId().intValue());
		return customerService.addAddress(customerAddressRequest);
	}
	
	@PostMapping("/update-address")
	@DemandStoreLoginRequired
	public ResponseEntity<DemandStoreAPIResponse> updateAddress(@RequestBody CustomerAddressRequest customerAddressRequest, HttpServletRequest request) throws BusinessProcessException {
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		if(0 == customerDetail.getCustomerId().intValue()) {
			throw new BusinessProcessException(CommonConstant.ANOTHER_USER_TOKEN, CommonConstant.FAILURE_STATUS_CODE);
		}
		customerAddressRequest.setCustomerId(customerDetail.getCustomerId().intValue());
		return customerService.updateAddress(customerAddressRequest);
	}
	
	@GetMapping("/{customerId}/address")
	@DemandStoreLoginRequired
	public ResponseEntity<DemandStoreAPIResponse> findAddress(@PathVariable Integer customerId, HttpServletRequest request) throws BusinessProcessException {
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		if(customerId != customerDetail.getCustomerId().intValue()) {
			throw new BusinessProcessException(CommonConstant.ANOTHER_USER_TOKEN, CommonConstant.FAILURE_STATUS_CODE);
		}
		return customerService.findAddress(customerId);
	}
	
	@GetMapping("/credit/{customerId}")
	@DemandStoreLoginRequired
	public ResponseEntity<DemandStoreAPIResponse> fetchCredits(@PathVariable Integer customerId) {
		return customerCreditService.fetchCredits(customerId);
	}
	
	@DeleteMapping("/delete-address/{addressId}")
	@DemandStoreLoginRequired
	public ResponseEntity<DemandStoreAPIResponse> deleteAddress(@PathVariable Integer addressId, HttpServletRequest request) throws BusinessProcessException {
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		if(0 == customerDetail.getCustomerId().intValue()) {
			throw new BusinessProcessException(CommonConstant.ANOTHER_USER_TOKEN, CommonConstant.FAILURE_STATUS_CODE);
		}
		return customerService.deleteAddress(customerDetail.getCustomerId(), addressId);
	}
	
	@GetMapping("/first-install")
	@DemandStoreLoginRequired
	public ResponseEntity<DemandStoreAPIResponse> firstInstall(HttpServletRequest request) throws BusinessProcessException {
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		if(0 == customerDetail.getCustomerId().intValue()) {
			//throw new BusinessProcessException(CommonConstant.ANOTHER_USER_TOKEN, CommonConstant.FAILURE_STATUS_CODE);
		}
		return customerService.processCreditsForFirstInstall(customerDetail.getCustomerId(), request.getHeader(RequestHeaders.USER_CLIENT));
	}
	
	
	@PostMapping("/add-address-guest")
	@DemandStoreLoginRequired
	public ResponseEntity<DemandStoreAPIResponse> addAddressGuest(@RequestBody CustomerAddressRequest customerAddressRequest, HttpServletRequest request) throws BusinessProcessException {
		LOGGER.info("add customer request recieve with request body : {}", customerAddressRequest);
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		if(0 == customerDetail.getCustomerId().intValue()) {
			throw new BusinessProcessException(CommonConstant.ANOTHER_USER_TOKEN, CommonConstant.FAILURE_STATUS_CODE);
		}
		customerAddressRequest.setCustomerId(customerDetail.getCustomerId().intValue());
		LOGGER.info("hitting the service layer with request : {} and customer detail : {}", customerAddressRequest);
		return customerService.addAddressGuest(customerAddressRequest, customerDetail);
	}
	
	@GetMapping("/{customerId}/address/{addressId}")
	@DemandStoreLoginRequired
	public ResponseEntity<DemandStoreAPIResponse> findAddress(@PathVariable Integer customerId, @PathVariable Integer addressId, HttpServletRequest request) throws BusinessProcessException {
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		if(customerId != customerDetail.getCustomerId().intValue()) {
			throw new BusinessProcessException(CommonConstant.ANOTHER_USER_TOKEN, CommonConstant.FAILURE_STATUS_CODE);
		}
		return customerService.findAddressByAddressId(customerId, addressId);
	}
	
	@GetMapping("/rank")
	@DemandStoreLoginRequired
	public ResponseEntity<DemandStoreAPIResponse> participantRanking(HttpServletRequest request) throws BusinessProcessException {
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		return customerService.participantRanking(customerDetail.getCustomerId());

	}

	
}
