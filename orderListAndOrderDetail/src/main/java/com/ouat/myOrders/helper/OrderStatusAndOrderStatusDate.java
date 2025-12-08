package com.ouat.myOrders.helper;

public class OrderStatusAndOrderStatusDate {
    private	String orderStatus;
    private String orderStatusDate;
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderstatus) {
		this.orderStatus = orderstatus;
	}
	public String getOrderStatusDate() {
		return orderStatusDate;
	}
	public void setOrderStatusDate(String orderStatusDate) {
		this.orderStatusDate = orderStatusDate;
	}
	public OrderStatusAndOrderStatusDate(String orderStatus, String orderStatusDate) {
		super();
		this.orderStatus = orderStatus;
		this.orderStatusDate = orderStatusDate;
	}
	public OrderStatusAndOrderStatusDate() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "OrderStatusAndOrderStatusDate [orderstatus=" + orderStatus + ", orderDate=" + orderStatusDate + "]";
	}
    
    

}
