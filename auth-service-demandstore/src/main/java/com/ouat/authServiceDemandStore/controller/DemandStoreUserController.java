package com.ouat.authServiceDemandStore.controller;

import javax.servlet.http.HttpServletRequest;

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

import com.ouat.authServiceDemandStore.exception.BusinessProcessException;
import com.ouat.authServiceDemandStore.request.CustomerRequest;
import com.ouat.authServiceDemandStore.response.DemandStoreAPIResponse;
import com.ouat.authServiceDemandStore.service.DemandStoreService;

@RestController
@RequestMapping("/token")
public class DemandStoreUserController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DemandStoreUserController.class);
	
	@Autowired
	DemandStoreService service;
	
	@PostMapping("/generate")
	public ResponseEntity<DemandStoreAPIResponse> generateToken(@RequestBody CustomerRequest customer, HttpServletRequest request) throws BusinessProcessException {
		return service.generateToken(customer, request);
	}
	
	@GetMapping("/validate")
	public ResponseEntity<DemandStoreAPIResponse> validateToken(@RequestParam String token, HttpServletRequest request) throws BusinessProcessException {
		return service.validateToken(token, request);
	}

}
