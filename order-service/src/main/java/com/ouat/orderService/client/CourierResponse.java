package com.ouat.orderService.client;

import com.ouat.orderService.constants.CourierDetailOrderStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CourierResponse {
	private String awbNo;
	private CourierDetailOrderStatus status;
	private String courierCode;
	private String courierName;
	private String additionalInfo;

}

