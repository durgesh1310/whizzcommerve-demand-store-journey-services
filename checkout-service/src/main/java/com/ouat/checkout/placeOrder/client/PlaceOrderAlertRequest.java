package com.ouat.checkout.placeOrder.client;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PlaceOrderAlertRequest {
	private String customerName;
	private Long customerId;
	private String orderId;
	private String mobile;
	private String placeOrderDateTime;
	private String coupon ;
	private DeliveryAddressPlaceOrderAlert billingAndShippingDetail;
	private PaymentMethodForPlaceOrderAlert paymentMethod;
	private List<ProductItemDetailForPlaceOrderAlert>productItemDetailList;
	private PriceSummaryForPlaceOrderAlert priceSummary;
	private EmailSendRequest emailSendRequest;
	
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
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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
	public PaymentMethodForPlaceOrderAlert getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(PaymentMethodForPlaceOrderAlert paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public List<ProductItemDetailForPlaceOrderAlert> getProductItemDetailList() {
		return productItemDetailList;
	}
	public void setProductItemDetailList(List<ProductItemDetailForPlaceOrderAlert> productItemDetailList) {
		this.productItemDetailList = productItemDetailList;
	}
	public PriceSummaryForPlaceOrderAlert getPriceSummary() {
		return priceSummary;
	}
	public void setPriceSummary(PriceSummaryForPlaceOrderAlert priceSummary) {
		this.priceSummary = priceSummary;
	}
	public EmailSendRequest getEmailSendRequest() {
		return emailSendRequest;
	}
	public void setEmailSendRequest(EmailSendRequest emailSendRequest) {
		this.emailSendRequest = emailSendRequest;
	}
	
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	@Override
	public String toString() {
		return "PlaceOrderAlertRequest [customerName=" + customerName + ", customerId=" + customerId + ", orderId="
				+ orderId + ", mobile=" + mobile + ", placeOrderDateTime=" + placeOrderDateTime + ", coupon=" + coupon
				+ ", billingAndShippingDetail=" + billingAndShippingDetail + ", paymentMethod=" + paymentMethod
				+ ", productItemDetailList=" + productItemDetailList + ", priceSummary=" + priceSummary
				+ ", emailSendRequest=" + emailSendRequest + "]";
	}
	
	public PlaceOrderAlertRequest(String customerName, String orderId, String mobile, String placeOrderDateTime,
			String coupon, com.ouat.checkout.placeOrder.client.DeliveryAddressPlaceOrderAlert billingAndShippingDetail,
			PaymentMethodForPlaceOrderAlert paymentMethod,
			List<ProductItemDetailForPlaceOrderAlert> productItemDetailList,
			PriceSummaryForPlaceOrderAlert priceSummary,
			com.ouat.checkout.placeOrder.client.EmailSendRequest emailSendRequest) {
		super();
		this.customerName = customerName;
		this.orderId = orderId;
		this.mobile = mobile;
		this.placeOrderDateTime = placeOrderDateTime;
		this.coupon = coupon;
		this.billingAndShippingDetail = billingAndShippingDetail;
		this.paymentMethod = paymentMethod;
		this.productItemDetailList = productItemDetailList;
		this.priceSummary = priceSummary;
		this.emailSendRequest = emailSendRequest;
	}
	
	public PlaceOrderAlertRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
}
