package com.ouat.notificationsender.client;
public enum DLTTemplateType {
	
	OTP("1107163540550886797"),
	PLACE_ORDER("1107163341056935515"),
	ORDER_SHIPPED("1107163349369851121"),
	ORDER_CANCEL("1107163349335743208"),
	REFUND("1107163843979487604"),
	REFERRAL("1107165466404918364");
	
	
	private String value;

	private DLTTemplateType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
