package com.ouat.suggestionandsearch.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ouat.suggestionandsearch.annotations.SessionRequired;
import com.ouat.suggestionandsearch.controller.header.RequestHeader;
import com.ouat.suggestionandsearch.controller.request.SearchRequest;
import com.ouat.suggestionandsearch.controller.response.CustomerDetailVO;
import com.ouat.suggestionandsearch.controller.response.DemandStoreAPIResponse;
import com.ouat.suggestionandsearch.services.AutoSearchService;
import com.ouat.suggestionandsearch.services.RecommendationService;

@RestController
@CrossOrigin
@RequestMapping("/search-plp")
@SessionRequired
public class AutoSearchController {

    private static Logger log = LoggerFactory.getLogger(AutoSearchController.class);

    @Autowired
    private AutoSearchService service;

    @Autowired
    private RecommendationService recommendationService;
    
    @PostMapping(value = "/{search-query}")
    public ResponseEntity<DemandStoreAPIResponse> getSearchResponses(@RequestBody SearchRequest query, HttpServletRequest servletRequest) {
        CustomerDetailVO customerDetail = (CustomerDetailVO) servletRequest.getAttribute(RequestHeader.CUSTOMER_DETAIL);
        return service.getSearchResponses(query, customerDetail);
    }
    
    @GetMapping(value = "/{search-query}")
    public ResponseEntity<DemandStoreAPIResponse> getSearchResponses(
            @RequestParam Map<String, String> params, HttpServletRequest servletRequest) {
        CustomerDetailVO customerDetail =
                (CustomerDetailVO) servletRequest.getAttribute(RequestHeader.CUSTOMER_DETAIL);
        log.info("param : {}", params);
        return service.getSearchResponsesV1(params, customerDetail);
    }

    @GetMapping(value = "/recommend/{productId}")
    public ResponseEntity<DemandStoreAPIResponse> getRecommendationResponses(
            @PathVariable Integer productId, HttpServletRequest servletRequest) {
        return recommendationService.getRecommendationResponses(productId);
    }

}
