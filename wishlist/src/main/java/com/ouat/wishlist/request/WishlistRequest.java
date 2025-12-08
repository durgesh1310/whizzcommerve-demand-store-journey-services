package com.ouat.wishlist.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WishlistRequest {
	
	private Long productId;
	
	private Double fromPrice;

	private Double toPrice;
	
	private SourceType source;
	
	private PlatformType platform;
	
	private String utmCampaign;
	
	private String utmMedium;
	
	private String utmSource;

	

	public Double getFromPrice() {
		return fromPrice;
	}

	public void setFromPrice(Double fromPrice) {
		this.fromPrice = fromPrice;
	}

	public Double getToPrice() {
		return toPrice;
	}

	public void setToPrice(Double toPrice) {
		this.toPrice = toPrice;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public SourceType getSource() {
		return source;
	}

	public void setSource(SourceType source) {
		this.source = source;
	}

	public PlatformType getPlatform() {
		return platform;
	}

	public void setPlatform(PlatformType platform) {
		this.platform = platform;
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

	public String getUtmSource() {
		return utmSource;
	}

	public void setUtmSource(String utmSource) {
		this.utmSource = utmSource;
	}

	public WishlistRequest(Long productId, Double fromPrice, Double toPrice, SourceType source, PlatformType platform, String utmCampaign, String utmMedium, String utmSource) {
		super();
		this.productId = productId;
		this.fromPrice = fromPrice;
		this.toPrice = toPrice;
		this.source = source;
		this.platform = platform;
		this.utmCampaign = utmCampaign;
		this.utmMedium = utmMedium;
		this.utmSource = utmSource;
	}

	public WishlistRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		String serialized = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			serialized = objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException jpe) {
			jpe.printStackTrace();
		}
		return serialized;
	}

}
