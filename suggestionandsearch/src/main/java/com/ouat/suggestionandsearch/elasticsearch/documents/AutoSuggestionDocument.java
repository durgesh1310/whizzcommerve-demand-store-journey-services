package com.ouat.suggestionandsearch.elasticsearch.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ouat.suggestionandsearch.constants.elasticsearch.ElasticSearchConstants;


@Document(indexName = ElasticSearchConstants.SUGGESTION_INDEX, createIndex = false)
@Setting(settingPath = ElasticSearchConstants.SUGGESTION_SETTING_PATH)
public class AutoSuggestionDocument {
	@Id
	@Field(type = FieldType.Keyword, name = "id")
	private String id;

	@Field(type = FieldType.Text, analyzer = ElasticSearchConstants.SUGGESTION_ANALYZER, searchAnalyzer = ElasticSearchConstants.SUGGESTION_ANALYZER)
	private String suggest;
	
	@JsonProperty("thumb_nail")
	@Field(type = FieldType.Keyword, name = "thumb_nail")
	private String thumbNail;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	public String getThumbNail() {
		return thumbNail;
	}

	public void setThumbNail(String thumbNail) {
		this.thumbNail = thumbNail;
	}

}
