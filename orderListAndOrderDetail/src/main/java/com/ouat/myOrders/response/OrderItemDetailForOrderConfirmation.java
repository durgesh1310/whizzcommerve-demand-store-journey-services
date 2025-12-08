package com.ouat.myOrders.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderItemDetailForOrderConfirmation {
	private Long orderItemId;
	private String orderStatus;
   	private ItemDetail itemDetail;
	public Long getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
 
	public ItemDetail getItemDetail() {
		return itemDetail;
	}
	public void setItemDetail(ItemDetail itemDetail) {
		this.itemDetail = itemDetail;
	}
	public OrderItemDetailForOrderConfirmation(Long orderItemId, String orderStatus, ItemDetail itemDetail) {
		super();
		this.orderItemId = orderItemId;
		this.orderStatus = orderStatus;
		this.itemDetail = itemDetail;
	}
	public OrderItemDetailForOrderConfirmation() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "OrderItemDetailForOrderConfirmation [orderItemId=" + orderItemId + ", orderStatus=" + orderStatus
				+ ", itemDetail=" + itemDetail + "]";
	}
	 

}
