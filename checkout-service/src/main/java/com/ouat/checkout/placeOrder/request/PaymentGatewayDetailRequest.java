package com.ouat.checkout.placeOrder.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.ToString;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
@ToString
public class PaymentGatewayDetailRequest {
	
	private String razorpayPaymentId;
	
	private String razorpayOrderId;
	
	private String razorpaySignature;
	
	
	

}
