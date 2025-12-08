package com.ouat.customerService.customer.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ouat.customerService.customer.request.SendOTPRequest;
import com.ouat.customerService.customer.request.ValidateOTPRequest;
import com.ouat.customerService.customer.service.LoginService;
import com.ouat.customerService.exception.BusinessProcessException;
import com.ouat.customerService.interceptor.SessionRequired;
import com.ouat.customerService.response.DemandStoreAPIResponse;

@RestController
@CrossOrigin
@SessionRequired
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	@PostMapping("/send-otp")
	public ResponseEntity<DemandStoreAPIResponse> login(@RequestBody SendOTPRequest sendOtpRequest) throws BusinessProcessException {
		return loginService.sendOTP(sendOtpRequest);
	}
	
	@PostMapping("/validate-otp")
	public ResponseEntity<DemandStoreAPIResponse> validateOTP(@RequestBody ValidateOTPRequest validateOTP, HttpServletRequest request) throws BusinessProcessException {
		return loginService.validateOTP(validateOTP, request);
	}
	
	
	@PostMapping("v1/send-otp")
	public ResponseEntity<DemandStoreAPIResponse> loginV1(@RequestBody SendOTPRequest sendOtpRequest) throws BusinessProcessException {
		LOGGER.info("Login request received : {} ", sendOtpRequest.toString());
		return loginService.sendOTPV1(sendOtpRequest);
	}
	

}
