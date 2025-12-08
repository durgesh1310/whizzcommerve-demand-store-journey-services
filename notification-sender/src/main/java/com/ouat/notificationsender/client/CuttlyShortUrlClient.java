package com.ouat.notificationsender.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CuttlyShortUrlClient {
	
	private static final Logger log = LoggerFactory.getLogger(CuttlyShortUrlClient.class);
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${cuttly.get.api.url}")
	private String cuttlyUrl;
	
	@Value("${cuttly.access.key}")
	private String accessKey;
	
	public String createShortLink(Long customerId) {
		String param = "utm_medium="+customerId;
 		String taggdUrl = "https://www.taggd.com?"+param;
 		
 		String finalUrl = cuttlyUrl + "&key="+ accessKey + "&url=" + taggdUrl;
		try {
			ResponseEntity<CuttlyShortUrlClientResponse> response = restTemplate.getForEntity(finalUrl, CuttlyShortUrlClientResponse.class);
			log.info("downstream api call has been successfully excecuted with response : {}", response);
			if(null == response || null == response.getBody() || null == response.getBody().getShortLink()) {
				return taggdUrl;
			}else {
				return response.getBody().getShortLink();
			} 
		} catch (Exception e) {
			log.error("Error while calling Email-SMS Service {} ", e.getMessage(), e);
		}
		return taggdUrl;
	}

}
