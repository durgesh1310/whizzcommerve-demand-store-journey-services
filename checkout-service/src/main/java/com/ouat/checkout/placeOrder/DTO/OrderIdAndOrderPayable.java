package com.ouat.checkout.placeOrder.DTO;

public class OrderIdAndOrderPayable {
    private String orderNumber;
    private Integer orderId;
	private Double orderPayable;
	
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Double getOrderPayable() {
		return orderPayable;
	}
	public void setOrderPayable(Double orderPayable) {
		this.orderPayable = orderPayable;
	}
	public OrderIdAndOrderPayable(Integer orderId, Double orderPayable) {
		super();
		this.orderId = orderId;
		this.orderPayable = orderPayable;
	}
	public OrderIdAndOrderPayable() {
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
