package com.ouat.notificationsender.request;
public enum PaymentMethod {
    COD("Cash On Delivery"), ONLINE("Online"), OWP("Order Without Payment");
	private String value;
	private PaymentMethod(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}

}
