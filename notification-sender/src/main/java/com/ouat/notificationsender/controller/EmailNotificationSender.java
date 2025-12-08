package com.ouat.notificationsender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ouat.notificationsender.request.EmailNotificationRequest;
import com.ouat.notificationsender.response.DemandStoreAPIResponse;
import com.ouat.notificationsender.service.VelocityEngineService;

@RestController
public class EmailNotificationSender {
	
	@Autowired
	VelocityEngineService velocityEngineService;
	
	@PostMapping("/send-email")
	public ResponseEntity<DemandStoreAPIResponse> sendEmail(@RequestBody EmailNotificationRequest request) {
		return velocityEngineService.sendEmail(request);
	}
}
