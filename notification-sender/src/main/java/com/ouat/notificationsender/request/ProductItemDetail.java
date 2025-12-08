package com.ouat.notificationsender.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ProductItemDetail {
	private String productName;
	private String itemImageUrl;
	private String sku;
	private String size;
	private Integer qty;
	private Double price;
	private Double ouatMargin;
	
	
	
	public Double getOuatMargin() {
		return ouatMargin;
	}
	public void setOuatMargin(Double ouatMargin) {
		this.ouatMargin = ouatMargin;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getItemImageUrl() {
		return itemImageUrl;
	}
	public void setItemImageUrl(String itemImageUrl) {
		this.itemImageUrl = itemImageUrl;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public ProductItemDetail(String productName, String itemImageUrl, String sku, String size, Integer qty,
			Double price, Double ouatMargin) {
		super();
		this.productName = productName;
		this.itemImageUrl = itemImageUrl;
		this.sku = sku;
		this.size = size;
		this.qty = qty;
		this.price = price;
		this.ouatMargin = ouatMargin;
	}
	public ProductItemDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ProductItemDetail [productName=" + productName + ", itemImageUrl=" + itemImageUrl + ", sku=" + sku
				+ ", size=" + size + ", qty=" + qty + ", price=" + price + ", ouatMargin=" + ouatMargin + "]";
	}
	 
	 
	

}
