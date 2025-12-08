package com.ouat.orderService.constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CommonConstant {

	public static final String GENERIC_ERROR_MSG = "Oh no, our systems are getting upgraded !!";
	public static final String INVALID_REQUEST_PARAM = "Invalid Request Parameters";
	
	public static final String SOMETHING_WENT_WRONG = "Oh no, something went wrong !!";
	
	public static final String SOMETHING_WENT_WRONG_WITH_ORDER_CANCEL = "Oh no, something went wrong, looks like order is not applicable for cancellation !!";
	
	public static final String SOMETHING_WENT_WRONG_WITH_ORDER_RETURN = "Oh no, something went wrong, looks like order is not applicable for return !!";
	
	public static final String SOMETHING_WENT_WRONG_WITH_ORDER_EXCHANGE = "Oh no, something went wrong, looks like order is not applicable for exchnage !!";
	
	public static final String FAILURE_STATUS_CODE = "500";
	public static final String SUCCESS_STATUS_CODE = "200";
	public static final String CREATED = "201";
	public static final String UNAUTHORIZED = "401";
	
	public static final String NO_REASON_FOUND = "Looks like something is broker, please email to customer care !";
	
	public static final String USER_ACCOUNT_MISMATCHED = "Oh no, order does not belong to this account !!";
	
	public static final String INVALID_SKU = "Looks like you have choosen invalid order item.";
	
	public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static final String ORDER_ALREADY_CANCELLED = "Looks like your order is already cancelled or it can't be cancelled !!";
	
	public static final String ORDER_ALREADY_RETURNED = "Looks like your order is already returned or it can't be returned !!";
	
	public static final String ORDER_ALREADY_EXCHANGE = "Looks like your order is already returned/exchanged or it can't be exchanged !!";
	
	public static final String ORDER_CANCELLED = "We are sad, you cancelled your order !! ";
	
	public static final String ORDER_RETURNED = "We are sad, you returned your order !! ";
	
	public static final String EXCHANGED_PLACED = "We have registered your exchange !!";
	
	public static final String ORDER_CANCEL_SMS = "Hi %s, Your order no. %s has been cancelled. We hope to see you soon. In case of further queries, contact us at customercare@taggd.com -taggd"; // name, order id,

	public static final String EXCHANGE_NOT_AVAILABLE = "Exchange is not available for this order.";
	public static final String OUT_OF_STOCK  = "Oh no ! Looks like few item(s) are out of stock .. !";
	
	public static boolean SUCCESS_FLAG  = true;
	
	public static String ORDER_PLACED = "Your exchange order has been placed, thank you for shopping with us..";

}
