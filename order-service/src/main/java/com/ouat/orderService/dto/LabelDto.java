package com.ouat.orderService.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LabelDto {
	private CustomerAddress customerAddress;
	private VendorAddress vendorAddress;
	private Integer orderId;
	private Date orderDate;
 	private String size;
	private String qty;
	private Double orderItemPayable;
	private Double orderItemTotal;
	private Double discount;
	private Double shippingCharge;
	private Double storeCredit;
	private String sku;
	private String vendorSku;
	private String itemName;
	  
}
