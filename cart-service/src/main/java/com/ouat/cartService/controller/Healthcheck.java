package com.ouat.cartService.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Healthcheck {
	
	
	public static final Logger LOGGER = LoggerFactory.getLogger(Healthcheck.class);
	
	@GetMapping("/ping")
	public String ping() {
		LOGGER.info("Ping Request Received");
		
		return "pong";
	}

}
