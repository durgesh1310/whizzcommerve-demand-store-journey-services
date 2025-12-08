package com.ouat.wishlist.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageAlert {

	private String alterType;
	
	private String message;

	public String getAlterType() {
		return alterType;
	}

	public void setAlterType(String alterType) {
		this.alterType = alterType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MessageAlert(String alterType, String message) {
		super();
		this.alterType = alterType;
		this.message = message;
	}

	public MessageAlert() {
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
