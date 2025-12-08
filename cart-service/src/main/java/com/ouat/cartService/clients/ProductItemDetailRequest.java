package com.ouat.cartService.clients;
 
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ProductItemDetailRequest {
	
	List<String> skus;

	public List<String> getSkus() {
		return skus;
	}
	public void setSkus(List<String> skus) {
		this.skus = skus;
	}
	public ProductItemDetailRequest(List<String> skus) {
		super();
		this.skus = skus;
	}
	public ProductItemDetailRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ProductItemDetail [listOfsku=" +  skus + "]";
	}
	

}
