package com.ouat.suggestionandsearch.controller.response;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ouat.suggestionandsearch.dtos.autosearch.FilterDto;
import com.ouat.suggestionandsearch.dtos.autosearch.ProductDto;
import com.ouat.suggestionandsearch.dtos.autosearch.SeoMetaDataDto;
import com.ouat.suggestionandsearch.dtos.autosearch.SortOption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AutoSearchResponse {
	
	@JsonProperty("total_hits")
	private long totalHits;
	
	@JsonProperty("query")
	private String searchQuery;
	
	@JsonProperty("seo_meta_data")
	private SeoMetaDataDto plpSeo;
	
	@JsonProperty("plp_timer")
	private BigInteger timer;
	
	@JsonProperty("plp_banner")
	private String banner;
	
	@JsonProperty("plp_card")
	List<ProductDto>products;
	
	@JsonProperty("filters")
	List<FilterDto>filters;
	
	@JsonProperty("sort_by")
	SortOption sortOptions;
	
}
