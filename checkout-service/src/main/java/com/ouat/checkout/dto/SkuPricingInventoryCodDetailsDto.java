package com.ouat.checkout.dto;

import java.util.Map;
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
public class SkuPricingInventoryCodDetailsDto {

    private Map<String, SkuPricingInventoryCodDetail> details;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class SkuPricingInventoryCodDetail {
        private Boolean isCodApplicable = null;
        private Integer inventory = 0;
        private Boolean isInventoryExpired = true;
        private Boolean isSkuActive = false;
        private String category = null;
        private String productType = null;
        private String subCategory = null;
        private Integer productId = null;
        private Double retailPrice = 0.0;
        private Double regularPrice = 0.0;
        private Double ouatMargin = 0.0;
        private Double vendorPrice = 0.0;
        private Boolean isReturnable = false;
        private Boolean isExchangeable = false;
    }
}
