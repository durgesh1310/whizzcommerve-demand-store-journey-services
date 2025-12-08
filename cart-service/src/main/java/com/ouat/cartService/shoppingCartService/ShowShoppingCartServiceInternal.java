package com.ouat.cartService.shoppingCartService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.ouat.cartService.clients.CatalogueServiceClient;
import com.ouat.cartService.clients.ProductItemAndProductAttributeDetailResponse;
import com.ouat.cartService.exception.BusinessProcessException;
import com.ouat.cartService.shoppingCartDTOs.ShippingChargesDetailDTO;
import com.ouat.cartService.shoppingCartHelper.CartItem;
import com.ouat.cartService.shoppingCartHelper.CartServicePriceSummary;
import com.ouat.cartService.shoppingCartHelper.CommonConstant;
import com.ouat.cartService.shoppingCartRedisHelper.AddToCartDetailRedis;
import com.ouat.cartService.shoppingCartRedisHelper.RedisUtil;
import com.ouat.cartService.shoppingCartRepository.ShowShoppingCartRepository;
import com.ouat.cartService.shoppingCartRequest.ShowShoppingCartRequest;
import com.ouat.cartService.shoppingCartResponse.DemandStoreAPIResponse;
import com.ouat.cartService.shoppingCartResponse.DemandStoreAPIResponseHelper;
import com.ouat.cartService.shoppingCartResponse.MessageDetail;
import com.ouat.cartService.shoppingCartResponse.MessageType;
import com.ouat.cartService.shoppingCartResponse.ShoppingCartPriceSummaryResponse;
import com.ouat.cartService.shoppingCartResponse.ShowShoppingCart;
import com.ouat.cartService.shoppingCartResponse.ShowShoppingCartItemResponse;

@Service
public class ShowShoppingCartServiceInternal {
	public Logger LOGGER=LoggerFactory.getLogger(ShowShoppingCartService.class);
	
	@Autowired
	ShowShoppingCartRepository showShoppingCartRepository;
    
	@Autowired
    ShowShoppingCartService showShoppingCartService;
	
	@Autowired
	DemandStoreAPIResponse demandStoreAPIResponse;

	@Autowired
	DemandStoreAPIResponseHelper demandStoreAPIResponseHelper;
	
	@Autowired
	CatalogueServiceClient catalogueServiceClient;

	@Autowired
	RedisUtil<AddToCartDetailRedis> redisUtilCartItem;
	
	@Autowired
	RedisUtil<ShippingChargesDetailDTO>redisUtilShippingChargesDetail;
	
	@Autowired
	RedisUtil<ShowShoppingCart>redisUtilShowShoppingCart;
	
	@SuppressWarnings("null")
	public DemandStoreAPIResponse showShoppingCartInternal(ShowShoppingCartRequest showShoppingcartRequest) throws BusinessProcessException{
		 List<MessageDetail> messageDetailList = new ArrayList<>();
		 ShowShoppingCart showShoppingCart = null;
		/* LOGGER.info("request : {}",showShoppingcartRequest.toString());
		 if(null != showShoppingcartRequest.getCustomerId() && showShoppingcartRequest.getCustomerId()!=0) {
			 showShoppingCart =	redisUtilShowShoppingCart.getValue(CommonConstant.CACHE_CART_SERVICE + CommonConstant.IDEN + showShoppingcartRequest.getCustomerId() + CommonConstant.SHOW_SHOPPING_CART);
		 }
		 else {
			 showShoppingCart =	redisUtilShowShoppingCart.getValue(CommonConstant.CACHE_CART_SERVICE + CommonConstant.ANON + showShoppingcartRequest.getCustomerUuid() + CommonConstant.SHOW_SHOPPING_CART);
		 }
		 
        if(null != showShoppingCart && showShoppingCart.getShowShoppingCartData()!=null) {
    		demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO,CommonConstant.CHECK_OUT_FOR_YOUR_FAVROUT_CART_ITEM, messageDetailList);
   		 LOGGER.info("response : {}",showShoppingCart.getShowShoppingCartData());
   		return demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, showShoppingCart.getShowShoppingCartData()); 
        }*/
        if(null != showShoppingcartRequest.getCustomerUuid() && !showShoppingcartRequest.getCustomerUuid().isEmpty() && (null == showShoppingcartRequest.getCustomerId() || showShoppingcartRequest.getCustomerId()==0)){
			    AddToCartDetailRedis shoppingCartDetailRedis =  redisUtilCartItem.getValue(CommonConstant.CACHE_CART_SERVICE + CommonConstant.ANON + showShoppingcartRequest.getCustomerUuid() + CommonConstant.ADD_TO_CART);
			    if(shoppingCartDetailRedis==null ||(shoppingCartDetailRedis!=null && (shoppingCartDetailRedis.getCartItemsMap()==null || shoppingCartDetailRedis.getCartItemsMap().isEmpty()))){
						   demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.CART_IS_EMPTY , messageDetailList);
					       return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
				  }else {
					  return buildShoppingCartResponseInternal(shoppingCartDetailRedis.getCartItemsMap(),showShoppingcartRequest);
				  }
		  }
        else if( null != showShoppingcartRequest.getCustomerId() && showShoppingcartRequest.getCustomerId() != 0) {
			  AddToCartDetailRedis shoppingCartDetailRedis = redisUtilCartItem.getValue(CommonConstant.CACHE_CART_SERVICE + CommonConstant.IDEN + showShoppingcartRequest.getCustomerId() + CommonConstant.ADD_TO_CART);
			  if(shoppingCartDetailRedis==null) {
				  Map<String, CartItem> skuCartItemDetail = showShoppingCartRepository.getCartItem(showShoppingcartRequest.getCustomerId());
				  if(skuCartItemDetail.isEmpty()) {
					  demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.CART_IS_EMPTY, messageDetailList);
				      return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
				  }else {
					  LOGGER.info("show shopping cart is successfully executed for customerid : {} or customerUuid : {}",showShoppingcartRequest.getCustomerId(), showShoppingcartRequest.getCustomerUuid() );
					  return buildShoppingCartResponseInternal(skuCartItemDetail ,showShoppingcartRequest);
				  } 
			  } else {
				  return buildShoppingCartResponseInternal(shoppingCartDetailRedis.getCartItemsMap(), showShoppingcartRequest);
			  }
		 }
        else {
       	 demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.CART_IS_EMPTY, messageDetailList);
   	     return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
        }
  }
	
	private DemandStoreAPIResponse buildShoppingCartResponseInternal(Map<String, CartItem> RedisskuCartItemMap, ShowShoppingCartRequest showShoppingcartRequest) throws BusinessProcessException {
		LOGGER.info("calling the downstream Api call to fetch the product Item detail for skus : {}  with quantity", RedisskuCartItemMap.keySet().stream().collect(Collectors.toList()));
		StopWatch watch = new StopWatch();
		watch.start();
		ProductItemAndProductAttributeDetailResponse productItemAndProductAttributeDetailResponse = catalogueServiceClient.getProducItemDetailList( RedisskuCartItemMap.keySet().stream().collect(Collectors.toList()));
		if(null == productItemAndProductAttributeDetailResponse) {
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
		}
		watch.stop();
		long result = (long) watch.getTotalTimeSeconds();
		LOGGER.info("internal service Api called with total time in second : {} ", result);
		List<ShowShoppingCartItemResponse> showShoppingCartItemList = new ArrayList<>();
		CartServicePriceSummary cartItemAndPriceSummaryResponse = showShoppingCartService.buildCartItemDetail(RedisskuCartItemMap, productItemAndProductAttributeDetailResponse.getProductItemDetailMap(), productItemAndProductAttributeDetailResponse.getProductAttribute(), showShoppingCartItemList);
		ShoppingCartPriceSummaryResponse showShoppingCartSummary = showShoppingCartService.buildShowShoppingCartPriceSummary(cartItemAndPriceSummaryResponse.getTotalRegularPrice(), cartItemAndPriceSummaryResponse.getTotalRetailPrice(),showShoppingcartRequest.getPlatform());
		ShowShoppingCart showShoppingCart = new ShowShoppingCart();
		List<MessageDetail> messageDetailList = new ArrayList<>();
		showShoppingCart.setCartItemSummary(showShoppingCartSummary);
		showShoppingCart.setShowShoppingCartData(showShoppingCartItemList);
		showShoppingCart.setTotalItem(showShoppingCartItemList.size());
		String showShoppingCartRedisKey = CommonConstant.CACHE_CART_SERVICE + CommonConstant.IDEN + showShoppingcartRequest.getCustomerId() + CommonConstant.SHOW_SHOPPING_CART ;
		redisUtilShowShoppingCart.putValue(showShoppingCartRedisKey, showShoppingCart);
 		redisUtilCartItem.setExpire(showShoppingCartRedisKey,300, TimeUnit.SECONDS);  	
 		demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO,CommonConstant.CHECK_OUT_FOR_YOUR_FAVROUT_CART_ITEM, messageDetailList);
		return demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, showShoppingCart.getShowShoppingCartData());
	}
	
	public DemandStoreAPIResponse deleteShoppingCart(Integer customerId, String deviceId) {
		String idenKeyShow = CommonConstant.CACHE_CART_SERVICE + CommonConstant.IDEN + customerId
				+ CommonConstant.SHOW_SHOPPING_CART;
		String idenKeyAdd = CommonConstant.CACHE_CART_SERVICE + CommonConstant.IDEN + customerId
				+ CommonConstant.ADD_TO_CART;
		String anonKeyAdd = CommonConstant.CACHE_CART_SERVICE + CommonConstant.ANON + deviceId
				+ CommonConstant.ADD_TO_CART;
		String anonKeyShow = CommonConstant.CACHE_CART_SERVICE + CommonConstant.ANON + deviceId
				+ CommonConstant.SHOW_SHOPPING_CART;
		redisUtilCartItem.delete(idenKeyAdd);
		redisUtilCartItem.delete(anonKeyAdd);
		redisUtilShowShoppingCart.delete(anonKeyShow);
		redisUtilShowShoppingCart.delete(idenKeyShow);
		showShoppingCartRepository.deleteCartItem(customerId);
		return new DemandStoreAPIResponse(true, Arrays.asList(new MessageDetail(MessageType.INFO, "Cart Item Clear", null, null)), CommonConstant.SUCCESS_STATUS_CODE, null);
	}
	public DemandStoreAPIResponse getNumberOfCartItem(ShowShoppingCartRequest  showShoppingcartRequest) {
		 List<MessageDetail> messageDetailList = new ArrayList<>();
		 if(null != showShoppingcartRequest.getCustomerUuid() && !showShoppingcartRequest.getCustomerUuid().isEmpty() && (null == showShoppingcartRequest.getCustomerId() || showShoppingcartRequest.getCustomerId()==0)){
			    AddToCartDetailRedis shoppingCartDetailRedis =  redisUtilCartItem.getValue(CommonConstant.CACHE_CART_SERVICE + CommonConstant.ANON + showShoppingcartRequest.getCustomerUuid() + CommonConstant.ADD_TO_CART);
			    if(shoppingCartDetailRedis==null ||(shoppingCartDetailRedis!=null && (shoppingCartDetailRedis.getCartItemsMap()==null || shoppingCartDetailRedis.getCartItemsMap().isEmpty()))){
						   demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.CART_IS_EMPTY , messageDetailList);
					       return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
				  }else {
					  LOGGER.info("show shopping cart is successfully executed for customerid : {} or customerUuid : {}",showShoppingcartRequest.getCustomerId(), showShoppingcartRequest.getCustomerUuid());
				       return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, shoppingCartDetailRedis.getCartItemsMap().size());
				  }
		  }
		 else if(showShoppingcartRequest.getCustomerId() != 0 && showShoppingcartRequest.getCustomerId()!= null) {
			  AddToCartDetailRedis shoppingCartDetailRedis = redisUtilCartItem.getValue(CommonConstant.CACHE_CART_SERVICE + CommonConstant.IDEN + showShoppingcartRequest.getCustomerId() + CommonConstant.ADD_TO_CART);
			  if(shoppingCartDetailRedis==null) {
				  Map<String, CartItem> skuCartItemDetail =  showShoppingCartRepository.getCartItem(showShoppingcartRequest.getCustomerId());
				  if(skuCartItemDetail.isEmpty()) {
					  demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.CART_IS_EMPTY, messageDetailList);
				      return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
				  }else {
					  LOGGER.info("show shopping cart is successfully executed for customerid : {} or customerUuid : {}",showShoppingcartRequest.getCustomerId(), showShoppingcartRequest.getCustomerUuid() );
 				       return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, skuCartItemDetail.size());
 				  } 
			  } else {
				     LOGGER.info("show shopping cart is successfully executed for customerid : {} or customerUuid : {}",showShoppingcartRequest.getCustomerId(), showShoppingcartRequest.getCustomerUuid() );
 				       return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, shoppingCartDetailRedis.getCartItemsMap().size());
 			  }
		 }
		 demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.CART_IS_EMPTY, messageDetailList);
	     return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
		 

	}
}
