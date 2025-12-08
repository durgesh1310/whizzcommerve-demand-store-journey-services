package com.ouat.checkout.placeOrder.client;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PriceSummaryForPlaceOrderAlert {
	private Double subTotal;
	private Double couponDiscount;
	private Double shippingAndHandling;
	private Double grandTotal;
	private Double creditApllied;
	
	public Double getCreditApllied() {
		return creditApllied;
	}
	public void setCreditApllied(Double creditApllied) {
		this.creditApllied = creditApllied;
	}
	public Double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}
	public Double getCouponDiscount() {
		return couponDiscount;
	}
	public void setCouponDiscount(Double couponDiscount) {
		this.couponDiscount = couponDiscount;
	}
 
	public Double getShippingAndHandling() {
		return shippingAndHandling;
	}
	public void setShippingAndHandling(Double shippingAndHandling) {
		this.shippingAndHandling = shippingAndHandling;
	}
	public Double getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}
 
	public PriceSummaryForPlaceOrderAlert() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "PriceSummaryForPlaceOrderAlert [subTotal=" + subTotal + ", couponDiscount=" + couponDiscount
				+ ", shippingAndHandling=" + shippingAndHandling + ", grandTotal=" + grandTotal + ", creditApllied="
				+ creditApllied + "]";
	}
	public PriceSummaryForPlaceOrderAlert(Double subTotal, Double couponDiscount, Double shippingAndHandling,
			Double grandTotal, Double creditApllied) {
		super();
		this.subTotal = subTotal;
		this.couponDiscount = couponDiscount;
		this.shippingAndHandling = shippingAndHandling;
		this.grandTotal = grandTotal;
		this.creditApllied = creditApllied;
	}
	 
	 
	 
}
