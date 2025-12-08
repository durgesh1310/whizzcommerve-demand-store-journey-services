package com.ouat.cartService.shoppingCartResponse;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ProductItemPrice {
	
	private Double retailPrice;
	private Double regularPrice;
	private Double salePrice;
 
	
	
	public Double getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}
	public Double getRegularPrice() {
		return regularPrice;
	}
	public void setRegularPrice(Double regularPrice) {
		this.regularPrice = regularPrice;
	}
	public Double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}
	public ProductItemPrice() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public ProductItemPrice(Double retailPrice, Double regularPrice, Double salePrice) {
		super();
		this.retailPrice = retailPrice;
		this.regularPrice = regularPrice;
		this.salePrice = salePrice;
	}
	
	
	
	@Override
	public String toString() {
		return "ProductItemPrice [retailPrice=" + retailPrice + ", regularPrice=" + regularPrice + ", salePrice="
				+ salePrice + "]";
	}
	

}
