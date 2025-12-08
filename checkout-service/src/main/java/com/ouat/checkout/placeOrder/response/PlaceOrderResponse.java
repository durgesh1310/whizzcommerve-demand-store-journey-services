package com.ouat.checkout.placeOrder.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PlaceOrderResponse {
	private Integer orderId;
	private String orderStatus;
	private String thirdPartyOrderId;
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public PlaceOrderResponse(Integer orderId, String orderStatus) {
		super();
		this.orderId = orderId;
		this.orderStatus = orderStatus;
	}
	public PlaceOrderResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "PlaceOrderResponse [orderId=" + orderId + ", orderStatus=" + orderStatus + "]";
	}
    public String getThirdPartyOrderId() {
        return thirdPartyOrderId;
    }
    public void setThirdPartyOrderId(String thirdPartyOrderId) {
        this.thirdPartyOrderId = thirdPartyOrderId;
    }
	
}
