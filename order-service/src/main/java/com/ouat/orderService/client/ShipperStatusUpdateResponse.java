package com.ouat.orderService.client;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShipperStatusUpdateResponse {

	private String orderItemId;
	private String awb;
	private String courrierPartner;
	private String transporterCourier;
	private String routingCode;
	private Date createAt;
	private String createBy;

}