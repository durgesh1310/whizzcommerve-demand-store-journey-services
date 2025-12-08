package com.ouat.myOrders.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PriceSummary {
	private double orderTotal;
	private double orderPayable;
	private double platformDiscount;
	private double promoDiscount;
	private double creditApplied;
	private double shippingCharge;
	private String promocode;
	public double getOderTotal() {
		return orderTotal;
	}
	public void setOderTotal(double oderTotal) {
		this.orderTotal = oderTotal;
	}
	public double getOrderPayable() {
		return orderPayable;
	}
	public void setOrderPayable(double orderPayable) {
		this.orderPayable = orderPayable;
	}
	public double getPlatformDiscount() {
		return platformDiscount;
	}
	public void setPlatformDiscount(double platformDiscount) {
		this.platformDiscount = platformDiscount;
	}
	public double getPromoDiscount() {
		return promoDiscount;
	}
	public void setPromoDiscount(double promoDiscount) {
		this.promoDiscount = promoDiscount;
	}
	public double getCreditApplied() {
		return creditApplied;
	}
	public void setCreditApplied(double creditApplied) {
		this.creditApplied = creditApplied;
	}
	public double getShippingCharge() {
		return shippingCharge;
	}
	public void setShippingCharge(double shippingCharge) {
		this.shippingCharge = shippingCharge;
	}
	public String getPromocode() {
		return promocode;
	}
	public void setPromocode(String promocode) {
		this.promocode = promocode;
	}
	public PriceSummary(double oderTotal, double orderPayable, double platformDiscount, double promoDiscount,
			double creditApplied, double shippingCharge, String promocode) {
		super();
		this.orderTotal = oderTotal;
		this.orderPayable = orderPayable;
		this.platformDiscount = platformDiscount;
		this.promoDiscount = promoDiscount;
		this.creditApplied = creditApplied;
		this.shippingCharge = shippingCharge;
		this.promocode = promocode;
	}
	public PriceSummary() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "PriceSummary [oderTotal=" + orderTotal + ", orderPayable=" + orderPayable + ", platformDiscount="
				+ platformDiscount + ", promoDiscount=" + promoDiscount + ", creditApplied=" + creditApplied
				+ ", shippingCharge=" + shippingCharge + ", promocode=" + promocode + "]";
	}
	 

}
