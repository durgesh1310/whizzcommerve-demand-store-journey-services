package com.ouat.checkout.dto.redis;

public class ShippingChargesDetailsRedisDto {
    private int shippingCharges;
    private int cartValue;
	public int getShippingCharges() {
		return shippingCharges;
	}
	public void setShippingCharges(int shippingCharges) {
		this.shippingCharges = shippingCharges;
	}
	public int getCartValue() {
		return cartValue;
	}
	public void setCartValue(int cartValue) {
		this.cartValue = cartValue;
	}
	public ShippingChargesDetailsRedisDto(int shippingCharges, int cartValue) {
		super();
		this.shippingCharges = shippingCharges;
		this.cartValue = cartValue;
	}
	public ShippingChargesDetailsRedisDto() {
		super();
		// TODO Auto-generated constructor stub
	}
}
