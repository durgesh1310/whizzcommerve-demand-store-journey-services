package com.ouat.myOrders.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ouat.myOrders.interceptor.DemandStoreLoginRequired;

@RestController
@DemandStoreLoginRequired
public class HealthCheck {
	@GetMapping("/ping")
	public String healthCheck() {
		return "pong";
	}

}
