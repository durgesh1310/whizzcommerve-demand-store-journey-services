package com.ouat.myOrders.DTO;

import java.sql.Date;

import com.ouat.myOrders.response.OrderStatus;
import com.ouat.myOrders.response.RecordType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OrderDetailDTO {
	//order detail item table
	 private Integer isReturnable;
	 private Integer isExchangable;
	 private Date orderItemCreatedDate;
	 private Integer orderItemCreditApplied;
	 private Integer orderItempromocodeDiscount;
	 private Integer platformDiscount;
	 private Integer orderItemPayable;
	 private Integer orderItemTotalAmount;
	 private Integer quantity;
	 private String sku;
	 private Long orderItemId;
	 //cancel exchange record table
	 private Date cancelExchangeRequestedDate;
	 private Integer isRefundRequired;
	 private Integer isRefunded;
	 private RecordType recordType;
	 private Date refundDate;
	 private String refundType;
	 private Integer refundAmount;
	 private Integer orderId;
	 private String orderNumber;

	private Integer customerId;
	 private Date orderDate;
	 private String promoCodeApplied;
	 private String paymentMethod;
	 //order price summary
	 private Integer orderTotalAmount;
	 private Integer orderPayable;
	 private Integer orderPlatformDiscount;
	 private Integer orderPromoDiscount;
	 private Integer orderCreditApplied;
	 private Integer shippingCharged;
	 private OrderStatus orderItemStatus;
	 private String trackingUrl;
	 //antthor routiene
	 private Integer shippingAddressId;
	 private String orderType;
	 private String awb;
	 private String courrierPartner;
	 private Date shippedDate;
	 private Date deliveredDate;
	private Date rtoDate;
	private Date rtoDeliverDate;
	private String rtoTrackingNumber;
	 
	 
			public OrderDetailDTO() {
				super();
				// TODO Auto-generated constructor stub
			}
			 
 
	 
}
