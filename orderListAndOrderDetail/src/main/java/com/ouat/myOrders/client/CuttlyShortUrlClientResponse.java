package com.ouat.myOrders.client;

public class CuttlyShortUrlClientResponse {
	
	private String fullLink;
	
	private String date;
	
	private String title;
	
	private String shortLink;

	public String getFullLink() {
		return fullLink;
	}

	public void setFullLink(String fullLink) {
		this.fullLink = fullLink;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortLink() {
		return shortLink;
	}

	public void setShortLink(String shortLink) {
		this.shortLink = shortLink;
	}

	@Override
	public String toString() {
		return "CuttlyShortUrlClientResponse [fullLink=" + fullLink + ", date=" + date + ", title=" + title
				+ ", shortLink=" + shortLink + "]";
	}
	

}
