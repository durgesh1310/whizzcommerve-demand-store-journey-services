package com.ouat.checkout.placeOrder.client;

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

import com.ouat.checkout.response.DemandStoreAPIResponse;

@Service
public class NotificationSenderClient {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(NotificationSenderClient.class);
	@Autowired
	private RestTemplate restTemplate;

	@Value("${notification.service..order.alert.url}")
	private String notificationSenderForOrderAlert;
	
	
	public boolean sentEmailForOderConformation(PlaceOrderAlertRequest placeOrderAlert) {
 		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<PlaceOrderAlertRequest> request = new HttpEntity<PlaceOrderAlertRequest>(placeOrderAlert, headers);
		try {
			LOGGER.info(" calling the notification service to send order alert with request : {} ",request);
			DemandStoreAPIResponse emailServiceResponse = restTemplate.exchange(notificationSenderForOrderAlert, HttpMethod.POST, request, DemandStoreAPIResponse.class).getBody();
			LOGGER.info(" Notification service successfully called with emailService response : {} ", emailServiceResponse.toString());
			if(null != emailServiceResponse)
				return emailServiceResponse.isSuccess();
		} catch (Exception e) {
			LOGGER.error("Error while calling notification service for order alert email");
		}
		return false;
	}

}


