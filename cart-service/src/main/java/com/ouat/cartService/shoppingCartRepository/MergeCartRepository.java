package com.ouat.cartService.shoppingCartRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ouat.cartService.shoppingCartDTOs.CartItemDTO;
import com.ouat.cartService.shoppingCartHelper.CartServiceSql;
import com.ouat.cartService.shoppingCartService.MergeCartService;

@Repository
public class MergeCartRepository {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(MergeCartService.class);

	@Autowired
	JdbcTemplate jdbcTemplate;
	public void upsertInCartItemTable( List< CartItemDTO>cartItemDtoList, Integer customerId) {
		  int alreadyAvailableItemOfCustomerInDb = getNumberOfItemAlreadyExistInCartItemTable(customerId);
          if((cartItemDtoList.size()+alreadyAvailableItemOfCustomerInDb)>10) {
        	  deleteExceedingRow(customerId);
          }
		  
		  int size =  jdbcTemplate.batchUpdate(CartServiceSql.UPSERT_IN_CART_ITEM, new BatchPreparedStatementSetter() {
	         public void setValues(PreparedStatement ps, int i) throws SQLException {
	            ps.setInt(1,customerId);
	            ps.setString(2, cartItemDtoList.get(i).getSku());
	            ps.setInt(3, cartItemDtoList.get(i).getQty());
	            ps.setDouble(4, cartItemDtoList.get(i).getRetailPrice());
	            ps.setDouble(5,cartItemDtoList.get(i).getRegularPrice());
	            ps.setString(6,cartItemDtoList.get(i).getPlatform());
	            ps.setString(7,cartItemDtoList.get(i).getLocation());
	            ps.setString(8,cartItemDtoList.get(i).getUtmCampaign());
	            ps.setString(9,cartItemDtoList.get(i).getUtmSource());
	            ps.setString(10,cartItemDtoList.get(i).getUtmMedium());
	            ps.setInt(11,cartItemDtoList.get(i).getPlpId() == null ? 0 : cartItemDtoList.get(i).getPlpId());
	            ps.setInt(12, cartItemDtoList.get(i).getQty());
	            ps.setDouble(13, cartItemDtoList.get(i).getRetailPrice());
	            ps.setDouble(14,cartItemDtoList.get(i).getRegularPrice());
	            ps.setString(15,cartItemDtoList.get(i).getPlatform());
	            ps.setString(16,cartItemDtoList.get(i).getLocation());
	            ps.setString(17,cartItemDtoList.get(i).getUtmCampaign());
	            ps.setString(18,cartItemDtoList.get(i).getUtmSource());
	            ps.setString(19,cartItemDtoList.get(i).getUtmMedium());
	            ps.setInt(20,cartItemDtoList.get(i).getPlpId() == null ? 0 : cartItemDtoList.get(i).getPlpId());
	         }
	         public int getBatchSize() {
	            return cartItemDtoList.size();
	         }
	      }).length;
	      if(size != cartItemDtoList.size()) {
	    	  LOGGER.error("upsert of cart item is not succussefully done while merging the cart item of customerId:{}",customerId); 
	      }else {
	    	  LOGGER.error("merging of item is succussefully done while merging the cart item of customerId:{}",customerId); 
	      }
		return;
	}
	public int getNumberOfItemAlreadyExistInCartItemTable(Integer cuustomerId) {
		return jdbcTemplate.queryForObject(CartServiceSql.NUMBER_OF_CART_ITEM_EXIST, Integer.class,cuustomerId);
	}

	public int deleteExceedingRow(Integer customerId) {
		return jdbcTemplate.update(CartServiceSql.DELETE_ALL_CART_ITEM,customerId);
	}
	
	public List<Integer> getIDsListToDelete(Integer customerId, Integer limit){
		jdbcTemplate.queryForList("",customerId,limit );
		return null;
	}
	
	public static <T, U> List<U> convertIntListToStringList(List<T> listOfInteger, Function<T, U> function){
        return listOfInteger.stream().map(function).collect(Collectors.toList());
    }
}
