package com.ouat.cartService.shoppingCartRequest;

import com.ouat.cartService.shoppingCartHelper.Platform;
public class ShowShoppingCartRequest {
	private String customerUuid;
	private  Platform platform;
	private Integer customerId;
	
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Platform getPlatform() {
		return platform;
	}
	public void setPlatform(Platform platform) {
		this.platform = platform;
	}
	public String getCustomerUuid() {
		return customerUuid;
	}
	public void setCustomerUuid(String customerUuid) {
		this.customerUuid = customerUuid;
	}
	public ShowShoppingCartRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ShowShoppingCartRequest(String customerUuid, Platform platform, Integer customerId) {
		super();
		this.customerUuid = customerUuid;
		this.platform = platform;
		this.customerId = customerId;
	}
	@Override
	public String toString() {
		return "ShowShoppingCartRequest [customerUuid=" + customerUuid + ", platform=" + platform + ", customerId="
				+ customerId + "]";
	}
	
}
