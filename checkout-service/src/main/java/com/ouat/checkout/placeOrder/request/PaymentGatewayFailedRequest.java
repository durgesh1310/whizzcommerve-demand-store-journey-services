package com.ouat.checkout.placeOrder.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.ToString;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
@ToString
public class PaymentGatewayFailedRequest {
	
	private String paymentId;
	
	private String orderId;

}
