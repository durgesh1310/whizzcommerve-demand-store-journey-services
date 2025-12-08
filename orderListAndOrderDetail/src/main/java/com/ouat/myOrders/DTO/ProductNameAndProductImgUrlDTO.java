package com.ouat.myOrders.DTO;

public class ProductNameAndProductImgUrlDTO {
	private String sku;
	private String itemImageUrl;
	private String itemName;
	private Integer productId;
	private String attributeName;
    private String skuAttributevalue;
	
	
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public String getSkuAttributevalue() {
		return skuAttributevalue;
	}
	public void setSkuAttributevalue(String skuAttributevalue) {
		this.skuAttributevalue = skuAttributevalue;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getItemImageUrl() {
		return itemImageUrl;
	}
	public void setItemImageUrl(String itemImageUrl) {
		this.itemImageUrl = itemImageUrl;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	 
	public ProductNameAndProductImgUrlDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProductNameAndProductImgUrlDTO(String sku, String itemImageUrl, String itemName, Integer productId,
			String attributeName, String skuAttributevalue) {
		super();
		this.sku = sku;
		this.itemImageUrl = itemImageUrl;
		this.itemName = itemName;
		this.productId = productId;
		this.attributeName = attributeName;
		this.skuAttributevalue = skuAttributevalue;
	}
	@Override
	public String toString() {
		return "ProductNameAndProductImgUrlDTO [sku=" + sku + ", itemImageUrl=" + itemImageUrl + ", itemName="
				+ itemName + ", productId=" + productId + ", attributeName=" + attributeName + ", skuAttributevalue="
				+ skuAttributevalue + "]";
	}
	 
	
	 
}
