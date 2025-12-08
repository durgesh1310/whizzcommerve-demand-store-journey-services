package com.ouat.myOrders.client;

import com.ouat.myOrders.response.Address;

public class CustomerServiceResponse {
	
	
	private Boolean success = false;
	
	
	private Address data;


	public Boolean getSuccess() {
		return success;
	}


	public void setSuccess(Boolean success) {
		this.success = success;
	}


	public Address getData() {
		return data;
	}


	public void setData(Address data) {
		this.data = data;
	}

	
}
