package com.ouat.orderService.exchange.request;


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
public class SkuAndQtyRequest {
	
	private String sku;
	private Integer qty;

}
