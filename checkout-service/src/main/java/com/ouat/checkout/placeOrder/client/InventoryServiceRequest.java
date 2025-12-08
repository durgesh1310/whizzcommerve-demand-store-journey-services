package com.ouat.checkout.placeOrder.client;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.ToString;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@ToString
public class InventoryServiceRequest {
	
	private List<SkuAndQty>skuAndQtyList;

	public List<SkuAndQty> getSkuAndQtyList() {
		return skuAndQtyList;
	}

	public void setSkuAndQtyList(List<SkuAndQty> skuAndQtyList) {
		this.skuAndQtyList = skuAndQtyList;
	}

	public InventoryServiceRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InventoryServiceRequest(List<SkuAndQty> skuAndQtyList) {
		super();
		this.skuAndQtyList = skuAndQtyList;
	}
}
