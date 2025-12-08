package com.ouat.suggestionandsearch.controller.request;

import static com.ouat.suggestionandsearch.util.AutoSearchUtility.rangeQueryAttributes;
import static com.ouat.suggestionandsearch.util.AutoSearchUtility.termQueryProductAttributes;
import static com.ouat.suggestionandsearch.util.AutoSearchUtility.termQueryProductHirerarcy;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest {
   
    private static final Logger log = LoggerFactory.getLogger(FilterRequest.class);
    
	private String key;
	private String value;
	private String from;
	private String to;
	private String type;

	public QueryBuilder toQuery() {
		if ("term".equals(type)) {
			return termQuery();
		} else if ("range".equals(type)) {
			return rangeQuery();
		} else {
			throw new RuntimeException("Unknown type: " + type);
		}
	}

	private QueryBuilder rangeQuery() {
		if (rangeQueryAttributes.contains(key.toLowerCase())) {
			return createRangeQueryBuilder(key, from, to);
		}
		return null;
	}

	private QueryBuilder termQuery() {
        if (termQueryProductHirerarcy
                .contains(this.key.toLowerCase().equalsIgnoreCase("category") ? "product_type"
                        : key.toLowerCase())) {
            log.debug("category is changed to product_type");
            return termQueryProductHirerarcy(
                    this.key.toLowerCase().equalsIgnoreCase("category") ? "product_type"
                            : key.toLowerCase(),
                    value);
        } else if (termQueryProductAttributes.contains(key.toLowerCase())) {
            return termQueryAttributeLevel(key, value);
        }
        return null;
    }

	private QueryBuilder termQueryProductHirerarcy(String key, String value) {
		return QueryBuilders.termQuery(key + ".keyword", value);
	}

	private QueryBuilder termQueryAttributeLevel(String key, String value) {
		return QueryBuilders.nestedQuery("attributes", QueryBuilders.termQuery("attributes." + key + ".keyword", value), ScoreMode.None);
	}

	private QueryBuilder createRangeQueryBuilder(String name, String from, String to) {
		if (StringUtils.hasText(from) && StringUtils.hasText(to)) {
			return QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("from_" + name).gte(from))
					.must(QueryBuilders.rangeQuery("to_" + name).lte(to));
		}
		return QueryBuilders.rangeQuery(name);
	}
}
