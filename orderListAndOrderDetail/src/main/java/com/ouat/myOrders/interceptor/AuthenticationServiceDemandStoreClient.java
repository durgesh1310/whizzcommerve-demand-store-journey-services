package com.ouat.myOrders.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ouat.myOrders.demandStoreAPIResponse.AuthServiceDemandStoreAPIResponse;

@Service
public class AuthenticationServiceDemandStoreClient {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${auth.service.demand.store.url}")
	private String authServiceURL;
	
	public AuthServiceDemandStoreAPIResponse validateAccessToken(String token, HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(RequestHeaders.OS, request.getHeader(RequestHeaders.OS));
		headers.add(RequestHeaders.BROWSER, request.getHeader(RequestHeaders.BROWSER));
		headers.add(RequestHeaders.USER_CLIENT, request.getHeader(RequestHeaders.USER_CLIENT));
		headers.add(RequestHeaders.DEVICE_ID, request.getHeader(RequestHeaders.DEVICE_ID));
		headers.add(RequestHeaders.DEVICE_TYPE, request.getHeader(RequestHeaders.DEVICE_TYPE));
		headers.add(RequestHeaders.KEY, request.getHeader(RequestHeaders.KEY));
		headers.add(RequestHeaders.PLATFORM, request.getHeader(RequestHeaders.PLATFORM));
		headers.add(RequestHeaders.APIVERSION, request.getHeader(RequestHeaders.APIVERSION));
		headers.add(RequestHeaders.APPVERSION, request.getHeader(RequestHeaders.APPVERSION));
		HttpEntity<String> entity = new HttpEntity<>(headers);
		return restTemplate.exchange(authServiceURL+"/token/validate?token="+token, HttpMethod.GET, entity, AuthServiceDemandStoreAPIResponse.class).getBody();
	}
}
