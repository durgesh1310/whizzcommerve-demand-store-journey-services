package com.ouat.checkout.placeOrder.client;

public class EmailServiceRequest {
	private Integer customerId;
	private String email;
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public EmailServiceRequest(Integer customerId, String email) {
		super();
		this.customerId = customerId;
		this.email = email;
	}
	public EmailServiceRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "EmailServiceRequest [customerId=" + customerId + ", email=" + email + "]";
	}
	
	

}
