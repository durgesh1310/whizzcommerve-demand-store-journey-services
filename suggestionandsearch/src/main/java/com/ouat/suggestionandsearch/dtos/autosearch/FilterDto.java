package com.ouat.suggestionandsearch.dtos.autosearch;

import java.util.List;

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
@JsonIgnoreProperties({"multi"})
public class FilterDto {
	
	@JsonProperty("filter_name")
	private String filterName;
	
	@JsonProperty("display_name")
	private String displayName;
	
	private String type;
	
	@JsonProperty("data")
	private List<DataDto>data;
	
	@JsonProperty("is_multi")
	private boolean isMulti;
}
