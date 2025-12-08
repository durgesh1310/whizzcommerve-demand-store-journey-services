package com.ouat.checkout.placeOrder.DTO;

import java.io.Serializable;

public class ShippingChargesDetailDTO  implements Serializable {
	private Double shippingCharges;
	private Double cartValue;
	public Double getShippingCharges() {
		return shippingCharges;
	}
	public void setShippingCharges(Double shippingCharges) {
		this.shippingCharges = shippingCharges;
	}
	public Double getCartValue() {
		return cartValue;
	}
	public void setCartValue(Double cartValue) {
		this.cartValue = cartValue;
	}
	public ShippingChargesDetailDTO(Double shippingCharges, Double cartValue) {
		super();
		this.shippingCharges = shippingCharges;
		this.cartValue = cartValue;
	}
	public ShippingChargesDetailDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ShippingChargesDetailDTO [shippingCharges=" + shippingCharges + ", cartValue=" + cartValue + "]";
	}
	
	
	
}
