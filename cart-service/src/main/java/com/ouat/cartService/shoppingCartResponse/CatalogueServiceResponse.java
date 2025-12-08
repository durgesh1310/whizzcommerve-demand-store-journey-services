package com.ouat.cartService.shoppingCartResponse;

import com.ouat.cartService.clients.ProductItemAndProductAttributeDetailResponse;

public class CatalogueServiceResponse {

	private Boolean success = false;

	private String message;

	private String code;
	
	private ProductItemAndProductAttributeDetailResponse data;

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

	public ProductItemAndProductAttributeDetailResponse getData() {
		return data;
	}

	public void setData(ProductItemAndProductAttributeDetailResponse data) {
		this.data = data;
	}
	
	

}
