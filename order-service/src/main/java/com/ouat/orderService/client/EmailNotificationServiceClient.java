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
public class EmailNotificationServiceClient {
	
	@Value("${auth.service.notification.sender.order.alert}")
	private String orderCancelAlertExchangeRequest;
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailSMSServiceClient.class);

	@Autowired
	RestTemplate restTemplate;
	
	public Boolean sendOrderReturnCancelAlert(ReturnExchangeCancelAlert alert) {
		boolean flag = false;
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<ReturnExchangeCancelAlert> entity = new HttpEntity<>(alert, headers);
		try {
			DemandStoreAPIResponse response = restTemplate.exchange( orderCancelAlertExchangeRequest, HttpMethod.POST, entity, DemandStoreAPIResponse.class).getBody();
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
