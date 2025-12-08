package com.ouat.cartService.shoppingCartRequest;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.cartService.shoppingCartHelper.Location;
import com.ouat.cartService.shoppingCartHelper.Platform;
 
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AddToCartRequest {
	
    private Integer customerId;
	
    private String customerUuid;
    
	private Platform platform;
	
    private Location location;
   
    private String utmSource;
    
    private String utmCampaign;
    
    private String utmMedium;
    
    private String sku; 
    
    private Integer quantity;
    
    private Integer plpId;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerUuid() {
		return customerUuid;
	}

	public void setCustomerUuid(String customerUuid) {
		this.customerUuid = customerUuid;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getUtmSource() {
		return utmSource;
	}

	public void setUtmSource(String utmSource) {
		this.utmSource = utmSource;
	}

	public String getUtmCampaign() {
		return utmCampaign;
	}

	public void setUtmCampaign(String utmCampaign) {
		this.utmCampaign = utmCampaign;
	}

	public String getUtmMedium() {
		return utmMedium;
	}

	public void setUtmMedium(String utmMedium) {
		this.utmMedium = utmMedium;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getPlpId() {
		return plpId;
	}

	public void setPlpId(Integer plpId) {
		this.plpId = plpId;
	}
 

	public AddToCartRequest(Integer customerId, String customerUuid, Platform platform, Location location,
			String utmSource, String utmCampaign, String utmMedium, String sku, Integer quantity, Integer plpId) {
		super();
		 
		this.customerId = customerId;
		this.customerUuid = customerUuid;
		this.platform = platform;
		this.location = location;
		this.utmSource = utmSource;
		this.utmCampaign = utmCampaign;
		this.utmMedium = utmMedium;
		this.sku = sku;
		this.quantity = quantity;
		this.plpId = plpId;
	}

	public AddToCartRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
    @Override
	public String toString() {
		return "AddToCartRequest [ customerId=" + customerId + ", customerUuid=" + customerUuid
				+ ", platform=" + platform + ", location=" + location + ", utmSource=" + utmSource + ", utmCampaign="
				+ utmCampaign + ", utmMedium=" + utmMedium + ", sku=" + sku + ", quantity=" + quantity + ", plpId="
				+ plpId + "]";
	}
    
}
