package com.ouat.myOrders.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderListRequest {


	private Integer customerId;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public OrderListRequest(Integer customerId) {
		super();
		this.customerId = customerId;
	}
	
	public OrderListRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "OrderListRequest [customerId=" + customerId + "]";
	}
	

}
