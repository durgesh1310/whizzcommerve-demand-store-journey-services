package com.ouat.checkout.apistore.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouat.checkout.app.config.PaymentGatewayConfig;
import com.ouat.checkout.constant.DownStreamConstant;
import com.ouat.checkout.constant.DownStreamConstant.RequestType;
import com.ouat.checkout.dto.payments.RazorPayCreateOrderRequestDto;
import com.ouat.checkout.dto.payments.RazorPayCreateOrderResponseDto;
import com.ouat.checkout.exception.DownStreamException;
import com.ouat.checkout.placeOrder.request.PaymentGatewayCustomerDetail;
import com.ouat.checkout.util.MetricsUtility;
import com.ouat.checkout.util.Utility;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentGatewayClient {

    private static final Logger log = LoggerFactory.getLogger(PaymentGatewayClient.class);

    @Qualifier("razorPayRestTemplate")
    private final RestTemplate razorPayRestTemplate;

    private final PaymentGatewayConfig paymentGatewayConfig;

    private final ObjectMapper mapper;

    public RazorPayCreateOrderResponseDto createOrderOnRazorpayResponse(
            RazorPayCreateOrderRequestDto createOrderRequest, String transactionId)
            throws DownStreamException {
        boolean isSuccess = false;
        StopWatch timer = new StopWatch();
        timer.start();
        try {
            String fullUrl = paymentGatewayConfig.getRazorPay().getUrl();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBasicAuth(paymentGatewayConfig.getRazorPay().getAccessKey(),
                    paymentGatewayConfig.getRazorPay().getSecretKey());
            HttpEntity<RazorPayCreateOrderRequestDto> httpEntity =
                    new HttpEntity<>(createOrderRequest, headers);
            log.info("Going to hit the Url = {}, RequestBody={}, ouatOrderId = {}, TranscationId={}", fullUrl, Utility.getJson(createOrderRequest),
                    createOrderRequest.getReceipt(), transactionId);

            ResponseEntity<String> responseEntity = razorPayRestTemplate.exchange(fullUrl,
                    HttpMethod.POST, httpEntity, String.class);

            isSuccess = handleResponse(responseEntity, transactionId,
                    RequestType.CREATE_ORDER_ON_RAZORPAY_RESPONSE, DownStreamConstant.RAZORPAY);
            return mapper.readValue(responseEntity.getBody(),
                    new TypeReference<RazorPayCreateOrderResponseDto>() {});
        } catch (Exception ex) {
            handleException(ex, transactionId, RequestType.CREATE_ORDER_ON_RAZORPAY_RESPONSE);
        } finally {
            timer.stop();
            handleFinallyBlock(timer.getTotalTimeSeconds(),
                    RequestType.CREATE_ORDER_ON_RAZORPAY_RESPONSE, isSuccess, transactionId,
                    DownStreamConstant.RAZORPAY);
        }
        return null;
    }
    
	    public RazorPayCreateOrderResponseDto checkStatusOfOrderOnRazorpayResponse(
	            String orderId, String transactionId)
	            throws DownStreamException {
	    HttpEntity<String> httpEntity = new HttpEntity<String>(setHeaders());
        String fullUrl = paymentGatewayConfig.getRazorPay().getUrl()  + "/"+ orderId;
	    log.info("hitting razorpay url : {}", fullUrl);
	    try {
	            ResponseEntity<String> responseEntity = razorPayRestTemplate.exchange(fullUrl, HttpMethod.GET, httpEntity, String.class);
	    	    log.info("response : {}", fullUrl);
	            return mapper.readValue(responseEntity.getBody(), RazorPayCreateOrderResponseDto.class);
	        } catch (Exception ex) {
	        	log.info("Exception : {} while hitting razorpay", ex);
	        }  
	        return null;
	    }

		private HttpHeaders setHeaders() {
			HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.setBasicAuth(paymentGatewayConfig.getRazorPay().getAccessKey(),
                    paymentGatewayConfig.getRazorPay().getSecretKey());
			return headers;
		}


    private void handleException(Exception ex, String transactionId, String requestType)
            throws DownStreamException {
        throw new DownStreamException("Exception occurred while processing request, RequestType="
                + requestType + "," + " TransactionId=" + transactionId, ex);
    }

    private boolean handleResponse(ResponseEntity<String> responseEntity, String transactionId,
            String requestType, String downStreamName) throws DownStreamException {
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info(
                    "Successful Response from {}, ResponseCode = {}, Response = {}, TransactionId = {}",
                    downStreamName, responseEntity.getStatusCode(), responseEntity.getBody(),
                    transactionId);
            return true;
        }
        log.error(
                "Error response received from {}, ResponseCode = {}, Response = {}, TransactionId = {}",
                downStreamName, responseEntity.getStatusCode(), responseEntity.getBody(),
                transactionId);
        throw new DownStreamException("Error response received from " + downStreamName
                + ", RequestType = " + requestType + ", TransactionId = " + transactionId);
    }

    private void handleFinallyBlock(Double duration, String requestType, boolean isSuccess,
            String transactionId, String downStreamName) {
        MetricsUtility.addDownStreamMetrics(isSuccess, downStreamName, duration, requestType,
                transactionId);
    }
    
    
    public RazorPayCreateOrderResponseDto createCustomerOnRazorpay(
            PaymentGatewayCustomerDetail createCustomerRequest, String transactionId)
            throws DownStreamException {
        boolean isSuccess = false;
        StopWatch timer = new StopWatch();
        timer.start();
        try {
            String fullUrl = paymentGatewayConfig.getRazorPay().getCustomer();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBasicAuth(paymentGatewayConfig.getRazorPay().getAccessKey(),
                    paymentGatewayConfig.getRazorPay().getSecretKey());
            HttpEntity<PaymentGatewayCustomerDetail> httpEntity =
                    new HttpEntity<>(createCustomerRequest, headers);
            log.info("Going to hit the Url = {}, RequestBody={}, TranscationId={}", fullUrl, Utility.getJson(createCustomerRequest), transactionId);

            ResponseEntity<String> responseEntity = razorPayRestTemplate.exchange(fullUrl,
                    HttpMethod.POST, httpEntity, String.class);

            isSuccess = handleResponse(responseEntity, transactionId,
                    RequestType.CREATE_CUSTOMER_ON_RAZORPAY_RESPONSE, DownStreamConstant.RAZORPAY);
            return mapper.readValue(responseEntity.getBody(),
                    new TypeReference<RazorPayCreateOrderResponseDto>() {});
        } catch (Exception ex) {
            handleException(ex, transactionId, RequestType.CREATE_CUSTOMER_ON_RAZORPAY_RESPONSE);
        } finally {
            timer.stop();
            handleFinallyBlock(timer.getTotalTimeSeconds(),
                    RequestType.CREATE_CUSTOMER_ON_RAZORPAY_RESPONSE, isSuccess, transactionId,
                    DownStreamConstant.RAZORPAY);
        }
        return null;
    }

	public Object fetchSavedCards(String razorpayCustomerId, String transactionId)
            throws DownStreamException {
        boolean isSuccess = false;
        StopWatch timer = new StopWatch();
        timer.start();
        try {
            String fullUrl = paymentGatewayConfig.getRazorPay().getCustomer() + "/"+razorpayCustomerId+"/tokens";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBasicAuth(paymentGatewayConfig.getRazorPay().getAccessKey(),
                    paymentGatewayConfig.getRazorPay().getSecretKey());
            HttpEntity<PaymentGatewayCustomerDetail> httpEntity =
                    new HttpEntity<>(headers);
            log.info("Going to hit the Url = {}, TranscationId={}", fullUrl, transactionId);

            ResponseEntity<Object> responseEntity = razorPayRestTemplate.exchange(fullUrl, HttpMethod.GET, httpEntity, Object.class);

            return responseEntity.getBody();
        } catch (Exception ex) {
            handleException(ex, transactionId, RequestType.FETCH_SAVED_CARDS);
        } finally {
            timer.stop();
            handleFinallyBlock(timer.getTotalTimeSeconds(),
                    RequestType.FETCH_SAVED_CARDS, isSuccess, transactionId,
                    DownStreamConstant.RAZORPAY);
        }
        return null;
	}


}
