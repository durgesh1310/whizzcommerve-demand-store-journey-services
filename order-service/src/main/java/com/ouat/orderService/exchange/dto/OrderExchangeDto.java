package com.ouat.orderService.exchange.dto;

public class OrderExchangeDto {

	private String sku;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
	
	public OrderExchangeDto(String sku) {
		this.sku = sku;
	}

	@Override
	public String toString() {
		return "OrderExchangeDto [sku=" + sku + "]";
	}
	
	
}
