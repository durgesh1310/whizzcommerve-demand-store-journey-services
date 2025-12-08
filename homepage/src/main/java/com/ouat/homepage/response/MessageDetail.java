package com.ouat.homepage.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MessageDetail {
	
	private MessageType msgType;
	
	private String msgText;
	
	
	private String leftAction;
	
	
	private String rightAction;


	public MessageType getMsgType() {
		return msgType;
	}


	public void setMsgType(MessageType msgType) {
		this.msgType = msgType;
	}


	public String getMsgText() {
		return msgText;
	}


	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}


	public String getLeftAction() {
		return leftAction;
	}


	public void setLeftAction(String leftAction) {
		this.leftAction = leftAction;
	}


	public String getRightAction() {
		return rightAction;
	}


	public void setRightAction(String rightAction) {
		this.rightAction = rightAction;
	}
	
	
	public MessageDetail() {
		super();
	}


	public MessageDetail(MessageType msgType, String msgText, String leftAction, String rightAction) {
		super();
		this.msgType = msgType;
		this.msgText = msgText;
		this.leftAction = leftAction;
		this.rightAction = rightAction;
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
