package com.ouat.notificationsender.request;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EmailNotificationRequest {
	
	private EmailNotificationType type;
	
	private Map<String, Object> data;
	
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
	

	public EmailNotificationType getType() {
		return type;
	}

	public void setType(EmailNotificationType type) {
		this.type = type;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
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
