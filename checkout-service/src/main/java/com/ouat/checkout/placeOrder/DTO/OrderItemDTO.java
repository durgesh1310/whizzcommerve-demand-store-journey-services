package com.ouat.checkout.placeOrder.DTO;

import java.sql.Date;

public class OrderItemDTO {
	private Integer orderId;
	private String sku;
	private Integer qty;
	private Double orderItemTotalAmount;
	private Double orderItemPayable;
	private Double orderItemPlatformOfferedDiscount;
	private Double orderItemCreditApplied;
	private Double orderItemPromoDiscount;
	private Double vendorPrice;
	private Double ouatPrice;
	private Integer orderStatusId;
	private Date orderCreatedDate;
	private String orderCreatedBy;
	private Date updatedAt;
	private String updatedBy;
	private Boolean isReturnable;
	private Boolean isExchangable;
	private Long parentOrderItemId;
	private Date estimatedShippedDate;
	private Date estimatedDeliveryDate;
	private Double orderItemShippingCharges;
	private Double ouatMargin;
	
	
	
	 
	public Double getOuatMargin() {
		return ouatMargin;
	}
	public void setOuatMargin(Double ouatMargin) {
		this.ouatMargin = ouatMargin;
	}
	public Double getOrderItemShippingCharges() {
		return orderItemShippingCharges;
	}
	public void setOrderItemShippingCharges(Double orderItemShippingCharges) {
		this.orderItemShippingCharges = orderItemShippingCharges;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public Double getOrderItemTotalAmount() {
		return orderItemTotalAmount;
	}
	public void setOrderItemTotalAmount(Double orderItemTotalAmount) {
		this.orderItemTotalAmount = orderItemTotalAmount;
	}
	public Double getOrderItemPayable() {
		return orderItemPayable;
	}
	public void setOrderItemPayable(Double orderItemPayable) {
		this.orderItemPayable = orderItemPayable;
	}
	public Double getOrderItemPlatformOfferedDiscount() {
		return orderItemPlatformOfferedDiscount;
	}
	public void setOrderItemPlatformOfferedDiscount(Double orderItemPlatformOfferedDiscount) {
		this.orderItemPlatformOfferedDiscount = orderItemPlatformOfferedDiscount;
	}
	public Double getOrderItemCreditApplied() {
		return orderItemCreditApplied;
	}
	public void setOrderItemCreditApplied(Double orderItemCreditApplied) {
		this.orderItemCreditApplied = orderItemCreditApplied;
	}
	public Double getOrderItemPromoDiscount() {
		return orderItemPromoDiscount;
	}
	public void setOrderItemPromoDiscount(Double orderItemPromoDiscount) {
		this.orderItemPromoDiscount = orderItemPromoDiscount;
	}
	public Double getVendorPrice() {
		return vendorPrice;
	}
	public void setVendorPrice(Double vendorPrice) {
		this.vendorPrice = vendorPrice;
	}
	public Double getOuatPrice() {
		return ouatPrice;
	}
	public void setOuatPrice(Double ouatPrice) {
		this.ouatPrice = ouatPrice;
	}
	public Integer getOrderStatusId() {
		return orderStatusId;
	}
	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
	}
	public Date getOrderCreatedDate() {
		return orderCreatedDate;
	}
	public void setOrderCreatedDate(Date orderCreatedDate) {
		this.orderCreatedDate = orderCreatedDate;
	}
	public String getOrderCreatedBy() {
		return orderCreatedBy;
	}
	public void setOrderCreatedBy(String orderCreatedBy) {
		this.orderCreatedBy = orderCreatedBy;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Boolean getIsReturnable() {
		return isReturnable;
	}
	public void setIsReturnable(Boolean isReturnable) {
		this.isReturnable = isReturnable;
	}
	public Boolean getIsExchangable() {
		return isExchangable;
	}
	public void setIsExchangable(Boolean isExchangable) {
		this.isExchangable = isExchangable;
	}
	 
	public Long getParentOrderItemId() {
		return parentOrderItemId;
	}
	public void setParentOrderItemId(Long parentOrderItemId) {
		this.parentOrderItemId = parentOrderItemId;
	}
	public Date getEstimatedShippedDate() {
		return estimatedShippedDate;
	}
	public void setEstimatedShippedDate(Date estimatedShippedDate) {
		this.estimatedShippedDate = estimatedShippedDate;
	}
	public Date getEstimatedDeliveryDate() {
		return estimatedDeliveryDate;
	}
	public void setEstimatedDeliveryDate(Date estimatedDeliveryDate) {
		this.estimatedDeliveryDate = estimatedDeliveryDate;
	}
	
	public OrderItemDTO(Integer orderId, String sku, Integer qty, Double orderItemTotalAmount, Double orderItemPayable,
			Double orderItemPlatformOfferedDiscount, Double orderItemCreditApplied, Double orderItemPromoDiscount,
			Double vendorPrice, Double ouatPrice, Integer orderStatusId, Date orderCreatedDate, String orderCreatedBy,
			Date updatedAt, String updatedBy, Boolean isReturnable, Boolean isExchangable, Long parentOrderItemId,
			Date estimatedShippedDate, Date estimatedDeliveryDate, Double orderItemShippingCharges, Double ouatMargin) {
		super();
		this.orderId = orderId;
		this.sku = sku;
		this.qty = qty;
		this.orderItemTotalAmount = orderItemTotalAmount;
		this.orderItemPayable = orderItemPayable;
		this.orderItemPlatformOfferedDiscount = orderItemPlatformOfferedDiscount;
		this.orderItemCreditApplied = orderItemCreditApplied;
		this.orderItemPromoDiscount = orderItemPromoDiscount;
		this.vendorPrice = vendorPrice;
		this.ouatPrice = ouatPrice;
		this.orderStatusId = orderStatusId;
		this.orderCreatedDate = orderCreatedDate;
		this.orderCreatedBy = orderCreatedBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
		this.isReturnable = isReturnable;
		this.isExchangable = isExchangable;
		this.parentOrderItemId = parentOrderItemId;
		this.estimatedShippedDate = estimatedShippedDate;
		this.estimatedDeliveryDate = estimatedDeliveryDate;
		this.orderItemShippingCharges = orderItemShippingCharges;
		this.ouatMargin = ouatMargin;
	}
	public OrderItemDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	 
	
	
	
	
	
	

}
