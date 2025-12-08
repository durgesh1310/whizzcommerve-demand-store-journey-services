package com.ouat.authServiceDemandStore.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DemandStoreAPIResponse implements Serializable {

	private static final long serialVersionUID = -450456684552656231L;

	private boolean success = true;

	private List<MessageDetail> message;

	private String code;

	private Object data;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<MessageDetail> getMessage() {
		return message;
	}

	public void setMessage(List<MessageDetail> message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public DemandStoreAPIResponse() {
		super();
	}

	public DemandStoreAPIResponse(boolean success, List<MessageDetail> message, String code, Object data) {
		super();
		this.success = success;
		this.message = message;
		this.code = code;
		this.data = data;
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
