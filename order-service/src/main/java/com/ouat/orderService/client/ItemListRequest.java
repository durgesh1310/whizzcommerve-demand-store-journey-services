package com.ouat.orderService.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemListRequest {

	private Double price;

	@JsonProperty("item_name")
	private String itemName;

	private Integer quantity;
	private String sku;

	@JsonProperty("item_tax_percentage")
	private Integer itemTaxPercentage;

}