package com.ouat.myOrders.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ItemDetail {
	
	private Integer productId;
	private String sku;
	private String itemName;
	private String imageUrl;
	private Integer ItemPayable;
	private Integer quantity;
	private String size;

	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Integer getItemPayable() {
		return ItemPayable;
	}
	public void setItemPayable(Integer itemPayable) {
		ItemPayable = itemPayable;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
 
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public ItemDetail(Integer productId, String sku, String itemName, String imageUrl, Integer itemPayable, Integer quantity
			, String size ) {
		super();
		this.productId=productId;
		this.sku = sku;
		this.itemName = itemName;
		this.imageUrl = imageUrl;
		this.ItemPayable = itemPayable;
		this.quantity = quantity;
		this.size = size;
	}
	public ItemDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ItemDetail [productId=" + productId + ", sku=" + sku + ", itemName=" + itemName + ", imageUrl="
				+ imageUrl + ", ItemPayable=" + ItemPayable + ", quantity=" + quantity + ", size=" + size + "]";
	}
	 
	 
	
}
