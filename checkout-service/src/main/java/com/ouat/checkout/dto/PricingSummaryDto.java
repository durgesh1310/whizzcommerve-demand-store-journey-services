package com.ouat.checkout.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PricingSummaryDto {
    private Double totalPrice = 0.0;// total retail price
    private Double totalPlatformDiscount = 0.0; // regular - retail_price
    private Double totalPromoDiscount = 0.0;
    private String promoCode;
    private Double totalCreditValue = 0.0;
    private Double totalDeliveryCharges = 0.0;
    private Double totalOrderPayable = (totalPrice + totalDeliveryCharges) - (totalPromoDiscount + totalCreditValue);
}
