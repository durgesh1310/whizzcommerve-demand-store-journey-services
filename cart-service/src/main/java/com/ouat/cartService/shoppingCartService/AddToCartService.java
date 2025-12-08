package com.ouat.cartService.shoppingCartService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.ouat.cartService.clients.CatalogueServiceClient;
import com.ouat.cartService.clients.ProductItemAndProductAttributeDetailResponse;
import com.ouat.cartService.exception.BusinessProcessException;
import com.ouat.cartService.shoppingCartHelper.CartItem;
import com.ouat.cartService.shoppingCartHelper.CommonConstant;
import com.ouat.cartService.shoppingCartRedisHelper.AddToCartDetailRedis;
import com.ouat.cartService.shoppingCartRedisHelper.RedisUtil;
import com.ouat.cartService.shoppingCartRepository.AddToCartRepository;
import com.ouat.cartService.shoppingCartRepository.DeleteCartItemRepository;
import com.ouat.cartService.shoppingCartRequest.AddToCartRequest;
import com.ouat.cartService.shoppingCartResponse.DemandStoreAPIResponse;
import com.ouat.cartService.shoppingCartResponse.DemandStoreAPIResponseHelper;
import com.ouat.cartService.shoppingCartResponse.MessageDetail;
import com.ouat.cartService.shoppingCartResponse.MessageType;
import com.ouat.cartService.shoppingCartResponse.ProductItemPrice;

@Service
public class AddToCartService {
	public Logger LOGGER = LoggerFactory.getLogger(AddToCartService.class);

    @Autowired
    RedisUtil<AddToCartDetailRedis> redisUtilCartItem;
    
    @Autowired
    AddToCartRepository  addToCartRepository;
    
    @Autowired
	DemandStoreAPIResponseHelper demandStoreAPIResponseHelper;
    
    @Autowired
    DeleteCartItemRepository deletecartItemRepo;
   
    @Autowired
    CatalogueServiceClient catalogueServiceClient;
	public DemandStoreAPIResponse upsertCart(AddToCartRequest addToCartRequest) throws BusinessProcessException {
		List<MessageDetail> messageDetailList = new ArrayList<>();
		Date currentdate = new Date();
		List<String> skuList = new ArrayList<>();
		skuList.add(addToCartRequest.getSku());
		LOGGER.info("calling the internal service Api call to fetch the product Item detail for sku : {} ",addToCartRequest.getSku());
		StopWatch watch = new StopWatch();
		watch.start();
		ProductItemAndProductAttributeDetailResponse productItemAndProductAttributeDetailResponse = catalogueServiceClient.getProducItemDetailList(skuList);
		if(null == productItemAndProductAttributeDetailResponse ||  productItemAndProductAttributeDetailResponse.getProductItemDetailMap()==null|| 
				productItemAndProductAttributeDetailResponse.getProductItemDetailMap().isEmpty() || 
				productItemAndProductAttributeDetailResponse.getProductItemDetailMap().get(addToCartRequest.getSku()).getProductItemPrices()==null) {
			LOGGER.info("API to catalogue service failed  or user are trying to add an Item which do not exist in our database!! "+ skuList );
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
		}
		watch.stop();
		long result = (long) watch.getTotalTimeSeconds();
		LOGGER.info("internal service Api called with total time in second : {} with respnse : {} ", result, productItemAndProductAttributeDetailResponse);
		if (0 == productItemAndProductAttributeDetailResponse.getProductItemDetailMap().get(addToCartRequest.getSku())
				.getInventory()) {
			LOGGER.warn("Item out of stock : {} ", addToCartRequest.getCustomerId());
			demandStoreAPIResponseHelper.setMessageDetailList(MessageType.WARNING, CommonConstant.ITEM_OUT_OF_STOCK,
					messageDetailList);
			return demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG,
					CommonConstant.SUCCESS_STATUS_CODE, null);
		}
		ProductItemPrice productItemPrices = productItemAndProductAttributeDetailResponse.getProductItemDetailMap().get(addToCartRequest.getSku()).getProductItemPrices();
		if (null != addToCartRequest.getCustomerUuid() && (null == addToCartRequest.getCustomerId() || addToCartRequest.getCustomerId() == 0)) {
			return upsertItemToRedisServer(addToCartRequest,CommonConstant.CACHE_CART_SERVICE + CommonConstant.ANON + addToCartRequest.getCustomerUuid() + CommonConstant.ADD_TO_CART, currentdate,productItemPrices);
		} else if (addToCartRequest.getCustomerId() != 0 && addToCartRequest.getCustomerId() != null) {
			return upsertItemInDatabase(addToCartRequest, currentdate, productItemPrices);
		} else {
			demandStoreAPIResponseHelper.setMessageDetailList(MessageType.ERROR, CommonConstant.SOMETHING_WENT_WRONG,messageDetailList);
			return demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
		}
	}
	private DemandStoreAPIResponse upsertItemInDatabase(AddToCartRequest addToCartrequest, Date currentdate, ProductItemPrice productItemPrices) throws BusinessProcessException {
		    List<MessageDetail> messageDetailList = new ArrayList<>();
		 	LOGGER.warn("fething show shopping cart detail from  db with customer id : {} ", addToCartrequest.getCustomerId());
		       Map<String, Integer> skuAndQtyMap = addToCartRepository.getSkuAndQty(addToCartrequest.getCustomerId());
			 	LOGGER.warn("shopping cart detail from  db with customer id : {} successfully fethced with response  with qty : {}", addToCartrequest.getCustomerId(), skuAndQtyMap, skuAndQtyMap.size());
		       if(!skuAndQtyMap.isEmpty() && skuAndQtyMap!=null) {
		    	   if (skuAndQtyMap.get(addToCartrequest.getSku())!=null && skuAndQtyMap.get(addToCartrequest.getSku())<=1 && addToCartrequest.getQuantity()<0) {
						//TODO : delete the item 
		    			if(null != addToCartrequest.getCustomerUuid() && !addToCartrequest.getCustomerUuid().isEmpty() ) {
							deletecartItemRepo.deleteItemFromRedis(CommonConstant.CACHE_CART_SERVICE + CommonConstant.ANON + addToCartrequest.getCustomerUuid(), addToCartrequest.getSku());
							 LOGGER.info("sku : {} deleted from redis of customeruuId : {}",addToCartrequest.getSku(), addToCartrequest.getCustomerId() );
                        }
			        	if(deletecartItemRepo.deleteItemFromRedis(CommonConstant.CACHE_CART_SERVICE + CommonConstant.IDEN + addToCartrequest.getCustomerId(), addToCartrequest.getSku())) {
							 LOGGER.info("sku : {} deleted from redis of customerId : {}",addToCartrequest.getSku(), addToCartrequest.getCustomerId() );
  						 }
		    		    deletecartItemRepo.deleteItemFromDB(addToCartrequest.getCustomerId(),addToCartrequest.getSku());
						demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.ITEM_DELETED_SUCCESSFULLY,messageDetailList);
						return demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
		    	   }
		       else if(skuAndQtyMap.size()>CommonConstant.VALID_NUMBER_OF_ITEM){
					 	LOGGER.warn("the maximum limit to add the item is exceed for the customerIdOrCustomerUuid : {} ", addToCartrequest.getCustomerId());
				        demandStoreAPIResponseHelper.setMessageDetailList(MessageType.WARNING, CommonConstant.CART_LIMIT_EXCEED, messageDetailList);
				        return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
				    }
				    else if(skuAndQtyMap.get(addToCartrequest.getSku())!=null  && ((int)skuAndQtyMap.get(addToCartrequest.getSku()) + addToCartrequest.getQuantity())> CommonConstant.VALID_NUMBER_OF_QTY) {
					 	LOGGER.warn("the maximumto update the qty is exceed for the sku : {} of customerIdOrCustomerUuid : {} ",addToCartrequest.getSku(), addToCartrequest.getCustomerId());
					 	demandStoreAPIResponseHelper.setMessageDetailList(MessageType.WARNING, CommonConstant.QTY_EXCEEDED, messageDetailList);
				        return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
				    }		     
		    }
		if(null != skuAndQtyMap &&  !skuAndQtyMap.isEmpty() && skuAndQtyMap.containsKey(addToCartrequest.getSku())) {
		    demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.ITEM_UPDATED_SUCCESSFULLY, messageDetailList);
		}else {
			demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.ITEM_ADDED_SUCCESSFULLY, messageDetailList);
		}
		if(addToCartRepository.upsertItemInCartItemTable( addToCartrequest, productItemPrices)>0) {
			return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
		}else {
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
			}
		/*addToCartRepository.upsertItemInCartItemTable( addToCartrequest, productItemPrices);
	       LOGGER.info("cart item upserted successfully in redis for customerId : {} for sku : {} with quantity : {}", addToCartrequest.getCustomerId(), addToCartrequest.getSku(), addToCartrequest.getQuantity());
		  return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
		  return upsertItemToRedisServer(addToCartrequest,CommonConstant.CACHE_CART_SERVICE + CommonConstant.IDEN + addToCartrequest.getCustomerId().toString() + CommonConstant.ADD_TO_CART, currentdate, productItemPrices); */
	}
	private DemandStoreAPIResponse upsertItemToRedisServer(AddToCartRequest addToCartrequest,String anonOrIdenCartKey,Date currentDate, ProductItemPrice retailAndRegularPrice) {
	 	LOGGER.warn("fething show shopping cart detail from redis from key : {} ", anonOrIdenCartKey);
		AddToCartDetailRedis shoppingCartDetailRedis = redisUtilCartItem.getValue(anonOrIdenCartKey);
 		List<MessageDetail> messageDetailList = new ArrayList<>();
		if (shoppingCartDetailRedis != null && shoppingCartDetailRedis.getCartItemsMap()!=null) {
		 	LOGGER.warn("fething show shopping cart detail from redis from key : {} , with detail : {}  with total item in the cart : {}", anonOrIdenCartKey, shoppingCartDetailRedis);
			if (shoppingCartDetailRedis.getCartItemsMap().size() > CommonConstant.VALID_NUMBER_OF_ITEM) {
			 	LOGGER.warn("the maximum limit to add the item is exceed for the customerIdOrCustomerUuid : {} ", anonOrIdenCartKey);
				demandStoreAPIResponseHelper.setMessageDetailList(MessageType.WARNING, CommonConstant.CART_LIMIT_EXCEED, messageDetailList);
				return demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG, CommonConstant.SUCCESS_STATUS_CODE, null);
			}
			else if(shoppingCartDetailRedis.getCartItemsMap().get(addToCartrequest.getSku())!=null && shoppingCartDetailRedis.getCartItemsMap().get(addToCartrequest.getSku()).getQuantity() > CommonConstant.VALID_NUMBER_OF_QTY){
			 	LOGGER.warn("the maximum limit to add the item is exceed for the customerIdOrCustomerUuid : {} ", anonOrIdenCartKey);
			 	demandStoreAPIResponseHelper.setMessageDetailList(MessageType.WARNING, CommonConstant.QTY_EXCEEDED, messageDetailList);
		        return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
			}else {
				return updateItemToRedisServer(addToCartrequest, currentDate, shoppingCartDetailRedis, retailAndRegularPrice, anonOrIdenCartKey);
			}
		} else {
			return addItemToRedisServer(addToCartrequest, currentDate, retailAndRegularPrice, anonOrIdenCartKey);
		}
	}
	private DemandStoreAPIResponse addItemToRedisServer(AddToCartRequest addToCartRequest, Date currentdate, ProductItemPrice retailAndRegularPrice, String anonOrIdenCartKey) {
		 AddToCartDetailRedis shoppingCartDetailRedis = buildCustomerDetail(addToCartRequest);
		 CartItem cartItems =  buildCartItemDetail(addToCartRequest, currentdate,  retailAndRegularPrice);
		 Map<String, CartItem> map = new HashMap<String, CartItem>();
	 		List<MessageDetail> messageDetailList = new ArrayList<>();
		 if(map.size()> CommonConstant.VALID_NUMBER_OF_ITEM) {
			 LOGGER.warn("the maximum limit to add the item is exceed for the customerIdOrCustomerUuid : {} ", anonOrIdenCartKey);
			 	demandStoreAPIResponseHelper.setMessageDetailList(MessageType.WARNING, CommonConstant.QTY_EXCEEDED, messageDetailList);
		        return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
		 }
		 map.put(addToCartRequest.getSku() , cartItems);
		 shoppingCartDetailRedis.setCartItemsMap(map);
		 redisUtilCartItem.putValue(anonOrIdenCartKey  , shoppingCartDetailRedis);
	 	 redisUtilCartItem.setExpire(anonOrIdenCartKey, 2592000, TimeUnit.SECONDS);  
 		 LOGGER.info("cart item added successfully in redis for customerIdOrCustomerUuid : {}", anonOrIdenCartKey);
		 demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.ITEM_ADDED_SUCCESSFULLY, messageDetailList);
	     return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE,  null);
	}
	private DemandStoreAPIResponse updateItemToRedisServer(AddToCartRequest addToCartRequest, Date currentDate,
			AddToCartDetailRedis shoppingCartDetail, ProductItemPrice retailAndRegularPrice,
			String anonOrIdenCartKey) {
 		List<MessageDetail> messageDetailList = new ArrayList<>();
		Map<String, CartItem> cartItemDetail = shoppingCartDetail.getCartItemsMap();
		CartItem cartItem = cartItemDetail.get(addToCartRequest.getSku());
		if (cartItem != null) {
			if(cartItem.getQuantity()<=1 && addToCartRequest.getQuantity()<0) {
				//TODO : delete the item 
				if(null != addToCartRequest.getCustomerUuid() && !addToCartRequest.getCustomerUuid().isEmpty() ) {
					deletecartItemRepo.deleteItemFromRedis(CommonConstant.CACHE_CART_SERVICE + CommonConstant.ANON + addToCartRequest.getCustomerUuid(), addToCartRequest.getSku());
	        	}
	        	deletecartItemRepo.deleteItemFromRedis(CommonConstant.CACHE_CART_SERVICE + CommonConstant.IDEN + addToCartRequest.getCustomerId(), addToCartRequest.getSku());
					 LOGGER.info("sku : {} deleted from redis of customeruuId : {}",addToCartRequest.getSku(), addToCartRequest.getCustomerId() );
					 demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.ITEM_DELETED_SUCCESSFULLY, messageDetailList);
				     return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
				 
				/*deletecartItemRepo.deleteItemFromRedis(CommonConstant.CACHE_CART_SERVICE + CommonConstant.ANON +  addToCartRequest.getCustomerUuid(), addToCartRequest.getSku());
				demandStoreAPIResponseHelper.setMessageDetailList(MessageType.DIALOG, CommonConstant.WANT_TO_DELETE_CART_ITEM,messageDetailList);
				return demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
				*/
			}else if ((cartItem.getQuantity() + addToCartRequest.getQuantity())>CommonConstant.VALID_NUMBER_OF_QTY) {
				demandStoreAPIResponseHelper.setMessageDetailList(MessageType.WARNING, CommonConstant.QTY_EXCEEDED,messageDetailList);
				return demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
			}
			cartItem.setQuantity(cartItem.getQuantity() + addToCartRequest.getQuantity());
			cartItemDetail.put(addToCartRequest.getSku(), cartItem);
			redisUtilCartItem.putValue(anonOrIdenCartKey, shoppingCartDetail);
	 		redisUtilCartItem.setExpire(anonOrIdenCartKey, 2592000, TimeUnit.SECONDS);  
			LOGGER.info("cart item updated successfully in redis for customerIdOrCustomerUuid : {}", anonOrIdenCartKey);
			demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.ITEM_UPDATED_SUCCESSFULLY,messageDetailList);
			return demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
		} else {
			cartItemDetail.put(addToCartRequest.getSku(), buildCartItemDetail(addToCartRequest, currentDate, retailAndRegularPrice));
			redisUtilCartItem.putValue(anonOrIdenCartKey, shoppingCartDetail);
	 		redisUtilCartItem.setExpire(anonOrIdenCartKey, 2592000, TimeUnit.SECONDS);  
			LOGGER.info("cart item updated successfully in redis for customerIdOrCustomerUuid : {}", anonOrIdenCartKey);
			demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.ITEM_ADDED_SUCCESSFULLY,messageDetailList);
			return demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
		}
	} 
	private AddToCartDetailRedis buildCustomerDetail(AddToCartRequest addToCartRequest) {
		 AddToCartDetailRedis shoppingCartDetailRedis = new  AddToCartDetailRedis();
		 shoppingCartDetailRedis.setCustomerId(addToCartRequest.getCustomerId() );
		 shoppingCartDetailRedis.setCustomerUuid(addToCartRequest.getCustomerUuid());
		 shoppingCartDetailRedis.setPlatform(addToCartRequest.getPlatform());
		 shoppingCartDetailRedis.setLocation(addToCartRequest.getLocation());
		 return shoppingCartDetailRedis;
	}
	
	private CartItem  buildCartItemDetail(AddToCartRequest addToCartRequest, Date currentDate, ProductItemPrice retailAndRegularPrice) {
		CartItem cartItems  = new CartItem();
		cartItems.setSku(addToCartRequest.getSku());
		cartItems.setQuantity(addToCartRequest.getQuantity());
		cartItems.setDate(currentDate);
		cartItems.setRegularPrice(retailAndRegularPrice.getRegularPrice());
		cartItems.setRetailPrice(retailAndRegularPrice.getRetailPrice());
		cartItems.setPlpId(addToCartRequest.getPlpId());
		return cartItems;
	}
}

