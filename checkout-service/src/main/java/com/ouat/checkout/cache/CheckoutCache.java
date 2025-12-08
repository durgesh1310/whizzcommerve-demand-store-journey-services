package com.ouat.checkout.cache;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.checkout.dto.AddressDto;
import com.ouat.checkout.dto.PricingSummaryDto;
import com.ouat.checkout.enums.PaymentMode;

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
public class CheckoutCache {
    private Integer addressId;
    private List<OrderedItemCache> orderSummary;
    private PricingSummaryDto pricingSummary;
    private String orderConfirmationEmail;
    private Map<String, Double> creditApplied;
    private String promoCode;
    private List<PaymentMode> paymentMode;
    private AddressDto address;
    
    
}
