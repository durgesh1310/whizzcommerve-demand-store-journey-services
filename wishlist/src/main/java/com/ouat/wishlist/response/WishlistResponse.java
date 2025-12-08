package com.ouat.wishlist.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WishlistResponse  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WishlistResponse(Long id, Long productId, String productName, String price, Double discount,
			String productImage, List<MessageAlert> messageList, String salePrice) {
		super();
		this.id = id;
		this.productId = productId;
		this.productName = productName;
		this.price = price;
		this.discount = discount;
		this.productImage = productImage;
		this.messageList = messageList;
		this.salePrice = salePrice;
	}
	private Long id;
	
	private Long productId;
	
	private String productName;
	
	private String price;
	
	private Double discount;
	
	private String productImage;
	
	private List<MessageAlert> messageList;
	
	private String salePrice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public List<MessageAlert> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<MessageAlert> messageList) {
		this.messageList = messageList;
	}

	public String getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}

	public WishlistResponse() {
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
