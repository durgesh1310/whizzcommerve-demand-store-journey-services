package com.ouat.notificationsender.request;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.notificationsender.client.EmailSendRequest;
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PlaceOrderAlertRequest {
	
	private String customerName;
	private Long customerId;
	private String orderId;
	private String mobile;
	private String placeOrderDateTime;
	private DeliveryAddressPlaceOrderAlert billingAndShippingDetail;
	private PaymentMethod paymentMethod;
	private List<ProductItemDetail> productItemDetailList;
	private PriceSummary priceSummary;
	private EmailSendRequest emailSendRequest;
	private String coupon;
	

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public EmailSendRequest getEmailSendRequest() {
		return emailSendRequest;
	}

	public void setEmailSendRequest(EmailSendRequest emailSendRequest) {
		this.emailSendRequest = emailSendRequest;
	}

	public PriceSummary getPriceSummary() {
		return priceSummary;
	}

	public void setPriceSummary(PriceSummary priceSummary) {
		this.priceSummary = priceSummary;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPlaceOrderDateTime() {
		return placeOrderDateTime;
	}

	public void setPlaceOrderDateTime(String placeOrderDateTime) {
		this.placeOrderDateTime = placeOrderDateTime;
	}
 
	public DeliveryAddressPlaceOrderAlert getBillingAndShippingDetail() {
		return billingAndShippingDetail;
	}
	public void setBillingAndShippingDetail(DeliveryAddressPlaceOrderAlert billingAndShippingDetail) {
		this.billingAndShippingDetail = billingAndShippingDetail;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public List<ProductItemDetail> getProductItemDetailList() {
		return productItemDetailList;
	}

	public void setProductItemDetailList(List<ProductItemDetail> productItemDetailList) {
		this.productItemDetailList = productItemDetailList;
	}

	public PlaceOrderAlertRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PlaceOrderAlertRequest(String customerName, Long customerId, String orderId, String mobile, String placeOrderDateTime,
			DeliveryAddressPlaceOrderAlert billingAndShippingDetail, PaymentMethod paymentMethod,
			List<ProductItemDetail> productItemDetailList, PriceSummary priceSummary, EmailSendRequest emailSendRequest,
			String coupon) {
		super();
		this.customerName = customerName;
		this.customerId = customerId;
		this.orderId = orderId;
		this.mobile = mobile;
		this.placeOrderDateTime = placeOrderDateTime;
		this.billingAndShippingDetail = billingAndShippingDetail;
		this.paymentMethod = paymentMethod;
		this.productItemDetailList = productItemDetailList;
		this.priceSummary = priceSummary;
		this.emailSendRequest = emailSendRequest;
		this.coupon = coupon;
	}

	@Override
	public String toString() {
		return "PlaceOrderAlert [customerName=" + customerName + ", customerId=" + customerId + ", orderId=" + orderId + ", mobile=" + mobile
				+ ", placeOrderDateTime=" + placeOrderDateTime + ", billingAndShippingDetail="
				+ billingAndShippingDetail + ", paymentMethod=" + paymentMethod + ", productItemDetailList="
				+ productItemDetailList + ", priceSummary=" + priceSummary + ", emailSendRequest=" + emailSendRequest
				+ ", coupon=" + coupon + "]";
	}

 
 
}
