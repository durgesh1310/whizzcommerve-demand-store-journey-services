package com.ouat.notificationsender.response;

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

