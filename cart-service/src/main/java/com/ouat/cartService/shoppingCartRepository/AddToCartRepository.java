package com.ouat.cartService.shoppingCartRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ouat.cartService.shoppingCartDTOs.SkuAndQty;
import com.ouat.cartService.shoppingCartHelper.CartServiceSql;
import com.ouat.cartService.shoppingCartRequest.AddToCartRequest;
import com.ouat.cartService.shoppingCartResponse.ProductItemPrice;
 
@Repository
public class  AddToCartRepository {
	@Autowired
	JdbcTemplate jdbcTemplate;
	public Integer totalCartItemInDB(Integer  customerDetailId) {
		return jdbcTemplate.queryForObject( CartServiceSql.NUMBER_OF_ITEM , Integer.class,customerDetailId );	 
	}
	public Map<String, Integer> getSkuAndQty(Integer customerId) {
		Map<String, Integer> data = new HashMap<String, Integer>();
		jdbcTemplate.query("SELECT  sku, quantity FROM cart.cart_item where customer_id = ?",new RowMapper<SkuAndQty>(){  
		    @Override  
		    public SkuAndQty mapRow(ResultSet rs, int rownumber) throws SQLException {  
		    	SkuAndQty e=new SkuAndQty();  
		    	data.put(rs.getString("sku"), rs.getInt("quantity"));
		        return e;  
		    }  
		    }, customerId);  
		
		return data;
	}
	public int getNumberOfItemAlreadyExistInCart(int customerId) {
		return jdbcTemplate.queryForObject("select count(1) from cart.cart_item where customer_id = ?;", Integer.class, customerId);
	}
	public int upsertItemInCartItemTable(AddToCartRequest addToCartRequest, ProductItemPrice productItemPrices) {
		return jdbcTemplate.update(CartServiceSql.UPSERT_IN_CART_ITEM ,
				addToCartRequest.getCustomerId(), 
				addToCartRequest.getSku(),
				addToCartRequest.getQuantity(),
				productItemPrices.getRetailPrice(),
				productItemPrices.getRegularPrice(),
				//date
				addToCartRequest.getPlatform().getValue(),
				addToCartRequest.getLocation().toString(),
				addToCartRequest.getUtmCampaign(),
				addToCartRequest.getUtmSource(),
				addToCartRequest.getUtmMedium(),
				addToCartRequest.getPlpId(),
				//for update the item
				addToCartRequest.getQuantity(),
				productItemPrices.getRetailPrice(),
				productItemPrices.getRegularPrice(),
				addToCartRequest.getPlatform().getValue(),
				addToCartRequest.getLocation().toString(),
				addToCartRequest.getUtmCampaign(),
				addToCartRequest.getUtmSource(),
				addToCartRequest.getUtmMedium(),
				addToCartRequest.getPlpId()
				);
	}
	
}


	 
