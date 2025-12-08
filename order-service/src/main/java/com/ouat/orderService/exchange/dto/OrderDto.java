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
public class OrderDto {
	
	private Long customerId;
	private Integer orderStatusId;
	private Double totalAmount;
	private Double orderPayable;
	private Double platformOfferedDiscount;
	private Double promoDiscount;
	private Double creditApplied;
	private Double shippingCharges;
	private Date orderDate;
	private String orderType;
	private Date cartCreatedDate;
	private String paymentMethod;
	private String promocode;
	private String platform ;
	}
