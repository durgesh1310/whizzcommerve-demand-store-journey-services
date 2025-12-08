package com.ouat.myOrders.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AdditionalDetail {
	private String awb;
	private String courierCompany;
	private String message;
	public String getAwb() {
		return awb;
	}
	public void setAwb(String awb) {
		this.awb = awb;
	}
	public String getCourierCompany() {
		return courierCompany;
	}
	public void setCourierCompany(String courierCompany) {
		this.courierCompany = courierCompany;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public AdditionalDetail(String awb, String courierCompany, String message) {
		super();
		this.awb = awb;
		this.courierCompany = courierCompany;
		this.message = message;
	}
	public AdditionalDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	

}
