package com.ouat.checkout.placeOrder.client;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

 
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderItemCreditDetail implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3930980333721807357L;

	private Long orderItemId;
	
	private Double amount;
	
	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public OrderItemCreditDetail(Long orderItemId, Double amount) {
		super();
		this.orderItemId = orderItemId;
		this.amount = amount;
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
	

}
