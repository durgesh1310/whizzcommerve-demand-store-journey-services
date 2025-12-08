package com.ouat.suggestionandsearch.services;

import static com.ouat.suggestionandsearch.constants.DownStreamConstants.ELASTICSEARCH;
import static com.ouat.suggestionandsearch.util.MetricsUtility.addDownStreamMetrics;
import static com.ouat.suggestionandsearch.util.MetricsUtility.addMethodMetrics;
import static com.ouat.suggestionandsearch.util.Utility.getTrackingId;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.util.CollectionUtils;

import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import com.google.common.collect.Lists;
import com.ouat.suggestionandsearch.builder.RecommedationSearchBuilder;
import com.ouat.suggestionandsearch.configuration.ElasticSearchConfiguration;
import com.ouat.suggestionandsearch.controller.response.DemandStoreAPIResponse;
import com.ouat.suggestionandsearch.controller.response.RecommendationResponse;
import com.ouat.suggestionandsearch.controller.response.RecommendationResponse.Details;
import com.ouat.suggestionandsearch.dtos.autosearch.ProductDto;
import com.ouat.suggestionandsearch.elasticsearch.documents.SearchDocument;
import com.ouat.suggestionandsearch.enums.RecommendationType;
import com.ouat.suggestionandsearch.exception.BusinessProcessException;
import com.ouat.suggestionandsearch.util.Utility;

@Service
public class RecommendationService {

    private static final Logger log = LoggerFactory.getLogger(RecommendationService.class);
    private static final String RECOMMENDATION_RESPONSES = "GetRecommendationResponses";
    private static final Integer PAGE_SIZE = 20;
    private static final Integer NEW_ARRIVAL_PAGE_SIZE = 10;
    private static final Integer THRESHOLD = 5;
    private static final Integer MIN_SIMILAR_PRODUCTS = 12;
    private static final Integer MIN_NEW_ARRIVALS = 5;
    // Mention attributes only in lower case letters
    private static Set<String> excludeAttributes = Stream
            .of("country of origin", "google product category", "offers", "edd", "instructions")
            .collect(Collectors.toSet());

    @Autowired
    private ElasticSearchConfiguration config;

    @Autowired
    private RecommedationSearchBuilder builder;

    @Value("${auto.search.index.name:}")
    private String searchIndex;

    private RestHighLevelClient client;

    private String trackingId;

    @PostConstruct
    public void init() {
        client = config.elasticsearchClient();
        trackingId = getTrackingId();
    }

    public ResponseEntity<DemandStoreAPIResponse> getRecommendationResponses(Integer productId) {
        StopWatch timer = new StopWatch();
        timer.start();
        DemandStoreAPIResponse demandStoreResponse = new DemandStoreAPIResponse();
        try {
            validateRequest(searchIndex);

            GetResponse productDetails =
                    builder.getProductDetails(client, searchIndex, trackingId, productId);

            if (!productDetails.isExists())
                throw new BusinessProcessException("Product doesn't exists");

            SearchDocument productDetailsDocument = Utility
                    .convertJsonToObject(productDetails.getSourceAsString(), SearchDocument.class);

            BoolQueryBuilder queryBuilder =
                    builder.recommendationQueryBuilder(excludeAttributes, productDetailsDocument);
            SearchResponse recommendationResponse = search(queryBuilder, PAGE_SIZE);
            List<ProductDto> allProducts = builder.productsResponseBuilder(
                    recommendationResponse.getHits().getHits(), trackingId);

            builder.newArrivalRecommnendationQueryBuilder(queryBuilder, allProducts);
            SearchResponse newArrivalSearchResponse = search(queryBuilder, NEW_ARRIVAL_PAGE_SIZE);
            List<ProductDto> newArrivalProducts = builder.productsResponseBuilder(
                    newArrivalSearchResponse.getHits().getHits(), trackingId);

            return buildSuccessResponse(demandStoreResponse, allProducts, newArrivalProducts);
        } catch (BusinessProcessException ex) {
            log.warn("Business Logic Occurred in RequestType={}, TrackingId={}, Message={}",
                    RECOMMENDATION_RESPONSES, trackingId, ExceptionUtils.getMessage(ex));
        } catch (Exception ex) {
            log.error("some error occurrerd in RequestType={}, TrackingId={}, Error={}",
                    RECOMMENDATION_RESPONSES, trackingId, ExceptionUtils.getStackTrace(ex));
        } finally {
            timer.stop();
            addMethodMetrics(RECOMMENDATION_RESPONSES, timer.getTotalTimeSeconds(),
                    RECOMMENDATION_RESPONSES, trackingId);
        }
        return buildFailureResponse(demandStoreResponse);
    }

    private SearchResponse search(BoolQueryBuilder queryBuilder, Integer pageSize)
            throws IOException {

        StopWatch timer = new StopWatch();
        timer.start();

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(pageSize);
        sourceBuilder.query(queryBuilder);
        sourceBuilder.sort("to_discount", SortOrder.DESC);

        SearchRequest request = new SearchRequest();
        request.indices(searchIndex);
        request.source(sourceBuilder);

        BytesRef source = XContentHelper
                .toXContent(request.source(), XContentType.JSON, ToXContent.EMPTY_PARAMS, true)
                .toBytesRef();

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        timer.stop();
        log.info("SearchQUERY={}, TotalHits={}, RequestType={}, TrackingId={}",
                source.utf8ToString(), response.getHits().getTotalHits().value,
                RECOMMENDATION_RESPONSES, trackingId);
        addDownStreamMetrics(ELASTICSEARCH, timer.getTotalTimeSeconds(), RECOMMENDATION_RESPONSES,
                trackingId);
        return response;
    }

    private ResponseEntity<DemandStoreAPIResponse> buildFailureResponse(
            DemandStoreAPIResponse demandStoreResponse) {
        demandStoreResponse.setSuccess(false);
        demandStoreResponse.setCode(String.format("%d", HttpStatus.TEMPORARY_REDIRECT.value()));
        return new ResponseEntity<DemandStoreAPIResponse>(demandStoreResponse,
                HttpStatus.TEMPORARY_REDIRECT);
    }

    private ResponseEntity<DemandStoreAPIResponse> buildSuccessResponse(
            DemandStoreAPIResponse demandStoreResponse, List<ProductDto> allProducts,
            List<ProductDto> newArrivalProducts) {
        List<Details> details = new ArrayList<RecommendationResponse.Details>();
        int totalRecommendations = allProducts.size();
        RecommendationResponse recommendationResponse = new RecommendationResponse();
        if (totalRecommendations < MIN_SIMILAR_PRODUCTS) {

            if (totalRecommendations > THRESHOLD) {
                details.add(
                        buildDetails(RecommendationType.SIMILIAR_PRODUCTS.toString(), allProducts));
            }
        } else {
            List<List<ProductDto>> partitionedList = Lists.partition(allProducts,
                    (int) Math.round(Math.ceil(allProducts.size() / 2)));
            details.add(
                    buildDetails(RecommendationType.BEST_DEAL.toString(), partitionedList.get(0)));
            details.add(buildDetails(RecommendationType.SIMILIAR_PRODUCTS.toString(),
                    partitionedList.get(1)));
        }

        if (!CollectionUtils.isEmpty(newArrivalProducts)
                && newArrivalProducts.size() > MIN_NEW_ARRIVALS) {
            details.add(
                    buildDetails(RecommendationType.NEW_ARRIVAL.toString(), newArrivalProducts));
        }

        recommendationResponse.setResults(details);
        demandStoreResponse.setData(recommendationResponse);
        demandStoreResponse.setCode(String.format("%d", HttpStatus.OK.value()));
        return new ResponseEntity<DemandStoreAPIResponse>(demandStoreResponse, HttpStatus.OK);
    }

    private Details buildDetails(String key, List<ProductDto> products) {
        Details details = new Details();
        details.setKey(key);
        details.setValues(products);
        return details;
    }

    private void validateRequest(String searchIndex) throws RuntimeException {
        if (!StringUtils.hasText(searchIndex)) {
            throw new RuntimeException("Index Name found to be null");
        }
    }

}
