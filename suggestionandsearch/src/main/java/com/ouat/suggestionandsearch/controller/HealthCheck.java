package com.ouat.suggestionandsearch.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {
	
	
	private static final Logger log = LoggerFactory.getLogger(HealthCheck.class);
	
	@GetMapping("/ping")
	public String ping() {
		log.info("ping api called");
		return "pong";
	}
}
