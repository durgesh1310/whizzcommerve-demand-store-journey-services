package com.ouat.notificationsender.request;

import com.ouat.notificationsender.client.EmailSendRequest;

public class VendorOrderAlert {
 	private String customerName;
 	private String orderId;
 	private String placeOrderDateTime;
 	private DeliveryAddressPlaceOrderAlert billingAndShippingDetail;
 	private PaymentMethod paymentMethod;
 	private ProductItemDetail productItemDetail;
 	private EmailSendRequest emailSendRequest;
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
 	public PaymentMethod getPaymentMethod() {
 		return paymentMethod;
 	}
 	public void setPaymentMethod(PaymentMethod paymentMethod) {
 		this.paymentMethod = paymentMethod;
 	}
 	public ProductItemDetail getProductItemDetail() {
 		return productItemDetail;
 	}
 	public void setProductItemDetail(ProductItemDetail productItemDetailList) {
 		this.productItemDetail = productItemDetailList;
 	}
 	public EmailSendRequest getEmailSendRequest() {
 		return emailSendRequest;
 	}
 	public void setEmailSendRequest(EmailSendRequest emailSendRequest) {
 		this.emailSendRequest = emailSendRequest;
 	}
 	@Override
 	public String toString() {
 		return "VendorOrderAlert [customerName=" + customerName + ", orderId=" + orderId + ", placeOrderDateTime="
 				+ placeOrderDateTime + ", billingAndShippingDetail=" + billingAndShippingDetail + ", paymentMethod="
 				+ paymentMethod + ", productItemDetailList=" + productItemDetail + ", emailSendRequest="
 				+ emailSendRequest + "]";
 	}
 	public VendorOrderAlert(String customerName, String orderId, String placeOrderDateTime,
 			DeliveryAddressPlaceOrderAlert billingAndShippingDetail, PaymentMethod paymentMethod,
 			ProductItemDetail productItemDetailList, EmailSendRequest emailSendRequest) {
 		super();
 		this.customerName = customerName;
 		this.orderId = orderId;
 		this.placeOrderDateTime = placeOrderDateTime;
 		this.billingAndShippingDetail = billingAndShippingDetail;
 		this.paymentMethod = paymentMethod;
 		this.productItemDetail = productItemDetailList;
 		this.emailSendRequest = emailSendRequest;
 	}
 	public VendorOrderAlert() {
 		super();
 		// TODO Auto-generated constructor stub
 	}




 }