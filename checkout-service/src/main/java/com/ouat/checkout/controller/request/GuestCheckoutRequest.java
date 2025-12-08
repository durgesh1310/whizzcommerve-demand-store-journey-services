package com.ouat.checkout.controller.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class GuestCheckoutRequest {
    private GuestAddress address;
    private String mobileNo;
    private String promoCode;
    
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(SnakeCaseStrategy.class)
    public static class GuestAddress{
        private String fullName;
        private Integer pincode;
        private String address;
        private String landmark;
        private String city;
        private String state;
        private String email;
    }
}
