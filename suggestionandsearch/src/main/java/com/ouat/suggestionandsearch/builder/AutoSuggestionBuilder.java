package com.ouat.suggestionandsearch.builder;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Component;

import com.ouat.suggestionandsearch.elasticsearch.documents.AutoSuggestionDocument;
import static com.ouat.suggestionandsearch.util.Utility.convertJsonToObject;

@Component
public class AutoSuggestionBuilder {

	public QueryBuilder getAutoSuggestionQuery(String query) {
		BoolQueryBuilder builder = QueryBuilders.boolQuery();
		builder.must(QueryBuilders.termQuery("suggest", query));
		return builder;
	}

	public List<AutoSuggestionDocument> getAutoSuggestionsResponse(SearchResponse response) {
		List<AutoSuggestionDocument> suggestions = new ArrayList<AutoSuggestionDocument>();
		SearchHit[] hits = response.getHits().getHits();
		for(SearchHit hit : hits) {
			suggestions.add(convertJsonToObject(hit.getSourceAsString(), AutoSuggestionDocument.class));
		}
		return suggestions;
	}

}
