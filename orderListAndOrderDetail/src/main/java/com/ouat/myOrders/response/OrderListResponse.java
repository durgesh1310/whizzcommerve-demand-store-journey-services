package com.ouat.myOrders.response;

 import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderListResponse {
	
    @Deprecated
	private Integer orderId;
	private String orderDate;
	private Integer orderTotalPayAmount;
	List<OrderListItemDetail> orderResponseList;
	private String orderType;
	private String orderNumber;
	
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String date) {
		this.orderDate = date;
	}
	public Integer getOrderTotalPayAmount() {
		return orderTotalPayAmount;
	}
	public void setOrderTotalPayAmount(Integer orderTotalPayAmount) {
		this.orderTotalPayAmount = orderTotalPayAmount;
	}
	public List<OrderListItemDetail> getOrderResponseList() {
		return orderResponseList;
	}
	public void setOrderResponseList(List<OrderListItemDetail> orderResponseList) {
		this.orderResponseList = orderResponseList;
	}
	
	
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public OrderListResponse(Integer orderId, String orderDate, Integer orderTotalPayAmount,
			List<OrderListItemDetail> orderResponseList) {
		super();
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.orderTotalPayAmount = orderTotalPayAmount;
		this.orderResponseList = orderResponseList;
	}
	public OrderListResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "OrderListDetailResponse [orderId=" + orderId + ", orderDate=" + orderDate + ", orderTotalPayAmount="
				+ orderTotalPayAmount + ", orderResponseList=" + orderResponseList + "]";
	}
    public String getOrderNumber() {
        return orderNumber;
    }
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
	
}
