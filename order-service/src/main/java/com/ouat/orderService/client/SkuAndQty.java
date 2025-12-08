package com.ouat.orderService.client;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;


@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SkuAndQty {
	private String sku;
	private Integer qty;
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
	public SkuAndQty(String sku, Integer qty) {
		super();
		this.sku = sku;
		this.qty = qty;
	}
	public SkuAndQty() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
