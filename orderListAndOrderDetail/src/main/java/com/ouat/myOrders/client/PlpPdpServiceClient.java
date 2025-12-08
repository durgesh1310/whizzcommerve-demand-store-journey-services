package com.ouat.myOrders.client;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

import com.ouat.myOrders.DTO.ProductNameAndProductImgUrlDTO;
 
@Service
public class PlpPdpServiceClient {

	public static final Logger LOGGER = LoggerFactory.getLogger(PlpPdpServiceClient.class);

	@Autowired
	private RestTemplate restTemplate;

	@Value("${productImageAndProductUrl.api.url}")
	private String productItemDetailUrl;

	public Map<String , ProductNameAndProductImgUrlDTO> getProudctNameAndProductImgUrl(List<String> skus) {
		LOGGER.info("Catalogue Service Request : {} ", skus);
		ProductItemDetailRequest productItemDetailRequest = new ProductItemDetailRequest();
		productItemDetailRequest.setSkus(skus);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<ProductItemDetailRequest> request = new HttpEntity<ProductItemDetailRequest>(productItemDetailRequest, headers);
		try {
			CatalogueServiceResponse catalogueServiceResponse = restTemplate.exchange(productItemDetailUrl, HttpMethod.POST, request, CatalogueServiceResponse.class).getBody();
			if (catalogueServiceResponse != null && catalogueServiceResponse.getData() != null) {
				LOGGER.info("Catalogue Service Response : {} ", catalogueServiceResponse.getData());
				return catalogueServiceResponse.getData();
			}
		} catch (Exception e) {
			LOGGER.error("Error while calling catalogue service for product detail : {} ", e.getMessage(), e);
		}
		return null;
	}

}
