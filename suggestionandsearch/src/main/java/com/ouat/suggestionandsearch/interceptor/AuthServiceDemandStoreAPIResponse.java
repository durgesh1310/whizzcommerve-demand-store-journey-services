package com.ouat.suggestionandsearch.interceptor;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.suggestionandsearch.controller.response.CustomerDetailVO;
import com.ouat.suggestionandsearch.controller.response.MessageDetail;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AuthServiceDemandStoreAPIResponse implements Serializable {

	private static final long serialVersionUID = -450456684552656231L;

	private boolean success = true;

	private List<MessageDetail> message;

	private String code;

	private CustomerDetailVO data;

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

	public CustomerDetailVO getData() {
		return data;
	}

	public void setData(CustomerDetailVO data) {
		this.data = data;
	}
	
	public AuthServiceDemandStoreAPIResponse() {
		super();
	}

	public AuthServiceDemandStoreAPIResponse(boolean success, List<MessageDetail> message, String code, CustomerDetailVO data) {
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
