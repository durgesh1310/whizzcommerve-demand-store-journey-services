package com.ouat.homepage.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PromoBannerResponse {
	
	private String imageWeb;
	
	private String imageMweb;
	
	private String imageMobile;
	
	private String url;
		
	private String action;
	
	private String type;
	
	private Integer mobileBannerId;

	public String getImageWeb() {
		return imageWeb;
	}

	public void setImageWeb(String imageWeb) {
		this.imageWeb = imageWeb;
	}

	public String getImageMobile() {
		return imageMobile;
	}

	public void setImageMobile(String imageMobile) {
		this.imageMobile = imageMobile;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImageMweb() {
		return imageMweb;
	}

	public void setImageMweb(String imageMweb) {
		this.imageMweb = imageMweb;
	}

	public Integer getMobileBannerId() {
		return mobileBannerId;
	}

	public void setMobileBannerId(Integer mobileBannerId) {
		this.mobileBannerId = mobileBannerId;
	}

	@Override
	public String toString() {
		return "PromoBannerResponse [imageWeb=" + imageWeb + ", imageMweb=" + imageMweb + ", imageMobile=" + imageMobile
				+ ", url=" + url + ", action=" + action + ", type=" + type + ", mobileBannerId=" + mobileBannerId + "]";
	}
	
}
