package com.ouat.checkout.cache;

import java.util.List;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.checkout.controller.request.GuestCheckoutRequest.GuestAddress;
import com.ouat.checkout.dto.PricingSummaryDto;
import com.ouat.checkout.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GuestCheckoutCache {
    private List<OrderedItemCache> orderSummary;
    private PricingSummaryDto pricingSummary;
    private GuestAddress address;
    private List<PaymentMode> paymentMode;
    private String mobileNo;
    private String promoCode;
}
