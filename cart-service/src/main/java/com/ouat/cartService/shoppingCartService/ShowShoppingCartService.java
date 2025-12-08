package com.ouat.cartService.shoppingCartService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.ouat.cartService.clients.CatalogueServiceClient;
import com.ouat.cartService.clients.ProductItemAndProductAttributeDetailResponse;
import com.ouat.cartService.clients.ProductItemDetail;
import com.ouat.cartService.exception.BusinessProcessException;
import com.ouat.cartService.shoppingCartDTOs.ShippingChargesDetailDTO;
import com.ouat.cartService.shoppingCartHelper.CartItem;
import com.ouat.cartService.shoppingCartHelper.CartServicePriceSummary;
import com.ouat.cartService.shoppingCartHelper.CommonConstant;
import com.ouat.cartService.shoppingCartHelper.Platform;
import com.ouat.cartService.shoppingCartRedisHelper.AddToCartDetailRedis;
import com.ouat.cartService.shoppingCartRedisHelper.RedisUtil;
import com.ouat.cartService.shoppingCartRepository.ShowShoppingCartRepository;
import com.ouat.cartService.shoppingCartRequest.ShowShoppingCartRequest;
import com.ouat.cartService.shoppingCartResponse.DemandStoreAPIResponse;
import com.ouat.cartService.shoppingCartResponse.DemandStoreAPIResponseHelper;
import com.ouat.cartService.shoppingCartResponse.MessageDetail;
import com.ouat.cartService.shoppingCartResponse.MessageType;
import com.ouat.cartService.shoppingCartResponse.ProductAttribute;
import com.ouat.cartService.shoppingCartResponse.ProductAttributeEddAndSize;
import com.ouat.cartService.shoppingCartResponse.ShoppingCartPriceSummaryResponse;
import com.ouat.cartService.shoppingCartResponse.ShowShoppingCart;
import com.ouat.cartService.shoppingCartResponse.ShowShoppingCartItemResponse;
import com.ouat.cartService.shoppingCartService.util.DateTimeUtil;

@Service
public class ShowShoppingCartService {
	
	public Logger LOGGER=LoggerFactory.getLogger(ShowShoppingCartService.class);
	
	@Autowired
	ShowShoppingCartRepository showShoppingCartRepository;

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
	
	public DemandStoreAPIResponse showShoppingCart(ShowShoppingCartRequest  showShoppingcartRequest) throws BusinessProcessException{
		 List<MessageDetail> messageDetailList = new ArrayList<>();
		 if(null != showShoppingcartRequest.getCustomerUuid() && !showShoppingcartRequest.getCustomerUuid().isEmpty() && (null == showShoppingcartRequest.getCustomerId() || showShoppingcartRequest.getCustomerId()==0)){
			    AddToCartDetailRedis shoppingCartDetailRedis =  redisUtilCartItem.getValue(CommonConstant.CACHE_CART_SERVICE + CommonConstant.ANON + showShoppingcartRequest.getCustomerUuid() + CommonConstant.ADD_TO_CART);
				LOGGER.info("successfully hit for the anon user  to redis with reponse : {}", shoppingCartDetailRedis);
			    if(shoppingCartDetailRedis==null ||(shoppingCartDetailRedis!=null && (shoppingCartDetailRedis.getCartItemsMap()==null || shoppingCartDetailRedis.getCartItemsMap().isEmpty()))){
						   demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.CART_IS_EMPTY , messageDetailList);
					       return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
				  }else {
					  LOGGER.info("building showshopping cart with map : {}",shoppingCartDetailRedis.getCartItemsMap());
					  return buildShoppingCartResponse(shoppingCartDetailRedis.getCartItemsMap(),showShoppingcartRequest);
				  }
		  }
		 else if(showShoppingcartRequest.getCustomerId() != 0 && showShoppingcartRequest.getCustomerId()!= null) {
			  AddToCartDetailRedis shoppingCartDetailRedis = redisUtilCartItem.getValue(CommonConstant.CACHE_CART_SERVICE + CommonConstant.IDEN + showShoppingcartRequest.getCustomerId() + CommonConstant.ADD_TO_CART);
			  LOGGER.info("successfully hit the redis for iden user with reponse :{}", shoppingCartDetailRedis);
			  if(shoppingCartDetailRedis==null) {
				  Map<String, CartItem> skuCartItemDetail =  showShoppingCartRepository.getCartItem(showShoppingcartRequest.getCustomerId());
				  LOGGER.info("successfully hit the db for iden user with reponse :{}", skuCartItemDetail);

				  if(skuCartItemDetail.isEmpty()) {
					  demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.CART_IS_EMPTY, messageDetailList);
				      return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
				  }else {
					  
					  LOGGER.info("building showshopping skuCartItemDetail : {}",skuCartItemDetail);
					  return  buildShoppingCartResponse(skuCartItemDetail ,showShoppingcartRequest);
 				  } 
			  } else {
				  LOGGER.info("building showshopping cart with map : {}",shoppingCartDetailRedis.getCartItemsMap());
				     return  buildShoppingCartResponse(shoppingCartDetailRedis.getCartItemsMap(), showShoppingcartRequest);
 			  }
		 }
		 demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.CART_IS_EMPTY, messageDetailList);
	     return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
    }
	
	private DemandStoreAPIResponse buildShoppingCartResponse(Map<String, CartItem> RedisskuCartItemMap, ShowShoppingCartRequest showShoppingcartRequest) throws BusinessProcessException {
		LOGGER.info("calling the downstream Api call to fetch the product Item detail for skus : {}  with quantity", RedisskuCartItemMap.keySet().stream().collect(Collectors.toList()));
		StopWatch watch = new StopWatch();
		watch.start();
		ProductItemAndProductAttributeDetailResponse productItemAndProductAttributeDetailResponse = catalogueServiceClient.getProducItemDetailList(RedisskuCartItemMap.keySet().stream().collect(Collectors.toList()));
		if(null == productItemAndProductAttributeDetailResponse) {
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
		}
		watch.stop();
		long result = (long) watch.getTotalTimeSeconds();
		LOGGER.info("internal service Api called with total time in second : {} with response : {} ", result,productItemAndProductAttributeDetailResponse );
		List<ShowShoppingCartItemResponse> showShoppingCartItemList = new ArrayList<>();
		CartServicePriceSummary cartItemAndPriceSummaryResponse = buildCartItemDetail(RedisskuCartItemMap, productItemAndProductAttributeDetailResponse.getProductItemDetailMap(), productItemAndProductAttributeDetailResponse.getProductAttribute(), showShoppingCartItemList);
		ShoppingCartPriceSummaryResponse showShoppingCartSummary = buildShowShoppingCartPriceSummary(cartItemAndPriceSummaryResponse.getTotalRegularPrice(), cartItemAndPriceSummaryResponse.getTotalRetailPrice(),showShoppingcartRequest.getPlatform());
 		ShowShoppingCart showShoppingCart = new ShowShoppingCart();
		List<MessageDetail> messageDetailList = new ArrayList<>();
		showShoppingCart.setCartItemSummary(showShoppingCartSummary);
		showShoppingCart.setShowShoppingCartData(showShoppingCartItemList);
		showShoppingCart.setTotalItem(showShoppingCartItemList.size());
		String showShoppingCartRedisKey;
		if(null != showShoppingcartRequest.getCustomerId() && showShoppingcartRequest.getCustomerId()!=0) {
		       showShoppingCartRedisKey = CommonConstant.CACHE_CART_SERVICE + CommonConstant.IDEN + showShoppingcartRequest.getCustomerId() + CommonConstant.SHOW_SHOPPING_CART ;
		}else {
			   showShoppingCartRedisKey = CommonConstant.CACHE_CART_SERVICE + CommonConstant.ANON + showShoppingcartRequest.getCustomerUuid() + CommonConstant.SHOW_SHOPPING_CART ;
		}
		redisUtilShowShoppingCart.putValue(showShoppingCartRedisKey, showShoppingCart);
 		redisUtilCartItem.setExpire(showShoppingCartRedisKey,300, TimeUnit.SECONDS);  
 		demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO,CommonConstant.CHECK_OUT_FOR_YOUR_FAVROUT_CART_ITEM, messageDetailList);
		LOGGER.info("show shopping cart response successfully build with response : {}",showShoppingCart );
  		return demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE,  showShoppingCart);
	}
	
	CartServicePriceSummary buildCartItemDetail(Map<String, CartItem> redisskuCartItemMap, Map<String, ProductItemDetail> skuProductItemDetail, List<ProductAttribute> productAttributeList, List<ShowShoppingCartItemResponse> showShoppingCartItemList) {
		Double totalRegularPrice=0.0, totalRetailPrice=0.0;
	    CartServicePriceSummary cartServiceSummaryPrices = new CartServicePriceSummary();
 	    for(Entry<String, ProductItemDetail> skuAndProductItemDetailIterator : skuProductItemDetail.entrySet()){
		   MessageDetail messageDetail = new MessageDetail();
		   ShowShoppingCartItemResponse shoppingCartItemResponse = new ShowShoppingCartItemResponse();
		   shoppingCartItemResponse.setSku(skuAndProductItemDetailIterator.getKey());
		   shoppingCartItemResponse.setProductName(skuAndProductItemDetailIterator.getValue().getName());
		   ProductAttributeEddAndSize productAttributeEddSize = getProductAttributeSize(productAttributeList,skuAndProductItemDetailIterator.getKey());
		   shoppingCartItemResponse.setSize((!("".equals(productAttributeEddSize.getSize()))?productAttributeEddSize.getSize(): null));
		   shoppingCartItemResponse.setEdd((null!=productAttributeEddSize.getEdd())?productAttributeEddSize.getEdd():"0");
		   shoppingCartItemResponse.setDefaultImageUrl(skuAndProductItemDetailIterator.getValue().getDefualtImageUrl());
		   shoppingCartItemResponse.setProductId(skuAndProductItemDetailIterator.getValue().getProductId());
		   shoppingCartItemResponse.setCategory(skuAndProductItemDetailIterator.getValue().getCategory());
		   shoppingCartItemResponse.setSubCategory(skuAndProductItemDetailIterator.getValue().getSubCategory());
		   shoppingCartItemResponse.setProductType(skuAndProductItemDetailIterator.getValue().getProductType());
		   shoppingCartItemResponse.setPlpId(redisskuCartItemMap.get(skuAndProductItemDetailIterator.getValue().getSku()).getPlpId());
		   shoppingCartItemResponse.setQuantity(redisskuCartItemMap.get(skuAndProductItemDetailIterator.getValue().getSku()).getQuantity());
		   shoppingCartItemResponse.setRegularPrice(skuAndProductItemDetailIterator.getValue().getProductItemPrices().getRegularPrice()*(redisskuCartItemMap.get(skuAndProductItemDetailIterator.getValue().getSku()).getQuantity())); 
		   totalRegularPrice = totalRegularPrice + (skuAndProductItemDetailIterator.getValue().getProductItemPrices().getRegularPrice())*(redisskuCartItemMap.get(skuAndProductItemDetailIterator.getValue().getSku()).getQuantity());
		   totalRetailPrice = totalRetailPrice + setRetailAndSalePrice( redisskuCartItemMap, skuAndProductItemDetailIterator, messageDetail, shoppingCartItemResponse);
			if (skuAndProductItemDetailIterator.getValue().getInventory() <= 0) {
				messageDetail.setMsgType(MessageType.OUT_OF_STOCK);
				messageDetail.setMsgText("Oh no ! this item is out of stock.");
				shoppingCartItemResponse.setMessageDetail(messageDetail);
			}
			else if (skuAndProductItemDetailIterator.getValue().getInventory() < redisskuCartItemMap
					.get(skuAndProductItemDetailIterator.getValue().getSku()).getQuantity()) {
				messageDetail.setMsgType(MessageType.LOW_INVENTORY);
				messageDetail.setMsgText("Only " + skuAndProductItemDetailIterator.getValue().getInventory() + " quantity is available.");
				shoppingCartItemResponse.setMessageDetail(messageDetail);
			}
			
			if(null != skuAndProductItemDetailIterator.getValue().getIsSkuActive() && ! skuAndProductItemDetailIterator.getValue().getIsSkuActive()) {
				messageDetail.setMsgType(MessageType.ERROR);
				messageDetail.setMsgText("This item is not available for now.");
				shoppingCartItemResponse.setMessageDetail(messageDetail);
			}
            showShoppingCartItemList.add(shoppingCartItemResponse);     
		 }
	   cartServiceSummaryPrices.setTotalRegularPrice(totalRegularPrice);
 	   cartServiceSummaryPrices.setTotalRetailPrice(totalRetailPrice);
 	   return cartServiceSummaryPrices;
	}
	private Double setRetailAndSalePrice( Map<String, CartItem> redisSkuCartItemMap,  Entry<String, ProductItemDetail> skuAndProductItemDetail, MessageDetail messageDetail, ShowShoppingCartItemResponse showShoppingCartItemResponse) {
 		Double filledRetailPrices=0.00;
 		
		//when sale is ON!!
		if (skuAndProductItemDetail.getValue().getProductItemPrices().getSalePrice() != null
				&& skuAndProductItemDetail.getValue().getSaleEndDate() != null
				&& skuAndProductItemDetail.getValue().getSaleStartDate() != null
				&& skuAndProductItemDetail.getValue().getSaleEndDate().after(new Date())
				&& skuAndProductItemDetail.getValue().getSaleStartDate().before(new Date())) {
			filledRetailPrices = (skuAndProductItemDetail.getValue().getProductItemPrices().getSalePrice())
					* (redisSkuCartItemMap.get(skuAndProductItemDetail.getValue().getSku()).getQuantity());
			showShoppingCartItemResponse.setRetailPrice(filledRetailPrices);
			/*
			if (skuAndProductItemDetail.getValue().getProductItemPrices().getSalePrice() < redisSkuCartItemMap
					.get(skuAndProductItemDetail.getValue().getSku()).getRetailPrice()) {
				messageDetail.setMsgType(MessageType.PRICE_DROP);
				messageDetail
						.setMsgText("Wow this is now available on lower price");
				showShoppingCartItemResponse.setMessageDetail(messageDetail);
				showShoppingCartItemResponse.setPriceChange(filledRetailPrices);

			} */
		}
		//if item price has been increase
		else if (skuAndProductItemDetail.getValue().getProductItemPrices().getRetailPrice() > redisSkuCartItemMap
				.get(skuAndProductItemDetail.getValue().getSku()).getRetailPrice()) {
			showShoppingCartItemResponse
					.setRetailPrice(skuAndProductItemDetail.getValue().getProductItemPrices().getRetailPrice());
			filledRetailPrices = (skuAndProductItemDetail.getValue().getProductItemPrices().getRetailPrice())
					* (redisSkuCartItemMap.get(skuAndProductItemDetail.getValue().getSku()).getQuantity());
		}
		/*
	    //when item price has been decrease
		else if (skuAndProductItemDetail.getValue().getProductItemPrices().getRetailPrice() < redisSkuCartItemMap
				.get(skuAndProductItemDetail.getValue().getSku()).getRetailPrice()) {
			messageDetail.setMsgType(MessageType.PRICE_DROP);
			messageDetail.setMsgText("Wow this is now available on lower price");
			filledRetailPrices = (skuAndProductItemDetail.getValue().getProductItemPrices().getRetailPrice())
					* (redisSkuCartItemMap.get(skuAndProductItemDetail.getValue().getSku()).getQuantity());
			showShoppingCartItemResponse.setPriceChange(filledRetailPrices);
			showShoppingCartItemResponse.setMessageDetail(messageDetail);
		}
		*/
		else {

			filledRetailPrices = (skuAndProductItemDetail.getValue().getProductItemPrices().getRetailPrice())
					* (redisSkuCartItemMap.get(skuAndProductItemDetail.getValue().getSku()).getQuantity());

		}
   	    showShoppingCartItemResponse.setRetailPrice(filledRetailPrices);
   	    //item discount = regular - retail
		showShoppingCartItemResponse.setItemDiscount(getItemDiscount(redisSkuCartItemMap, skuAndProductItemDetail, filledRetailPrices));
  		return filledRetailPrices;
	}
     private Double getItemDiscount(Map<String, CartItem> redisSkuCartItemMap,
            Entry<String, ProductItemDetail> skuAndProductItemDetail, Double filledRetailPrices) {
        Double itemDiscount = (skuAndProductItemDetail.getValue().getProductItemPrices().getRegularPrice())*(redisSkuCartItemMap.get(skuAndProductItemDetail.getValue().getSku()).getQuantity())-filledRetailPrices;
        if(Math.round(itemDiscount)==0)
            return null;
        return itemDiscount;
    }
    
	public ShoppingCartPriceSummaryResponse buildShowShoppingCartPriceSummary( Double totalRegularPrice, Double totalRetailPrice, Platform platform) {
		
		   Double shippingCharge = (Double) getShippingCharges(totalRetailPrice, platform.getValue()).getData();
		   Double promotionDiscount=0.0;
		   ShoppingCartPriceSummaryResponse shoppingCartPriceSummary = new ShoppingCartPriceSummaryResponse();
		   shoppingCartPriceSummary.setTotalRegularPrice(totalRegularPrice);
 		   shoppingCartPriceSummary.setTotalRetailPrice(totalRetailPrice);
		   shoppingCartPriceSummary.setTotalDiscountByApplyPromo(promotionDiscount);
		   shoppingCartPriceSummary.setTotalplatformDiscount(totalRegularPrice-totalRetailPrice);
		   shoppingCartPriceSummary.setShippingCharge(shippingCharge);
		   shoppingCartPriceSummary.setOrderTotal(
				   (double)Math.round(totalRetailPrice + shippingCharge-promotionDiscount));
		   return shoppingCartPriceSummary;
	}

    /**
     * <blockquote> if its wednesday then allow free shipping means shipping charges are zero (Business Requirement)
     * </blockquote>
     */
	public DemandStoreAPIResponse getShippingCharges(Double totalRetailPrice, String platform) {
		Double shippingCharge = 0.0;
		   String key = CommonConstant.SHIPPING_CARGES + platform;
		   ShippingChargesDetailDTO shippingChargesDetailDTO = new ShippingChargesDetailDTO();
		   shippingChargesDetailDTO = redisUtilShippingChargesDetail.getValue(key);
		   LOGGER.info("shipping charges is DTO is successfully fetched from the redis with shipping charges detail : {}",  shippingChargesDetailDTO);
			if (null == shippingChargesDetailDTO || shippingChargesDetailDTO.getCartValue() == 0
					|| shippingChargesDetailDTO.getShippingCharges() == 0) {
				   LOGGER.info("calling the get shipping charges function form repository layer with platform : {} and total retail price: {}",  platform, totalRetailPrice);
			   shippingChargesDetailDTO = showShoppingCartRepository.getshippingCharge(platform);
			   LOGGER.info("shipping charges detail successfullly recieved with shippoing detail :  ", shippingChargesDetailDTO);
               if(shippingChargesDetailDTO == null) {
            	   shippingCharge = CommonConstant.DEFAULT_SHIPPING_CARGES;
               }else {
    			   LOGGER.info("putting shipping charge data into redis with key : {} and shipping charges detail : {}  ",key, shippingChargesDetailDTO);
            	   redisUtilShippingChargesDetail.putValue(key, shippingChargesDetailDTO);
    			   LOGGER.info("setting expire time of shipping charges on redis with numer of days : {} and key: {}  ",10,key);
    			   redisUtilShippingChargesDetail.setExpire(key, 10, TimeUnit.DAYS);   
    			   if(shippingChargesDetailDTO.getCartValue() > totalRetailPrice) {
    				   shippingCharge = shippingChargesDetailDTO.getShippingCharges();
    			   }else {
    				   shippingCharge = 0.0;
    			   }
               }
		   }else {
			   if(shippingChargesDetailDTO.getCartValue() > totalRetailPrice) {
				   shippingCharge = shippingChargesDetailDTO.getShippingCharges();
			   }else {
				   shippingCharge = 0.0;
			   }
		   }
			
			LOGGER.info("shipping charges successfully get with shipping charges : {} with retail price : {} on platform", shippingCharge,totalRetailPrice, platform);
			 List<MessageDetail> messageDetailList = new ArrayList<>();
			 demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, String.format(CommonConstant.SHIPPING_CHARGES, shippingChargesDetailDTO.getCartValue().intValue()), messageDetailList);
			 return demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, shippingCharge);
	}
	
	
	public DemandStoreAPIResponse getV1ShippingCharges(Double totalRetailPrice, String platform) {
		Double shippingCharge = 0.0;
		   String key = CommonConstant.SHIPPING_CARGES + platform;
		   ShippingChargesDetailDTO shippingChargesDetailDTO = new ShippingChargesDetailDTO();
		   shippingChargesDetailDTO = redisUtilShippingChargesDetail.getValue(key);
		   LOGGER.info("shipping charges is DTO is successfully fetched from the redis with shipping charges detail : {}",  shippingChargesDetailDTO);
			if (null == shippingChargesDetailDTO || shippingChargesDetailDTO.getCartValue() == 0
					|| shippingChargesDetailDTO.getShippingCharges() == 0) {
				   LOGGER.info("calling the get shipping charges function form repository layer with platform : {} and total retail price: {}",  platform, totalRetailPrice);
			   shippingChargesDetailDTO = showShoppingCartRepository.getshippingCharge(platform);
			   LOGGER.info("shipping charges detail successfullly recieved with shippoing detail :  ", shippingChargesDetailDTO);
               if(shippingChargesDetailDTO == null) {
            	   shippingCharge = CommonConstant.DEFAULT_SHIPPING_CARGES;
               }else {
    			   LOGGER.info("putting shipping charge data into redis with key : {} and shipping charges detail : {}  ",key, shippingChargesDetailDTO);
            	   redisUtilShippingChargesDetail.putValue(key, shippingChargesDetailDTO);
    			   LOGGER.info("setting expire time of shipping charges on redis with numer of days : {} and key: {}  ",10,key);
    			   redisUtilShippingChargesDetail.setExpire(key, 10, TimeUnit.DAYS);   
    			   if(shippingChargesDetailDTO.getCartValue() > totalRetailPrice) {
    				   shippingCharge = shippingChargesDetailDTO.getShippingCharges();
    			   }else {
    				   shippingCharge = 0.0;
    			   }
               }
		   }else {
			   if(shippingChargesDetailDTO.getCartValue() > totalRetailPrice) {
				   shippingCharge = shippingChargesDetailDTO.getShippingCharges();
			   }else {
				   shippingCharge = 0.0;
			   }
		   }
			 Integer toShippingOn = (int) (shippingChargesDetailDTO.getCartValue().intValue() - totalRetailPrice);
			 LOGGER.info("shipping charges successfully get with shipping charges : {} with retail price : {} on platform", shippingCharge,totalRetailPrice, platform);
			 List<MessageDetail> messageDetailList = null;
			 return demandStoreAPIResponseHelper.apiResponse(messageDetailList, CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, 
					 shippingCharge );
	}
	public ProductAttributeEddAndSize getProductAttributeSize(List<ProductAttribute>productAttributeList, String sku) {
		ProductAttributeEddAndSize productAttributeEddSize = new ProductAttributeEddAndSize();
		for(ProductAttribute it : productAttributeList) {	
			  if(it.getAttributeName().equals("Size") && it.getSku().equals(sku) && it.getIsProductLevel()==0) {
				  if(null != it.getAttributeValue() && "".equals(it.getAttributeValue().trim())) {
					  productAttributeEddSize.setSize(null);
				  }
				  productAttributeEddSize.setSize(it.getAttributeValue());
 			  }
			  if(it.getAttributeName().equals("Edd") && it.getSku().equals(sku) && it.getIsProductLevel()==1) {
				  if(null != it.getAttributeValue() && "".equals(it.getAttributeValue().trim())) {
					  productAttributeEddSize.setEdd("1");
				  }
				  productAttributeEddSize.setEdd(it.getAttributeValue());;
			  }
		  }
		LOGGER.info("returning all the attribute of products with response ", productAttributeEddSize);
		return productAttributeEddSize;
 	   }
	
 	}