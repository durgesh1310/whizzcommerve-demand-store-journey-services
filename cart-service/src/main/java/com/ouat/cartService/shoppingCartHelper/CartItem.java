package com.ouat.cartService.shoppingCartHelper;

import java.io.Serializable;
import java.util.Date;

public class CartItem implements Serializable {
     
	 /**
	 * 
	 */
	//private static final long serialVersionUID = 5490647108511544549L;
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private String sku;
	private Integer quantity; 
	private Double regularPrice;
	private Double retailPrice;
	private Date date;
	private Integer plpId;
 	 
    
     public Integer getPlpId() {
		return plpId;
	}
	public void setPlpId(Integer plpId) {
		this.plpId = plpId;
	}
	
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
     
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
 
 
	public Double getRegularPrice() {
		return regularPrice;
	}
	public void setRegularPrice(Double regularPrice) {
		this.regularPrice = regularPrice;
	}
	public Double getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public CartItem( String sku,  Integer quantity, Double retailPrice , Double regularPrice, Date date, Integer plpId) {
		super();
		 
		this.quantity = quantity;
		this.retailPrice=retailPrice;
		this.regularPrice = regularPrice;
		this.sku = sku;
		this.date = date;
		this.plpId = plpId;
	}

	public CartItem() {
		super();
	}
	@Override
	public String toString() {
		return "CartItem [sku=" + sku + ", quantity=" + quantity + ", regularPrice=" + regularPrice + ", retailPrice="
				+ retailPrice + ", date=" + date + ", plpId=" + plpId + "]";
	}
    
	
}
