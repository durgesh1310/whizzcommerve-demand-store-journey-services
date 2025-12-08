package com.ouat.orderService.client;

import java.util.List;

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
public class PlpPdpServiceClientRequest {
	
	private Integer productId;
	
	private String name;
	
	private String imageUrl;
	
	private List<ExchangeSkuDataRequest> skuResponse;

	@Override
	public String toString() {
		return "PlpPdpServiceClientRequest [productId=" + productId + ", name=" + name + ", imageUrl=" + imageUrl
				+ ", skuResponse=" + skuResponse + "]";
	}
	
	

}
