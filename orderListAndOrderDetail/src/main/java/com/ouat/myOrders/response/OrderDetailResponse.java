package com.ouat.myOrders.response;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderDetailResponse {
    private Integer orderNumber;
	private String customOrderNumber;
	private String orderDate;
	private List<OrderItemDetail> orderItem;
	private PriceSummary priceSummary;
	private Address shippingAddress;
	private String orderType;
	private Long customerId;
	

	public Integer getOrderNumber()
	{
	    return this.orderNumber;
	}
	
	public void setOrderNumber(Integer orderNumber) {
	    this.orderNumber = orderNumber;
	}
	
	public String getCustomOrderNumber() {
		return customOrderNumber;
	}
	public void setCustomOrderNumber(String customOrderNumber) {
		this.customOrderNumber = customOrderNumber;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String formatedDate) {
		this.orderDate = formatedDate;
	}
	public List<OrderItemDetail> getOrderItem() {
		return orderItem;
	}
	public void setOrderItem(List<OrderItemDetail> orderItem) {
		this.orderItem = orderItem;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	public OrderDetailResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OrderDetailResponse(String orderNumber, String orderDate, List<OrderItemDetail> orderItem,
			PriceSummary priceSummary, Address shippingAddress, Long customerId) {
		super();
		this.customOrderNumber = orderNumber;
		this.orderDate = orderDate;
		this.orderItem = orderItem;
		this.priceSummary = priceSummary;
		this.shippingAddress = shippingAddress;
		this.customerId = customerId;
	}
	 
	
	@Override
	public String toString() {
		return "OrderDetailResponse [orderNumber=" + customOrderNumber + ", orderDate=" + orderDate + ", orderItem="
				+ orderItem + ", priceSummary=" + priceSummary + ", shippingAddress=" + shippingAddress + ", customerId=" +customerId +"]";
	}
	public PriceSummary getPriceSummary() {
		return priceSummary;
	}
	public void setPriceSummary(PriceSummary priceSummary) {
		this.priceSummary = priceSummary;
	}
	public Address getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	 
	 
	 
	
	

}
