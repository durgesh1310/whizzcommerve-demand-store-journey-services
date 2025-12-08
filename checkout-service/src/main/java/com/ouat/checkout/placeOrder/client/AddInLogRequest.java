package com.ouat.checkout.placeOrder.client;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AddInLogRequest {

	private long customerId;
	
	private String promocode;
	
	private String currentDateTime;
	
	private long orderId;
	
	private Double cartValue;
	
	private Double discount;
	
 
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public String getPromocode() {
		return promocode;
	}
	public void setPromocode(String promocode) {
		this.promocode = promocode;
	}
	public String getCurrentDateTime() {
		return currentDateTime;
	}
	public void setCurrentDateTime(String currentDateTime) {
		this.currentDateTime = currentDateTime;
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public Double getCartValue() {
		return cartValue;
	}
	public void setCartValue(Double cartValue) {
		this.cartValue = cartValue;
	}
	public Double getDiscount() {
		return discount;
	}
 
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public AddInLogRequest( long customerId, String promocode, String currentDateTime, long orderId,
			Double cartValue, Double discount) {
		super();
		 
		this.customerId = customerId;
		this.promocode = promocode;
		this.currentDateTime = currentDateTime;
		this.orderId = orderId;
		this.cartValue = cartValue;
		this.discount = discount;
	}
	public AddInLogRequest() {
		super();
	}
	@Override
	public String toString() {
		return "AddInLogRequest [customerId=" + customerId + ", promocode=" + promocode + ", currentDateTime="
				+ currentDateTime + ", orderId=" + orderId + ", cartValue=" + cartValue + ", discount=" + discount
				+ "]";
	}
	
	
	 
	
	
}
