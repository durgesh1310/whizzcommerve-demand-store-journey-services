package com.ouat.checkout.dto.redis;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouat.checkout.dto.ShowShoppingCartItemDto;
import com.ouat.checkout.response.Location;
import com.ouat.checkout.response.Platform;


 
public class ShowShoppingCartDetailsRedisDto {
    
    String promoCode;

    Integer customerId;

    String CustomerUuid = null;

    Platform platform;

    Location location;

    Map<String, ShowShoppingCartItemDto> cartItemsMap = null;
    


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



	public Map<String, ShowShoppingCartItemDto> getCartItemsMap() {
		return cartItemsMap;
	}



	public void setCartItemsMap(Map<String, ShowShoppingCartItemDto> cartItemsMap) {
		this.cartItemsMap = cartItemsMap;
	}



	public ShowShoppingCartDetailsRedisDto(String promoCode, Integer customerId, String customerUuid, Platform platform,
			Location location, Map<String, ShowShoppingCartItemDto> cartItemsMap) {
		super();
		this.promoCode = promoCode;
		this.customerId = customerId;
		CustomerUuid = customerUuid;
		this.platform = platform;
		this.location = location;
		this.cartItemsMap = cartItemsMap;
	}



	public ShowShoppingCartDetailsRedisDto() {
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
