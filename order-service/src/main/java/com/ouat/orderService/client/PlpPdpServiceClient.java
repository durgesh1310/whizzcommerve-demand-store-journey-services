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

import com.ouat.orderService.exchange.dto.OrderExchangeDto;
import com.ouat.orderService.response.DemandStoreAPIResponse;

@Service
public class PlpPdpServiceClient {
	
	public static final Logger log = LoggerFactory.getLogger(PlpPdpServiceClient.class);
	
	private static final String ACCESS_KEY = "AccessKey";
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${plp-pdp.service.sku.fetch.detail.url}")
	private String skuDetailsFetch;
	
	@Value("${internal.secure.accessKey}")
    private String accessKey;
	
	public DemandStoreAPIResponse getSkuDetails(ExchangeSkuRequest sku ) {
		log.info("Response Body : {}", sku.toString());
		DemandStoreAPIResponse response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add(ACCESS_KEY, accessKey);
		HttpEntity<ExchangeSkuRequest> entity = new HttpEntity<>(sku, headers);
		try {
			response = restTemplate
					.exchange(skuDetailsFetch, HttpMethod.POST, entity, DemandStoreAPIResponse.class)
					.getBody();
			log.info("Response : {}",response.toString());
			
		} catch (Exception e) {
			log.error("Error while calling PLP-PDP Service {} ", e.getMessage(), e);
		}
		return response;
	}
	
	

}
