package com.ouat.suggestionandsearch.elasticsearch.documents;

import java.util.List;
import java.util.Map;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SkuDocument {
	@Field(name = "sku", type = FieldType.Text)
	private String sku;

	@Field(name = "attributes", type = FieldType.Object)
	private List<Map<String, AttributesDocument>> attributes;
	
	@JsonProperty("retail_price")
	@Field(name = "retail_price", type = FieldType.Double)
	private Double retailPrice;
	
	@JsonProperty("regular_price")
	@Field(name = "regular_price", type = FieldType.Double)
	private Double regularPrice;
	
	@JsonProperty("sale_price")
	@Field(name = "sale_price", type = FieldType.Double)
	private Double salePrice;
	
	@JsonProperty("vendor_price")
	@Field(name = "vendor_price", type = FieldType.Double)
	private Double vendorPrice;
	
	@JsonProperty("inventory_count")
	@Field(name = "inventory_count", type = FieldType.Long)
	private Long inventoryCount;
	
}
