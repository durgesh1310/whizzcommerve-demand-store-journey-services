package com.ouat.checkout.placeOrder.commonConstant;

import java.util.regex.Pattern;

public class CommonConstant {
	public static String SOMETHING_WENT_WRONG  = "Oh no ! Something went wrong please try again";
	public static final String FAILURE_STATUS_CODE = "500";
	public static boolean SUCCESS_FLAG  = true;
	public static String SUCCESS_STATUS_CODE = "200";
	public static String EMAIL_CONFIRMATION_RESPONSE ="Your email address is wrong but  dont panic your order has been placed thankyou";
	public static String ORDER_PLACED = "Your order has been placed, thankyou for shopping with us.";
	public static final String GENERIC_ERROR_MSG = "Oh no, our systems are getting upgraded !!";
	public static final String INVALID_REQUEST_PARAM = "Invalid Request Parameters";
	
	public static String SOMETHING_WENT_WRONG_WHILE_UPDATING_ORDER_STATUS  = "Oh no ! Something went wrong while placing order !!";
	public static final String MOBILE_ALREADY_EXIST_WITH_DIFF_ACC = "Sorry But this phone number already Exist with different email ";
	public static final String ADDRESS_IS_NOT_SELECTED = "Looks like address is not selected";
	
	public static final String CREDIT_VALIDATION = "Looks like credit is not available in your  wallet ..!";
	
	public static final String OUT_OF_STOCK  = "Oh no ! Looks like few item(s) are out of stock .. !";
	public static final String MOBILE_REGEX = "^(((\\+)?91(|-)?)|0)?\\d{5}(|-)?\\d{5}$";
	
	private static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
	
	public static final Pattern MOBILE_NUMBER_PATTERN = Pattern.compile(MOBILE_REGEX);
	
	public static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);


}
