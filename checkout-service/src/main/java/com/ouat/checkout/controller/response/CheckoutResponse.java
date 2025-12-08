package com.ouat.checkout.controller.response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.checkout.dto.AddressDto;
import com.ouat.checkout.dto.CustomerCreditDto;
import com.ouat.checkout.dto.PricingSummaryDto;
import com.ouat.checkout.dto.ShowShoppingCartItemDto;
import com.ouat.checkout.enums.PaymentMode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutResponse {
    private AddressDto address;
    private List<CustomerCreditDto> credits;
    private List<ShowShoppingCartItemDto> orderSummary;
    private List<PaymentMode> paymentMethod;
    private PricingSummaryDto pricingSummary;
    private String orderConfirmationEmail = null;
    private Map<String, Object> pgCustomerData = null;

	public CheckoutResponse(AddressDto address, List<CustomerCreditDto> credits,
			List<ShowShoppingCartItemDto> orderSummary, List<PaymentMode> paymentMethod,
			PricingSummaryDto pricingSummary, String orderConfirmationEmail) {
		super();
		this.address = address;
		this.credits = credits;
		this.orderSummary = orderSummary;
		this.paymentMethod = paymentMethod;
		this.pricingSummary = pricingSummary;
		this.orderConfirmationEmail = orderConfirmationEmail;
	}
}
