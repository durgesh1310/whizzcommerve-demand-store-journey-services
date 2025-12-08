package com.ouat.checkout.controller.response;

import java.util.List;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.checkout.controller.request.GuestCheckoutRequest.GuestAddress;
import com.ouat.checkout.dto.PricingSummaryDto;
import com.ouat.checkout.dto.ShowShoppingCartItemDto;
import com.ouat.checkout.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class GuestCheckoutResponse {
    private List<ShowShoppingCartItemDto> orderSummary;
    private List<PaymentMode> paymentMethod;
    private PricingSummaryDto pricingSummary;
    private GuestAddress address;
    private String mobileNo;
}
