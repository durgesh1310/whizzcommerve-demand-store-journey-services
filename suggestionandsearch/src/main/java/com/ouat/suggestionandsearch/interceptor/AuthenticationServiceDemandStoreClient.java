package com.ouat.suggestionandsearch.interceptor;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.ouat.suggestionandsearch.controller.header.RequestHeader;

 

@Service
public class AuthenticationServiceDemandStoreClient {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${auth.service.demand.store.url}")
	private String authServiceURL;
	
	public AuthServiceDemandStoreAPIResponse validateAccessToken(String token, HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(RequestHeader.OS, request.getHeader(RequestHeader.OS));
		headers.add(RequestHeader.BROWSER, request.getHeader(RequestHeader.BROWSER));
		headers.add(RequestHeader.USER_CLIENT, request.getHeader(RequestHeader.USER_CLIENT));
		headers.add(RequestHeader.DEVICE_ID, request.getHeader(RequestHeader.DEVICE_ID));
		headers.add(RequestHeader.DEVICE_TYPE, request.getHeader(RequestHeader.DEVICE_TYPE));
		headers.add(RequestHeader.KEY, request.getHeader(RequestHeader.KEY));
		headers.add(RequestHeader.PLATFORM, request.getHeader(RequestHeader.PLATFORM));
		headers.add(RequestHeader.APIVERSION, request.getHeader(RequestHeader.APIVERSION));
		headers.add(RequestHeader.APPVERSION, request.getHeader(RequestHeader.APPVERSION));
		HttpEntity<String> entity = new HttpEntity<>(headers);
		return restTemplate.exchange(authServiceURL+"/token/validate?token="+token, HttpMethod.GET, entity, AuthServiceDemandStoreAPIResponse.class).getBody();
	}
}
