package com.ouat.checkout.placeOrder.request;

public class PlaceOrderRequest {
	
	private String paymentMode ="COD";
	
	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	 

	public PlaceOrderRequest(String paymentMode) {
		super();
		this.paymentMode = paymentMode;
	}

	public PlaceOrderRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
