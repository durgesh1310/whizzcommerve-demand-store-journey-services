package com.ouat.customerService.customer.service;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ouat.customerService.customer.response.CustomerDetailResponse;
import com.ouat.customerService.interceptor.AuthServiceDemandStoreTokenGenerateResponse;
import com.ouat.customerService.interceptor.RequestHeaders;

@Service
public class LoginHelper {
	
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${auth.service.demand.store.url}")
	private String authServiceURL;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(LoginHelper.class);
	
	
	public String generateToken(CustomerDetailResponse customerDetailResponse,  HttpServletRequest request) {
		String token = null;
		try {
			AuthServiceDemandStoreTokenGenerateResponse authServiceDemandStoreTokenGenerateResponse = generateAccessToken(customerDetailResponse, request);
			if(null != authServiceDemandStoreTokenGenerateResponse) {
				token = authServiceDemandStoreTokenGenerateResponse.getData();
			}
			
		} catch (Exception e) {
			LOGGER.error("Error while generating token : {} ", e.getMessage(), e);
		}
		return token;
	}
	
	private AuthServiceDemandStoreTokenGenerateResponse generateAccessToken(CustomerDetailResponse customerDetail, HttpServletRequest request) {
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
		HttpEntity<CustomerDetailResponse> entity = new HttpEntity<>(customerDetail, headers);
		return restTemplate.exchange(authServiceURL+"/token/generate", HttpMethod.POST, entity, AuthServiceDemandStoreTokenGenerateResponse.class).getBody();
	}
	

}
