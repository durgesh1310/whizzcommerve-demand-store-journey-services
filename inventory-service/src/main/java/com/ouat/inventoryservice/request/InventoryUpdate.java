package com.ouat.inventoryservice.request;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class InventoryUpdate {

	private List<SkuAndQty> skuAndQtyList;
	
	private String user;

	public List<SkuAndQty> getSkuAndQtyList() {
		return skuAndQtyList;
	}

	public void setSkuAndQtyList(List<SkuAndQty> skuAndQtyList) {
		this.skuAndQtyList = skuAndQtyList;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "InventoryUpdate [skuAndQtyList=" + skuAndQtyList + ", user=" + user + "]";
	}

	

}