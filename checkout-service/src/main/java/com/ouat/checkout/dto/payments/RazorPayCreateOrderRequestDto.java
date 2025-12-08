package com.ouat.checkout.dto.payments;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RazorPayCreateOrderRequestDto {
    private Integer amount;
    private String currency;
    private String receipt;
}
