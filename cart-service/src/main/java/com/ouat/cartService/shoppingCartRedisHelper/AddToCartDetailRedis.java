package com.ouat.cartService.shoppingCartRedisHelper;

import java.io.Serializable;
import java.util.Map;
import java.util.SortedSet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouat.cartService.shoppingCartHelper.CartItem;
import com.ouat.cartService.shoppingCartHelper.Location;
import com.ouat.cartService.shoppingCartHelper.Platform;

public class AddToCartDetailRedis implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1798957019319958765L;

	/**
	 * 
	 */

	String promoCode;

	Integer customerId;

	String CustomerUuid;

	Platform platform;

	Location location;

	Map<String, CartItem> cartItemsMap = null;
 

	public Map<String, CartItem> getCartItemsMap() {
		return cartItemsMap;
	}

	public void setCartItemsMap(Map<String, CartItem> cartItemsMap) {
		this.cartItemsMap = cartItemsMap;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerUuid() {
		return CustomerUuid;
	}

	public void setCustomerUuid(String customerUuid) {
		CustomerUuid = customerUuid;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public AddToCartDetailRedis(String promoCode, Integer customerId, String customerUuid, Platform platform,
			Location location, SortedSet<CartItem> cartItems) {
		super();

		this.promoCode = promoCode;
		this.customerId = customerId;
		CustomerUuid = customerUuid;
		this.platform = platform;
		this.location = location;

	}

	public AddToCartDetailRedis() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		String serialized = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			serialized = objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException jpe) {
			jpe.printStackTrace();
		}
		return serialized;
	}
}
