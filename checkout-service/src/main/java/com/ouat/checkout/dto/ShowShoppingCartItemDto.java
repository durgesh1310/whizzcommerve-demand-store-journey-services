package com.ouat.checkout.dto;

import java.util.List;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.checkout.response.MessageDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShowShoppingCartItemDto {
    private String defaultImageUrl;
    private String productName;
    private String sku;
    private String size;
    private int quantity;
    private Double retailPrice;
    private Double regularPrice;
    private Double priceChange;
    private Double itemDiscount;
    private Integer productId;
    private String edd = null;
    private List<MessageDetail> cartItemMessage = null;
}
