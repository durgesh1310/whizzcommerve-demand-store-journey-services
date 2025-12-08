package com.ouat.orderService.client;

import java.util.Arrays;
import java.util.List;

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

@Service
public class InventoryServiceClient {
	public static final Logger LOGGER = LoggerFactory.getLogger(InventoryServiceClient.class);
	private static final String API_KEY_AUTH_HEADER_NAME = "AccessKey";
	
	@Autowired
	private RestTemplate restTemplate;

	@Value("${inventory.service.inventory.update.url}")
	private String inventoryUpdateUrl;
	
	@Value("${internal.secure.accessKey}")
    private String accessKey;
	
	public boolean updateInventory(List<SkuAndQty> skuAndQtyList) {
		InventoryServiceRequest inventoryServiceRequest = new InventoryServiceRequest();
		inventoryServiceRequest.setSkuAndQtyList(skuAndQtyList);
		HttpHeaders headers = new HttpHeaders();
		headers.add(API_KEY_AUTH_HEADER_NAME, accessKey);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<InventoryServiceRequest> request = new HttpEntity<>(
				inventoryServiceRequest, headers);
		try {
			Boolean inventoryAndCustomerServiceResponse = restTemplate.exchange(inventoryUpdateUrl , HttpMethod.POST, request, Boolean.class).getBody();
			if (null != inventoryAndCustomerServiceResponse) {
				return inventoryAndCustomerServiceResponse;
			}
		} catch (Exception e) {
			LOGGER.error("Error while calling Inventory service for product detail : {} ", e.getMessage(), e);
		}
		return false;
	}

}


