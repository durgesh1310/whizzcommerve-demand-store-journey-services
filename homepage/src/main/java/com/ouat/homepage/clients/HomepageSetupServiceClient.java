package com.ouat.homepage.clients;

import java.net.URLEncoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouat.homepage.constants.CommonConstant;
import com.ouat.homepage.exception.BusinessProcessException;


@Service
public class HomepageSetupServiceClient {
	
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${homepage.setup.service.baseurl}")
	private String homepageSetupService;
	
	@Autowired
	private ObjectMapper mapper;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HomepageSetupServiceClient.class);

	
	public String makeGetCall(String endPoint, Map<String, String> parameter) throws BusinessProcessException {
		try {
			StringBuilder finalURL = new StringBuilder(homepageSetupService + endPoint);
			StringBuilder params = new StringBuilder();
			if (null != parameter && !parameter.isEmpty()) {
				for (String key : parameter.keySet()) {
					params.append(key).append("=").append(URLEncoder.encode(parameter.get(key))).append("&");
				}
				finalURL.append("?").append(params);
				finalURL.deleteCharAt(finalURL.length()-1);  
			}
			ResponseEntity<APIResponse> response = restTemplate.getForEntity(finalURL.toString(),
					APIResponse.class);
			if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
				try {
					return mapper.writeValueAsString(response.getBody().getData());
				} catch (JsonProcessingException e) {
					LOGGER.error("Error while parsing API response : {} ", e.getMessage(), e);
				}
			} else {
				LOGGER.error("API call to homepage service failed !!");
				throw new BusinessProcessException(CommonConstant.GENERIC_ERROR_MSG, CommonConstant.SUCCESS_STATUS_CODE);
			}
		} catch (RestClientException e) {
			LOGGER.error("Exception while making API call to homepage service {} ",e.getMessage(), e);
			throw new BusinessProcessException(CommonConstant.GENERIC_ERROR_MSG, CommonConstant.SUCCESS_STATUS_CODE);
		}
		return null;
	}


	
	

}
