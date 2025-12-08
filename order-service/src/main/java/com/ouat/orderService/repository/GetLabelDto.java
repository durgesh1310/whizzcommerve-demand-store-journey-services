package com.ouat.orderService.repository;

import java.sql.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetLabelDto {
	private Long orderItemId;
	private String sku;
	private Integer qty;
	private String size;
	private Double sellingPrice;
	private Double regularPrice;
	private Double orderItemTotal;
	private Double orderItemPayable;
	private Double orderItemPlatformOfferedDiscount;
	private Double orderItemPromoDiscount;
	private Double orderItemShippingCharges;
	private Double orderItemCreditApplied;
	private String paymentMethod;
	private Date orderDate;
	private String productName;
	private String vendorSku;
	private String vendorName;
	private String vendorAddressLine1;
	private String vendorAddressLine2;
	private String vendorCity;
	private Integer vendorPincode;
	private String vendorState;
	private String vendorCoutry;
	private String vendorAddressType;
	private Integer vendorId;
	private String customerName;
	private String customerAddress;
	private Integer customerPincode;
	private String customerLandMark;
	private String customerCity;
	private String customerState;
	private String customerMobile;
    private String awb;
    private String courierPartner;
    private String routingCode;
    
    @Override
	public String toString() {
		String serialised = "";
		ObjectMapper object = new ObjectMapper();
		try {
			return object.writeValueAsString(this);
		}catch (JsonProcessingException e) {
			e.getStackTrace();
    }
		
		return serialised;
	}
    
    

}
