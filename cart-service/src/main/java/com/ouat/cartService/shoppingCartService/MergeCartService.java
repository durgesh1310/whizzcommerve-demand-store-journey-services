package com.ouat.cartService.shoppingCartService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ouat.cartService.shoppingCartDTOs.CartItemDTO;
import com.ouat.cartService.shoppingCartDTOs.ShippingChargesDetailDTO;
import com.ouat.cartService.shoppingCartHelper.CartItem;
import com.ouat.cartService.shoppingCartHelper.CommonConstant;
import com.ouat.cartService.shoppingCartRedisHelper.AddToCartDetailRedis;
import com.ouat.cartService.shoppingCartRedisHelper.RedisUtil;
import com.ouat.cartService.shoppingCartRepository.MergeCartRepository;
import com.ouat.cartService.shoppingCartResponse.DemandStoreAPIResponse;
import com.ouat.cartService.shoppingCartResponse.DemandStoreAPIResponseHelper;

@Service
public class MergeCartService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(MergeCartService.class);
	
	@Autowired
	MergeCartRepository mergeCartRepository;
	
	@Autowired
	DemandStoreAPIResponse demandStoreAPIResponse;

	@Autowired
	DemandStoreAPIResponseHelper demandStoreAPIResponseHelper;
	
	@Autowired
	RedisUtil<AddToCartDetailRedis> redisUtilCartItem;
	
	@Autowired
	RedisUtil<ShippingChargesDetailDTO>redisUtilShippingChargesDetail;
 
	public void mergeCart(String uuid, Integer customerId) {
	     
	    AddToCartDetailRedis shoppingCartDetailRedis =  redisUtilCartItem.getValue(CommonConstant.CACHE_CART_SERVICE + CommonConstant.ANON + uuid + CommonConstant.ADD_TO_CART);
	    if(shoppingCartDetailRedis==null ||(shoppingCartDetailRedis!=null && (shoppingCartDetailRedis.getCartItemsMap()==null || shoppingCartDetailRedis.getCartItemsMap().isEmpty()))){
	    	LOGGER.info("shopping cart item found empty while merging the cart of customer with uuid : {}", uuid);
	    	return;
		} else {
			mergeCartRepository.upsertInCartItemTable(buildCartItemDto(shoppingCartDetailRedis), customerId);
			redisUtilCartItem.delete(
					CommonConstant.CACHE_CART_SERVICE + CommonConstant.ANON + uuid + CommonConstant.ADD_TO_CART);
			redisUtilCartItem.delete(
					CommonConstant.CACHE_CART_SERVICE + CommonConstant.ANON + uuid + CommonConstant.SHOW_SHOPPING_CART);
			
			redisUtilCartItem.delete(
					CommonConstant.CACHE_CART_SERVICE + CommonConstant.IDEN + customerId + CommonConstant.ADD_TO_CART);
			redisUtilCartItem.delete(
					CommonConstant.CACHE_CART_SERVICE + CommonConstant.IDEN + customerId + CommonConstant.SHOW_SHOPPING_CART);
			return;
		}
	}
	public List<CartItemDTO> buildCartItemDto(AddToCartDetailRedis shoppingCartDetailRedis) {
		List<CartItemDTO>cartItemDtoList = new ArrayList<>();
		Map<String, CartItem> cartItemsMap = shoppingCartDetailRedis.getCartItemsMap();
		for(Entry<String, CartItem> skuAndCartItemIterator : cartItemsMap.entrySet()){
			CartItemDTO cartItemDTO = new CartItemDTO();
			cartItemDTO.setCustomerId(shoppingCartDetailRedis.getCustomerId());
			cartItemDTO.setSku(skuAndCartItemIterator.getKey() );
			cartItemDTO.setRetailPrice(skuAndCartItemIterator.getValue().getRetailPrice() );
			cartItemDTO.setRegularPrice(skuAndCartItemIterator.getValue().getRegularPrice());
			cartItemDTO.setQty(skuAndCartItemIterator.getValue().getQuantity());
			cartItemDTO.setDate(null);
			cartItemDTO.setLocation(shoppingCartDetailRedis.getLocation().toString() );
			cartItemDTO.setPlatform(shoppingCartDetailRedis.getPlatform().toString());
			cartItemDTO.setUtmCampaign(null);
			cartItemDTO.setUtmSource(null);
			cartItemDTO.setUtmMedium(null);
			cartItemDTO.setPlpId(skuAndCartItemIterator.getValue().getPlpId());
			cartItemDtoList.add(cartItemDTO);
        }
		
		 return cartItemDtoList;
	}

}
