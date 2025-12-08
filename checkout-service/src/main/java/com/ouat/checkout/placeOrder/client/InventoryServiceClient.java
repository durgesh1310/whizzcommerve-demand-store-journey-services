package com.ouat.checkout.placeOrder.client;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouat.checkout.constant.CommonConstant;
import com.ouat.checkout.exception.BusinessProcessException;
import com.ouat.checkout.response.DemandStoreAPIResponse;

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
	
	@Value("${inventory.service.inventory.add.url}")
	private String addInventoryUrl;
	
	@Autowired
	ObjectMapper mapper;
	
	public boolean updateInventory(List<SkuAndQty> skuAndQtyList) {
		HttpEntity<InventoryServiceRequest> request = InventoryRequest(skuAndQtyList);

	try {
		LOGGER.info("hitting url : {}, for update inventory, request : {}", inventoryUpdateUrl,request);
		String  inventoryUpdate = restTemplate.exchange(inventoryUpdateUrl, HttpMethod.POST, request, String.class).getBody();
		LOGGER.info("  inventory service successfully called with response : {} ", inventoryUpdate);
		DemandStoreAPIResponse demandStoreAPIResponse = mapper.readValue(inventoryUpdate, DemandStoreAPIResponse.class);
		if(! demandStoreAPIResponse.isSuccess() || demandStoreAPIResponse.getCode().equals(CommonConstant.FAILURE_STATUS_CODE)) {
			LOGGER.info("Error response from inventory service : {} ", demandStoreAPIResponse.toString());
			throw new BusinessProcessException(CommonConstant.OUT_OF_STOCK,  CommonConstant.FAILURE_STATUS_CODE);
		} 
	   return true;
		 
	} catch (Exception e) {
		LOGGER.error("Error by calling inventory service, Error={}", ExceptionUtils.getStackTrace(e));	
		}
	return false;
}
	
	public boolean addInventory(List<SkuAndQty> skuAndQtyList) {
		HttpEntity<InventoryServiceRequest> request = InventoryRequest(skuAndQtyList);
	try {
		LOGGER.info("hitting url: {}, for add inventory, request : {}",addInventoryUrl, request);
		Boolean  inventoryUpdate = restTemplate.exchange(addInventoryUrl, HttpMethod.POST,request, Boolean.class).getBody();
 		 return inventoryUpdate;
	} catch (Exception e) {
		LOGGER.error("Error by calling inventory service, Error={}", ExceptionUtils.getStackTrace(e));	
		return false;
		}
}

	private HttpEntity<InventoryServiceRequest> InventoryRequest(List<SkuAndQty> skuAndQtyList) {
		InventoryServiceRequest inventoryServiceRequest = new InventoryServiceRequest();
		inventoryServiceRequest.setSkuAndQtyList(skuAndQtyList);
		HttpHeaders headers = new HttpHeaders();
		headers.add(API_KEY_AUTH_HEADER_NAME, accessKey);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<InventoryServiceRequest> request = new HttpEntity<>(
				inventoryServiceRequest, headers);
		return request;
	}

}


