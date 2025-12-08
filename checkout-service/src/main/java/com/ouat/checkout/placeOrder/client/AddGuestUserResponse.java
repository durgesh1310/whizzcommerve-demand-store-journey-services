package com.ouat.checkout.placeOrder.client;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AddGuestUserResponse {
	private Integer customerId;
	private Integer addressId;
 
	public Integer getCustomerId() {
		return customerId;
	}
 
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
 
	public Integer getAddressId() {
		return addressId;
	}
 
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
 
	public AddGuestUserResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AddGuestUserResponse(Integer customerId, Integer addressId) {
		super();
		this.customerId = customerId;
		this.addressId = addressId;
	}

	@Override
	public String toString() {
		return "AddGuestUserResponse [customerId=" + customerId + ", addressId=" + addressId + "]";
	}
	
	
	

}
