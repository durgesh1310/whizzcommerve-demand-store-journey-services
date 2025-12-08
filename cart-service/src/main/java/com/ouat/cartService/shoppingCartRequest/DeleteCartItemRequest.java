package com.ouat.cartService.shoppingCartRequest;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DeleteCartItemRequest {
	private Integer customerId;
	private String customerUuid;
	private String sku;
	
	public String getCustomerUuid() {
		return customerUuid;
	}
	public void setCustomerUuid(String customerUuid) {
		this.customerUuid = customerUuid;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public DeleteCartItemRequest(Integer customerId, String sku) {
		super();
		this.customerId = customerId;
		this.sku = sku;
	}
	public DeleteCartItemRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "DeleteCartItemRequest [customerId=" + customerId + ", sku=" + sku + "]";
	}
	
	
}
