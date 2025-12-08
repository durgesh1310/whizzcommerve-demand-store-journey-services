package com.ouat.cartService.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OTPSentResponse {
	
	private Integer otpLength;
	
	private String notificationMessage;

	public Integer getOtpLength() {
		return otpLength;
	}

	public void setOtpLength(Integer otpLength) {
		this.otpLength = otpLength;
	}

	public String getNotificationMessage() {
		return notificationMessage;
	}

	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}
	
	
	
	public OTPSentResponse(Integer otpLength, String notificationMessage) {
		super();
		this.otpLength = otpLength;
		this.notificationMessage = notificationMessage;
	}

	@Override
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
