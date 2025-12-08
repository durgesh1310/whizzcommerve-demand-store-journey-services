package com.ouat.checkout.cache;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderedItemCache {
	private String productName;
    private String defaultImageUrl;
    private String sku;
    private String size;
    private int quantity;
    private Double retailPrice;
    private Double regularPrice;
    private Double orderItemPlatFormOfferedDiscount = 0.0;
    private Double orderItemPromoDiscount = 0.0;
    private Boolean isPromoApplicable;
    private String edd = null;
    private Double ouatMargin;
    private Double vendorPrice;
    private Boolean isReturnable;
    private Boolean isExchangeable;
}
