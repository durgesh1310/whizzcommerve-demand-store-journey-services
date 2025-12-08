package com.ouat.myOrders.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderListItemDetail {
	private String itemName;
	private String thumbNailImageUrl;
	private Integer itemPayAmount;
	private Integer qty;
	private  String orderStatus;
	private  String orderStatusDate ;
	private String orderType;
	
	
	
 	public OrderListItemDetail(String itemName, String thumbNailImageUrl, Integer itemPayAmount, Integer qty,
			String orderStatus, String orderStatusDate) {
		super();
		this.itemName = itemName;
		this.thumbNailImageUrl = thumbNailImageUrl;
		this.itemPayAmount = itemPayAmount;
		this.qty = qty;
		this.orderStatus = orderStatus;
		this.orderStatusDate = orderStatusDate;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getThumbNailImageUrl() {
		return thumbNailImageUrl;
	}
	public void setThumbNailImageUrl(String thumbNailImageUrl) {
		this.thumbNailImageUrl = thumbNailImageUrl;
	}
	public Integer getItemPayable() {
		return itemPayAmount;
	}
	public void setItemPayable(Integer orderItemPayable) {
		this.itemPayAmount = orderItemPayable;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderStatusDate() {
		return orderStatusDate;
	}
	public void setOrderStatusDate(String orderStatusDate) {
		this.orderStatusDate = orderStatusDate;
	}
	
	public Integer getItemPayAmount() {
		return itemPayAmount;
	}
	public void setItemPayAmount(Integer itemPayAmount) {
		this.itemPayAmount = itemPayAmount;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	
	
 
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public OrderListItemDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "OrderListItemDetail [itemName=" + itemName + ", thumbNailImageUrl=" + thumbNailImageUrl
				+ ", itemPayAmount=" + itemPayAmount + ", qty=" + qty + ", orderStatus=" + orderStatus
				+ ", orderStatusDate=" + orderStatusDate + "]";
	}
 
	

}
