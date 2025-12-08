package com.ouat.checkout.dto;

import java.util.List;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.checkout.enums.PaymentMode;
import com.ouat.checkout.response.Platform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ApplyPromoRequestDto {

    private String promocode;
    private PaymentMode paymentMode;
    private Platform platform;
    private Integer customerId;
    private Double cartValue;
    private String uuId;
    private List<CartItem> cartItemList;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class CartItem {
        private String sku;
        private Integer quantity;
        private Double retailPrice;
        private String category;
        private String subCategory;
        private String productType;
        private Integer productId;
        private long plpId;
    }

}
