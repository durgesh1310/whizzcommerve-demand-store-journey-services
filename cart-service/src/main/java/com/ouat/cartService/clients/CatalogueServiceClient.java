package com.ouat.cartService.clients;

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

import com.ouat.cartService.shoppingCartResponse.CatalogueServiceResponse;

@Service
public class CatalogueServiceClient {

	public static final Logger LOGGER = LoggerFactory.getLogger(CatalogueServiceClient.class);

	@Autowired
	private RestTemplate restTemplate;

	@Value("${productItemDetailUrl.api.url}")
	private String productItemDetailUrl;

	public ProductItemAndProductAttributeDetailResponse getProducItemDetailList(List<String> skus) {
		ProductItemDetailRequest productItemDetailRequest = new ProductItemDetailRequest();
		productItemDetailRequest.setSkus(skus);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<ProductItemDetailRequest> request = new HttpEntity<ProductItemDetailRequest>(
				productItemDetailRequest, headers);
		try {
			CatalogueServiceResponse catalogueServiceResponse = restTemplate
					.exchange(productItemDetailUrl, HttpMethod.POST, request, CatalogueServiceResponse.class).getBody();
			if (catalogueServiceResponse != null && catalogueServiceResponse.getData() != null) {
				return catalogueServiceResponse.getData();
			}
		} catch (Exception e) {
			LOGGER.error("Error while calling catalogue service for product detail : {} ", e.getMessage(), e);
		}
		return null;
	}
}
