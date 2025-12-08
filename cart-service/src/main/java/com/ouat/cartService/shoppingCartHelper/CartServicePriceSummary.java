package com.ouat.cartService.shoppingCartHelper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CartServicePriceSummary {
	
	private Double totalRegularPrice;
	private Double totalRetailPrice;
	public Double getTotalRegularPrice() {
		return totalRegularPrice;
	}
	public void setTotalRegularPrice(Double totalRegularPrice) {
		this.totalRegularPrice = totalRegularPrice;
	}
	public Double getTotalRetailPrice() {
		return totalRetailPrice;
	}
	public void setTotalRetailPrice(Double totalRetailPrice) {
		this.totalRetailPrice = totalRetailPrice;
	}
	public CartServicePriceSummary() {
		super();
		// TODO Auto-generated constructor stub
	}

   
	

}
