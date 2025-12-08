package com.ouat.suggestionandsearch.elasticsearch.documents;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ouat.suggestionandsearch.constants.elasticsearch.ElasticSearchConstants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "onSale", "trending", "bestSeller", "returnable", "exchangeable", "cod", "newArrival", "luxe"})
@Document(createIndex = false, indexName = ElasticSearchConstants.SEARCH_INDEX)
public class SearchDocument {
	@JsonProperty("product_id")
	@Id
	@Field(name = "product_id")
	private Integer productId;

	@Field(name = "name", type = FieldType.Text, analyzer = "standard", searchAnalyzer = "synonym_analyzer", fielddata = true)
	private String name;

	@Field(name = "description", type = FieldType.Text, analyzer = "standard", searchAnalyzer = "synonym_analyzer", fielddata = true)
	private String description;

	@JsonProperty("url_key")
    @Field(name = "url_key", type = FieldType.Text, analyzer = "standard", searchAnalyzer = "synonym_analyzer", fielddata = true)
    private String urlKey;
	
	@JsonProperty("ouat_margin")
	@Field(name = "ouat_margin", type = FieldType.Integer)
	private Integer ouatMargin;

	@Field(name = "attributes", type = FieldType.Object)
	private List<Map<String, AttributesDocument>> attributes;

	@JsonProperty("from_price")
	@Field(name = "from_price", type = FieldType.Double)
	private Double fromPrice;

	@JsonProperty("to_price")
	@Field(name = "to_price", type = FieldType.Double)
	private Double toPrice;
	
	@JsonProperty("from_discount")
	@Field(name = "from_discount", type = FieldType.Double)
	private Double fromDiscount;
	
	@JsonProperty("to_discount")
	@Field(name = "to_discount", type = FieldType.Double)
	private Double toDiscount;

	@JsonProperty("from_age")
	@Field(name = "from_age", type = FieldType.Double)
	private Double fromAge;
	
	@JsonProperty("to_age")
	@Field(name = "to_age", type = FieldType.Double)
	private Double toAge;

	@JsonProperty("vendor_name")
	@Field(name = "vendor_name", type = FieldType.Text, analyzer = "standard", searchAnalyzer = "synonym_analyzer", fielddata = true)
	private String vendorName;

	@Field(name = "inventory", type = FieldType.Object)
	private InventoryDocument inventory;

	@JsonProperty("seo_meta_data")
	@Field(name = "seo_meta_data", type = FieldType.Object)
	private SeoMetaDataDocument seoMetaDataDocument;

	@JsonProperty("bread_crum")
	@Field(name = "bread_crum", type = FieldType.Text, analyzer = "standard", searchAnalyzer = "synonym_analyzer", fielddata = true)
	private String breadCrum;

	@Field(name = "category", type = FieldType.Text, analyzer = "standard", searchAnalyzer = "synonym_analyzer", fielddata = true)
	private String category;

	@JsonProperty("sub_category")
	@Field(name = "sub_category", type = FieldType.Text, analyzer = "standard", searchAnalyzer = "synonym_analyzer", fielddata = true)
	private String subCategory;

	@JsonProperty("product_type")
	@Field(name = "product_type", type = FieldType.Text, analyzer = "standard", searchAnalyzer = "synonym_analyzer", fielddata = true)
	private String productType;

	@JsonProperty("product_subtype")
	@Field(name = "product_subtype", type = FieldType.Text, analyzer = "standard", searchAnalyzer = "synonym_analyzer", fielddata = true)
	private String productSubtype;

	@Field(name = "brand", type = FieldType.Text, analyzer = "standard", searchAnalyzer = "synonym_analyzer", fielddata = true)
	private String brand;

	@Field(name = "edd", type = FieldType.Text, analyzer = "standard", searchAnalyzer = "synonym_analyzer", fielddata = true)
	private String edd;

	@JsonProperty("image_urls")
	@Field(name = "image_urls", type = FieldType.Nested)
	private List<ImageUrlDocument> imageUrls;

	@JsonProperty("size_chart_urls")
	@Field(name = "size_chart_urls", type = FieldType.Nested)
	private List<ImageUrlDocument> sizeChartUrls;

	@Field(name = "suggest", type = FieldType.Text, analyzer = "standard", searchAnalyzer = "synonym_analyzer", fielddata = true)
	private String suggest;

	@JsonProperty("is_on_sale")
	@Field(name = "is_on_sale", type = FieldType.Boolean)
	private boolean isOnSale;

	@JsonProperty("is_trending")
	@Field(name = "is_trending", type = FieldType.Boolean)
	private boolean isTrending;

	@JsonProperty("is_best_seller")
	@Field(name = "is_best_seller", type = FieldType.Boolean)
	private boolean isBestSeller;

	@JsonProperty("can_wishlist")
	@Field(name = "can_wishlist", type = FieldType.Boolean)
	private boolean canWishList;

	@JsonProperty("is_returnable")
	@Field(name = "is_returnable", type = FieldType.Boolean)
	private boolean isReturnable;

	@JsonProperty("is_exchangeable")
	@Field(name = "is_exchangeable", type = FieldType.Boolean)
	private boolean isExchangeable;

	@JsonProperty("is_cod")
	@Field(name = "is_cod", type = FieldType.Boolean)
	private boolean isCod;

	@JsonProperty("is_new_arrival")
	@Field(name = "is_new_arrival", type = FieldType.Boolean)
	private boolean isNewArrival;
	
	@JsonProperty("is_express_shipping")
    @Field(name = "is_express_shipping", type = FieldType.Boolean)
    private boolean isExpressShipping;
	
    @JsonProperty("is_exclusive")
    @Field(name = "is_exclusive", type = FieldType.Boolean)
    private boolean isExclusive;
    
    @JsonProperty("is_luxe")
    @Field(name = "is_luxe", type = FieldType.Boolean)
    private boolean isLuxe;

	@JsonProperty("expiry_date")
	@Field(name = "expiry_date", type = FieldType.Date)
	private Date expiryDate;

	@Field(name = "skus", type = FieldType.Nested)
	private List<SkuDocument> skus;
	
	@JsonProperty("sorting_score")
	@Field(name = "sorting_score", type = FieldType.Nested)
	private List<Map<String, Object>> sortingScore;

}
