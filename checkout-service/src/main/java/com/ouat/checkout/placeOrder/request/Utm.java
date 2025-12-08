package com.ouat.checkout.placeOrder.request;

public class Utm {
	private String utmSource;
	private String utmCampaign;
	private String utmMedium;
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
	public Utm(String utmSource, String utmCampaign, String utmMedium) {
		super();
		this.utmSource = utmSource;
		this.utmCampaign = utmCampaign;
		this.utmMedium = utmMedium;
	}
	public Utm() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Utm [utmSource=" + utmSource + ", utmCampaign=" + utmCampaign + ", utmMedium=" + utmMedium + "]";
	}
	
	
	
	

}
