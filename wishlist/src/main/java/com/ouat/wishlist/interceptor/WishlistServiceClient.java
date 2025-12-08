package com.ouat.wishlist.interceptor;

import java.util.HashMap;
import java.util.List;
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

import com.ouat.wishlist.exception.BusinessProcessException;
import com.ouat.wishlist.response.CatalogueServiceAPIResponse;
import com.ouat.wishlist.response.ProductDetailResponse;

@Service
public class WishlistServiceClient {
	

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${catelogue}")
	private String productDetailService;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public List<ProductDetailResponse> makePostCall(List<Long> list) throws BusinessProcessException {
		try {
			Map<String, List<Long>> prdoucts = new HashMap<>();
			prdoucts.put("products", list);
			
			ResponseEntity<CatalogueServiceAPIResponse> response = restTemplate.postForEntity(productDetailService,
					prdoucts ,CatalogueServiceAPIResponse.class);
			if (response.getStatusCode() == HttpStatus.OK && null != response.getBody()) {
				return response.getBody().getData();
			} else {
				log.error("Some error occured : {} ", "No product found", "");
				return null;
			}
		} catch (RestClientException e) {
			log.error("Some error occured : {} ", e.getMessage(), e);
			return null;
		}
	}


}
