package com.ouat.orderService.client;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

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
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ExchangeOrderAlertRequest {
	
	private String customerName;
	private Long orderItemId;
	private String sku;
	private String mobile;
	private String placeOrderDateTime;
	private EmailSendRequest emailSendRequest;
	

}
