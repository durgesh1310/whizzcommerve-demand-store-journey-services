package com.ouat.homepage.service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouat.homepage.clients.HomepageSetupServiceClient;
import com.ouat.homepage.constants.CommonConstant;
import com.ouat.homepage.exception.BusinessProcessException;
import com.ouat.homepage.response.DemandStoreAPIResponse;
import com.ouat.homepage.response.PromoBannerResponse;

@Service
public class HomepageComponentService {
	
	@Autowired
	CacheService cacheService;
	
	@Autowired
	RedisHelper redisHelper;
	
	@Autowired
	ObjectMapper mapper;

	@Autowired
	private HomepageSetupServiceClient homepageSetupServiceClient;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HomepageComponentService.class);
	
	@Value("${promo.banner.image.mobile}")
	private String mobileImage;
	
	@Value("${promo.banner.image.web}")
	private String webImage;
	
	@Value("${promo.banner.url.web}")
	private String urlWeb;
	
	@Value("${promo.banner.action}")
	private String action;
	
	@Value("${promo.banner.type}")
	private String type;
	
	@Value("${promo.banner.image.mweb}")
	private String mwebImage;
	
	@Value("${promo.banner.show}")
	private Boolean showPromoBanner;
	
	@Value("${promo.banner.mobile.id}")
	private Integer mobileBannerId;

	
	public DemandStoreAPIResponse findActiveComponent(String componentKey, Map<String, String> params) throws BusinessProcessException {
		LOGGER.info("Going to fetch : {} ", componentKey);
		Long start = System.currentTimeMillis();
		DemandStoreAPIResponse storeAPIResponse = new DemandStoreAPIResponse();
		try {
			if (null != cacheService.cache.get(componentKey)) {
				try {
					Object data = mapper.readValue(cacheService.cache.get(componentKey), Object.class);
					storeAPIResponse.setData(data);
					storeAPIResponse.setCode(CommonConstant.SUCCESS_STATUS_CODE);
				} catch (JsonProcessingException jpe) {
					LOGGER.error("Error while reading data from cache for homepage component : {} ", jpe.getMessage(),
							jpe);
					throw new BusinessProcessException(CommonConstant.GENERIC_ERROR_MSG, CommonConstant.SUCCESS_STATUS_CODE);
				}
			}
		} catch (ExecutionException ex) {
			Object data = null;
			try {
				String json = loadComponentFromService(componentKey, params);
				data = mapper.readValue(json, Object.class);
				storeAPIResponse.setData(data);
				storeAPIResponse.setCode(CommonConstant.SUCCESS_STATUS_CODE);
			} catch (JsonProcessingException e) {
				LOGGER.error("Error while reading data from API response for homepage component : {} ",e.getMessage(), e);
				throw new BusinessProcessException(CommonConstant.GENERIC_ERROR_MSG, CommonConstant.SUCCESS_STATUS_CODE);
			}
		} finally {
			LOGGER.info("Total time took to fetch : {} ", System.currentTimeMillis() - start);
		}
		
		return storeAPIResponse;
	}
	
	
	private String loadComponentFromService(String key, Map<String, String> params) throws BusinessProcessException {
		String json = homepageSetupServiceClient.makeGetCall(CommonConstant.COMPONENT_URL_MAPPING.get(key), params);
		if(json == null || "".equals(json.trim())) {
			LOGGER.info("No response from API call for homepage component : key : {} and params {} ", key, params);
			throw new BusinessProcessException(CommonConstant.GENERIC_ERROR_MSG, CommonConstant.SUCCESS_STATUS_CODE);
		}
		redisHelper.set(key, json, CommonConstant.TTL);
		return json;
	}

	
	public DemandStoreAPIResponse findActiveSpecialPageComponent(String componentKey, Map<String, String> params) throws BusinessProcessException {
		DemandStoreAPIResponse storeAPIResponse = new DemandStoreAPIResponse();
		LOGGER.info("Going to fetch special page : {} ", componentKey);
		Long start = System.currentTimeMillis();
		try {
			if (null != cacheService.cache.get(componentKey+"_"+params.get("urlPattern"))) {
				try {
					Object data = mapper.readValue(cacheService.cache.get(componentKey+"_"+params.get("urlPattern")), Object.class);
					storeAPIResponse.setData(data);
					storeAPIResponse.setCode(CommonConstant.SUCCESS_STATUS_CODE);
				} catch (JsonMappingException e) {
					throw new BusinessProcessException(CommonConstant.GENERIC_ERROR_MSG, CommonConstant.SUCCESS_STATUS_CODE);
				} catch (JsonProcessingException e) {
					throw new BusinessProcessException(CommonConstant.GENERIC_ERROR_MSG, CommonConstant.SUCCESS_STATUS_CODE);
				}
			}
		} catch (ExecutionException ex) {
			Object data = null;
			try {
				String json = loadSpecialPageComponentFromService(componentKey, params);
				data = mapper.readValue(json, Object.class);
				storeAPIResponse.setData(data);
				storeAPIResponse.setCode(CommonConstant.SUCCESS_STATUS_CODE);
			} catch (JsonMappingException e) {
				throw new BusinessProcessException(CommonConstant.GENERIC_ERROR_MSG, CommonConstant.SUCCESS_STATUS_CODE);
			} catch (JsonProcessingException e) {
				throw new BusinessProcessException(CommonConstant.GENERIC_ERROR_MSG, CommonConstant.SUCCESS_STATUS_CODE);
			}
		} finally {
			LOGGER.info("Total time took to fetch special page : {} ", System.currentTimeMillis() - start);
		}
		return storeAPIResponse;
	}
	
	
	private String loadSpecialPageComponentFromService(String key, Map<String, String> params) throws BusinessProcessException {
		String json = homepageSetupServiceClient.makeGetCall(CommonConstant.COMPONENT_URL_MAPPING.get(key), params);
		if(json == null || "".equals(json.trim())) {
			throw new BusinessProcessException(CommonConstant.GENERIC_ERROR_MSG, CommonConstant.SUCCESS_STATUS_CODE);
		}
		redisHelper.set(key+"_"+params.get("urlPattern"), json, CommonConstant.TTL);
		return json;
	}


	public void deleteKey(String key) {
		redisHelper.del(key);
	}
	
	public DemandStoreAPIResponse promoBannerData() throws BusinessProcessException, JsonProcessingException {
		DemandStoreAPIResponse storeAPIResponse = new DemandStoreAPIResponse();
		storeAPIResponse.setCode(CommonConstant.SUCCESS_STATUS_CODE);
		storeAPIResponse.setData(setPromoBannerData());
		return storeAPIResponse;
	}
	
	public PromoBannerResponse setPromoBannerData() {
		PromoBannerResponse promoBannerData = new PromoBannerResponse();
		if(showPromoBanner == true) {
			promoBannerData.setImageWeb(webImage);
			promoBannerData.setImageMobile(mobileImage);
			promoBannerData.setImageMweb(mwebImage);
			promoBannerData.setUrl(urlWeb);
			promoBannerData.setAction(action);
			promoBannerData.setType(type);
			promoBannerData.setMobileBannerId(mobileBannerId);
		}else {
			promoBannerData = null;
		}
		return promoBannerData;
	}
	
	

}
