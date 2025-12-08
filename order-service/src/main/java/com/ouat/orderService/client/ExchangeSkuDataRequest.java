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
public class ExchangeSkuDataRequest {
	
	private String sku;
	
	private Integer inventory;
	
	private String skuSize;
	
	private String infoMsg;

	@Override
	public String toString() {
		return "ExchangeSkuDataRequest [sku=" + sku + ", inventory=" + inventory + ", skuSize=" + skuSize + ", infoMsg="
				+ infoMsg + "]";
	}
	
	

}
