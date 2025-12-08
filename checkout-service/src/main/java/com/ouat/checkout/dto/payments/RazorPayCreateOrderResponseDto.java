package com.ouat.checkout.dto.payments;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class RazorPayCreateOrderResponseDto {
    private String id;
    private String entity;
    private Integer amount;
    private Integer amountPaid;
    private Integer amountDue;
    private String currency;
    private String receipt;
    private String offerId;
    private String status;
    private Integer attempts;
    private List<Object>notes;
    private Long createdAt;
}
