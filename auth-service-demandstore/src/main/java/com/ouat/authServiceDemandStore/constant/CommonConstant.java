package com.ouat.authServiceDemandStore.constant;

public class CommonConstant {
	
	public static final String USER_ID = "user_id";
	
	public static final String FAILURE_STATUS_CODE = "500";
	public static final String SUCCESS_STATUS_CODE = "200";
	public static final String CREATED = "201";
	public static final String UNAUTHORIZED = "401";

	public static final String INVALID_TOKEN_OR_MISSING = "Looks like invalid token passed";
	
	public static final String EMAIL_ALREADY_VERIFIED = "Looks like email is already verified";
	
	public static final String GENERIC_ERROR_MSG = "Something went wrong !!";
	
	public static final String INVALID_REQUEST = "Invalid Request";
	
	public static final String INVALID_REQUEST_PARAM = "Invalid Request Parameters";
	
	
	public static final String OS = "os";
	
	public static final String BROWSER = "browser";
	
	public static final String USER_CLIENT = "user-client";
	
	public static final String DEVICE_ID = "device-id";
	
	public static final String DEVICE_TYPE = "device-type";
	
	public static final String KEY = "key";
	
	public static final String PLATFORM = "platform";
	
	public static final String APPVERSION = "app-version";
	
	public static final String APIVERSION = "api-version";
	
	public static final long JWT_TOKEN_VALIDITY = 48 * 60 * 60 * 60000; // 120 days
	public static final int JWT_TOKEN_VALIDITY_IN_DAYS = 120;
	
	public static final String USER_SESSION_KEY = "USER_SESSION:"; 
	
	public static final String TOKEN_GENERATED = "User's secure token generated"; 
	
	public static final String USER_NOT_LOGGED_IN = "User not logged in, please try after relogin"; 
	
	public static final String USER_DETAIL = "detail";
	
}
