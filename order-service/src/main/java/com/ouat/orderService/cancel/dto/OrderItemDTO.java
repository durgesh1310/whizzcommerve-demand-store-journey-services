package com.ouat.orderService.cancel.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderItemDTO {
	
	
	private Integer orderId;
	
	private String sku;
	
	private Integer qty;
	
	private Double orderItemPayable;
	
	private Double orderItemCreditApplied;
	
	private String orderStatus;
	
	private String paymentMethod;
	
	private Double shippingCharges;
	
	private String orderNumber;

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

	public Double getOrderItemPayable() {
		return orderItemPayable;
	}

	public void setOrderItemPayable(Double orderItemPayable) {
		this.orderItemPayable = orderItemPayable;
	}

	public Double getOrderItemCreditApplied() {
		return orderItemCreditApplied;
	}

	public void setOrderItemCreditApplied(Double orderItemCreditApplied) {
		this.orderItemCreditApplied = orderItemCreditApplied;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	

	public Double getShippingCharges() {
		return shippingCharges;
	}

	public void setShippingCharges(Double shippingCharges) {
		this.shippingCharges = shippingCharges;
	}
	
	public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        String serialized = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            serialized = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException jpe) {
            jpe.printStackTrace();
        }
        return serialized;
    }

	public OrderItemDTO(Integer orderId, String sku, Integer qty, Double orderItemPayable,
			Double orderItemCreditApplied, String orderStatus, String paymentMethod, Double shippingCharges, String orderNumber) {
		super();
		this.orderId = orderId;
		this.sku = sku;
		this.qty = qty;
		this.orderItemPayable = orderItemPayable;
		this.orderItemCreditApplied = orderItemCreditApplied;
		this.orderStatus = orderStatus;
		this.paymentMethod = paymentMethod;
		this.shippingCharges = shippingCharges;
		this.orderNumber = orderNumber;
	}

	
}
