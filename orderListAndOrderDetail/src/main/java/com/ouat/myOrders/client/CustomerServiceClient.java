package com.ouat.myOrders.client;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouat.myOrders.response.Address;
@Service
public class CustomerServiceClient {
	public static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceClient.class);
	private static final String API_KEY_AUTH_HEADER_NAME = "AccessKey";

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${internal.secure.accessKey}")
    private String accessKey;

	@Value("${customerServiceUrl}")
	private String  customerServiceurl;

	public Address getCustomerShippingAddressDetail(Integer customerId, Integer shippingAddressId ) {
		String customerServiceUrl = customerServiceurl + "internal" + "/" + customerId +"/address/" + shippingAddressId;
		HttpHeaders headers = new HttpHeaders();
		headers.add(API_KEY_AUTH_HEADER_NAME, accessKey);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> request = new HttpEntity<>(headers);
		try {
			ResponseEntity<CustomerServiceResponse>  customerServiceResponse = restTemplate.exchange(customerServiceUrl, HttpMethod.GET,request, CustomerServiceResponse.class);
  			if (customerServiceResponse != null && customerServiceResponse.getBody().getData() != null) {
				return customerServiceResponse.getBody().getData();
			}
		} catch (Exception e) {
			LOGGER.error("Error while calling customer service for  address detail : {} ", e.getMessage(), e);
		}
		return null;
	}

}
