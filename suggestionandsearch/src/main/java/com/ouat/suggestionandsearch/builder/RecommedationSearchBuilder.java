package com.ouat.suggestionandsearch.builder;

import static com.ouat.suggestionandsearch.constants.DownStreamConstants.ELASTICSEARCH;
import static com.ouat.suggestionandsearch.constants.RequestType.GET_PRODUCT_DETAILS;
import static com.ouat.suggestionandsearch.enums.autosearch.PLPTypeEnum.PRODUCT;
import static com.ouat.suggestionandsearch.util.MetricsUtility.addDownStreamMetrics;
import static com.ouat.suggestionandsearch.util.Utility.convertJsonToObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import com.ouat.suggestionandsearch.constants.RequestType;
import com.ouat.suggestionandsearch.dtos.autosearch.ProductDto;
import com.ouat.suggestionandsearch.dtos.autosearch.SkuDto;
import com.ouat.suggestionandsearch.elasticsearch.documents.AttributesDocument;
import com.ouat.suggestionandsearch.elasticsearch.documents.ImageUrlDocument;
import com.ouat.suggestionandsearch.elasticsearch.documents.InventoryDocument;
import com.ouat.suggestionandsearch.elasticsearch.documents.SearchDocument;
import com.ouat.suggestionandsearch.elasticsearch.documents.SkuDocument;
import com.ouat.suggestionandsearch.util.Utility;

@Component
public class RecommedationSearchBuilder {

    private static final Logger log = LoggerFactory.getLogger(RecommedationSearchBuilder.class);

    private static final String PDP_URL = "/ouat/pdp/";
    private static final String CURRENCY_TYPE = "RS";

    public GetResponse getProductDetails(RestHighLevelClient client, String searchIndex,
            String trackingId, Integer productId) throws IOException {
        StopWatch timer = new StopWatch();
        timer.start();

        GetRequest request = new GetRequest(searchIndex, String.valueOf(productId));
        GetResponse response = client.get(request, RequestOptions.DEFAULT);

        timer.stop();

        log.info("finding product details for ProductId={}, RequestType={}, TrackingId={}",
                productId, GET_PRODUCT_DETAILS, trackingId);
        addDownStreamMetrics(ELASTICSEARCH, timer.getTotalTimeSeconds(), GET_PRODUCT_DETAILS,
                trackingId);
        return response;
    }

    public BoolQueryBuilder recommendationQueryBuilder(Set<String> excludeAttributes,
            SearchDocument productDetailsDocument) {
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        queryBuilder
                .must(QueryBuilders.termQuery("category.keyword",
                        productDetailsDocument.getCategory()))
                .must(QueryBuilders.termQuery("sub_category.keyword",
                        productDetailsDocument.getSubCategory()))
                .must(QueryBuilders.termQuery("product_type.keyword",
                        productDetailsDocument.getProductType()))
                .must(QueryBuilders.termQuery("inventory.in_stock", true))
                .must(attributesQueryBuilder(excludeAttributes,
                        productDetailsDocument.getAttributes()))
                .mustNot(QueryBuilders.idsQuery()
                        .addIds(String.valueOf(productDetailsDocument.getProductId())));
        if (productDetailsDocument.getProductSubtype() != null) {
            queryBuilder.must(QueryBuilders.termQuery("product_subtype.keyword",
                    productDetailsDocument.getProductSubtype()));
        }
        if (productDetailsDocument.getProductType() != null) {
            queryBuilder.must(QueryBuilders.termQuery("product_type.keyword",
                    productDetailsDocument.getProductType()));
        }
        queryBuilder.must(
                QueryBuilders.rangeQuery("from_price").gte(productDetailsDocument.getFromPrice()));
        if (Double.compare(productDetailsDocument.getFromDiscount(),
                productDetailsDocument.getToDiscount()) != 0) {
            queryBuilder
                    .must(QueryBuilders.rangeQuery("from_discount")
                            .gte(productDetailsDocument.getFromDiscount()))
                    .must(QueryBuilders.rangeQuery("to_discount")
                            .lte(productDetailsDocument.getToDiscount()));
        }
        return queryBuilder;
    }

    private QueryBuilder attributesQueryBuilder(Set<String> excludeAttributes,
            List<Map<String, AttributesDocument>> attributes) {

        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        attributes.stream().forEach(attribute -> {
            attribute.keySet().stream().forEach(attr -> {
                if (!excludeAttributes.contains(attr.toLowerCase())) {
                    if (attribute.get(attr).getAttributeValue() != null) {
                        queryBuilder.should(QueryBuilders.termQuery(
                                "attributes." + attr + ".attribute_value.keyword",
                                attribute.get(attr).getAttributeValue()));
                    } ;
                }
            });
        });

        queryBuilder.minimumShouldMatch(0);

        return QueryBuilders.nestedQuery("attributes", queryBuilder, ScoreMode.None);
    }

    public List<ProductDto> productsResponseBuilder(SearchHit[] hits, String trackingId) {
        List<ProductDto> products = new ArrayList<ProductDto>();
        for (SearchHit hit : hits) {
            ProductDto product = new ProductDto();
            SearchDocument document =
                    convertJsonToObject(hit.getSourceAsString(), SearchDocument.class);
            product.setProductId(document.getProductId());
            product.setProductName(document.getName());
            product.setCanWishlist(document.isCanWishList());
            product.setCurrencyCode(CURRENCY_TYPE);
            product.setVendorName(document.getVendorName());
            product.setMaxRegularPrice(evaluateMaxRegularPrice(document.getSkus()));
            product.setMaxRetailPrice(Math.round(document.getToPrice()));
            product.setDiscountPercentage(discountBuilder(document.getToDiscount()));
            product.setIsOnSale(document.isOnSale());
            product.setImageUrls(imageUrlsBuilder(document.getImageUrls()));
            product.setSizeChartImageUrls(imageUrlsBuilder(document.getSizeChartUrls()));
            product.setMsgOnImage(buildMsgOnImg(document.isBestSeller(), document.isNewArrival(),
                    document.isTrending(), document.isExpressShipping(), document.isLuxe()));
            product.setType(PRODUCT);
            product.setRedirectTo(PDP_URL + document.getProductId());
            setInventoryDetails(product, document.getInventory());
            product.setSkuInfo(skusInfoBuilder(document.getSkus(), trackingId));
            product.setDeliveryMessage(document.getEdd());
            product.setBrandName(document.getBrand());
            product.setUrlKey(document.getUrlKey());
            products.add(product);
        }
        return products;
    }

    private Long evaluateMaxRegularPrice(List<SkuDocument> skus) {
        if (CollectionUtils.isEmpty(skus))
            return null;
        return Math
                .round(skus.stream().max(Comparator.comparingDouble(SkuDocument::getRegularPrice))
                        .get().getRegularPrice());
    }

    private List<String> buildMsgOnImg(boolean bestSeller, boolean newArrival, boolean trending,
            boolean expressShipping, boolean luxe) {
        List<String> msgOnImg = new ArrayList<String>();
        if (bestSeller == true) {
            msgOnImg.add("BEST SELLER");
        } else if (newArrival == true) {
            msgOnImg.add("NEW ARRIVAL");
        } else if (trending == true) {
            msgOnImg.add("TRENDING");
        } else if (expressShipping == true) {
            msgOnImg.add("EXPRESS SHIPPING");
        } else if (luxe == true) {
            msgOnImg.add("LUXE");
        }
        return msgOnImg;
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
                    sku.getSku(), RequestType.GET_RECOMMENDATION_RESPONSE, trackingId);
            return null;
        }
    }

    public void newArrivalRecommnendationQueryBuilder(BoolQueryBuilder queryBuilder,
            List<ProductDto> allProducts) {
        String[] recommendationProductIds = Utility.convertArrayIntoSeprateStrings(
                allProducts.stream().map(product -> product.getProductId().toString())
                        .collect(Collectors.toList()));
        queryBuilder.mustNot(QueryBuilders.idsQuery().addIds(recommendationProductIds))
                .must(QueryBuilders.termQuery("is_new_arrival", true));
    }
}
