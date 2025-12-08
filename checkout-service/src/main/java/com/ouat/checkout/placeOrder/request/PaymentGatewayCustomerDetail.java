package com.ouat.checkout.placeOrder.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentGatewayCustomerDetail {
	
	private String name;
    private String email;
    private String contact = null;

}
