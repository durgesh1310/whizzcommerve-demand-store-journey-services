package com.ouat.myOrders.response;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

 
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MyOrders {
	
	
	private Integer numberOfOrder;
	
	public MyOrders(Integer numberOfOrder, List<OrderListResponse> orderListDetailResponse) {
		super();
		this.numberOfOrder = numberOfOrder;
		this.orderListDetailResponse = orderListDetailResponse;
	}

	public Integer getNumberOfOrder() {
		return numberOfOrder;
	}

	public void setNumberOfOrder(Integer numberOfOrder) {
		this.numberOfOrder = numberOfOrder;
	}

	List<OrderListResponse> orderListDetailResponse;

	public List<OrderListResponse> getOrderListDetailResponse() {
		return orderListDetailResponse;
	}

	public void setOrderListDetailResponse(List<OrderListResponse> orderListDetailResponse) {
		this.orderListDetailResponse = orderListDetailResponse;
	}

	public MyOrders() {
		super();
		// TODO Auto-generated constructor stub
	}

	 
	
}

 
