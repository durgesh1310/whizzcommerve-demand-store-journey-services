package com.ouat.suggestionandsearch.elasticsearch.documents;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageUrlDocument {
	@Field(name = "url", type = FieldType.Text)
	private String url;
}
