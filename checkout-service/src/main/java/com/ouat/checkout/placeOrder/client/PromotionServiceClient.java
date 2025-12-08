package com.ouat.checkout.placeOrder.client;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PromotionServiceClient {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(PromotionServiceClient.class);
	private static final String API_KEY_AUTH_HEADER_NAME = "AccessKey";
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${promotion.service.promotion.service.url}")
	private String promotionServiceURL;
	
	@Value("${internal.secure.accessKey}")
    private String accessKey;
	
	public Boolean addInPromoLog(AddInLogRequest addInLogRequest) {
 	 
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add(API_KEY_AUTH_HEADER_NAME, accessKey);
		HttpEntity<AddInLogRequest> request = new HttpEntity<AddInLogRequest>(
				addInLogRequest, headers);
		try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(promotionServiceURL, HttpMethod.POST, request, String.class);
			if(responseEntity.getStatusCode() == HttpStatus.OK) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("Error while calling catalogue service for product detail : {} ", e.getMessage(), e);
		}
		return false;
	}		
}

