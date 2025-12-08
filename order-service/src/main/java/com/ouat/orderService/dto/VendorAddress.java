package com.ouat.orderService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendorAddress {
	private String addressline;
	private String city;
	private Integer pincode;
	private String state;
	private String country;
	private String addressType;
	

}
