package com.ouat.myOrders.client;

import java.util.Map;

import com.ouat.myOrders.DTO.ProductNameAndProductImgUrlDTO;


public class CatalogueServiceResponse {

	private Boolean success = false;

	private String message;

	private String code;
	
	private Map<String, ProductNameAndProductImgUrlDTO> data;

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

	public Map<String, ProductNameAndProductImgUrlDTO> getData() {
		return data;
	}

	public void setData(Map<String, ProductNameAndProductImgUrlDTO> data) {
		this.data = data;
	}
	
	

}
