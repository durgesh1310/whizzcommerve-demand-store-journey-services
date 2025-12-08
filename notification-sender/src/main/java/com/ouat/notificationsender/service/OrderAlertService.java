package com.ouat.notificationsender.service;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ouat.notificationsender.client.CuttlyShortUrlClient;
import com.ouat.notificationsender.client.DLTTemplateType;
import com.ouat.notificationsender.client.EmailSMSServiceClient;
import com.ouat.notificationsender.client.EmailSendRequest;
import com.ouat.notificationsender.client.MessageRequest;
import com.ouat.notificationsender.constants.CommonConstant;
import com.ouat.notificationsender.repository.NotificationSendorRepository;
import com.ouat.notificationsender.repository.VendorDetailDto;
import com.ouat.notificationsender.request.CashbackAlert;
import com.ouat.notificationsender.request.PlaceOrderAlertRequest;
import com.ouat.notificationsender.request.ProductItemDetail;
import com.ouat.notificationsender.request.ReturnExchangeCancelAlert;
import com.ouat.notificationsender.request.VendorOrderAlert;
import com.ouat.notificationsender.response.images.NotificationSenderImages;
@Service
public class OrderAlertService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(OrderAlertService.class);
	@Autowired
    VelocityEngine velocityEngine = new VelocityEngine();
	
	@Autowired
	EmailSMSServiceClient emailSMSServiceClient;
	
	@Value("${env}")
	private String env;
	
	@Value("${is.referral}")
	private boolean referralFlag;
	
	@Autowired
	NotificationSendorRepository repository;
	
	@Autowired
	CuttlyShortUrlClient cuttlyClient;
 
	public boolean returnExchangeCancelAlert(ReturnExchangeCancelAlert alert){
 		VelocityContext velocityContext = buildVelocityContextMapForOrderAlert();
		velocityContext.put("emailAlert",  alert);
	    velocityContext.put("IMAGE_URL", "https://taggd.gumlet.io/" + alert.getImgUrl());
	    velocityContext.put("ALERT_REQUEST",alert.getAlertType().getValue());
        Template template = velocityEngine.getTemplate("templates/return-exchange-cancel.vm" );
	    StringWriter writer = new StringWriter();
        template.merge(velocityContext, writer);
        return buildEmailSentRequesAndSentMail(alert.getEmailSendRequest(),writer, CommonConstant.CRR);
	}
	private boolean buildEmailSentRequesAndSentMail(EmailSendRequest emailSendRequest, StringWriter writer, String alertType) {
		emailSendRequest.setMessageBody(writer.toString());
		List<String> ccEmailAddress = new ArrayList<String>();
		ccEmailAddress.add("info@taggd.com");
		emailSendRequest.setCcEmailAddress(ccEmailAddress);
        LOGGER.info( "calling thdown stream api call with  email sms alert request: alert type : {} ", alertType);
        if(env != null && env.equals("prod") && CommonConstant.CRR.equals(alertType)) {
        	LOGGER.info("Sending alert to CC TEAM");
        	List<String> toCustomerEmailId = emailSendRequest.getToEmailAddress();
        	List<String> toEmailAddress = new ArrayList<String>();
        	toEmailAddress.add("customercare@taggd.com");
    		emailSendRequest.setToEmailAddress(toEmailAddress);
    		LOGGER.info("Sending alert to CC TEAM {} ", emailSendRequest);
    		emailSMSServiceClient.sendEmail(emailSendRequest);
    		emailSendRequest.setToEmailAddress(toCustomerEmailId);
    		LOGGER.info("Sending alert to Customer {} ", emailSendRequest);
        }
		return emailSMSServiceClient.sendEmail(emailSendRequest);
	}
	private VelocityContext buildVelocityContextMapForOrderAlert() {
		VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("LINKEDIN_LOGO",  NotificationSenderImages.LINKEDIN);
        velocityContext.put("FACEBOOK_LOGO", NotificationSenderImages.FACEBOOK);
        velocityContext.put("FOOT_DESIGNER_LOGO", NotificationSenderImages.FOOT_DESIGNER);
        velocityContext.put("FOOT_HAPPY_CUSTOMER_LOGO", NotificationSenderImages.FOOT_HAPPY_CUSTOMER);
        velocityContext.put("MAKE_IN_INDIA_LOGO", NotificationSenderImages.MAKE_IN_INDIA);
        velocityContext.put( "INSTAGRAM_LOGO", NotificationSenderImages.INSTAGRAM);
        velocityContext.put("LOGO_MAIN_LOGO", NotificationSenderImages.LOGO_MAIN);
        velocityContext.put("PINTEREST_LOGO", NotificationSenderImages.PINTEREST);
        velocityContext.put("YOUTUBE_LOGO", NotificationSenderImages.YOUTUBE);
        velocityContext.put("RUPEES_SIGN", "\u20B9");
        velocityContext.put("NULL", null);
        velocityContext.put("EMPTY_STRING", "");
        velocityContext.put("ZERO_VALUE", 0.00);
        LOGGER.info( "return exchanchge request has been build with velocoity context map : {}", velocityContext);
 		return velocityContext;
	}
	
	public void sendEmailToVendorForPlaceOrderAlert(PlaceOrderAlertRequest placeOrderAlert){
        LOGGER.info( "vendorAlert reqeust has been successfully recieved with request : {}", placeOrderAlert);
		List<ProductItemDetail> productItemDetailList = placeOrderAlert.getProductItemDetailList();
		Map<String, VendorDetailDto> vendorEmailSku =  repository.getVendorDetailFromDb(getSkuList(productItemDetailList), placeOrderAlert.getOrderId());
	    for (ProductItemDetail productItemIterator :  productItemDetailList) {
			    VendorOrderAlert vendorOrderAlert = buildVendorOrderAlert(placeOrderAlert, productItemIterator, vendorEmailSku.get(productItemIterator.getSku()).getOrderItemId());
		 		VelocityContext velocityContext = buildVelocityContextMapForOrderAlert();
				velocityContext.put("VENDOR_ORDER_ALERT", vendorOrderAlert);
				velocityContext.put("PRODUCT_SELLING_PRICE", productItemIterator.getPrice());
				DecimalFormat df = new DecimalFormat("0.00");
				velocityContext.put("VENDOR_PRICE", df.format(productItemIterator.getPrice() - (productItemIterator.getOuatMargin()* productItemIterator.getPrice())/100.00));
				velocityContext.put("VENDOR_NAME",vendorEmailSku.get(productItemIterator.getSku()).getVendorName() );
				Template template = velocityEngine.getTemplate( "templates/order-place-vendor-alert.vm");
		        StringWriter writer = new StringWriter();
		        template.merge(velocityContext, writer);
		        LOGGER.info( "place order request has been build with velocoity context map : {}", velocityContext);
				EmailSendRequest emailSendRequest = buildEmailForVendorOrderAlert(vendorEmailSku, productItemIterator, writer);
				LOGGER.info( "vendorAlert reqeust has been successfully recieved with request : {}", emailSendRequest);
			    emailSMSServiceClient.sendEmail(emailSendRequest);
	    }	
	}
	private List<String> getSkuList(List<ProductItemDetail> productItemDetailList) {
		List<String>skuList = new ArrayList<String>();
		for(ProductItemDetail it : productItemDetailList) {
			skuList.add(it.getSku());
		}
		return skuList;
	}
	private VendorOrderAlert buildVendorOrderAlert(PlaceOrderAlertRequest placeOrderAlert, ProductItemDetail it, String orderItemId) {
		VendorOrderAlert vendorOrderAlert = new VendorOrderAlert();
			vendorOrderAlert.setOrderId(orderItemId);
			vendorOrderAlert.setPlaceOrderDateTime(placeOrderAlert.getPlaceOrderDateTime());
			vendorOrderAlert.setBillingAndShippingDetail(placeOrderAlert.getBillingAndShippingDetail());
			vendorOrderAlert.setPaymentMethod(placeOrderAlert.getPaymentMethod());
			vendorOrderAlert.setProductItemDetail(it);
		return vendorOrderAlert;
	}
	private EmailSendRequest buildEmailForVendorOrderAlert(Map<String, VendorDetailDto> vendorEmailSku, ProductItemDetail it,
			StringWriter writer) {
		EmailSendRequest emailSendRequest = new EmailSendRequest();
		emailSendRequest.setFromEmail("customercare@mail.taggd.com");
		emailSendRequest.setFromNickName("taggd");
		emailSendRequest.setMessageBody(writer.toString());
		emailSendRequest.setSubject("Order Alert : "+ vendorEmailSku.get(it.getSku()).getOrderItemId());
		List<String> vendorEmailList = new ArrayList<String>();
		vendorEmailList.add(vendorEmailSku.get(it.getSku()).getVendorEmail());
		emailSendRequest.setToEmailAddress(vendorEmailList);
		List<String> ccEmailAddress = new ArrayList<String>();
		ccEmailAddress.add("info@taggd.com");
		emailSendRequest.setCcEmailAddress(ccEmailAddress);
		LOGGER.info( "calling thdown stream api call with  email sms alert request: {} ", emailSendRequest);
		/*
		if(env != null && env.equals("prod") ) {
			
			LOGGER.info("Sending alter to CC TEAM");
			List<String> toCustomerEmailId = emailSendRequest.getToEmailAddress();
			List<String> toEmailAddress = new ArrayList<String>();
			toEmailAddress.add("customercare@taggd.com");
			emailSendRequest.setToEmailAddress(toEmailAddress);
			emailSMSServiceClient.sendEmail(emailSendRequest);
			emailSendRequest.setToEmailAddress(toCustomerEmailId);
		}
		*/
		return emailSendRequest;
	}

	public boolean placeOrderAlert(PlaceOrderAlertRequest placeOrderAlert) {
		VelocityContext velocityContext = buildVelocityContextMapForOrderAlert();
		velocityContext.put("PAYMENT_METHOD", placeOrderAlert.getPaymentMethod().getValue());
		velocityContext.put("PlaceOrderAlert", placeOrderAlert);
		Template template = velocityEngine.getTemplate("templates/order-place.vm");
		StringWriter writer = new StringWriter();
		template.merge(velocityContext, writer);
		LOGGER.info("place order request has been build with velocoity context map : {}", velocityContext);
		buildMessageRequestAndSentMessage(placeOrderAlert);
		LOGGER.info("Msg request is built : {} ", env);
		if (env != null && env.equals("prod")) {
			LOGGER.info("Going to send to vendor");
			sendEmailToVendorForPlaceOrderAlert(placeOrderAlert);
		}
		return buildEmailSentRequesAndSentMail(placeOrderAlert.getEmailSendRequest(), writer,
				CommonConstant.PLACE_ORDER);
	}
	private void buildMessageRequestAndSentMessage(PlaceOrderAlertRequest placeOrderAlert) {
		MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMobileNumber(placeOrderAlert.getMobile());
        messageRequest.setContent(String.format(CommonConstant.PLACE_ORDER_SMS, placeOrderAlert.getCustomerName(), placeOrderAlert.getOrderId()));
        messageRequest.setDltTemplateType(DLTTemplateType.PLACE_ORDER);
        emailSMSServiceClient.sendMessage(messageRequest); 
        /*
        LOGGER.info("Going to initiate SMS for Referral Link");
        if(referralFlag) {
        	buildMessageRequestForReferralAndSendSMS(placeOrderAlert);
        	LOGGER.info("SMS sent for Referral Link");
        }
        */
	}
	
	private void buildMessageRequestForReferralAndSendSMS(PlaceOrderAlertRequest placeOrderAlert) {
		String cuttlyShortUrl = cuttlyClient.createShortLink(placeOrderAlert.getCustomerId());
		MessageRequest referMessageRequest = new MessageRequest();
		referMessageRequest.setMobileNumber(placeOrderAlert.getMobile());
		referMessageRequest.setContent(String.format(CommonConstant.REFER_MESSAGE, placeOrderAlert.getCustomerName(), cuttlyShortUrl));
		referMessageRequest.setDltTemplateType(DLTTemplateType.REFERRAL);
		emailSMSServiceClient.sendMessage(referMessageRequest); 
		
		
		
	}
	public boolean cashbackAlert(CashbackAlert alert) {
		VelocityContext velocityContext = buildVelocityContextMapForOrderAlert();
		velocityContext.put("alert", alert);
		Template template = velocityEngine.getTemplate("templates/loyalty-cashback.vm");
		StringWriter writer = new StringWriter();
		template.merge(velocityContext, writer);
		LOGGER.info("loyalty cashback map : {}", velocityContext);
		return buildEmailSentRequesAndSentMail(alert.getEmail() , writer,
				CommonConstant.LOYALTY_CASHBACK);
	}
	 
}
