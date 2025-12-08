package com.ouat.homepage.clients;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class APIResponse implements Serializable {

    private static final long serialVersionUID = -45045684552656231L;

    private Boolean success = true;

    private String message;

    private String code;   
    
   private  Object data;
 

	public APIResponse() {
        super();
    }

    public APIResponse(Boolean success, String message, String code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }
    
    public APIResponse(Boolean success, String message, String code, Object data) {
		super();
		this.success = success;
		this.message = message;
		this.code = code;
		this.data = data;
	}

    
    public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
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
