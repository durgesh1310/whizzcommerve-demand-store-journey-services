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
public class AddressDetailDto {
    private String fullName;

    private Integer pincode;

    private String address;

    private String landmark;

    private String city;

    private String state;

    private String mobile;

    private Integer addressId;
    
    private Boolean isSelected = Boolean.FALSE;
    
}
