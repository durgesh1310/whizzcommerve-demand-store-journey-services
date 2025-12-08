package com.ouat.suggestionandsearch.apistore.clients;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouat.suggestionandsearch.apistore.APIStoreHandler;
import com.ouat.suggestionandsearch.constants.DownStreamConstants;
import com.ouat.suggestionandsearch.constants.RequestType;
import com.ouat.suggestionandsearch.controller.response.DemandStoreAPIResponse;
import com.ouat.suggestionandsearch.exception.DownStreamException;
import com.ouat.suggestionandsearch.util.MetricsUtility;
import com.ouat.suggestionandsearch.util.Utility;

@Component
public class APIStoreClient {

    private static final Logger log = LoggerFactory.getLogger(APIStoreClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private APIStoreHandler apiStoreHandler;

    @Autowired
    private ObjectMapper mapper;

    public List<Long> getCustomerWishListProductIds(Long customerId, String transactionId,
            HttpHeaders downStreamHeaders) throws DownStreamException {
        boolean isSuccess = false;
        StopWatch timer = new StopWatch();
        timer.start();
        try {
            String fullUrl = apiStoreHandler.fetchAPIStoreWishListGetProductId(customerId);
            HttpEntity<?> httpEntity = new HttpEntity<>(null, downStreamHeaders);
            log.info("Going to hit the Url = {}, customerId = {}", fullUrl, customerId);
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(fullUrl, HttpMethod.GET, httpEntity, String.class);
            DemandStoreAPIResponse response = Utility.convertJsonToObject(responseEntity.getBody(),
                    DemandStoreAPIResponse.class);
            isSuccess = handleResponse(response, transactionId, RequestType.GET_WISHLIST_PRODUCTIDS,
                    DownStreamConstants.WISHLIST_SERVICE);
            if (response.getData() == null) {
                return new ArrayList<Long>();
            }
            String jsonKey = Utility.getJson(response.getData());
            Map<String, List<Long>> wishListResponse =
                    mapper.readValue(jsonKey, new TypeReference<Map<String, List<Long>>>() {});
            return wishListResponse.get("products");
        } catch (Exception ex) {
            handleException(ex, transactionId, RequestType.GET_WISHLIST_PRODUCTIDS);
        } finally {
            timer.stop();
            handleFinallyBlock(timer.getTotalTimeSeconds(), RequestType.GET_WISHLIST_PRODUCTIDS,
                    isSuccess, transactionId, DownStreamConstants.WISHLIST_SERVICE);
        }
        return null;
    }

    private void handleException(Exception ex, String transactionId, String requestType)
            throws DownStreamException {
        throw new DownStreamException("Exception occurred while processing request, RequestType="
                + requestType + "," + " TransactionId=" + transactionId, ex);
    }

    private boolean handleResponse(DemandStoreAPIResponse responseEntity, String transactionId,
            String requestType, String downStreamName)
            throws DownStreamException, JsonProcessingException {
        if (Integer.parseInt(responseEntity.getCode()) == HttpStatus.OK.value()) {
            log.info(
                    "Successful Response from {}, ResponseCode = {}, Response = {}, TransactionId = {}",
                    downStreamName, responseEntity.getCode(), Utility.getJson(responseEntity),
                    transactionId);
            return true;
        }
        log.error(
                "Error response received from {}, ResponseCode = {}, Response = {}, TransactionId = {}",
                downStreamName, responseEntity.getCode(), Utility.getJson(responseEntity),
                transactionId);
        throw new DownStreamException("Error response received from " + downStreamName
                + ", RequestType = " + requestType + ", TransactionId = " + transactionId);
    }

    private void handleFinallyBlock(Double duration, String requestType, boolean isSuccess,
            String transactionId, String downStreamName) {
        MetricsUtility.addDownStreamMetrics(downStreamName, duration, requestType,
                transactionId);
    }

}
