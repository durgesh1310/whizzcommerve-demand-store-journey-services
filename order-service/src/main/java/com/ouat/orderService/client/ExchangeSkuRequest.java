package com.ouat.orderService.client;

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
public class ExchangeSkuRequest {
	
	private String sku;

	@Override
	public String toString() {
		return "SkuAvailableRequest [sku=" + sku + "]";
	}
	
	
	
	

	

}
