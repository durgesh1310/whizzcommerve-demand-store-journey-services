package com.ouat.suggestionandsearch.elasticsearch.documents;

import java.util.Date;

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
public class InventoryDocument {
	
	@JsonProperty("in_stock")
	@Field(name = "in_stock", type = FieldType.Boolean)
	private boolean inStock;

	@JsonProperty("age_of_inventory")
	@Field(name = "age_of_inventory", type = FieldType.Date)
	private Date ageOfInventory;
	
	@JsonProperty("inventory_message")
	@Field(name = "inventory_message", type = FieldType.Text)
	private String inventoryMessage;
}
