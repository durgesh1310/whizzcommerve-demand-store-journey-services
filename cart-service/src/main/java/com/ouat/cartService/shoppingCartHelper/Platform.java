package com.ouat.cartService.shoppingCartHelper;

public enum Platform {
	 
	ANDROID("ANDROID"),  IOS("IOS"), WEB("WEB"), MWEB("MWEB"), MOBILE("MOBILE");
	private String value;

	private Platform(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}

}
