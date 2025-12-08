package com.ouat.checkout.placeOrder.client;

public class EmailServiceResponse {
	
	private Boolean success = false;

	private String message;

	private String code;
	
	private boolean data;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "InventoryServiceResponse [success=" + success + ", message=" + message + ", code=" + code + ", data="
				+ data + "]";
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	 
	public boolean isData() {
		return data;
	}

	public void setData(boolean data) {
		this.data = data;
	}

	public EmailServiceResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmailServiceResponse(Boolean success, String message, String code, boolean data) {
		super();
		this.success = success;
		this.message = message;
		this.code = code;
		this.data = data;
	}
	

}
