package com.ouat.cartService.shoppingCartHelper;

public class CartPriceSummary {
	private Double totalPrice;
	private Double totalpriceWithDiscount;
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Double getTotalpriceWithDiscount() {
		return totalpriceWithDiscount;
	}
	public void setTotalpriceWithDiscount(Double totalpriceWithDiscount) {
		this.totalpriceWithDiscount = totalpriceWithDiscount;
	}
	public CartPriceSummary(Double totalPrice, Double totalpriceWithDiscount) {
		super();
		this.totalPrice = totalPrice;
		this.totalpriceWithDiscount = totalpriceWithDiscount;
	}
 

}
