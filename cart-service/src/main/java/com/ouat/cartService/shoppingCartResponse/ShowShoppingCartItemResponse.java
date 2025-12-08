package com.ouat.cartService.shoppingCartResponse;
import java.io.Serializable;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShowShoppingCartItemResponse  implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -1613027795579451433L;
	/**
	 * 
	 */
 	private String defaultImageUrl;
	private String productName;
	private String sku;
	private String size;
	private String edd;
	private int quantity;
	private Double retailPrice;
	private Double regularPrice;
	private Double priceChange;
	private Double itemDiscount;
	private Integer productId;
	private String category;
	private String subCategory;
	private String productType;
	private Integer plpId;
	private MessageDetail messageDetail;
	
	
	
	
	
	
	
	public String getEdd() {
		return edd;
	}
	public void setEdd(String edd) {
		this.edd = edd;
	}
	public Integer getPlpId() {
		return plpId;
	}
	public void setPlpId(Integer plpId) {
		this.plpId = plpId;
	}
	public String getDefaultImageUrl() {
		return defaultImageUrl;
	}
	public void setDefaultImageUrl(String defaultImageUrl) {
		this.defaultImageUrl = defaultImageUrl;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
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
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
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
	public Double getPriceChange() {
		return priceChange;
	}
	public void setPriceChange(Double priceChange) {
		this.priceChange = priceChange;
	}
	public Double getItemDiscount() {
		return itemDiscount;
	}
	public void setItemDiscount(Double itemDiscount) {
		this.itemDiscount = itemDiscount;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
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
	
	public MessageDetail getMessageDetail() {
		return messageDetail;
	}
	public void setMessageDetail(MessageDetail messageDetail) {
		this.messageDetail = messageDetail;
	}
	 
	public ShowShoppingCartItemResponse() {
		super();
	}
	public ShowShoppingCartItemResponse(String defaultImageUrl, String productName, String sku, String size, String edd,
			int quantity, Double retailPrice, Double regularPrice, Double priceChange, Double itemDiscount,
			Integer productId, String category, String subCategory, String productType, Integer plpId,
			MessageDetail messageDetail) {
		super();
		this.defaultImageUrl = defaultImageUrl;
		this.productName = productName;
		this.sku = sku;
		this.size = size;
		this.edd = edd;
		this.quantity = quantity;
		this.retailPrice = retailPrice;
		this.regularPrice = regularPrice;
		this.priceChange = priceChange;
		this.itemDiscount = itemDiscount;
		this.productId = productId;
		this.category = category;
		this.subCategory = subCategory;
		this.productType = productType;
		this.plpId = plpId;
		this.messageDetail = messageDetail;
	} 
	 
	
	
}
