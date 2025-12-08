package com.ouat.suggestionandsearch.elasticsearch.documents;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({ "filterApplicable", "multi" })
public class AttributesDocument {

	@JsonProperty("attribute_value")
	@Field(name = "attribute_value", type = FieldType.Object)
	Object attributeValue;

	@JsonProperty("is_filter_applicable")
	@Field(name = "is_filter_applicable", type = FieldType.Boolean)
	boolean isFilterApplicable;

	@JsonProperty("is_multi")
	@Field(name = "is_multi", type = FieldType.Boolean)
	boolean isMulti;
	
	@JsonProperty("is_visible")
    @Field(name = "is_visible", type = FieldType.Boolean)
    Boolean isVisible;
}
