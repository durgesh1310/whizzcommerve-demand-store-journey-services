package com.ouat.checkout.dto;

import java.util.List;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.checkout.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@ToString
public class PromoDto {
    private Double total;
    private String promoCode;
    private List<SkuPromoDisountMapping> skus;
    private PaymentMode paymentMode;
    private String discountType;
    
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
	public static class SkuPromoDisountMapping {
        private String sku;
        private Double promoValue;
        private Boolean isPromoApplicable;
    }
}
