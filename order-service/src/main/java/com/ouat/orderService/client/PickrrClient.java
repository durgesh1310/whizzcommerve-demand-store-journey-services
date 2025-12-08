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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ouat.orderService.exception.DownStreamException;

@Service
public class PickrrClient {

	public static final Logger log = LoggerFactory.getLogger(PickrrClient.class);

	@Autowired
	private RestTemplate restTemplate;

	@Value("${pickrr.shipping.api.url}")
	private String bestShipperUrl;

	@Value("${pickrr.auth.token}")
	private String pickrrAuthToken;

	public AllocateShipperResponse getTrackingIdAndCourierFromPickrr(String allocateBestShipperRequestBody)
			throws DownStreamException {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("pickrr.auth.token", pickrrAuthToken);
		headers.add("pickrr.shipping.api.url", bestShipperUrl);
 		HttpEntity<String> entity = new HttpEntity<>(allocateBestShipperRequestBody, headers);
		ResponseEntity<AllocateShipperResponse> responseEntity = restTemplate.exchange(bestShipperUrl, HttpMethod.POST, entity,AllocateShipperResponse.class);
		log.info("picker : {}", responseEntity.getBody());
		//return Utility.convertJsonToObject(responseEntity.getBody(), AllocateShipperResponse.class);
		return responseEntity.getBody();
	}

}