package com.ouat.suggestionandsearch.elasticsearch.repositories;

import static com.ouat.suggestionandsearch.constants.DownStreamConstants.ELASTICSEARCH;
import static com.ouat.suggestionandsearch.constants.RequestType.GET_SUGGESTIONS;
import static com.ouat.suggestionandsearch.util.MetricsUtility.addDownStreamMetrics;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.lucene.util.BytesRef;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.ouat.suggestionandsearch.configuration.ElasticSearchConfiguration;

@Component
public class AutoSuggestionRepository {

	private static final Logger log = LoggerFactory.getLogger(AutoSuggestionRepository.class);

	private final Integer MAX_SUGGESTIONS = 10;

	private RestHighLevelClient client;

	@Value("${auto.suggestion.index.name:}")
	private String index;

	@Autowired
	private ElasticSearchConfiguration config;

	@PostConstruct
	public void init() {
		this.client = config.elasticsearchClient();
	}

	public SearchResponse findByText(QueryBuilder query, String trackingId) throws IOException {
		
		StopWatch timer = new StopWatch();
		timer.start();
		
		SearchRequest request = new SearchRequest();
		request.indices(index);
		SearchSourceBuilder esSearchSourceBuilder = createSearchSourceBuilder(query);
		request.source(esSearchSourceBuilder);

		BytesRef source = XContentHelper.toXContent(request.source(), XContentType.JSON, ToXContent.EMPTY_PARAMS, true)
				.toBytesRef();
		
		SearchResponse response = client.search(request, RequestOptions.DEFAULT);
		timer.stop();
		log.info("SearchQUERY {}, TotalHits = {}, RequestType = {}, TrackingId = {}", source.utf8ToString(),
				response.getHits().getTotalHits().value, GET_SUGGESTIONS, trackingId);
		addDownStreamMetrics(ELASTICSEARCH, timer.getTotalTimeSeconds(), GET_SUGGESTIONS, trackingId);
		return response;
	}

	private SearchSourceBuilder createSearchSourceBuilder(QueryBuilder query) {
		SearchSourceBuilder esSearchSourceBuilder = new SearchSourceBuilder();
		esSearchSourceBuilder.size(MAX_SUGGESTIONS);
		esSearchSourceBuilder.query(query);
		return esSearchSourceBuilder;
	}
}
