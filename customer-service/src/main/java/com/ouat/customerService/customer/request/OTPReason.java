package com.ouat.customerService.customer.request;

public enum OTPReason {
	
	LOGIN("LOGIN"), PROFILE_UPDATE("PROFILE_UPDATE");
	
	private String value;
	
	private OTPReason(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}


}
