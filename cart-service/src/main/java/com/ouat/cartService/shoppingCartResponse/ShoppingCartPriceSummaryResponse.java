package com.ouat.cartService.shoppingCartResponse;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class  ShoppingCartPriceSummaryResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9098312175966304319L;
	private Double totalRegularPrice;
	private Double totalRetailPrice;
	private Double totalPlatformDiscount;
	private Double totalDiscountByApplyPromo;
	private Double shippingCharge;
	private Double orderTotal;
	private List<MessageDetail>message;
	
	
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
	public Double getTotalplatformDiscount() {
		return totalPlatformDiscount;
	}
	public void setTotalplatformDiscount(Double totalplatformDiscount) {
		this.totalPlatformDiscount = totalplatformDiscount;
	}
	public Double getTotalDiscountByApplyPromo() {
		return totalDiscountByApplyPromo;
	}
	public void setTotalDiscountByApplyPromo(Double totalDiscountByApplyPromo) {
		this.totalDiscountByApplyPromo = totalDiscountByApplyPromo;
	}
	public Double getShippingCharge() {
		return shippingCharge;
	}
	public void setShippingCharge(Double shippingCharge) {
		this.shippingCharge = shippingCharge;
	}
	public Double getOrderTotal() {
		return orderTotal;
	}
	public void setOrderTotal(Double orderTotal) {
		this.orderTotal = orderTotal;
	}
	public List<MessageDetail> getMessage() {
		return message;
	}
	public void setMessage(List<MessageDetail> message) {
		this.message = message;
	}
	
	
	
	public ShoppingCartPriceSummaryResponse(Double totalRegularPrice, Double totalRetailPrice,
			Double totalplatformDiscount, Double totalDiscountByApplyPromo, Double shippingCharge, Double orderTotal,
			List<MessageDetail> message) {
		super();
		this.totalRegularPrice = totalRegularPrice;
		this.totalRetailPrice = totalRetailPrice;
		this.totalPlatformDiscount = totalplatformDiscount;
		this.totalDiscountByApplyPromo = totalDiscountByApplyPromo;
		this.shippingCharge = shippingCharge;
		this.orderTotal = orderTotal;
		this.message = message;
	}
	public ShoppingCartPriceSummaryResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	 
}
