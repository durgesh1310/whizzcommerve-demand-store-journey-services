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

import com.ouat.orderService.response.CustomerDetailVO;
import com.ouat.orderService.response.DemandStoreAPIResponse;

@Service
public class NotificationSenderClient {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(NotificationSenderClient.class);
	@Autowired
	private RestTemplate restTemplate;

	@Value("${auth.service.notification.order.alert.url}")
	private String notificationSenderForOrderAlert;
	
	
	public boolean sentEmailForExchangeOrderAlert(ExchangeOrderAlertRequest exchangeOrderAlert) {
		boolean flag = false;
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<ExchangeOrderAlertRequest> entity = new HttpEntity<>(exchangeOrderAlert, headers);
		try {
			DemandStoreAPIResponse response = restTemplate.exchange( notificationSenderForOrderAlert, HttpMethod.POST, entity, DemandStoreAPIResponse.class).getBody();
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
