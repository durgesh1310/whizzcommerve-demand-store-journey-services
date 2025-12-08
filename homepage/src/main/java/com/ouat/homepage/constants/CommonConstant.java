package com.ouat.homepage.constants;

import java.util.HashMap;
import java.util.Map;

public class CommonConstant {

	public static final String GENERIC_ERROR_MSG = "Oh no, our systems are getting upgraded !!";
	public static final String INVALID_REQUEST_PARAM = "Invalid Request Parameters";
	
	public static final String FAILURE_STATUS_CODE = "500";
	public static final String SUCCESS_STATUS_CODE = "200";
	public static final String CREATED = "201";
	public static final String UNAUTHORIZED = "401";
	
	public static final int TTL = 360000;
	
	public static final String CATEGORY_TREE_KEY = "CACHE_CATEGORY_TREE";

	public static final String SORTBAR_KEY = "CACHE_SORTBAR";

	public static final String HOME_PAGE_KEY = "CACHE_HOME_PAGE_";

	public static final String SPECIAL_PAGE_KEY = "CACHE_SPECIAL_PAGE_";//platform_urlpattern
	
	public static final Map<String, String> COMPONENT_URL_MAPPING = new HashMap<String, String>() {{
        put(CATEGORY_TREE_KEY, "homepage-setup/active-tree-detail");
        put(SORTBAR_KEY, "homepage-setup/active-sortbar");
        put(HOME_PAGE_KEY+"MOBILE", "homepage-setup/active-homepage");
        put(HOME_PAGE_KEY+"WEB", "homepage-setup/active-homepage");
        put(SPECIAL_PAGE_KEY+"MOBILE", "homepage-setup/active-specialpage");
        put(SPECIAL_PAGE_KEY+"WEB", "homepage-setup/active-specialpage");
    }};
	
    public static final String INVALID_REQUEST = "Invalid Request";

}
