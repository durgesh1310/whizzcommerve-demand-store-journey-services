package com.ouat.cartService.shoppingCartHelper;

public class CommonConstant {
	
    public static final String  CACHE_CART_SERVICE = "CACHE_CART_SERVICE:";
    
    public static final String ANON = "ANON:";
    
    public static final String IDEN="IDEN:";
    
    public static final String SHOW_SHOPPING_CART = ":SHOW_SHOPPING_CART";
    
    public static final String ADD_TO_CART = ":ADD_TO_CART";
    
    public static final  Integer VALID_NUMBER_OF_ITEM = 10;
    
    public static final Integer VALID_NUMBER_OF_QTY = 10;
	
	public static final String FAILURE_STATUS_CODE = "500";
	public static final String SUCCESS_STATUS_CODE = "200";
	public static final String CREATED = "201";
	public static final String UNAUTHORIZED = "401";
	public static final String UPDATED = "204";
	
	public static final String USER_NOT_EXIST = "User Does not exist";
	public static final String ITEM_NOT_EXIST = "Item Does not exist";
	public static final String CART_LIMIT_EXCEED = "Cart limit exceeded, please move some products into wishlist";
	
	public static final String ITEM_EXIST = "Item exist check futher";
	
	public static final String CART_IS_EMPTY = "cart is empty";
	
	public static final String SOMETHING_WENT_WRONG = "Oh no ! Something went wrong !!";
	
	public static final String ITEM_ADDED_SUCCESSFULLY = "Item added successfully";
	
	public static final String ITEM_UPDATED_SUCCESSFULLY = "Item quantity updated successfully";
	
	public static final String QUANTITY_UPDATED = "Quantity is updated";
	
	public static final String ITEM_DELETED_SUCCESSFULLY = "Item deleted successfully";
	
	public static final String CHECK_OUT_FOR_YOUR_FAVROUT_CART_ITEM = "Check out for your Favorite";
	
	public static final String SHIPPING_CHARGES = "Free shipping on order above %s";
	
	public static final Boolean SUCCESS_FLAG = true;
	public static final Boolean FAILED_FLAG = false;
	
	public static final String ITEM_OUT_OF_STOCK = "Item out of Stock";

	public static final String ITEM_NOT_PRESENT_ERROR = "Error !";

	public static final String GENERIC_ERROR_MSG = "Oh no, our systems are getting upgraded !!";
	public static final String INVALID_REQUEST_PARAM = "Invalid Request Parameters";
	public static final String ANOTHER_USER_TOKEN = "Oh no ! Seems there is some issue on your device, please relogin !!";
	public static final String INVALID_REQUEST = "Invalid Request";

	public static final String SHIPPING_CARGES = "SHIPPING_CHARGES:";
	
	public static final Double DEFAULT_SHIPPING_CARGES = 100.00;

	public static final String WANT_TO_DELETE_CART_ITEM = "Do you really want to delete the cart item";
	
	public static final String QTY_EXCEEDED = "Quantiy limit exceeded you can not order more then 10 item of this product";
}
