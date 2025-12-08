package com.ouat.cartService.clients;

import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.cartService.shoppingCartResponse.ProductItemPrice;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ProductItemDetail {
	private int productId;
	private String defualtImageUrl;
	private String sku;
	private String name;
	private Date saleStartDate;
	private Date saleEndDate;
	private Boolean isSkuActive;
	private int inventory;
	private ProductItemPrice  productItemPrices;
	private String category;
	private String subCategory;
	private String productType;
	public ProductItemDetail(int productId, String defualtImageUrl, String sku, String name, Date saleStartDate,
			Date saleEndDate, Boolean isSkuActive, int inventory, ProductItemPrice productItemPrices, String category,
			String subCategory, String productType) {
		super();
		this.productId = productId;
		this.defualtImageUrl = defualtImageUrl;
		this.sku = sku;
		this.name = name;
		this.saleStartDate = saleStartDate;
		this.saleEndDate = saleEndDate;
		this.isSkuActive = isSkuActive;
		this.inventory = inventory;
		this.productItemPrices = productItemPrices;
		this.category = category;
		this.subCategory = subCategory;
		this.productType = productType;
	}
	public ProductItemDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getDefualtImageUrl() {
		return defualtImageUrl;
	}
	public void setDefualtImageUrl(String defualtImageUrl) {
		this.defualtImageUrl = defualtImageUrl;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getSaleStartDate() {
		return saleStartDate;
	}
	public void setSaleStartDate(Date saleStartDate) {
		this.saleStartDate = saleStartDate;
	}
	public Date getSaleEndDate() {
		return saleEndDate;
	}
	public void setSaleEndDate(Date saleEndDate) {
		this.saleEndDate = saleEndDate;
	}
	public Boolean getIsSkuActive() {
		return isSkuActive;
	}
	public void setIsSkuActive(Boolean isSkuActive) {
		this.isSkuActive = isSkuActive;
	}
	public int getInventory() {
		return inventory;
	}
	public void setInventory(int inventory) {
		this.inventory = inventory;
	}
	public ProductItemPrice getProductItemPrices() {
		return productItemPrices;
	}
	public void setProductItemPrices(ProductItemPrice productItemPrices) {
		this.productItemPrices = productItemPrices;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	@Override
	public String toString() {
		return "ProductItemDetail [productId=" + productId + ", defualtImageUrl=" + defualtImageUrl + ", sku=" + sku
				+ ", name=" + name + ", saleStartDate=" + saleStartDate + ", saleEndDate=" + saleEndDate
				+ ", isSkuActive=" + isSkuActive + ", inventory=" + inventory + ", productItemPrices="
				+ productItemPrices + ", category=" + category + ", subCategory=" + subCategory + ", productType="
				+ productType + "]";
	}
	
	 

}
