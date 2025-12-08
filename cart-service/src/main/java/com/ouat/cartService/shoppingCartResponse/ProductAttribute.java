package com.ouat.cartService.shoppingCartResponse;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ProductAttribute {
	private String sku;
	private int productId;
	private String attributeName;
	private String attributeValue;
	private int isProductLevel;
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	public int getIsProductLevel() {
		return isProductLevel;
	}
	public void setIsProductLevel(int isProductLevel) {
		this.isProductLevel = isProductLevel;
	}
	public ProductAttribute(String sku, int productId, String attributeName, String attributeValue,
			int isProductLevel) {
		super();
		this.sku = sku;
		this.productId = productId;
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
		this.isProductLevel = isProductLevel;
	}
	public ProductAttribute() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ProductAttribute [sku=" + sku + ", productId=" + productId + ", attributeName=" + attributeName
				+ ", attributeValue=" + attributeValue + ", isProductLevel=" + isProductLevel + "]";
	}
 
    
 
	 
	
	

}
