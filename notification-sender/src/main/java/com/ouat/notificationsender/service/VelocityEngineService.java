package com.ouat.notificationsender.service;

import java.io.StringWriter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
//import org.springframework.ui.velocity.VelocityEngineUtils;
//import org.springframework.ui.velocity.VelocityEngineUtils;

import com.ouat.notificationsender.client.EmailSMSServiceClient;
import com.ouat.notificationsender.client.EmailSendRequest;
import com.ouat.notificationsender.request.EmailNotificationRequest;
import com.ouat.notificationsender.response.DemandStoreAPIResponse;

@Service
public class VelocityEngineService {
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired
	EmailSMSServiceClient emailSMSServiceClient;
	/**
	 * This is sample method to send email content to customers, we have to change it as per our need
	 * @param request
	 * @return
	 * 
	 */
	public ResponseEntity<DemandStoreAPIResponse> sendEmail(EmailNotificationRequest request) {
		
		VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("title", "Apache Velocity Test");
        StringWriter writer = new StringWriter();
		velocityEngine.mergeTemplate("templates/nudge.vm", "UTF-8", velocityContext,  writer);
		EmailSendRequest emailSendRequest = new EmailSendRequest();
		emailSendRequest.setFromEmail(request.getFromEmail());
		emailSendRequest.setFromNickName(request.getFromNickName());
		emailSendRequest.setMessageBody(writer.toString());
		emailSendRequest.setSubject(request.getSubject());
		emailSendRequest.setToEmailAddress(request.getToEmailAddress());
		if(emailSMSServiceClient.sendEmail(emailSendRequest)) {
			return ResponseEntity.ok(new DemandStoreAPIResponse(true, null, null, null));
		} else {
			return ResponseEntity.ok(new DemandStoreAPIResponse(false, null, null, null));
		}
	}
}
