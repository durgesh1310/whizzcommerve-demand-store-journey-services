package com.ouat.cartService.shoppingCartResponse;

public class ShippingCharges {
	String platform ;
	Integer shippingCharges;
	public ShippingCharges(String platform, Integer shippingCharges) {
		super();
		this.platform = platform;
		this.shippingCharges = shippingCharges;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public Integer getShippingCharges() {
		return shippingCharges;
	}
	public void setShippingCharges(Integer shippingCharges) {
		this.shippingCharges = shippingCharges;
	}
	

}
