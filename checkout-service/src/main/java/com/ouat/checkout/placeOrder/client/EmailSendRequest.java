package com.ouat.checkout.placeOrder.client;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class EmailSendRequest {
	
	@JsonProperty("to_email_address")
	private List<String> toEmailAddress;
	
	@JsonProperty("subject")
	private String subject;
	
	@JsonProperty("from_email")
	private String fromEmail;
	
	@JsonProperty("from_nick_name")
	private String fromNickName;
	
	@JsonProperty("message_body")
	private String messageBody;


	public List<String> getToEmailAddress() {
		return toEmailAddress;
	}


	public void setToEmailAddress(List<String> toEmailAddress) {
		this.toEmailAddress = toEmailAddress;
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getFromEmail() {
		return fromEmail;
	}


	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}


	public String getFromNickName() {
		return fromNickName;
	}


	public void setFromNickName(String fromNickName) {
		this.fromNickName = fromNickName;
	}


	public String getMessageBody() {
		return messageBody;
	}


	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	
	
	
	
	public EmailSendRequest() {
		super();
	}


	public EmailSendRequest(List<String> toEmailAddress, String subject, String fromEmail, String fromNickName,
			String messageBody) {
		super();
		this.toEmailAddress = toEmailAddress;
		this.subject = subject;
		this.fromEmail = fromEmail;
		this.fromNickName = fromNickName;
		this.messageBody = messageBody;
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
