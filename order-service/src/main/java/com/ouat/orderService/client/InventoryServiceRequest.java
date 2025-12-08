package com.ouat.orderService.client;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;


@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
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
	}

	public InventoryServiceRequest(List<SkuAndQty> skuAndQtyList) {
		super();
		this.skuAndQtyList = skuAndQtyList;
	}
}
