package com.ouat.suggestionandsearch.elasticsearch.documents;

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
public class SeoMetaDataDocument {
	
	@Field(name = "h1_tag", type = FieldType.Text, analyzer = "standard", searchAnalyzer = "synonym_analyzer", fielddata = true)
	@JsonProperty("h1_tag")
	private String h1Tag;

	@JsonProperty("no_follow")
	private Integer noFollow;

	@JsonProperty("no_index")
	private Integer noIndex;

	@Field(name = "canonical_url", type = FieldType.Text, analyzer = "standard", searchAnalyzer = "synonym_analyzer", fielddata = true)
	@JsonProperty("canonical_url")
	private String canonicalUrl;

	@Field(name = "meta_keywords", type = FieldType.Text, analyzer = "standard", searchAnalyzer = "synonym_analyzer", fielddata = true)
	@JsonProperty("meta_keywords")
	private String metaKeywords;
	
	@Field(name = "meta_description", type = FieldType.Text, analyzer = "standard", searchAnalyzer = "synonym_analyzer", fielddata = true)
	@JsonProperty("meta_description")
	private String metaDescription;
	
	@Field(name = "meta_title", type = FieldType.Text, analyzer = "standard", searchAnalyzer = "synonym_analyzer", fielddata = true)
	@JsonProperty("meta_title")
	private String metaTitle;
}
