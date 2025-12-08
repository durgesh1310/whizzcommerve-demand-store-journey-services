package com.ouat.notificationsender.repository;

import java.io.Serializable;

public class VendorDetailDto  implements Serializable{
 	/**
 	 * 
 	 */
 	private static final long serialVersionUID = 1L;
	private String vendorEmail;
	private String sku;
	private String vendorName;
	private String orderItemId;

 	public String getVendorEmail() {
 		return vendorEmail;
 	}

 	public void setVendorEmail(String vendorEmail) {
 		this.vendorEmail = vendorEmail;
 	}

 	public VendorDetailDto() {
 		super();
 	}

 	public String getSku() {
 		return sku;
 	}

 	public void setSku(String sku) {
 		this.sku = sku;
 	}



 	public String getVendorName() {
 		return vendorName;
 	}

 	public void setVendorName(String vendorName) {
 		this.vendorName = vendorName;
 	}
 	
 	

 	public String getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}

	public VendorDetailDto(String vendorEmail, String sku,   String vendorName, String orderItemId) {
 		super();
 		this.vendorEmail = vendorEmail;
 		this.sku = sku;
 		this.vendorName = vendorName;
 		this.orderItemId = orderItemId;
 	}






 }
