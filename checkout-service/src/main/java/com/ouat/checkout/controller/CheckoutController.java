package com.ouat.checkout.controller;

import static com.ouat.checkout.constant.CommonConstant.DEFAULT_TRANSACTION_ID;
import static com.ouat.checkout.util.Utility.addHeadersForDownStreamCalls;
import static com.ouat.checkout.util.Utility.convertToString;
import static com.ouat.checkout.util.Utility.generateTransactionId;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ouat.checkout.annotations.SessionRequired;
import com.ouat.checkout.controller.request.CheckoutRequest;
import com.ouat.checkout.controller.request.GuestCheckoutRequest;
import com.ouat.checkout.enums.MessageType;
import com.ouat.checkout.exception.BusinessProcessException;
import com.ouat.checkout.exception.DownStreamException;
import com.ouat.checkout.interceptor.DemandStoreLoginRequired;
import com.ouat.checkout.interceptor.RequestHeaders;
import com.ouat.checkout.placeOrder.request.PaymentGatewayCustomerDetail;
import com.ouat.checkout.placeOrder.request.PaymentGatewayDetailRequest;
import com.ouat.checkout.placeOrder.request.PlaceOrderRequest;
import com.ouat.checkout.placeOrder.request.Utm;
import com.ouat.checkout.placeOrder.service.PlaceOrderService;
import com.ouat.checkout.response.CustomerDetailVO;
import com.ouat.checkout.response.DemandStoreAPIResponse;
import com.ouat.checkout.response.DeviceType;
import com.ouat.checkout.response.MessageDetail;
import com.ouat.checkout.response.Platform;
import com.ouat.checkout.service.CheckoutService;
import com.ouat.checkout.util.Utility;

@CrossOrigin
@RestController
public class CheckoutController {

    private static final Logger log = LoggerFactory.getLogger(CheckoutController.class);

    @Autowired
    private CheckoutService service;

    @Autowired
    private PlaceOrderService placeOrderService;
    

	@Value("${order.confirmation.banner}")
	private String bannerImage;
	
	@Value("${callbackurl}")
	private String callbackUrl;
    
    @GetMapping("/proceed")
    @DemandStoreLoginRequired
    public ResponseEntity<DemandStoreAPIResponse> checkout(
            @RequestParam(required = false) String promoCode,
            @RequestHeader(value = "Transaction-ID",
                    defaultValue = DEFAULT_TRANSACTION_ID) String transactionId,
            HttpServletRequest servletRequest)
            throws BusinessProcessException, DownStreamException {

        transactionId = getValidTransactionId(transactionId);
        Enumeration<String> headers = servletRequest.getHeaderNames();
        HttpHeaders downStreamHeaders = addHeadersForDownStreamCalls(headers, servletRequest);
        log.info(transactionId + " User on checkout");
        CustomerDetailVO customerDetail =
                (CustomerDetailVO) servletRequest.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
        Platform platform = Platform.valueOf(servletRequest.getHeader(RequestHeaders.USER_CLIENT));
        try {
        	if(null != promoCode) {
            	promoCode = promoCode.substring(1, promoCode.length()-1);
        	}
        	customerDetail.setUserClient(servletRequest.getHeader(RequestHeaders.USER_CLIENT));
            return service.callCheckoutWorkFlow(customerDetail.getCustomerId().intValue(),
                    promoCode, customerDetail, transactionId, downStreamHeaders, platform);
        } catch (BusinessProcessException ex) {
            log.warn(
                    "BusinessProcessException occured RequestType = proceedToCheckout, TransactionId = {}, Action = Redirect, Cause = {}, Message = {}",
                    transactionId, ExceptionUtils.getCause(ex), ex.getMessage());
            return buildRedirectResponse(ex);
        }
    }
    @PostMapping("/do-refresh")
    @DemandStoreLoginRequired
    public ResponseEntity<DemandStoreAPIResponse> refreshCheckout(
            @RequestBody(required = false) CheckoutRequest request,
            @RequestHeader(value = "Transaction-ID",
                    defaultValue = DEFAULT_TRANSACTION_ID) String transactionId,
            HttpServletRequest servletRequest)
            throws DownStreamException, BusinessProcessException {
        transactionId = getValidTransactionId(transactionId);
        Enumeration<String> headers = servletRequest.getHeaderNames();
        HttpHeaders downStreamHeaders = addHeadersForDownStreamCalls(headers, servletRequest);
        log.info(transactionId + " User on do-refresh");
        
        CustomerDetailVO customerDetail =
                (CustomerDetailVO) servletRequest.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
        Platform platform = Platform.valueOf(servletRequest.getHeader(RequestHeaders.USER_CLIENT));

        try {
        	String deviceId = servletRequest.getHeader(RequestHeaders.DEVICE_ID);
            return service.callRefreshCheckoutWorkflow(customerDetail.getCustomerId().intValue(),
                    customerDetail, request, transactionId, downStreamHeaders, platform, deviceId);
        } catch (BusinessProcessException ex) {
            log.warn(
                    "BusinessProcessException occured RequestType = proceedToCheckout, TransactionId = {}, Action = Redirect, Cause = {}, Message = {}",
                    transactionId, ExceptionUtils.getCause(ex), ex.getMessage());
            return buildRedirectResponse(ex);
        }
    }
    
    @SessionRequired
    @PostMapping("/guest/proceed")
    public ResponseEntity<DemandStoreAPIResponse> doGuestCheckout(@RequestBody GuestCheckoutRequest request,
            @RequestHeader(value = "Transaction-ID",
                    defaultValue = DEFAULT_TRANSACTION_ID) String transactionId,
            HttpServletRequest servletRequest)
            throws DownStreamException, BusinessProcessException {
        transactionId = getValidTransactionId(transactionId);
        Enumeration<String> headers = servletRequest.getHeaderNames();
        HttpHeaders downStreamHeaders = addHeadersForDownStreamCalls(headers, servletRequest);
        String deviceId = servletRequest.getHeader(RequestHeaders.DEVICE_ID);
        String userClient = servletRequest.getHeader(RequestHeaders.USER_CLIENT);
        Platform platform = Platform.valueOf(servletRequest.getHeader(RequestHeaders.USER_CLIENT));
        log.info(transactionId + " User on guest-checkout, deviceId=" + deviceId);
        try {
            return service.callGuestCheckoutWorkflow(deviceId, userClient, platform, request, transactionId, downStreamHeaders);
        } catch (BusinessProcessException ex) {
            log.warn(
                    "BusinessProcessException occured RequestType=doGuestCheckout, TransactionId = {}, Action = Redirect, Cause = {}, Message = {}",
                    transactionId, ExceptionUtils.getCause(ex), ex.getMessage());
            return buildRedirectResponse(ex);
        }
    }
    
    private String getValidTransactionId(String transactionId) {
        transactionId =
                transactionId.equalsIgnoreCase(DEFAULT_TRANSACTION_ID) ? generateTransactionId()
                        : transactionId;
        return transactionId;
    }
    @PostMapping("/do-refresh-2")
    @DemandStoreLoginRequired
    public ResponseEntity<DemandStoreAPIResponse> placeOrder(
            @RequestBody PlaceOrderRequest placeOrderRequest, HttpServletRequest servletRequest)
            throws Exception {
        CustomerDetailVO customerDetail = (CustomerDetailVO) servletRequest.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
        log.info("CHECKOUT_DEVICE | {} ",  servletRequest.getHeader(RequestHeaders.DEVICE_TYPE));
		Enumeration<String> headers = servletRequest.getHeaderNames();
        HttpHeaders downStreamHeaders = addHeadersForDownStreamCalls(headers, servletRequest);
		Utm utm = buildUtms(servletRequest);
		if(null != servletRequest.getHeader(RequestHeaders.USER_CLIENT) && ("android".equalsIgnoreCase(servletRequest.getHeader(RequestHeaders.USER_CLIENT)) 
				|| "ios".equalsIgnoreCase(servletRequest.getHeader(RequestHeaders.USER_CLIENT)))) {
			customerDetail.setDeviceType(DeviceType.valueOf(servletRequest.getHeader(RequestHeaders.USER_CLIENT)));
		}  else {
			customerDetail.setDeviceType(DeviceType.valueOf(servletRequest.getHeader(RequestHeaders.DEVICE_TYPE)));
		}
		
		log.info("place orderrequest has been recieved with customerdetail detail : {}: ",customerDetail);
        return ResponseEntity.ok(placeOrderService.placeOrder(placeOrderRequest.getPaymentMode(), customerDetail, utm, downStreamHeaders));
    }
    @PutMapping("/status")
    @DemandStoreLoginRequired
    public ResponseEntity<DemandStoreAPIResponse> orderStatus(
            @RequestParam(name = "order_id") Integer orderId,
            @RequestParam(name = "payment_status") String paymentStatus, @RequestBody PaymentGatewayDetailRequest paymentGatewayDetailRequest ,
            HttpServletRequest servletRequest)
            throws BusinessProcessException {
        CustomerDetailVO customerDetail =
                (CustomerDetailVO) servletRequest.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
        return ResponseEntity.ok(placeOrderService.orderStatus(orderId,
        		customerDetail, paymentStatus, paymentGatewayDetailRequest));
    }
     
    private ResponseEntity<DemandStoreAPIResponse> buildRedirectResponse(
            BusinessProcessException ex) {
        DemandStoreAPIResponse response = new DemandStoreAPIResponse();
        response.setCode(convertToString(HttpStatus.TEMPORARY_REDIRECT.value()));
        response.setMessage(buildMessageList(ex.getMessage()));
        return new ResponseEntity<DemandStoreAPIResponse>(response, HttpStatus.TEMPORARY_REDIRECT);
    }

    private List<MessageDetail> buildMessageList(String message) {
        MessageDetail details = new MessageDetail();
        details.setMsgType(MessageType.INFO);
        details.setMsgText(message);
        return Arrays.asList(details);
    }
    /**
     * This api is only for Guest User
     * @param placeOrderRequest
     * @param servletRequest
     * @return DemandStoreAPIResponse
     * @throws Exception 
     */
    @SessionRequired
    @PostMapping("/do-refresh-3")
     public ResponseEntity<DemandStoreAPIResponse> placeOrderForGuestCheckout(@RequestBody PlaceOrderRequest placeOrderRequest, HttpServletRequest servletRequest)
				throws Exception {
			log.info("place orderrequest has been recieved with  request body : {} ",
					placeOrderRequest.getPaymentMode());
			log.info("CHECKOUT_DEVICE | {} ",  servletRequest.getHeader(RequestHeaders.DEVICE_TYPE));
			Utm utm = buildUtms(servletRequest);
			CustomerDetailVO customerDetail = new CustomerDetailVO();
			customerDetail.setDeviceType(DeviceType.valueOf(servletRequest.getHeader(RequestHeaders.DEVICE_TYPE)));
			customerDetail.setDeviceId(servletRequest.getHeader(RequestHeaders.DEVICE_ID));
			log.info(" hitting the service layer  with customer detail : {} ", placeOrderRequest.getPaymentMode());
			
			if(null != servletRequest.getHeader(RequestHeaders.USER_CLIENT) && ("android".equalsIgnoreCase(servletRequest.getHeader(RequestHeaders.USER_CLIENT)) 
					|| "ios".equalsIgnoreCase(servletRequest.getHeader(RequestHeaders.USER_CLIENT)))) {
				customerDetail.setDeviceType(DeviceType.valueOf(servletRequest.getHeader(RequestHeaders.USER_CLIENT)));
			}  else {
				customerDetail.setDeviceType(DeviceType.valueOf(servletRequest.getHeader(RequestHeaders.DEVICE_TYPE)));
			}
			Enumeration<String> headers = servletRequest.getHeaderNames();
			String uuid = servletRequest.getHeader(RequestHeaders.DEVICE_ID);

		    HttpHeaders downStreamHeaders = addHeadersForDownStreamCalls(headers, servletRequest);
			return ResponseEntity.ok(placeOrderService.placeOrderForGuestCheckout(placeOrderRequest.getPaymentMode(), customerDetail, utm, downStreamHeaders, uuid));
		}
    
	private Utm buildUtms(HttpServletRequest servletRequest) {
		Utm utm =new Utm();
		utm.setUtmCampaign( servletRequest.getHeader(RequestHeaders.UTM_CAMPAIGN));
		utm.setUtmMedium(servletRequest.getHeader(RequestHeaders.UTM_MEDIUM));
		utm.setUtmSource(servletRequest.getHeader(RequestHeaders.UTM_SOURCE));
		return utm;
	}
    
	 @PutMapping("/v1/status")
	 @SessionRequired
	 public ResponseEntity<DemandStoreAPIResponse> orderStatusV1(
	            @RequestParam(name = "order_id") Integer orderId,
	            @RequestParam(name = "payment_status") String paymentStatus, @RequestBody PaymentGatewayDetailRequest paymentGatewayDetailRequest ,
	            HttpServletRequest servletRequest)
	            throws BusinessProcessException, DownStreamException {
	        String deviceId = servletRequest.getHeader(RequestHeaders.DEVICE_ID);
	        return ResponseEntity.ok(placeOrderService.orderV1status(orderId,
	        		deviceId, paymentStatus, paymentGatewayDetailRequest));
	 }
    @DemandStoreLoginRequired
    @PostMapping("/payment-gateway-customer")
    public ResponseEntity<DemandStoreAPIResponse> createCustomerOnRazorpay(@RequestBody PaymentGatewayCustomerDetail paymentGatewayCustomerDetail, HttpServletRequest servletRequest)
			throws BusinessProcessException {
    	String transactionId = Utility.generateTransactionId();
    	paymentGatewayCustomerDetail.setName(transactionId);
		CustomerDetailVO customerDetail = (CustomerDetailVO) servletRequest
				.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		return ResponseEntity.ok(placeOrderService.createCustomerOnRazorpay(paymentGatewayCustomerDetail, customerDetail.getCustomerId(), transactionId));
	}
    
    @PostMapping(path="/callback-url", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public void getCallBackUrlFromRazorPay(HttpServletRequest request, HttpServletResponse resp) throws IOException {
    	String response = placeOrderService.callBackUrlForRazorPay(request);

    	try {
			resp.sendRedirect(callbackUrl+response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
