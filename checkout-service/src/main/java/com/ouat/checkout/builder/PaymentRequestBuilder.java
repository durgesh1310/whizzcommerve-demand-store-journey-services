package com.ouat.checkout.builder;

import org.springframework.stereotype.Component;
import com.ouat.checkout.dto.payments.RazorPayCreateOrderRequestDto;

@Component
public class PaymentRequestBuilder {

    private static final String CURRENCY = "INR";

    public RazorPayCreateOrderRequestDto getRazorPayCreateOrderRequest(Long amount,
            Integer orderId) {
        RazorPayCreateOrderRequestDto request = new RazorPayCreateOrderRequestDto();
        request.setAmount(amount.intValue());
        request.setCurrency(CURRENCY);
        request.setReceipt(orderId.toString());
        return request;
    }

}
