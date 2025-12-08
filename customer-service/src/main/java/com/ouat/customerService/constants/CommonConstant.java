package com.ouat.customerService.constants;

import java.util.regex.Pattern;

public class CommonConstant {

	public static final String GENERIC_ERROR_MSG = "Oh no, our systems are getting upgraded !!";
	public static final String INVALID_REQUEST_PARAM = "Invalid Request Parameters";
	
	public static final String SOMETHING_WENT_WRONG = "Oh no, something went wrong !!";
	
	public static final String FAILURE_STATUS_CODE = "500";
	public static final String USER_BLOCKED_STATUS_CODE = "1213";
	public static final String CUSTOMER_NOT_REGISTERED_CODE = "1212";
	public static final String SUCCESS_STATUS_CODE = "200";
	public static final String CREATED = "201";
	public static final String UNAUTHORIZED = "401";
	
	public static final String ACCOUNT_ALREADY_EXISTS_WITH_MOBILE = "Looks like we already have an account with %s mobile number.";
	
	public static final String ACCOUNT_ALREADY_EXISTS_WITH_EMAIL = "Looks like we already have an account with %s email.";
	
	public static final String ACCOUNT_ALREADY_EXISTS_WITH_EMAIL_USE_DIFFERENT_EMAIL = "Looks like we already have an account with %s email, please use new email id.";
	
	public static final String MOBILE_REGEX = "^(((\\+)?91(|-)?)|0)?\\d{5}(|-)?\\d{5}$";
	
	private static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
	
	public static final Pattern MOBILE_NUMBER_PATTERN = Pattern.compile(MOBILE_REGEX);
	
	public static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
	
	public static final String INVALID_EMAIL = "Looks like invalid email id is provided.";
	
	public static final String INVALID_MOBILE = "Looks like invalid mobile number is provided.";
	
	public static final String CUSTOMER_ADDED = "Hurrah! Your profile's complete.";
	
	public static final String CUSTOMER_ADDRESS_CREATED = "Hurrah! Your address added.";
	
	public static final String CUSTOMER_ADDRESS_UPDATED = "Hurrah! Your address updated.";
	
	public static final String EMAIL_NOT_REGISTERED = "Looks like email id is not registered.";
	
	public static final String MOBILE_NOT_REGISTERED = "Looks like mobile number is not registered.";
	
	public static final String CUSTOMER_NOT_REGISTERED = "Looks like customer is blocked or not registered with us.";
	
	public static final String INVALID_ADDRESS = "Looks like invalid address passed.";
	
	public static final String CUSTOMER_ADDRESS_EXISTS_REGISTERED = "Looks like this address is already added.";
	
	public static final String PLZ_ADD_ADDRESS = "For smooth shipping please add address.";
	
	public static final String INVALID_PARAM_FOR_LOGIN = "Please enter valid email or mobile.";
	
	public static final Integer OTP_LENGTH = 6;
	
	public static final String FROM_EMAIL = "noreply@mail.taggd.com";
	
	public static final String FROM_NAME = "taggd";
	
	public static final String SUBJECT = "Your OTP to Login";
	
	public static final String MESSAGE_BODY = "Hello, your OTP to log in is %s , validity of the same is 2 minutes.";
	
	public static final String OTP_ON_EMAIL = "We have sent an OTP on your email %s";
	
	public static final String OTP_ON_SMS = "We have sent an OTP on your mobile %s";
	
	public static final String RESENT_OTP_ON_EMAIL = "Looks like your OTP was expired, no worries  we have re sent new OTP on your email %s ";
	
	public static final String RESENT_OTP_ON_SMS = "Looks like your OTP was expired, no worries  we have re sent new OTP on your mobile %s ";
	
	public static final String USER_LOGIN_SUCCESS = "Hurrah! Login successful, enjoy shoppping !";
	
	public static final Long TTL_FOR_OTP = 122L;
	
	public static final String OTP_COUNTER_KEY = "SIGNINID_COUNTER:";
	
	public static final String SIGNIN_KEY = "SIGNIN:";
	
	public static final String TEMP_BLOCK = "Oh no ! multiple invalid attempts to sign in, your account is temporarily blocked, please try after 30 mintues.";
	
	public static final String ANOTHER_USER_TOKEN = "Oh no ! Seems there is some issue on your device, please relogin !!";
	
	public static final String CREDIT_HISTORY_ADDED = "Credit history added for customer"; 
	
	public static final String NOT_REGISTERED_WITH_EMAIL = "Customer not registered with this email";
	
	public static final String NOT_REGISTERED_WITH_MOBILE = "Customer not registered with this mobile";
	
	public static final String USER_BLOCKED = "Looks like you're blocked, please contact customer care.";
	
	public static final String VERIFICATION_SUCESS = "Hurrah! Verification successful, enjoy shoppping !";
	
	public static final String CUSTOMER_ALREADY_AVAILED = "Oh no ! Right now you're not eligible for welcome credits.";
	
	public static final String INVALID_GUEST_USER = "Invalid guest user, please sign up";
	public static final String ADD_USER_FOR_GUEST_CHECKOUT = "user successfully build and address added successfully";
	public static final String MOBILE_ALREADY_EXIST_WITH_DIFF_EMAIL = "Given mobile number belongs to different email";
	public static final String EMAIL_ALREADY_EXIST_WITH_DIFF_MOBILE = "Given email belongs to different mobile";

	public static final String THIS_MOBILE_BELONGS_TO_SOMEONE_ELSE = "Oh no ! %s can not be linked to %s, please login";
	
	public static final String EMAIL_EXISTS_WITHOUT_MOBILE = "Oh no ! you can not place order as guest, please use email to login";
	
	public static final String EMAIL_INVLAID = "This email belongs to some other customer, use new email";
	
	public static final String NO_RANK ="Participate in the Big Fashion Festival by placing an order and stand a chance to WIN iPhone 13 and iWatch Series 7.";
	
 	
	
}
