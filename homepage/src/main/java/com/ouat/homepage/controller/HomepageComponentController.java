package com.ouat.homepage.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ouat.homepage.constants.CommonConstant;
import com.ouat.homepage.exception.BusinessProcessException;
import com.ouat.homepage.interceptor.RequestHeader;
import com.ouat.homepage.interceptor.SessionRequired;
import com.ouat.homepage.response.DemandStoreAPIResponse;
import com.ouat.homepage.service.HomepageComponentService;

@RestController
@SessionRequired
public class HomepageComponentController {
	
	
	@Autowired
	private HomepageComponentService service;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HomepageComponentController.class);
	
	@RequestMapping("/category-tree")
	public ResponseEntity<DemandStoreAPIResponse> findActiveCateogoryTreeDetail() throws BusinessProcessException {
		return ResponseEntity.ok(service.findActiveComponent(CommonConstant.CATEGORY_TREE_KEY, null));
	}
	
	
	@RequestMapping("/sort-bar")
	public ResponseEntity<DemandStoreAPIResponse> findActiveSortBarDetail() throws BusinessProcessException {
		return ResponseEntity.ok(service.findActiveComponent(CommonConstant.SORTBAR_KEY, null));
	}

	@RequestMapping
	public ResponseEntity<DemandStoreAPIResponse> findHomePage(HttpServletRequest request) throws BusinessProcessException {
		Map<String, String> params = new HashMap<>();
		params.put("platform", request.getHeader(RequestHeader.PLATFORM));
		LOGGER.info("Homepage Request FROM " + request.getHeader(RequestHeader.UTM_CAMPAIGN) + " | " + request.getHeader(RequestHeader.UTM_MEDIUM) + " | "+ request.getHeader(RequestHeader.UTM_SOURCE));
		return ResponseEntity.ok(service.findActiveComponent(CommonConstant.HOME_PAGE_KEY+request.getHeader(RequestHeader.PLATFORM) , params));
	}

	@RequestMapping("/page/{urlPattern}")
	public ResponseEntity<DemandStoreAPIResponse> findSpecialPage(@PathVariable String urlPattern, HttpServletRequest request) throws BusinessProcessException {
		Map<String, String> params = new HashMap<>();
		params.put("platform", request.getHeader(RequestHeader.PLATFORM));
		params.put("urlPattern", urlPattern);
		return ResponseEntity.ok(service.findActiveSpecialPageComponent(CommonConstant.SPECIAL_PAGE_KEY+request.getHeader(RequestHeader.PLATFORM) , params));
	}
	
	@GetMapping("/promo-banner")
	public ResponseEntity<DemandStoreAPIResponse> getPromoBanner() throws BusinessProcessException, JsonProcessingException{
		return ResponseEntity.ok(service.promoBannerData());
	}
	
	

}
