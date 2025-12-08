package com.ouat.homepage.exception;

public class BusinessProcessException extends Exception {
	
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 4543017544224686172L;

	private String message;
	
	private String statusCode;

	public BusinessProcessException() {
		super();
	}
	
	public BusinessProcessException(String message, String statusCode) {
		super(message);
		this.message = message;
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	
}
