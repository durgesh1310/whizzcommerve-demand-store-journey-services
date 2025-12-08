package com.ouat.orderService.exchange.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItemDTO {
	private Long orderId;
	private String sku;
	private Integer qty;
	private Double orderItemTotalAmount;
	private Double orderItemPayable;
	private Double orderItemPlatformOfferedDiscount;
	private Double orderItemCreditApplied;
	private Double orderItemPromoDiscount;
	private Double vendorPrice;
	private Double ouatPrice;
	private Integer orderStatusId;
	private Date orderCreatedDate;
	private String orderCreatedBy;
	private Date updatedAt;
	private String updatedBy;
	private Boolean isReturnable;
	private Boolean isExchangable;
	private Long parentOrderItemId;
	private Date estimatedShippedDate;
	private Date estimatedDeliveryDate;
	private Double orderItemShippingCharges;
	private Double ouatMargin;
	

}
