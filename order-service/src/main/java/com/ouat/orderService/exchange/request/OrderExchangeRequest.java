package com.ouat.orderService.exchange.request;

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
public class OrderExchangeRequest {

	private Long orderItemId;

	private String sku;
	
	private Integer reasonId;

	@Override
	public String toString() {
		return "OrderExchangeRequest [orderItemId=" + orderItemId + ", sku=" + sku + ", reasonId=" + reasonId + "]";
	}
	
	
	

}
