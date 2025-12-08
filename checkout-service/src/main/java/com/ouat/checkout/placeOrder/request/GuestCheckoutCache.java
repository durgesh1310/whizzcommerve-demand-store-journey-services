package com.ouat.checkout.placeOrder.request;

import java.util.List;

import com.ouat.checkout.cache.OrderedItemCache;
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
public class GuestCheckoutCache {
    private List<OrderedItemCache> orderSummary;
    private PricingSummaryDto pricingSummary;
    private GuestAddress address;
    private List<PaymentMode> paymentMode;
    private String promoCode;
    private String mobileNo;
}
