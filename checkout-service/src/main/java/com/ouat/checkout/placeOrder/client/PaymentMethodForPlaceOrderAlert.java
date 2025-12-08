package com.ouat.checkout.placeOrder.client;
 
public enum PaymentMethodForPlaceOrderAlert {
    COD("Cash On Delivery"), ONLINE("Online"), OWP("Order Without Payment");
	
	private String value;

	private PaymentMethodForPlaceOrderAlert(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}

}
