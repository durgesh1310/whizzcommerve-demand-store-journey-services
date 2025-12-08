package com.ouat.cartService.clients;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.cartService.shoppingCartResponse.ProductAttribute;
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ProductItemAndProductAttributeDetailResponse {
	
	private String message;
		
	private String status;
	
	private List<ProductItemDetail> productItemDetail;
	
	private List<ProductAttribute> productAttribute;
	
	private Map<String,ProductItemDetail> productItemDetailMap  ;
    private Map<String,ProductAttribute> productAttributeMap  ;

	public Map<String, ProductItemDetail> getProductItemDetailMap() {
		return productItemDetailMap;
	}
	public void setProductItemDetailMap(Map<String, ProductItemDetail> productItemDetailMap) {
		this.productItemDetailMap = productItemDetailMap;
	}
	public Map<String, ProductAttribute> getProductAttributeMap() {
		return productAttributeMap;
	}
	public void setProductAttributeMap(Map<String, ProductAttribute> productAttributeMap) {
		this.productAttributeMap = productAttributeMap;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<ProductItemDetail> getProductItemDetail() {
		return productItemDetail;
	}
	public void setProductItemDetail(List<ProductItemDetail> productItemDetail) {
		this.productItemDetail = productItemDetail;
	}
	public List<ProductAttribute> getProductAttribute() {
		return productAttribute;
	}
	public void setProductAttribute(List<ProductAttribute> productAttribute) {
		this.productAttribute = productAttribute;
	}
	
	public ProductItemAndProductAttributeDetailResponse(List<ProductItemDetail> productItemDetail,
			List<ProductAttribute> productAttribute) {
		super();
		this.productItemDetail = productItemDetail;
		this.productAttribute = productAttribute;
		 
	}
	@Override
	public String toString() {
		return "ProductItemAndProductAttributeDetailResponse [productItemDetail=" + productItemDetail
				+ ", productAttribute=" + productAttribute + "]";
	}
	public ProductItemAndProductAttributeDetailResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}