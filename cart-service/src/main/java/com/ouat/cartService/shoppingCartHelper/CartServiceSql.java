package com.ouat.cartService.shoppingCartHelper;

public class CartServiceSql {
 
	public static final String NUMBER_OF_ITEM = "SELECT count(1) from promotions.customer_promo_history_table where customer_detail_id= ? ";
 
    public static final String GET_SHIPPING_CHARGES = "SELECT * FROM cart.shipping_charges where platform = ?;";
 
    public static final String UPSERT_IN_CART_ITEM = "INSERT into cart.cart_item (customer_id, sku, quantity, retail_price, regular_price, date, platform, location, utm_campaign, utm_source, utm_medium, plp_id) values (?,?,?,?,?, CONVERT_TZ(now(), '+00:00', '+05:30'), ? , ? , ?, ? , ?,?) ON DUPLICATE KEY UPDATE quantity=quantity+?, retail_price=?, regular_price=?,date = CONVERT_TZ(now(), '+00:00', '+05:30'), platform= ?, location=?,utm_source = ?, utm_campaign = ?, utm_medium = ?, plp_id = ? ;";  
    
	public static final String DELETE_CART_ITEM = "DELETE FROM cart.cart_item WHERE customer_id = ? AND sku = ?";
	
	public static final String UPDATE_CART_ITEM = "UPDATE cart.cart_item set date = now() WHERE customer_id = ? AND sku = ?";
	
	public static final String GET_CART_ITEM_DETAILS = "SELECT  sku, quantity, retail_price, regular_price, date, plp_id  FROM cart.cart_item where customer_id = ?";
    
	public static final String NUMBER_OF_CART_ITEM_EXIST = "SELECT count(*) FROM cart.cart_item where customer_id = ?;";
	
	public static final String DELETE_LISTED_IDS = "DELETE FROM cart.cart_item WHERE id IN(%s);";
	
	public static final String DELETE_ALL_CART_ITEM = "DELETE FROM cart.cart_item WHERE customer_id = ?";
    		
}