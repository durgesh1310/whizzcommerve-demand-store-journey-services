package com.ouat.suggestionandsearch.dtos.autosearch;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ouat.suggestionandsearch.enums.autosearch.PLPTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"wishlisted", "onSale"})
public class ProductDto {

    @JsonProperty("product_id")
    private Integer productId;

    @JsonProperty("name")
    private String productName;

    private String price;
    
    @JsonProperty("max_retail_price")
    private Long maxRetailPrice;

    @JsonProperty("max_regular_price")
    private Long maxRegularPrice;

    @JsonProperty("currency_code")
    private String currencyCode;

    @JsonProperty("discount")
    private String discountPercentage;

    @JsonProperty("image_urls")
    private List<String> imageUrls;

    @JsonProperty("size_chart_image_urls")
    private List<String> sizeChartImageUrls;

    @JsonProperty("action")
    private String redirectTo;

    @JsonProperty("type")
    private Enum<PLPTypeEnum> type;

    @JsonProperty("delivery_message")
    private String deliveryMessage;

    @JsonProperty("vendor_name")
    private String vendorName;

    @JsonProperty("sku_info")
    private List<SkuDto> skuInfo;

    @JsonProperty("is_wishlisted")
    private Boolean isWishlisted;

    @JsonProperty("can_wishlist")
    private Boolean canWishlist;

    @JsonProperty("is_exclusive")
    private Boolean isExclusive;

    @JsonProperty("message_on_image")
    private List<String> msgOnImage;

    @JsonProperty("stock_status")
    private Boolean inStock;

    @JsonProperty("stock_msg")
    private String stockMessage;

    @JsonProperty("is_on_sale")
    private Boolean isOnSale;

    @JsonProperty("brand_name")
    private String brandName;
    
    @JsonProperty("url_key")
    private String urlKey;
}
