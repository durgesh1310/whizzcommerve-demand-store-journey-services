package com.ouat.myOrders.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderDetailRequest {
    @Deprecated
	private Integer orderId;
    private String orderNumber;
    
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "OrderDetailRequest [orderId=" + orderId + "]";
	}

	public OrderDetailRequest(Integer orderId) {
		super();
		this.orderId = orderId;
	}

	public OrderDetailRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
	
}
