package com.ouat.suggestionandsearch.controller.request;

import static com.ouat.suggestionandsearch.util.AutoSearchUtility.sortOptionsMappings;

import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SortRequest {
	
	private static final Logger log = LoggerFactory.getLogger(SortRequest.class);
	
	@JsonProperty("sort_by")
	private String sortBy;

	public FieldSortBuilder toQuery() {
		String checkSort = (String) sortOptionsMappings.get(sortBy);
		if (checkSort!=null) {
			return SortBuilders.fieldSort(checkSort).order((SortOrder) sortOptionsMappings.get("order_" + sortBy));
		} else {
			log.warn("Sorting option doesn't exists Option = {}", sortBy);
			return SortBuilders.fieldSort("ouat_margin").order(SortOrder.DESC);
		}
	}
}
