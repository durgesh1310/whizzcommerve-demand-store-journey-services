package com.ouat.checkout.placeOrder.DTO;
public class OrderItemVO {
	
	private Long orderItemId;
	private String sku;
	private Integer OrderItemcreditAmount;
	public Long getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Integer getOrderItemcreditAmount() {
		return OrderItemcreditAmount;
	}
	public void setOrderItemcreditAmount(Integer orderItemcreditAmount) {
		OrderItemcreditAmount = orderItemcreditAmount;
	}
	public OrderItemVO(Long orderItemId, String sku, Integer orderItemcreditAmount) {
		super();
		this.orderItemId = orderItemId;
		this.sku = sku;
		OrderItemcreditAmount = orderItemcreditAmount;
	}
	public OrderItemVO() {
		super();
		// TODO Auto-generated constructor stub
	}
}
