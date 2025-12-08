package com.ouat.checkout.placeOrder.request;

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
public class GuestAddress{
    private String fullName;
    private Integer pincode;
    private String address;
    private String landmark;
    private String city;
    private String state;
    private String email;
    private String mobile = null;    
}
