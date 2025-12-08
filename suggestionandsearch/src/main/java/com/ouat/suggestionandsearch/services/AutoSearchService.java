package com.ouat.suggestionandsearch.services;

import static com.ouat.suggestionandsearch.constants.DownStreamConstants.ELASTICSEARCH;
import static com.ouat.suggestionandsearch.constants.RequestType.GET_SEARCH_RESPONSE;
import static com.ouat.suggestionandsearch.util.AutoSearchUtility.rangeQueryAttributes;
import static com.ouat.suggestionandsearch.util.AutoSearchUtility.termQueryProductAttributes;
import static com.ouat.suggestionandsearch.util.AutoSearchUtility.termQueryProductHirerarcy;
import static com.ouat.suggestionandsearch.util.AutoSearchUtility.termQuerySkuAttributes;
import static com.ouat.suggestionandsearch.util.MetricsUtility.addDownStreamMetrics;
import static com.ouat.suggestionandsearch.util.MetricsUtility.addMethodMetrics;
import static com.ouat.suggestionandsearch.util.Utility.getTrackingId;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import com.ouat.suggestionandsearch.apistore.clients.APIStoreClient;
import com.ouat.suggestionandsearch.builder.AutoSearchBuilder;
import com.ouat.suggestionandsearch.configuration.ElasticSearchConfiguration;
import com.ouat.suggestionandsearch.controller.request.SearchRequest;
import com.ouat.suggestionandsearch.controller.response.CustomerDetailVO;
import com.ouat.suggestionandsearch.controller.response.DemandStoreAPIResponse;

@Service
public class AutoSearchService {

	private static final Logger log = LoggerFactory.getLogger(AutoSearchService.class);
	private static final String SEARCH_RESPONSES = "GetSearchResponses";
	private Set<Long>customerWishListProductIds;

	@Autowired
	private AutoSearchBuilder builder;
	
	@Autowired
	private APIStoreClient apiStoreClient;

	@Autowired
	private ElasticSearchConfiguration config;
	
	@Value("${auto.search.index.name:}")
	private String searchIndex;

	private RestHighLevelClient client;

	private String trackingId;
	
	@PostConstruct
	public void init() {
		client = config.elasticsearchClient();
		trackingId = getTrackingId();
		customerWishListProductIds = new HashSet<Long>();
	}

	public ResponseEntity<DemandStoreAPIResponse> getSearchResponses(SearchRequest query, CustomerDetailVO customerDetail) {
        StopWatch timer = new StopWatch();
        timer.start();
        DemandStoreAPIResponse demandStoreResponse = new DemandStoreAPIResponse();
        try {
            validateRequest(query, searchIndex);

            CompletableFuture<SearchResponse> exactMatchSearchResponse = CompletableFuture.supplyAsync(() -> {
                try {
                    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
                    log.info("Going to perform exactMatchSearch RequestType={}, transactionId={}", GET_SEARCH_RESPONSE, trackingId);
                    queryBuilder.must(builder.fullTextSearchQueryWithExactSearchBuilder(query));
                    return searchWithFilteredAggregations(query, queryBuilder);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            
            CompletableFuture<SearchResponse> priorityMatchSearchResponse = CompletableFuture.supplyAsync(() -> {
                try {
                    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
                    log.info("Going to perform priorityMatchSearchResponse RequestType={}, transactionId={}", GET_SEARCH_RESPONSE, trackingId);
                    queryBuilder.must(builder.fullTextSearchQueryBuilderWithMorePriorityAttributes(query));
                    return searchWithFilteredAggregations(query, queryBuilder);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            
            CompletableFuture<SearchResponse> generalSearchResponse = CompletableFuture.supplyAsync(() -> {
                try {
                    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
                    log.info("Going to do generalSearchResponse  RequestType={}, transactionId={}", GET_SEARCH_RESPONSE, trackingId);
                    queryBuilder.must(builder.fullTextSearchQueryBuilder(query));
                    return searchWithFilteredAggregations(query, queryBuilder);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            
            SearchResponse response = getRequestedSearchResponse(exactMatchSearchResponse, generalSearchResponse, priorityMatchSearchResponse);
            
            if (customerDetail != null) {
                customerWishListProductIds =
                        apiStoreClient.getCustomerWishListProductIds(customerDetail.getCustomerId(),
                                trackingId, null).stream().collect(Collectors.toSet());
            }
            return buildSuccessResponse(query, demandStoreResponse, response, customerWishListProductIds);
        } catch (Exception ex) {
            log.error("some error occurrerd in RequestType = {}, TrackingId = {}, Error = {}", GET_SEARCH_RESPONSE,
                    trackingId, ExceptionUtils.getStackTrace(ex));
        } finally {
            timer.stop();
            addMethodMetrics(SEARCH_RESPONSES, timer.getTotalTimeSeconds(), GET_SEARCH_RESPONSE, trackingId);
        }
        return buildFailureResponse(demandStoreResponse);
    }
	
	public ResponseEntity<DemandStoreAPIResponse> getSearchResponsesV1(Map<String, String> params, CustomerDetailVO customerDetail) {
		StopWatch timer = new StopWatch();
		timer.start();
		DemandStoreAPIResponse demandStoreResponse = new DemandStoreAPIResponse();
		try {
            SearchRequest query = builder.buildSearchRequest(params, trackingId);
            validateRequest(query, searchIndex);

			CompletableFuture<SearchResponse> exactMatchSearchResponse = CompletableFuture.supplyAsync(() -> {
                try {
                    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
                    log.info("Going to perform exactMatchSearch RequestType={}, transactionId={}", GET_SEARCH_RESPONSE, trackingId);
                    queryBuilder.must(builder.fullTextSearchQueryWithExactSearchBuilder(query));
                    return searchWithFilteredAggregations(query, queryBuilder);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
			
			CompletableFuture<SearchResponse> priorityMatchSearchResponse = CompletableFuture.supplyAsync(() -> {
                try {
                    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
                    log.info("Going to perform priorityMatchSearchResponse RequestType={}, transactionId={}", GET_SEARCH_RESPONSE, trackingId);
                    queryBuilder.must(builder.fullTextSearchQueryBuilderWithMorePriorityAttributes(query));
                    return searchWithFilteredAggregations(query, queryBuilder);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
			
			CompletableFuture<SearchResponse> generalSearchResponse = CompletableFuture.supplyAsync(() -> {
                try {
                    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
                    log.info("Going to do generalSearchResponse  RequestType={}, transactionId={}", GET_SEARCH_RESPONSE, trackingId);
                    queryBuilder.must(builder.fullTextSearchQueryBuilder(query));
                    return searchWithFilteredAggregations(query, queryBuilder);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
			
			SearchResponse response = getRequestedSearchResponse(exactMatchSearchResponse, generalSearchResponse, priorityMatchSearchResponse);
			
            if (customerDetail != null) {
                customerWishListProductIds =
                        apiStoreClient.getCustomerWishListProductIds(customerDetail.getCustomerId(),
                                trackingId, null).stream().collect(Collectors.toSet());
            }
			return buildSuccessResponse(query, demandStoreResponse, response, customerWishListProductIds);
		} catch (Exception ex) {
			log.error("some error occurrerd in RequestType = {}, TrackingId = {}, Error = {}", GET_SEARCH_RESPONSE,
					trackingId, ExceptionUtils.getStackTrace(ex));
		} finally {
			timer.stop();
			addMethodMetrics(SEARCH_RESPONSES, timer.getTotalTimeSeconds(), GET_SEARCH_RESPONSE, trackingId);
		}
		return buildFailureResponse(demandStoreResponse);
	}

    private SearchResponse getRequestedSearchResponse(
            CompletableFuture<SearchResponse> exactMatchSearchResponse,
            CompletableFuture<SearchResponse> generalSearchResponse,
            CompletableFuture<SearchResponse> priorityMatchSearchResponse) {

        try {
            SearchResponse response = exactMatchSearchResponse.get();
            if (response == null || response.getHits().getTotalHits().value == 0) {
                try {
                    SearchResponse prioritySearchResponse = priorityMatchSearchResponse.get();
                    if (prioritySearchResponse == null
                            || prioritySearchResponse.getHits().getTotalHits().value == 0) {
                        return generalSearchResponse.get();
                    } else {
                        return prioritySearchResponse;
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                return response;
            }
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }

    private ResponseEntity<DemandStoreAPIResponse> buildFailureResponse(DemandStoreAPIResponse demandStoreResponse) {
		demandStoreResponse.setSuccess(false);
		demandStoreResponse.setCode(String.format("%d", HttpStatus.TEMPORARY_REDIRECT.value()));
		return new ResponseEntity<DemandStoreAPIResponse>(demandStoreResponse, HttpStatus.TEMPORARY_REDIRECT);
	}

	private ResponseEntity<DemandStoreAPIResponse> buildSuccessResponse(SearchRequest query,
			DemandStoreAPIResponse demandStoreResponse, SearchResponse response, Set<Long> customerWishListProductIds) {
		demandStoreResponse.setData(builder.plpResponseBuilder(query, response, trackingId, customerWishListProductIds));
		demandStoreResponse.setCode(String.format("%d", HttpStatus.OK.value()));
		return new ResponseEntity<DemandStoreAPIResponse>(demandStoreResponse, HttpStatus.OK);
	}

	private SearchResponse searchWithFilteredAggregations(SearchRequest query, BoolQueryBuilder queryBuilder) throws IOException {

		query.getFilters().stream().filter(filter -> rangeQueryAttributes.contains(filter.getKey()))
				.forEach(filter -> queryBuilder.must(filter.toQuery()));

		List<AggregationBuilder> minRangeFilterAgg = getMinRangeFilterAggregation(rangeQueryAttributes, query);
		List<AggregationBuilder> maxRangeFilterAgg = getMaxRangeFilterAggregation(rangeQueryAttributes, query);

		List<AggregationBuilder> allAggregation = getAllAggregations(query);

		BoolQueryBuilder postFilterQuery = builder.postFilterQueryBuilder(query);

		FieldSortBuilder sortBuilder = query.getSortBy().toQuery();

		return search(queryBuilder, postFilterQuery, query.getFrom(), allAggregation, minRangeFilterAgg,
				maxRangeFilterAgg, sortBuilder, trackingId);
	}

	private SearchResponse search(BoolQueryBuilder queryBuilder, BoolQueryBuilder postFilterQuery, Integer from,
			List<AggregationBuilder> allAggregation, List<AggregationBuilder> minRangeFilterAgg,
			List<AggregationBuilder> maxRangeFilterAgg, FieldSortBuilder sortBuilder, String trackingId)
			throws IOException {

		StopWatch timer = new StopWatch();
		timer.start();

		org.elasticsearch.action.search.SearchRequest request = new org.elasticsearch.action.search.SearchRequest();
		request.indices(searchIndex);

		builder.searchSourceBuilder(queryBuilder, postFilterQuery, from, allAggregation, minRangeFilterAgg,
				maxRangeFilterAgg, sortBuilder, request);

		BytesRef source = XContentHelper.toXContent(request.source(), XContentType.JSON, ToXContent.EMPTY_PARAMS, true)
				.toBytesRef();

		SearchResponse response = client.search(request, RequestOptions.DEFAULT);

		timer.stop();

		log.info("SearchQUERY {}, TotalHits = {}, RequestType = {}, TrackingId = {}", source.utf8ToString(),
				response.getHits().getTotalHits().value, GET_SEARCH_RESPONSE, trackingId);
		addDownStreamMetrics(ELASTICSEARCH, timer.getTotalTimeSeconds(), GET_SEARCH_RESPONSE, trackingId);
		return response;
	}

	private List<AggregationBuilder> getAllAggregations(SearchRequest query) {
		List<AggregationBuilder> aggregationBuilders = new ArrayList<AggregationBuilder>();
		for (String attribute : termQueryProductHirerarcy) {
			AggregationBuilder aggregationBuilder = AggregationBuilders.terms("by_" + attribute)
					.field(attribute + ".keyword");
			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
			appendFiltersInRangeAggregrations(query, attribute, queryBuilder);
			if (!queryBuilder.filter().isEmpty()) {
				aggregationBuilder = AggregationBuilders.filter("by_" + attribute, queryBuilder)
						.subAggregation(aggregationBuilder);
			}
			aggregationBuilders.add(aggregationBuilder);
		}

		for (String attribute : termQueryProductAttributes) {
			AggregationBuilder aggregationBuilder = AggregationBuilders.nested("by_" + attribute, "attributes")
					.subAggregation(AggregationBuilders.terms("all_" + attribute)
							.field("attributes." + attribute + ".attribute_value" + ".keyword"));
			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
			appendFiltersInRangeAggregrations(query, attribute, queryBuilder);
			if (!queryBuilder.filter().isEmpty()) {
				aggregationBuilder = AggregationBuilders.filter("by_" + attribute, queryBuilder)
						.subAggregation(aggregationBuilder);
			}
			aggregationBuilders.add(aggregationBuilder);
		}
		
		for (String attribute : termQuerySkuAttributes) {
            AggregationBuilder aggregationBuilder = 
                    AggregationBuilders.nested("by_" + attribute, "skus.attributes")
                    .subAggregation(AggregationBuilders.terms("all_" + attribute)
                            .field("skus.attributes." + attribute + ".attribute_value" + ".keyword"));
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            appendFiltersInRangeAggregrations(query, attribute, queryBuilder);
            if (!queryBuilder.filter().isEmpty()) {
                aggregationBuilder = AggregationBuilders.filter("by_" + attribute, queryBuilder)
                        .subAggregation(aggregationBuilder);
            }
            aggregationBuilders.add(aggregationBuilder);
        }
		return aggregationBuilders;
	}

    private List<AggregationBuilder> getMaxRangeFilterAggregation(Set<String> rangeQueryAttributes,
            SearchRequest query) {
        List<AggregationBuilder> maxAggregationBuilder = new ArrayList<AggregationBuilder>();
        for (String attribute : rangeQueryAttributes) {
            AggregationBuilder aggregationBuilder = AggregationBuilders.max("max_" + attribute)
                    .field(String.format("to_" + attribute));
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            appendFiltersInRangeAggregrations(query, attribute, queryBuilder);
            if (!queryBuilder.filter().isEmpty()) {
                aggregationBuilder = AggregationBuilders.filter("max_" + attribute, queryBuilder)
                        .subAggregation(aggregationBuilder);
            }

            maxAggregationBuilder.add(aggregationBuilder);
        }
        return maxAggregationBuilder;
    }

    private List<AggregationBuilder> getMinRangeFilterAggregation(Set<String> rangeQueryAttributes,
            SearchRequest query) {
        List<AggregationBuilder> minAggregationBuilder = new ArrayList<AggregationBuilder>();
        for (String attribute : rangeQueryAttributes) {
            AggregationBuilder aggregationBuilder = AggregationBuilders.min("min_" + attribute)
                    .field(String.format("from_" + attribute));
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            appendFiltersInRangeAggregrations(query, attribute, queryBuilder);
            if (!queryBuilder.filter().isEmpty()) {
                aggregationBuilder = AggregationBuilders.filter("min_" + attribute, queryBuilder)
                        .subAggregation(aggregationBuilder);
            }
            minAggregationBuilder.add(aggregationBuilder);
        }
        return minAggregationBuilder;
    }

	private void appendFiltersInRangeAggregrations(SearchRequest query, String attribute,
            BoolQueryBuilder queryBuilder) {

        Map<String, List<String>> filterKeyValuesMapProductHirearchy =
                new HashMap<String, List<String>>();
        Map<String, List<String>> filterKeyValuesMapProductLevelAttributes =
                new HashMap<String, List<String>>();

        query.getFilters().stream().filter(filter -> !filter.getKey().equals(attribute)) // filter
                                                                                         // out
                                                                                         // itself
                .forEach(filter -> {
                    if (termQueryProductHirerarcy
                            .contains(filter.getKey().toLowerCase().equalsIgnoreCase("category")
                                    ? "product_type"
                                    : filter.getKey().toLowerCase())) {
                        log.debug("category is converted to product_type");
                        String key = filter.getKey().toLowerCase().equalsIgnoreCase("category")
                                ? "product_type"
                                : filter.getKey().toLowerCase();
                        if (filterKeyValuesMapProductHirearchy.get(key) == null) {
                            List<String> values = new ArrayList<String>();
                            values.add(filter.getValue());
                            filterKeyValuesMapProductHirearchy.put(key, values);
                        } else {
                            filterKeyValuesMapProductHirearchy.get(key).add(filter.getValue());
                        }
                    } else if (termQueryProductAttributes.contains(filter.getKey().toLowerCase())) {
                        if (filterKeyValuesMapProductLevelAttributes.get(filter.getKey()) == null) {
                            List<String> values = new ArrayList<String>();
                            values.add(filter.getValue());
                            filterKeyValuesMapProductLevelAttributes.put(filter.getKey(), values);
                        } else {
                            filterKeyValuesMapProductLevelAttributes.get(filter.getKey())
                                    .add(filter.getValue());
                        }
                    } else if (rangeQueryAttributes.contains(filter.getKey().toLowerCase())) {
                        queryBuilder.filter().add(filter.toQuery());
                    }
                });

        filterKeyValuesMapProductHirearchy.keySet().stream().forEach(filter -> {
            queryBuilder.filter().add(QueryBuilders.boolQuery().should(QueryBuilders.termsQuery(
                    filter + ".keyword", filterKeyValuesMapProductHirearchy.get(filter))));
        });

        filterKeyValuesMapProductLevelAttributes.keySet().stream().forEach(filter -> {
            queryBuilder.filter()
                    .add(QueryBuilders.boolQuery()
                            .must(QueryBuilders.nestedQuery("attributes",
                                    QueryBuilders.boolQuery().should(QueryBuilders.termsQuery(
                                            "attributes." + filter + ".attribute_value" + ".keyword",
                                            filterKeyValuesMapProductLevelAttributes.get(filter))),
                                    ScoreMode.None)));
        });
    }
	
	private void validateRequest(SearchRequest query, String searchIndex) throws RuntimeException {
		if (!StringUtils.hasText(query.getQuery())) {
			throw new RuntimeException("Search query is null");
		} else if (!StringUtils.hasText(searchIndex)) {
			throw new RuntimeException("Index Name found to be null");
		}
	}

}
