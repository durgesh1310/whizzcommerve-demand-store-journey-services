package com.ouat.cartService.shoppingCartDTOs;

import java.sql.Date;

public class CartItemDTO {
	private Integer customerId;
	private String sku;
	private Double retailPrice ;
	private Double regularPrice;
	private Integer qty;
	private Date date;
	private String platform;
	private String location;
	private String utmCampaign;
	private String utmSource;
	private String utmMedium;
	private Integer plpId;
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
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
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getUtmCampaign() {
		return utmCampaign;
	}
	public void setUtmCampaign(String utmCampaign) {
		this.utmCampaign = utmCampaign;
	}
	public String getUtmSource() {
		return utmSource;
	}
	public void setUtmSource(String utmSource) {
		this.utmSource = utmSource;
	}
	public String getUtmMedium() {
		return utmMedium;
	}
	public void setUtmMedium(String utmMedium) {
		this.utmMedium = utmMedium;
	}
	public Integer getPlpId() {
		return plpId;
	}
	public void setPlpId(Integer plpId) {
		this.plpId = plpId;
	}
	public CartItemDTO(Integer customerId, String sku, Double retailPrice, Double regularPrice, Integer qty,
			Date date, String platform, String location, String utmCampaign, String utmSource, String utmMedium,
			Integer plpId) {
		super();
		this.customerId = customerId;
		this.sku = sku;
		this.retailPrice = retailPrice;
		this.regularPrice = regularPrice;
		this.qty = qty;
		this.date = date;
		this.platform = platform;
		this.location = location;
		this.utmCampaign = utmCampaign;
		this.utmSource = utmSource;
		this.utmMedium = utmMedium;
		this.plpId = plpId;
	}
	public CartItemDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "cartItemDTO [customerId=" + customerId + ", sku=" + sku + ", retailPrice=" + retailPrice
				+ ", regularPrice=" + regularPrice + ", qty=" + qty + ", date=" + date + ", platform=" + platform
				+ ", location=" + location + ", utmCampaign=" + utmCampaign + ", utmSource=" + utmSource
				+ ", utmMedium=" + utmMedium + ", plpId=" + plpId + "]";
	}
	
	
 

}
