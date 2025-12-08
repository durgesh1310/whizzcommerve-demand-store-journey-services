package com.ouat.orderService.exchange.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderDetailDto {
	
	private Long orderId;
	private Long customerId;
	private Integer orderStatusId;
	private Double totalAmount;
	private Double orderPayable;
	private Double platformOfferDiscount;
	private Double promoDiscount;
	private Double creditApplied;
	private Double shippingCharges;
	private String orderType;
	private String paymentMethod;
	private String promoCode;
	private String platform;
	private Long orderItemId;
	private String sku;
	private Integer qty;
	private Double orderItemTotalAmount;
	private Double orderItemPayable;
	private Double orderItemPlatformOfferedDiscount;
	private Double orderItemCreditApplied;
	private Double orderItemPromoDiscount;
	private Double vendorPrice;
	private Boolean isReturnable;
	private Boolean isExchangable;
	private Double orderItemShippingCharges;
	private Double ouatMargin;
	private String edd = null;
	private Integer addressId;
	


}
