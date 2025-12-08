package com.ouat.checkout.apistore.clients;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouat.checkout.apistore.APIStoreHandler;
import com.ouat.checkout.constant.DownStreamConstant;
import com.ouat.checkout.constant.DownStreamConstant.RequestType;
import com.ouat.checkout.dto.AddressDetailDto;
import com.ouat.checkout.dto.ApplyPromoRequestDto;
import com.ouat.checkout.dto.CustomerCreditDto;
import com.ouat.checkout.dto.PromoDto;
import com.ouat.checkout.dto.ShowShoppingCartItemDto;
import com.ouat.checkout.dto.SkuPricingInventoryCodDetailsDto;
import com.ouat.checkout.exception.DownStreamException;
import com.ouat.checkout.response.DemandStoreAPIResponse;
import com.ouat.checkout.util.MetricsUtility;
import com.ouat.checkout.util.Utility;

@Component
public class APIStoreClient {

    private static final Logger log = LoggerFactory.getLogger(APIStoreClient.class);
    private static final String API_KEY_AUTH_HEADER_NAME = "AccessKey";

    @Value("${internal.secure.accessKey}")
    private String accessKey;
    
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private APIStoreHandler apiStoreHandler;
    
    @Autowired
    private ObjectMapper mapper;

    public List<AddressDetailDto> getCustomerAddressResponse(Integer customerId,
            String transactionId, HttpHeaders downStreamHeaders) throws DownStreamException {
        boolean isSuccess = false;
        StopWatch timer = new StopWatch();
        timer.start();
        try {
            String fullUrl = apiStoreHandler.fetchAPIStoreCustomerAddressDetailsUrl(customerId);
            downStreamHeaders.add(API_KEY_AUTH_HEADER_NAME, accessKey);
            HttpEntity<?> httpEntity = new HttpEntity<>(null, downStreamHeaders);
            log.info("Going to hit the Url = {}, customerId = {}", fullUrl, customerId);
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(fullUrl, HttpMethod.GET, httpEntity, String.class);
            DemandStoreAPIResponse response = Utility.convertJsonToObject(responseEntity.getBody(),
                    DemandStoreAPIResponse.class);
            isSuccess = handleResponse(response, transactionId,
                    RequestType.GET_CUSTOMER_ADDRESS_RESPONSE, DownStreamConstant.CUSTOMER_SERVICE);
            String jsonKey = Utility.getJson(response.getData());
            return mapper.readValue(jsonKey, new TypeReference<List<AddressDetailDto>>() {});
        } catch (Exception ex) {
            handleException(ex, transactionId, RequestType.GET_CUSTOMER_ADDRESS_RESPONSE);
        } finally {
            timer.stop();
            handleFinallyBlock(timer.getTotalTimeSeconds(),
                    RequestType.GET_CUSTOMER_ADDRESS_RESPONSE, isSuccess, transactionId,
                    DownStreamConstant.CUSTOMER_SERVICE);
        }
        return null;
    }

    public List<CustomerCreditDto> getCustomerCreditResponse(Integer customerId,
            String transactionId, HttpHeaders downStreamHeaders) throws DownStreamException {
        boolean isSuccess = false;
        StopWatch timer = new StopWatch();
        timer.start();
        try {
            String fullUrl = apiStoreHandler.fetchAPIStoreCustomerCreditUrl(customerId);
            log.info("Going to hit the Url = {}, customerId = {}", fullUrl, customerId);
            downStreamHeaders.add(API_KEY_AUTH_HEADER_NAME, accessKey);
            HttpEntity<?> httpEntity = new HttpEntity<>(null, downStreamHeaders);
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(fullUrl, HttpMethod.GET, httpEntity, String.class);
            DemandStoreAPIResponse response = Utility.convertJsonToObject(responseEntity.getBody(),
                    DemandStoreAPIResponse.class);
            isSuccess = handleResponse(response, transactionId,
                    RequestType.GET_CUSTOMER_CREDIT_RESPONSE, DownStreamConstant.CUSTOMER_SERVICE);
            String jsonKey = Utility.getJson(response.getData());
            return mapper.readValue(jsonKey, new TypeReference<List<CustomerCreditDto>>() {});
        } catch (Exception ex) {
            handleException(ex, transactionId, RequestType.GET_CUSTOMER_CREDIT_RESPONSE);
        } finally {
            timer.stop();
            handleFinallyBlock(timer.getTotalTimeSeconds(),
                    RequestType.GET_CUSTOMER_CREDIT_RESPONSE, isSuccess, transactionId,
                    DownStreamConstant.CUSTOMER_SERVICE);
        }
        return null;
    }

    public PromoDto getPromoCodeResponse(String customerId, String promoCode, String transactionId,
            ApplyPromoRequestDto applyPromoRequest, HttpHeaders downStreamHeaders)
            throws DownStreamException {
        boolean isSuccess = false;
        StopWatch timer = new StopWatch();
        timer.start();
        try {
            String requestBody = Utility.getJson(applyPromoRequest);
            String fullUrl =
                    apiStoreHandler.fetchAPIStorePromoCodeDetailsUrl(promoCode);
            log.info("Going to hit the Url = {}, customerId = {}", fullUrl, customerId);
            log.info("Promotion Service Request Body = {}", requestBody);
            downStreamHeaders.add(API_KEY_AUTH_HEADER_NAME, accessKey);
            HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, downStreamHeaders);
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(fullUrl, HttpMethod.POST, httpEntity, String.class);
            log.info("Promotion Service Response Body = {}", responseEntity.getBody());
            DemandStoreAPIResponse response = Utility.convertJsonToObject(responseEntity.getBody(),
                    DemandStoreAPIResponse.class);
            isSuccess = handleResponse(response, transactionId, RequestType.GET_PROMO_CODE_RESPONSE,
                    DownStreamConstant.PROMOTION_SERVICE);
            String jsonKey = Utility.getJson(response.getData());
            return mapper.readValue(jsonKey, new TypeReference<PromoDto>() {});
        } catch (Exception ex) {
            handleException(ex, transactionId, RequestType.GET_PROMO_CODE_RESPONSE);
        } finally {
            timer.stop();
            handleFinallyBlock(timer.getTotalTimeSeconds(), RequestType.GET_PROMO_CODE_RESPONSE,
                    isSuccess, transactionId, DownStreamConstant.PROMOTION_SERVICE);
        }
        return null;
    }
    public List<ShowShoppingCartItemDto> getCustomerShowShoppingCartItemResponse(String customerId,
            String platform, String transactionId) throws DownStreamException {
        boolean isSuccess = false;
        StopWatch timer = new StopWatch();
        timer.start();
        try {
            String fullUrl = apiStoreHandler.fetchAPIStoreCustomerShoppingCartDetailsUrl(customerId,
                    platform);
            log.info("Going to hit the Url = {}, customerId = {}", fullUrl, customerId);
            
            HttpHeaders downStreamHeaders = new HttpHeaders();
            downStreamHeaders.add(API_KEY_AUTH_HEADER_NAME, accessKey);
            
            HttpEntity<?>httpEntity = new HttpEntity<>(null, downStreamHeaders);
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(fullUrl, HttpMethod.GET, httpEntity, String.class);
            DemandStoreAPIResponse response = Utility.convertJsonToObject(responseEntity.getBody(),
                    DemandStoreAPIResponse.class);
            isSuccess = handleResponse(response, transactionId,
                    RequestType.GET_CUSTOMER_SHOW_SHOPPING_CART_ITEM_RESPONSE,
                    DownStreamConstant.CART_SERVICE);
            String jsonKey = Utility.getJson(response.getData());
            return mapper.readValue(jsonKey, new TypeReference<List<ShowShoppingCartItemDto>>() {});
        } catch (Exception ex) {
            handleException(ex, transactionId,
                    RequestType.GET_CUSTOMER_SHOW_SHOPPING_CART_ITEM_RESPONSE);
        } finally {
            timer.stop();
            handleFinallyBlock(timer.getTotalTimeSeconds(),
                    RequestType.GET_CUSTOMER_SHOW_SHOPPING_CART_ITEM_RESPONSE, isSuccess,
                    transactionId, DownStreamConstant.CART_SERVICE);
        }
        return null;
    }

    public SkuPricingInventoryCodDetailsDto getSkusInventoryCodDetailsResponse(List<String> skus,
            String transactionId, HttpHeaders downStreamHeaders) throws DownStreamException {
        boolean isSuccess = false;
        StopWatch timer = new StopWatch();
        timer.start();
        try {
            String requestBody = Utility.getJson(skus);
            String fullUrl = apiStoreHandler.fetchAPIStoreSkusInventoryCodDetailsUrl();
            downStreamHeaders.add(API_KEY_AUTH_HEADER_NAME, accessKey);
            log.info("Going to hit the Url = {}, skus = {}", fullUrl, requestBody);
            HttpEntity<?> httpEntity = new HttpEntity<>(requestBody, downStreamHeaders);
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(fullUrl, HttpMethod.POST, httpEntity, String.class);
            log.info("Response = {}", Utility.getJson(responseEntity));
            DemandStoreAPIResponse response = Utility.convertJsonToObject(responseEntity.getBody(),
                    DemandStoreAPIResponse.class);
            isSuccess = handleResponse(response, transactionId,
                    RequestType.GET_SKUS_INVENTORY_COD_DETAILS_RESPONSE,
                    DownStreamConstant.PLP_PDP_SERVICE);
            String jsonKey = Utility.getJson(response.getData());
            return mapper.readValue(jsonKey,
                    new TypeReference<SkuPricingInventoryCodDetailsDto>() {});
        } catch (Exception ex) {
            handleException(ex, transactionId, RequestType.GET_SKUS_INVENTORY_COD_DETAILS_RESPONSE);
        } finally {
            timer.stop();
            handleFinallyBlock(timer.getTotalTimeSeconds(),
                    RequestType.GET_SKUS_INVENTORY_COD_DETAILS_RESPONSE, isSuccess, transactionId,
                    DownStreamConstant.PLP_PDP_SERVICE);
        }
        return null;
    }

    private void handleException(Exception ex, String transactionId, String requestType)
            throws DownStreamException {
        throw new DownStreamException("Exception occurred while processing request, RequestType="
                + requestType + "," + " TransactionId=" + transactionId, ex);
    }


    private boolean handleResponse(DemandStoreAPIResponse responseEntity, String transactionId,
            String requestType, String downStreamName) throws DownStreamException {
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
        MetricsUtility.addDownStreamMetrics(isSuccess, downStreamName, duration, requestType,
                transactionId);
    }

}
