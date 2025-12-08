package com.ouat.suggestionandsearch.builder;

import static com.ouat.suggestionandsearch.constants.RequestType.GET_SEARCH_RESPONSE;
import static com.ouat.suggestionandsearch.enums.autosearch.PLPTypeEnum.PRODUCT;
import static com.ouat.suggestionandsearch.util.AutoSearchUtility.rangeQueryAttributes;
import static com.ouat.suggestionandsearch.util.AutoSearchUtility.searchQueryTermsProductHirerarcy;
import static com.ouat.suggestionandsearch.util.AutoSearchUtility.searchQueryTermsProductLevelAttributes;
import static com.ouat.suggestionandsearch.util.AutoSearchUtility.searchQueryTermsSkuLevelAttributes;
import static com.ouat.suggestionandsearch.util.AutoSearchUtility.sortOptionsMappings;
import static com.ouat.suggestionandsearch.util.AutoSearchUtility.termQueryProductAttributes;
import static com.ouat.suggestionandsearch.util.AutoSearchUtility.termQueryProductHirerarcy;
import static com.ouat.suggestionandsearch.util.AutoSearchUtility.termQuerySkuAttributes;
import static com.ouat.suggestionandsearch.util.AutoSearchUtility.hexCodeMappings;
import static com.ouat.suggestionandsearch.util.Utility.convertArrayIntoSeprateStrings;
import static com.ouat.suggestionandsearch.util.Utility.convertJsonToObject;
import static com.ouat.suggestionandsearch.util.Utility.toCamelCase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.aggregations.metrics.Min;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ouat.suggestionandsearch.constants.RequestType;
import com.ouat.suggestionandsearch.controller.request.FilterRequest;
import com.ouat.suggestionandsearch.controller.request.SearchRequest;
import com.ouat.suggestionandsearch.controller.request.SortRequest;
import com.ouat.suggestionandsearch.controller.response.AutoSearchResponse;
import com.ouat.suggestionandsearch.dtos.autosearch.DataDto;
import com.ouat.suggestionandsearch.dtos.autosearch.FilterDto;
import com.ouat.suggestionandsearch.dtos.autosearch.ProductDto;
import com.ouat.suggestionandsearch.dtos.autosearch.SkuDto;
import com.ouat.suggestionandsearch.dtos.autosearch.SortOption;
import com.ouat.suggestionandsearch.elasticsearch.documents.ImageUrlDocument;
import com.ouat.suggestionandsearch.elasticsearch.documents.InventoryDocument;
import com.ouat.suggestionandsearch.elasticsearch.documents.SearchDocument;
import com.ouat.suggestionandsearch.elasticsearch.documents.SkuDocument;
import com.ouat.suggestionandsearch.enums.autosearch.FilterTypeEnum;
import com.ouat.suggestionandsearch.util.AutoSearchUtility;
import com.ouat.suggestionandsearch.util.Utility;

@Component
public class AutoSearchBuilder {

    private static final Logger log = LoggerFactory.getLogger(AutoSearchResponse.class);

    private static final Integer PAGE_SIZE = 24;
    private static final String PDP_URL = "/ouat/pdp/";
    private static final String currencyType = "RS";

    public AutoSearchResponse plpResponseBuilder(SearchRequest query, SearchResponse response,
            String trackingId, Set<Long> customerWishListProductIds) {
        AutoSearchResponse autoSearchResponse = new AutoSearchResponse();
        autoSearchResponse.setSearchQuery(toCamelCase(query.getQuery()));
        autoSearchResponse.setTotalHits(response.getHits().getTotalHits().value);
        autoSearchResponse
                .setProducts(productsResponseBuilder(response.getHits().getHits(), trackingId, customerWishListProductIds));
        setFilters(autoSearchResponse, response, query, trackingId);
        autoSearchResponse
                .setSortOptions(sortOptionsBuilder(query.getSortBy(), sortOptionsMappings));
        return autoSearchResponse;
    }

    private List<ProductDto> productsResponseBuilder(SearchHit[] hits, String trackingId, Set<Long> customerWishListProductIds) {
        List<ProductDto> products = new ArrayList<ProductDto>();
        for (SearchHit hit : hits) {
            ProductDto product = new ProductDto();
            SearchDocument document =
                    convertJsonToObject(hit.getSourceAsString(), SearchDocument.class);
            product.setProductId(document.getProductId());
            product.setProductName(document.getName());
            product.setCanWishlist(document.isCanWishList());
            product.setCurrencyCode(currencyType);
            product.setVendorName(document.getVendorName());
            product.setMaxRegularPrice(evaluateMaxRegularPrice(document.getSkus()));
            product.setMaxRetailPrice(Math.round(document.getToPrice()));
            product.setDiscountPercentage(discountBuilder(document.getToDiscount()));
            product.setIsOnSale(document.isOnSale());
            product.setImageUrls(imageUrlsBuilder(document.getImageUrls()));
            product.setSizeChartImageUrls(imageUrlsBuilder(document.getSizeChartUrls()));
            product.setMsgOnImage(buildMsgOnImg(document.isBestSeller(), document.isNewArrival(), document.isTrending(), document.isExpressShipping(), document.isExclusive(),document.isLuxe()));
            product.setType(PRODUCT);
            product.setRedirectTo(PDP_URL + document.getProductId());
            product.setPrice(priceBuilder(document.getFromPrice(), document.getToPrice()));            
            setInventoryDetails(product, document.getInventory());
            product.setSkuInfo(skusInfoBuilder(document.getSkus(), trackingId));
            product.setDeliveryMessage(document.getEdd());
            product.setBrandName(document.getBrand());
            product.setIsExclusive(document.isExclusive());
            if (customerWishListProductIds.contains(Long.valueOf(product.getProductId()))) {
                product.setIsWishlisted(true);
            }
            product.setUrlKey(document.getUrlKey());
            products.add(product);
        }
        return products;
    }

    private Long evaluateMaxRegularPrice(List<SkuDocument> skus) {
        if(CollectionUtils.isEmpty(skus))
            return null;
        return Math.round(skus.stream().max(Comparator.comparingDouble(SkuDocument::getRegularPrice)).get().getRegularPrice());
    }

    private List<String> buildMsgOnImg(boolean bestSeller, boolean newArrival, boolean trending,
            boolean expressShipping, boolean isExclusive, boolean luxe) {
        List<String> msgOnImg = new ArrayList<String>();
        if (bestSeller) {
        	msgOnImg = new ArrayList<String>();
            msgOnImg.add("BEST SELLER");
        } else if (newArrival) {
        	msgOnImg = new ArrayList<String>();
            msgOnImg.add("NEW ARRIVAL");
        } else if (trending) {
        	msgOnImg = new ArrayList<String>();
            msgOnImg.add("TRENDING");
        } else if (expressShipping) {
        	msgOnImg = new ArrayList<String>();
            msgOnImg.add("EXPRESS SHIPPING");
        } else if (isExclusive) {
        	msgOnImg = new ArrayList<String>();
            msgOnImg.add("EXCLUSIVE");
        } else if (luxe) {
        	msgOnImg = new ArrayList<String>();
            msgOnImg.add("LUXE");
        } 
        return msgOnImg;
    }

    /***
     * @implNote as per the new requiremnet in response price range is not required
     * but instead of that max_retail_price, max_regular_price concept has been introduced
     * */
    @Deprecated
    private String priceBuilder(Double fromPrice, Double toPrice) {
        if (Double.compare(fromPrice, toPrice) == 0)
            return String.format("%1$,.2f", fromPrice);
        return String.format("%1$,.2f - %2$,.2f", fromPrice, toPrice);
    }

    private String discountBuilder(Double toDiscount) {
        if (toDiscount == null || Math.round(toDiscount) == 0)
            return null;
        return Math.round(toDiscount) + "% OFF";
    }

    private List<String> imageUrlsBuilder(List<ImageUrlDocument> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty())
            return null;
        return imageUrls.stream().map(url -> url.getUrl()).collect(Collectors.toList());
    }

    private void setInventoryDetails(ProductDto product, InventoryDocument inventory) {
        if (inventory == null || !StringUtils.hasText(inventory.getInventoryMessage()))
            return;
        product.setStockMessage(inventory.getInventoryMessage());
        product.setInStock(inventory.isInStock());
    }

    private List<SkuDto> skusInfoBuilder(List<SkuDocument> skus, String trackingId) {
        List<SkuDto> skuDtoList = new ArrayList<SkuDto>();
        for (SkuDocument sku : skus) {
            SkuDto skuDto = new SkuDto();
            skuDto.setSku(sku.getSku());
            skuDto.setSize(fetchSkuSize(sku, trackingId));
            skuDtoList.add(skuDto);
        }
        return skuDtoList;
    }

    private String fetchSkuSize(SkuDocument sku, String trackingId) {
        if (sku.getAttributes() == null || sku.getAttributes().isEmpty())
            return null;
        List<Object> sizes = sku.getAttributes().stream()
                .filter(attribute -> attribute.get("size") != null)
                .map(size -> size.get("size").getAttributeValue()).collect(Collectors.toList());
        try {
            return sizes.get(0).toString();
        } catch (Exception ex) {
            log.error(
                    "some error occurred while fetching sizes sku = {}, RequestType = {}, TrackingId = {}",
                    sku.getSku(), RequestType.GET_SEARCH_RESPONSE, trackingId);
            return null;
        }
    }

    private void setFilters(AutoSearchResponse autoSearchResponse, SearchResponse response,
            SearchRequest query, String trackingId) {
        List<FilterDto> filters = new ArrayList<FilterDto>();
        Set<String> requestedFilters = getSetOfRequestedFilters(query.getFilters());
        populateRangeFilters(response, filters, trackingId);
        populateTermFilters(response, filters, requestedFilters);
        autoSearchResponse.setFilters(filters);
    }

    private Set<String> getSetOfRequestedFilters(List<FilterRequest> filters) {
        if (filters != null && !filters.isEmpty()) {
            return filters.stream().map(filter -> filter.getValue()).collect(Collectors.toSet());
        }
        return new HashSet<String>();
    }

    private void populateRangeFilters(SearchResponse response, List<FilterDto> filters,
            String trackingId) {
        for (String rangeFilter : rangeQueryAttributes) {
            Filter minFilterAggregation = null;
            Min min = null;
            try {
                minFilterAggregation = response.getAggregations().get("min_" + rangeFilter);
                min = minFilterAggregation.getAggregations().get("min_" + rangeFilter);
            } catch (Exception ex) {
                try {
                    min = response.getAggregations().get("min_" + rangeFilter);
                } catch (Exception e) {
                    throw e;
                }
            }
            Filter maxFilterAggregation = null;
            Max max = null;
            try {
                maxFilterAggregation = response.getAggregations().get("max_" + rangeFilter);
                max = maxFilterAggregation.getAggregations().get("max_" + rangeFilter);
            } catch (Exception ex) {
                try {
                    max = response.getAggregations().get("max_" + rangeFilter);
                } catch (Exception e) {
                    throw e;
                }
            }
            FilterDto filter = new FilterDto();
            setFilterData(rangeFilter, filter, min, max, filters, trackingId);
        }
    }

    private void setFilterData(String rangeFilter, FilterDto filter, Min min, Max max,
            List<FilterDto> filters, String trackingId) {
        if (isSatisfyingSetCriteria(rangeFilter, min.getValue(), max.getValue(), trackingId)) {
            filter.setFilterName(rangeFilter);
            filter.setDisplayName(toCamelCase(rangeFilter));
            filter.setType(FilterTypeEnum.RANGE.toString());
            List<DataDto> data = new ArrayList<DataDto>();
            data.add(dataDtoBuilder("min_" + rangeFilter, Math.round(min.getValue()), null));
            data.add(dataDtoBuilder("max_" + rangeFilter, Math.round(max.getValue()), null));
            filter.setData(data);
            filters.add(filter);
        }
    }

    private boolean isSatisfyingSetCriteria(String rangeFilter, double minValue, double maxValue,
            String trackingId) {
        if (maxValue >= Double.MAX_VALUE
                || minValue == Double.POSITIVE_INFINITY || maxValue == Double.NEGATIVE_INFINITY) {
            log.error(
                    "min max filter value is out of range filterName = {}, RequestType = {}, TrackingId = {}",
                    rangeFilter, RequestType.GET_SEARCH_RESPONSE, trackingId);
            return false;
        } else if (Double.compare(minValue, maxValue)==0) {
            return false;
        }
        return true;
    }

    private DataDto dataDtoBuilder(String key, Object value, Set<String> requestedFilters) {
        DataDto dto = new DataDto();
        dto.setKey(key);
        dto.setValue(value);
        if (requestedFilters != null && requestedFilters.contains(key))
            dto.setSelected(true);
        dto.setOtherInfo(hexCodeMappings.get(key.toLowerCase()));
        return dto;
    }

    private void populateTermFilters(SearchResponse response, List<FilterDto> filters,
            Set<String> requestedFilters) {
        if (response != null && response.getAggregations() != null) {
            for (String attribute : termQueryProductHirerarcy) {
                try {
                    Filter filterAggregation = response.getAggregations().get("by_" + attribute);
                    Terms term = filterAggregation.getAggregations().get("by_" + attribute);
                    termfilterBuilder(filters, attribute, term, requestedFilters);
                } catch (ClassCastException ex) {
                    Terms term = response.getAggregations().get("by_" + attribute);
                    termfilterBuilder(filters, attribute, term, requestedFilters);
                }
            }

            for (String attribute : termQueryProductAttributes) {
                try {
                    Filter filterAggregation = response.getAggregations().get("by_" + attribute);
                    Nested nested = filterAggregation.getAggregations().get("by_" + attribute);
                    Terms term = nested.getAggregations().get("all_" + attribute);
                    termfilterBuilder(filters, attribute, term, requestedFilters);
                } catch (ClassCastException ex) {
                    Nested nested = response.getAggregations().get("by_" + attribute);
                    Terms term = nested.getAggregations().get("all_" + attribute);
                    termfilterBuilder(filters, attribute, term, requestedFilters);
                }
            }
            
            for (String attribute : termQuerySkuAttributes) {
                try {
                    Filter filterAggregation = response.getAggregations().get("by_" + attribute);
                    Nested nested = filterAggregation.getAggregations().get("by_" + attribute);
                    Terms term = nested.getAggregations().get("all_" + attribute);
                    termfilterBuilder(filters, attribute, term, requestedFilters);
                } catch (ClassCastException ex) {
                    Nested nested = response.getAggregations().get("by_" + attribute);
                    Terms term = nested.getAggregations().get("all_" + attribute);
                    termfilterBuilder(filters, attribute, term, requestedFilters);
                }
            }
        }
    }

    private void termfilterBuilder(List<FilterDto> filters, String attribute, Terms term,
            Set<String> requestedFilters) {
        FilterDto dto = new FilterDto();
        checkSpecialCaseToSetFIlterDisplayName(attribute, dto);
        dto.setFilterName(attribute);
        dto.setType(FilterTypeEnum.TERM.toString());
        List<DataDto> data = new ArrayList<DataDto>();
        if (isSatisfyingSetCriteria(term)) {
            for (Terms.Bucket bucket : term.getBuckets()) {
                data.add(dataDtoBuilder(bucket.getKey().toString(), bucket.getDocCount(),
                        requestedFilters));
            }
            dto.setData(data);
            filters.add(dto);
        }
        if ("brand".equalsIgnoreCase(attribute) || "color".equalsIgnoreCase(attribute)) {
            Collections.sort(data, new Comparator<DataDto>() {
                public int compare(DataDto o1, DataDto o2) {
                    return o1.getKey().compareTo(o2.getKey());
                }
            });
        }
    }

    private void checkSpecialCaseToSetFIlterDisplayName(String attribute, FilterDto dto) {
        if ("product_type".equalsIgnoreCase(attribute)) {
            dto.setDisplayName("Category");
        } else {
            dto.setDisplayName(toCamelCase(attribute));
        }
    }

    private boolean isSatisfyingSetCriteria(Terms term) {
        if (term.getBuckets() == null || term.getBuckets().isEmpty()
                || term.getBuckets().size() <= 1) {
            return false;
        }
        return true;
    }

    private SortOption sortOptionsBuilder(SortRequest sortRequest,
            Map<String, Object> sortOptionsMappings) {
        String requestSortOption = sortRequest == null ? null : sortRequest.getSortBy();
        SortOption options = new SortOption();
        List<DataDto> data = new ArrayList<DataDto>();
        for (Entry<String, Object> entry : sortOptionsMappings.entrySet()) {
            String key = entry.getKey();
            if (!key.contains("order_")) {
                data.add(sortDataDtoBuillder(key, requestSortOption));
            }
        }
        options.setData(data);
        return options;
    }

    private DataDto sortDataDtoBuillder(String key, String requestSortOption) {
        DataDto sortData = new DataDto();
        sortData.setKey(key);
        if (key.equalsIgnoreCase(requestSortOption))
            sortData.setSelected(true);
        return sortData;
    }

    public void searchSourceBuilder(BoolQueryBuilder queryBuilder, BoolQueryBuilder postFilterQuery,
            Integer from, List<AggregationBuilder> allAggregation,
            List<AggregationBuilder> minRangeFilterAgg, List<AggregationBuilder> maxRangeFilterAgg,
            FieldSortBuilder sortBuilder, org.elasticsearch.action.search.SearchRequest request) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        sourceBuilder.from(from*PAGE_SIZE);
        sourceBuilder.size(PAGE_SIZE);
        for (AggregationBuilder agg : allAggregation)
            sourceBuilder.aggregation(agg);
        for (AggregationBuilder agg : minRangeFilterAgg)
            sourceBuilder.aggregation(agg);
        for (AggregationBuilder agg : maxRangeFilterAgg)
            sourceBuilder.aggregation(agg);
        if (postFilterQuery != null)
            sourceBuilder.postFilter(postFilterQuery);
        sourceBuilder.query(queryBuilder);
        sourceBuilder.sort(sortBuilder);
        sourceBuilder.sort(appendSortBuilderOnInventory());

        request.source(sourceBuilder);
    }
    
    private FieldSortBuilder appendSortBuilderOnInventory() {
        return SortBuilders.fieldSort("inventory.in_stock").order(SortOrder.DESC);
    }
    
    public QueryBuilder fullTextSearchQueryWithExactSearchBuilder(SearchRequest query) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.boolQuery()
                .should(QueryBuilders.termQuery("name.keyword", query.getQuery()))
                .should(QueryBuilders.nestedQuery("skus",
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.termQuery("skus.sku.keyword",
                                        query.getQuery())),
                        ScoreMode.None))
                .should(QueryBuilders.multiMatchQuery(query.getQuery().toLowerCase(), "brand")
                        .minimumShouldMatch("75%").fuzziness(Fuzziness.AUTO)
                        .operator(Operator.AND)));
        return queryBuilder;
    }
    
    public QueryBuilder fullTextSearchQueryBuilderWithMorePriorityAttributes(SearchRequest query) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.boolQuery()
                .should(QueryBuilders
                        .multiMatchQuery(query.getQuery().toLowerCase(),
                                convertArrayIntoSeprateStrings(
                                        AutoSearchUtility.morePriorityAttributes))
                        .minimumShouldMatch("88%").fuzziness(Fuzziness.AUTO)
                        .operator(Operator.AND)));
        return queryBuilder;
    }

    public QueryBuilder fullTextSearchQueryBuilder(SearchRequest query) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders
                .boolQuery().should(
                        QueryBuilders
                                .multiMatchQuery(query.getQuery().toLowerCase(),
                                        convertArrayIntoSeprateStrings(
                                                searchQueryTermsProductHirerarcy))
                                .minimumShouldMatch("66%").fuzziness(Fuzziness.AUTO)
                                .operator(Operator.AND))
                .should(QueryBuilders.nestedQuery("attributes", QueryBuilders
                        .multiMatchQuery(query.getQuery().toLowerCase(),
                                convertArrayIntoSeprateStrings(
                                        searchQueryTermsProductLevelAttributes))
                        .minimumShouldMatch("66%").fuzziness(Fuzziness.AUTO).operator(Operator.AND),
                        ScoreMode.None))
                .should(QueryBuilders.nestedQuery("skus",
                        QueryBuilders.nestedQuery("attributes",
                                QueryBuilders
                                        .multiMatchQuery(query.getQuery().toLowerCase(),
                                                convertArrayIntoSeprateStrings(
                                                        searchQueryTermsSkuLevelAttributes))
                                        .minimumShouldMatch("66%").fuzziness(Fuzziness.AUTO)
                                        .operator(Operator.AND),
                                ScoreMode.None),
                        ScoreMode.None)));
        return queryBuilder;
    }

    public BoolQueryBuilder postFilterQueryBuilder(SearchRequest query) {
        BoolQueryBuilder postFilterQuery = QueryBuilders.boolQuery();
        Map<String, List<FilterRequest>> byKey = query.getFilters().stream()
                .filter(filter -> rangeQueryAttributes.contains(filter.getKey()) == false)
                .collect(Collectors.groupingBy(FilterRequest::getKey));
        for (Map.Entry<String, List<FilterRequest>> entry : byKey.entrySet()) {
            BoolQueryBuilder orQueryBuilder = QueryBuilders.boolQuery();
            for (FilterRequest filter : entry.getValue()) {
                String key = getConvertedAttributeName(filter);
                if (termQueryProductHirerarcy.contains(key.toLowerCase())) {
                    orQueryBuilder
                            .should(QueryBuilders.termQuery(key + ".keyword", filter.getValue()));
                } else if (termQueryProductAttributes.contains(key.toLowerCase())) {
                    orQueryBuilder
                            .should(QueryBuilders.nestedQuery("attributes",
                                    QueryBuilders.boolQuery().should(QueryBuilders.termQuery(
                                            "attributes." + key + ".attribute_value" + ".keyword",
                                            filter.getValue())),
                                    ScoreMode.None));
                } else if(termQuerySkuAttributes.contains(key.toLowerCase())) {
                    orQueryBuilder
                    .should(QueryBuilders.nestedQuery("skus.attributes",
                            QueryBuilders.boolQuery().should(QueryBuilders.termQuery(
                                    "skus.attributes." + key + ".attribute_value" + ".keyword",
                                    filter.getValue())),
                            ScoreMode.None));
                }
            }

            postFilterQuery.filter(orQueryBuilder);
        }
        postFilterQuery = postFilterQuery.filter().isEmpty() ? null : postFilterQuery;
        return postFilterQuery;
    }

    private String getConvertedAttributeName(FilterRequest filter) {
        if (filter.getKey().equalsIgnoreCase("category"))
            return "product_type";
        return filter.getKey();
    }

    public SearchRequest buildSearchRequest(Map<String, String> params, String trackingId)
            throws JsonProcessingException {
        log.info("Query Parameters params={} RequestType={}, transactionId={}",
                Utility.getJson(params), GET_SEARCH_RESPONSE, trackingId);
        SearchRequest request = new SearchRequest();
        request.setFrom(Integer.valueOf(params.get("from")));
        request.setQuery(params.get("query"));
        request.setSortBy(new SortRequest(params.get("sort")));
        request.setFilters(buildFilters(params.get("term"), params.get("range")));
        return request;
    }

    private List<FilterRequest> buildFilters(String terms, String ranges) {
        List<FilterRequest> filterRequests = new ArrayList<FilterRequest>();
        if (!StringUtils.hasText(terms) && !StringUtils.hasText(ranges))
            return filterRequests;
        if (StringUtils.hasText(terms))
            buildTermLevelFilters(terms, filterRequests);
        if (StringUtils.hasText(ranges))
            buildRangeLevelFilters(ranges, filterRequests);
        return filterRequests;
    }

    private void buildRangeLevelFilters(String ranges, List<FilterRequest> filterRequests) {
        String[] allRanges = ranges.split("::");
        for (int index = 0; index < allRanges.length; index++) {
            String[] keyValue = allRanges[index].split(":");
            if (keyValue.length != 2)
                throw new RuntimeException("Something went wrong");
            String key = keyValue[0];
            String[] allValues = keyValue[1].split("TO");
            FilterRequest eachRequest = new FilterRequest();
            eachRequest.setKey(key);
            eachRequest.setValue(keyValue[1]);
            eachRequest.setFrom(allValues[0]);
            eachRequest.setTo(allValues[1]);
            eachRequest.setType("range");
            filterRequests.add(eachRequest);
        }
    }

    private void buildTermLevelFilters(String terms, List<FilterRequest> filterRequests) {
        String[] allTerms = terms.split("::");
        for (int index = 0; index < allTerms.length; index++) {
            String[] keyValue = allTerms[index].split(":");
            if (keyValue.length != 2)
                throw new RuntimeException("Something went wrong");
            String key = keyValue[0];
            String[] allValues = keyValue[1].split(",");
            for (int iindex = 0; iindex < allValues.length; iindex++) {
                FilterRequest eachRequest = new FilterRequest();
                eachRequest.setKey(key);
                eachRequest.setValue(allValues[iindex]);
                eachRequest.setType("term");
                filterRequests.add(eachRequest);
            }
        }
    }
    
}
