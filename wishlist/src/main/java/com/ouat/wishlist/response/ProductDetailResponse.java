package com.ouat.wishlist.response;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public class ProductDetailResponse  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	private int productId;

	private String productName;

	private String productImage;
	
	private Integer fromPrice;
	
	private Integer toPrice;
	
	private Integer salePrice;
	
	

	public ProductDetailResponse(int productId, String productName, String productImage, Integer fromPrice,
			Integer toPrice, Integer salePrice) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productImage = productImage;
		this.fromPrice = fromPrice;
		this.toPrice = toPrice;
		this.salePrice = salePrice;
	}

	public Integer getFromPrice() {
		return fromPrice;
	}

	public void setFromPrice(Integer fromPrice) {
		this.fromPrice = fromPrice;
	}

	public Integer getToPrice() {
		return toPrice;
	}

	public void setToPrice(Integer toPrice) {
		this.toPrice = toPrice;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}


	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}
	
	
	public Integer getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Integer salePrice) {
		this.salePrice = salePrice;
	}

	public ProductDetailResponse() {
		super();
		// TODO Auto-generated constructor stub
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
