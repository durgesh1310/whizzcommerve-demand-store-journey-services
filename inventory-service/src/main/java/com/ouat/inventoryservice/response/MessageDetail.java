package com.ouat.inventoryservice.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDetail {
	
	private MessageType msgType;
	
	private String msgText;
	
	
	private String leftAction;
	
	
	private String rightAction;

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