package com.ouat.suggestionandsearch.services;

import static com.ouat.suggestionandsearch.constants.CommonConstant.EMPTY_QUERY;
import static com.ouat.suggestionandsearch.constants.RequestType.GET_SEARCH_RESPONSE;
import static com.ouat.suggestionandsearch.constants.RequestType.GET_SUGGESTIONS;
import static com.ouat.suggestionandsearch.util.MetricsUtility.addMethodMetrics;
import static com.ouat.suggestionandsearch.util.Utility.getJson;
import static com.ouat.suggestionandsearch.util.Utility.toLowerCase;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ouat.suggestionandsearch.builder.AutoSuggestionBuilder;
import com.ouat.suggestionandsearch.controller.response.DemandStoreAPIResponse;
import com.ouat.suggestionandsearch.elasticsearch.documents.AutoSuggestionDocument;
import com.ouat.suggestionandsearch.elasticsearch.repositories.AutoSuggestionRepository;
import com.ouat.suggestionandsearch.exception.BadRequestException;

@Service
public class AutoSuggestionService {

	private static final Logger log = LoggerFactory.getLogger(AutoSuggestionService.class);
	private static final String SUGGESTION_RESPONSES = "GetSuggestionResponses";

	@Autowired
	private AutoSuggestionBuilder builder;

	@Autowired
	private AutoSuggestionRepository repository;

	public ResponseEntity<DemandStoreAPIResponse> getSuggestions(String searchQuery, String trackingId) {
		StopWatch timer = new StopWatch();
		timer.start();
		try {
			validateRequest(searchQuery);
			SearchResponse response = repository.findByText(builder.getAutoSuggestionQuery(toLowerCase(searchQuery)),
					trackingId);
			List<AutoSuggestionDocument> results = builder.getAutoSuggestionsResponse(response);
			logSuggestionsForAnalysis(searchQuery, trackingId, results);
			return buildSuccessResponse(results);
		} catch (Exception ex) {
			log.info("some error occured RequesTType = {}, TrackingId = {}, Error = {}", GET_SUGGESTIONS, trackingId,
					ExceptionUtils.getStackTrace(ex));
		} finally {
			timer.stop();
			addMethodMetrics(SUGGESTION_RESPONSES, timer.getTotalTimeSeconds(), GET_SEARCH_RESPONSE, trackingId);
		}
		return buildFailureResponse();
	}

	private ResponseEntity<DemandStoreAPIResponse> buildFailureResponse() {
		DemandStoreAPIResponse response = new DemandStoreAPIResponse();
		response.setSuccess(false);
		response.setCode(String.format("%d", HttpStatus.BAD_REQUEST.value()));
		return new ResponseEntity<DemandStoreAPIResponse>(response, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<DemandStoreAPIResponse> buildSuccessResponse(List<AutoSuggestionDocument> results) {
		DemandStoreAPIResponse response = new DemandStoreAPIResponse();
		response.setCode(String.format("%d", HttpStatus.OK.value()));
		response.setData(results);
		return new ResponseEntity<DemandStoreAPIResponse>(response, HttpStatus.OK);
	}

	private void logSuggestionsForAnalysis(String searchQuery, String trackingId, List<AutoSuggestionDocument> results)
			throws JsonProcessingException {
		String jsonString = getJson(results);
		if (results == null || results.isEmpty()) {
			logSearchQuery(searchQuery, trackingId);
		} else {
			cacheAutoSuggestions(searchQuery, results, trackingId, jsonString);
		}
	}

	private void logSearchQuery(String searchQuery, String trackingId) {
		log.info("Responses are null for RequestType = {}, TrackingId = {}, SearchQuery = {}", GET_SUGGESTIONS,
				trackingId, searchQuery);
	}

	// use below method for implementing caching in future
	private void cacheAutoSuggestions(String searchQuery, List<AutoSuggestionDocument> results, String trackingId,
			String jsonString) {
		log.info("Result for RequestType = {}, TrackingId = {}, SearchQuery = {}, Responses = {}", GET_SUGGESTIONS,
				trackingId, searchQuery, jsonString);
	}

	private void validateRequest(String query) throws BadRequestException {
		if (!StringUtils.hasText(query))
			throw new BadRequestException(EMPTY_QUERY);
	}

}
