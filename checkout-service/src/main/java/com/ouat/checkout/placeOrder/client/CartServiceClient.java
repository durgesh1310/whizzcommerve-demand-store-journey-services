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
public class CartServiceClient {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(CartServiceClient.class);
	
	private static final String API_KEY_AUTH_HEADER_NAME = "AccessKey";

    @Value("${internal.secure.accessKey}")
    private String accessKey;
	
	@Autowired
	private RestTemplate restTemplate;
	@Value("${cart.service.url}")
	private String cartServiceURL;
	
	public boolean clearCustomerCart(Integer customerId, String deviceIdUUID) {
		StringBuilder url = new StringBuilder(cartServiceURL);
		url.append("?customer_id=").append(customerId).append("&device_id=").append(deviceIdUUID);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add(API_KEY_AUTH_HEADER_NAME, accessKey);
		HttpEntity entity = new HttpEntity(headers);
		try {
			LOGGER.info("Cart Service URL : {} ",url.toString());
			ResponseEntity<String> responseEntity = restTemplate.exchange(url.toString(), HttpMethod.DELETE, entity, String.class);
			LOGGER.info("CART Service delete api response : {} ", responseEntity);
			if(responseEntity.getStatusCode() == HttpStatus.OK) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("Error while calling cart service to clear cart : {} ", e.getMessage(), e);
		}
		return false;
	}		
}

