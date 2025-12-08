package com.ouat.authServiceDemandStore.request;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserTokenValidateRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2238705695908080146L;
	
	
	private String token;


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
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
