package com.ouat.suggestionandsearch.exception;

public class BadRequestException extends Exception {
	private static final long serialVersionUID = -450456684552656231L;
	private String message;

	private String statusCode;

	public BadRequestException() {
		super();
	}
	
	public BadRequestException(String message) {
		super();
		this.message = message;
	}
	
	public BadRequestException(String message, String statusCode) {
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
