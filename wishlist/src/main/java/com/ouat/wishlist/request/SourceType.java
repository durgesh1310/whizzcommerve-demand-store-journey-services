package com.ouat.wishlist.request;

public enum SourceType {
	
	PLP("PLP"), PDP("PDP");
	
	private String value;

	public String getValue() {
		return value;
	}
	
	private SourceType(String value) {
		this.value = value;
	}


	
	

}
