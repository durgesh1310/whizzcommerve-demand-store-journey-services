package com.ouat.suggestionandsearch.exception;

public class BusinessProcessException extends Exception {
	private static final long serialVersionUID = 1L;

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
	
	public BusinessProcessException(String message) {
        super(message);
        this.message = message;
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
