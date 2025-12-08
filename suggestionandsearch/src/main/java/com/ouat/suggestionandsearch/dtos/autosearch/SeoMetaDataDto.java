package com.ouat.suggestionandsearch.dtos.autosearch;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeoMetaDataDto {
	
	@JsonProperty("h1")
	private String h1Tag;
	
	@JsonProperty("no_follow")
	private String noFollow;
	
	@JsonProperty("no_index")
	private String noIndex;
	
	@JsonProperty("canonical_url")
	private String canonicalUrl;
	
	@JsonProperty("keyword")
	private String keyword;
	
}
