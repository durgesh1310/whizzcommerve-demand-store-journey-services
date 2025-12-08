package com.ouat.orderService.exchange.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class PlaceExchangeOrderDto {
	
	private Long orderItemId;
	private String sku;
	private Date createdAt;
	private String createdBy;

}
