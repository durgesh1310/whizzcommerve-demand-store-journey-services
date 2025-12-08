package com.ouat.orderService.client;

public enum EmailAlertType {
	RETURN("Return") , EXCHNAGE("Exchange"), CANCEL("Cancel");
	private String value;
	private EmailAlertType(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
 		
}

