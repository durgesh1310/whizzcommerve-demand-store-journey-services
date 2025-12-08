package com.ouat.suggestionandsearch.controller.request;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
	private String query;
	private Integer from = 0;
	private List<FilterRequest> filters = new ArrayList<>();
	@JsonProperty("sort_by")
	private SortRequest sortBy;
}
