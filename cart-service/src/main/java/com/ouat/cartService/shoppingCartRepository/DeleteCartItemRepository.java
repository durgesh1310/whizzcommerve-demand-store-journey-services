package com.ouat.cartService.shoppingCartRepository;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ouat.cartService.shoppingCartHelper.CartItem;
import com.ouat.cartService.shoppingCartHelper.CartServiceSql;
import com.ouat.cartService.shoppingCartHelper.CommonConstant;
import com.ouat.cartService.shoppingCartRedisHelper.AddToCartDetailRedis;
import com.ouat.cartService.shoppingCartRedisHelper.RedisUtil;
import com.ouat.cartService.shoppingCartResponse.DemandStoreAPIResponseHelper;

@Repository
public class DeleteCartItemRepository {
 
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	RedisUtil<AddToCartDetailRedis> redisUtilCartItem;
    
	@Autowired
	DemandStoreAPIResponseHelper demandStoreAPIResponseHelper;
	
	public boolean deleteItemFromDB(Integer customerId, String sku) {
		 Integer count = jdbcTemplate.update(CartServiceSql.DELETE_CART_ITEM, customerId, sku);
		  if(count>0) {
			  return true;
		  }
		  else return false;
	 }
	 
	 public boolean deleteItemFromRedis(String customerUniqueId, String sku) {
		 
		 AddToCartDetailRedis addToCartRedisDTO  = redisUtilCartItem.getValue(customerUniqueId + CommonConstant.ADD_TO_CART);
		 
		 if(addToCartRedisDTO==null) {
			 return false;
		 }
		 else if((addToCartRedisDTO.getCartItemsMap()==null ||addToCartRedisDTO.getCartItemsMap().isEmpty() || addToCartRedisDTO.getCartItemsMap().size()==1) && addToCartRedisDTO!=null) {
 	    	 //delete the whole key from the redis
			 redisUtilCartItem.delete(customerUniqueId + CommonConstant.ADD_TO_CART);
			 redisUtilCartItem.delete(customerUniqueId + CommonConstant.SHOW_SHOPPING_CART);
			 return true;

 		 }else {
			 
			 Map<String , CartItem> cartItemMap =  addToCartRedisDTO.getCartItemsMap();
			 cartItemMap.remove(sku);
			 redisUtilCartItem.putValue(customerUniqueId + CommonConstant.ADD_TO_CART, addToCartRedisDTO);
			 return  true;
		 }
		
	 }
	 
	
}
 
