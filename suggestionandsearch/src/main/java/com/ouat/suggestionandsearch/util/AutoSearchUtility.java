package com.ouat.suggestionandsearch.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.elasticsearch.search.sort.SortOrder;

import com.ouat.suggestionandsearch.enums.autosearch.SortOptionsEnum;

public class AutoSearchUtility {

	public static Set<String> rangeQueryAttributes = Stream.of("age", "price", "discount").collect(Collectors.toSet());
	public static Set<String> termQueryProductHirerarcy = Stream.of("product_type", "brand").collect(Collectors.toSet());
	public static Set<String> termQueryProductAttributes = Stream.of("color", "pattern")
			.collect(Collectors.toSet());
	public static Set<String> termQuerySkuAttributes = Stream.of("size", "kids size")
            .collect(Collectors.toSet());
	public static List<String> searchQueryTermsProductHirerarcy = Stream.of("name", "category", "sub_category",
			"product_type", "product_subtype", "brand", "vendor_name", "description", "suggest")
			.collect(Collectors.toList());
    public static List<String> morePriorityAttributes =
            Stream.of("product_type", "product_subtype").collect(Collectors.toList());
    
	public static List<String> searchQueryTermsProductLevelAttributes = Stream.of("attributes.color.attribute_value",
			"attributes.brand.attribute_value", "attributes.occasion.attribute_value").collect(Collectors.toList());
//	public static List<String> searchQueryTermsSkuLevelAttributes = Stream.of("skus.attributes.color.attribute_value",
//			"skus.attributes.brand.attribute_value", "skus.attributes.occasion.attribute_value")
//			.collect(Collectors.toList());
	public static List<String> searchQueryTermsSkuLevelAttributes = new ArrayList<String>();

	public static Map<String, Object> sortOptionsMappings;
	static {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(SortOptionsEnum.RECOMMENDED.toString(), "from_price");
		map.put(SortOptionsEnum.PRICE_LOW_TO_HIGH.toString(), "from_price");
		map.put(SortOptionsEnum.PRICE_HIGH_TO_LOW.toString(), "from_price");
		map.put(SortOptionsEnum.BETTER_DISCOUNT.toString(), "from_discount");
		map.put(SortOptionsEnum.NEW_ARRIVAL.toString(), "is_new_arrival");
		map.put(SortOptionsEnum.BEST_SELLER.toString(), "is_best_seller");
		map.put("order_" + SortOptionsEnum.RECOMMENDED.toString(), SortOrder.ASC);
		map.put("order_" + SortOptionsEnum.PRICE_LOW_TO_HIGH.toString(), SortOrder.ASC);
		map.put("order_" + SortOptionsEnum.PRICE_HIGH_TO_LOW.toString(), SortOrder.DESC);
		map.put("order_" + SortOptionsEnum.BETTER_DISCOUNT.toString(), SortOrder.DESC);
		map.put("order_" + SortOptionsEnum.NEW_ARRIVAL.toString(), SortOrder.DESC);
		map.put("order_" + SortOptionsEnum.BEST_SELLER.toString(), SortOrder.DESC);
		sortOptionsMappings = map;
	}
	
    public static final Map<String, Object> hexCodeMappings;
    static {
        Map<String, Object> hexMap = new HashMap<String, Object>();
        hexMap.put("pink","#FFC0CB");
        hexMap.put("yellow","#FFFF00");
        hexMap.put("white","#FFFFFF");
        hexMap.put("blue","#0000FF");
        hexMap.put("golden","#ffd700");
        hexMap.put("black","#000000");
        hexMap.put("grey","#808080");
        hexMap.put("navy blue","#000080");
        hexMap.put("off white","#F8F0E3");
        hexMap.put("turquoise","#30d5c8");
        hexMap.put("peach","#FFE5B4");
        hexMap.put("fuchsia","#FF00FF");
        hexMap.put("purple","#800080");
        hexMap.put("red","#FF0000");
        hexMap.put("beige","#f5f5dc");
        hexMap.put("rust","#C36241");
        hexMap.put("silver","#C0C0C0");
        hexMap.put("green","#008000");
        hexMap.put("brown","#A52A2A");
        hexMap.put("nude","#E3BC9A");
        hexMap.put("tan","#d2b48c");
        hexMap.put("cream","#FFFFCC");
        hexMap.put("teal","#008080");
        hexMap.put("wine","#722f37");
        hexMap.put("copper","#B87333");
        hexMap.put("bronze","#CD7F32");
        hexMap.put("orange","#FFA500");
        hexMap.put("maroon","#800000");
        hexMap.put("mustard","#ffdb58");
        hexMap.put("antique","#faebd7");
        hexMap.put("lavender","#754C78");
        hexMap.put("indigo","#6f00ff");
        hexMap.put("magenta","#FF00FF");
        hexMap.put("natural","#E5D3BF");
        hexMap.put("coral","#ff7f50");
        hexMap.put("neon","#39FF14");
        hexMap.put("aqua green","#00FFFF");
        hexMap.put("aqua blue","#00FFFF");
        hexMap.put("transparent","#0000ffff");
        hexMap.put("light green","#90ee90");
        hexMap.put("light pink","#ffb6c1");
        hexMap.put("tiffany blue","#81D8D0");
        hexMap.put("dark blue","#00008B");
        hexMap.put("dark brown","#654321");
        hexMap.put("baby pink","#FAAFBA");
        hexMap.put("dark green","#006400");
        hexMap.put("dark grey","#A9A9A9");
        hexMap.put("light grey","D3D3D3");
        hexMap.put("light brown","#B5651D");
        hexMap.put("lime green","#32CD32");
        hexMap.put("porcelain","#95C0CB");
        hexMap.put("hazelnut","#AE9F80");
        hexMap.put("aquamarine","#7FFFD4");
        hexMap.put("almond nude","#DFD5CA");
        hexMap.put("autumn nude","#AF8D7A");
        hexMap.put("beige nude","#E3BC9A");
        hexMap.put("cocoa nude","#59473f");
        hexMap.put("ivory nude","#EBDDBE");
        hexMap.put("rose nude","#E9ACB6");
        hexMap.put("ceramic","#FEFFFD");
        hexMap.put("berry","#AB0B23");
        hexMap.put("blue","#0000FF");
        hexMap.put("yellow","#FFFF00");
        hexMap.put("magenta","#FF00FF");
        hexMap.put("red","#FF0000");
        hexMap.put("white","#FFFFFF");
        hexMap.put("purple","#A020F0");
        hexMap.put("brown","#802A2A");
        hexMap.put("offwhite","#F8F0E3");
        hexMap.put("lilac","#c8a2c8");
        hexMap.put("orange","#ff7f00");
        hexMap.put("maroon","#c32148");
        hexMap.put("mauve","#e0b0ff");
        hexMap.put("navy","#000080");
        hexMap.put("burgundy","#800020");
        hexMap.put("cream","#fffdd0");
        hexMap.put("lemon","#fff700");
        hexMap.put("skyblue","#87ceeb");
        hexMap.put("rust","#b7410e");
        hexMap.put("taupe","#483c32");
        hexMap.put("olive green","#556b2f");
        hexMap.put("pastel blue","#aec6cf");
        hexMap.put("mint","#3eb489");
        hexMap.put("ivory","#fffff0");
        hexMap.put("pastel green","#77DD77");
        hexMap.put("oldrose","#c08081");
        hexMap.put("rosegold","#b76e79");
        hexMap.put("emerald green","#5FFB17");
        hexMap.put("violet","#8f00ff");
        hexMap.put("olive","#808000");
        hexMap.put("charcoal","#36454f");
        hexMap.put("gunmetal","#2C3539");
        hexMap.put("saffron","#f4c430");
        hexMap.put("rose","#ff007f");
        hexMap.put("aqua","#00FFFF");
        hexMap.put("sea green","#2e8b57");
        hexMap.put("khaki","#9F9F5F");
        hexMap.put("caramel","#C68E17");
        hexMap.put("lime","#00FF00");
        hexMap.put("jade","#00a86b");
        hexMap.put("crimson","#dc143c");
        hexMap.put("salmon","#ff8c69");
        hexMap.put("fawn","#e5aa70");
        hexMap.put("wood","#966F33");
        hexMap.put("cornflower blue","#6495ed");
        hexMap.put("light pink","#ffb6c1");
        hexMap.put("light green","#90ee90");
        hexMap.put("teal blue","#367588");
        hexMap.put("navy blue","#000080");
        hexMap.put("bottle green","#006a4e");
        hexMap.put("rosepink","#ff66cc");
        hexMap.put("dark blue","#00008b");
        hexMap.put("cerulean blue","#2a52be");
        hexMap.put("tiffany blue","#0abab5");
        hexMap.put("sage green","#848B79");
        hexMap.put("pistachio green","#9DC209");
        hexMap.put("hot pink","#ff69b4");
        hexMap.put("dark brown","#654321");
        hexMap.put("mint green","#98ff98");
        hexMap.put("teal green","#00827f");
        hexMap.put("baby pink","#f4c2c2");
        hexMap.put("blue green","#7BCCB5");
        hexMap.put("light gray","#d3d3d3");
        hexMap.put("dark green","#013220");
        hexMap.put("gray","#808080");
        hexMap.put("neon pink","#F535AA");
        hexMap.put("parrot green","#12AD2B");
        hexMap.put("dark gray","#a9a9a9");
        hexMap.put("sangria","#92000a");
        hexMap.put("daffodil","#ffff31");
        hexMap.put("grape","#543948");
        hexMap.put("light brown","#b5651d");
        hexMap.put("light blue","#add8e6");
        hexMap.put("sea blue","#006994");
        hexMap.put("blush","#de5d83");
        hexMap.put("cobalt blue","#0020C2");
        hexMap.put("pastel yellow","#fdfd96");
        hexMap.put("mustard yellow","#FFDB58");
        hexMap.put("turquoise blue","#00ffef");
        hexMap.put("aquamarine","#7fffd4");
        hexMap.put("neon green","#39ff14");
        hexMap.put("basil","#829F82");
        hexMap.put("royal blue","#4169e1");
        hexMap.put("pista green","#99bd9c");
        hexMap.put("multicolor", "");
        hexCodeMappings = hexMap;
    }
}
