package com.ouat.cartService.shoppingCartRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ouat.cartService.shoppingCartDTOs.ShippingChargesDetailDTO;
import com.ouat.cartService.shoppingCartHelper.CartItem;
import com.ouat.cartService.shoppingCartHelper.CartServiceSql;

@Repository
public class ShowShoppingCartRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;
	RowMapper<ShippingChargesDetailDTO> shippingChargesVORowMapper = (rs, rowNum) -> {
		return new ShippingChargesDetailDTO(rs.getDouble("shipping_charge"), rs.getDouble("cart_value"));
	};
	public ShippingChargesDetailDTO getshippingCharge(String platform) {
		ShippingChargesDetailDTO shippingChargesDetailDTO = null;
		try {
			shippingChargesDetailDTO = jdbcTemplate.queryForObject(CartServiceSql.GET_SHIPPING_CHARGES,
					shippingChargesVORowMapper, platform);
		} catch (Exception e) {
		}
		return shippingChargesDetailDTO;
	}
	RowMapper<CartItem> cartDetailVORowMapper = (rs, rowNum) -> {
		return new CartItem(rs.getString("sku"), rs.getInt("quantity"), rs.getDouble("retail_price"),
				rs.getDouble("regular_price"),rs.getDate("date"), rs.getInt("plp_id"));
	};
	
	
	public Map<String, CartItem> getCartItem(int customerId) {
		List<CartItem> cartItemsdetail = new ArrayList<>();
		try {
			cartItemsdetail = jdbcTemplate.query(CartServiceSql.GET_CART_ITEM_DETAILS, cartDetailVORowMapper, customerId);
		} catch (Exception e) {
		}
		Map<String, CartItem> cartItemDetails = new HashMap<String, CartItem>();
		for (CartItem it : cartItemsdetail) {
			cartItemDetails.put(it.getSku(), it);
		}
		return cartItemDetails;
	}

	public int deleteCartItem(Integer customerId) {
		return jdbcTemplate.update(CartServiceSql.DELETE_ALL_CART_ITEM, customerId);
	}

}
