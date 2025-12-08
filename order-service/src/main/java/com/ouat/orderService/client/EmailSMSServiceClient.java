package com.ouat.orderService.client;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ouat.orderService.response.DemandStoreAPIResponse;

@Service
public class EmailSMSServiceClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailSMSServiceClient.class);
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${email.sms.service.baseurl}")
	private String emailSMSServiceURL;
	
	public boolean sendOTP(EmailSendRequest request) {
		boolean flag = false;
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<EmailSendRequest> entity = new HttpEntity<>(request, headers);
		try {
			DemandStoreAPIResponse response = restTemplate
					.exchange(emailSMSServiceURL + "/email", HttpMethod.POST, entity, DemandStoreAPIResponse.class)
					.getBody();
			if (null != response && null != response.getCode() && "200".equals(response.getCode())) {
				flag = true;
				return flag;
			}
		} catch (Exception e) {
			LOGGER.error("Error while calling Email-SMS Service {} ", e.getMessage(), e);
		}
		return flag;
	}
	
	public boolean sendSMS(MessageRequest request) {
		boolean flag = false;
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<MessageRequest> entity = new HttpEntity<>(request, headers);
		try {
			DemandStoreAPIResponse response = restTemplate
					.exchange(emailSMSServiceURL + "/sms/send", HttpMethod.POST, entity, DemandStoreAPIResponse.class)
					.getBody();
			if (null != response && null != response.getCode() && "200".equals(response.getCode())) {
				flag = true;
				return flag;
			}
		} catch (Exception e) {
			LOGGER.error("Error while calling Email-SMS Service {} ", e.getMessage(), e);
		}
		return flag;
	}


	
	
	

}
