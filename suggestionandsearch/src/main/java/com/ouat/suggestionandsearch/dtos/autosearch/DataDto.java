package com.ouat.suggestionandsearch.dtos.autosearch;

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
@JsonIgnoreProperties({ "selected" })
public class DataDto {
	
	private String key;
	
	private Object value;
	
	@JsonProperty("is_selected")
	private boolean isSelected;
	
	@JsonProperty("other_info")
    private Object otherInfo;
}
