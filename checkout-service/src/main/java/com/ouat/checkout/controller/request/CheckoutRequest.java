package com.ouat.checkout.controller.request;

import java.util.List;

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
public class CheckoutRequest {
    private Integer addressId = null;
    private String promoCode = null;
    private List<String> appliedCredits = null;
    private String orderConfirmationEmail = null;
}
