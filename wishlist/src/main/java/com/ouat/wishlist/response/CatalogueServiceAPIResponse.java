package com.ouat.wishlist.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CatalogueServiceAPIResponse  implements Serializable {

    private static final long serialVersionUID = -450456684552656231L;

    private Boolean success = true;

    private String message;

    private String code;   
    
   private List<ProductDetailResponse> data;
 

	public CatalogueServiceAPIResponse () {
        super();
    }

    public CatalogueServiceAPIResponse (Boolean success, String message, String code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }
    
    public CatalogueServiceAPIResponse (Boolean success, String message, String code, List<ProductDetailResponse> data) {
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

	public List<ProductDetailResponse> getData() {
		return data;
	}

	public void setData(List<ProductDetailResponse> data) {
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
