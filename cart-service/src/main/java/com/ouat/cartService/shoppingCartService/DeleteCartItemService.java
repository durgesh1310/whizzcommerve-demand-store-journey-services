package com.ouat.cartService.shoppingCartService;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ouat.cartService.shoppingCartHelper.CommonConstant;
import com.ouat.cartService.shoppingCartRepository.DeleteCartItemRepository;
import com.ouat.cartService.shoppingCartRequest.DeleteCartItemRequest;
import com.ouat.cartService.shoppingCartResponse.DemandStoreAPIResponse;
import com.ouat.cartService.shoppingCartResponse.DemandStoreAPIResponseHelper;
import com.ouat.cartService.shoppingCartResponse.MessageDetail;
import com.ouat.cartService.shoppingCartResponse.MessageType;

@Service
public class DeleteCartItemService {
	public Logger LOGGER = LoggerFactory.getLogger(DeleteCartItemService.class);
	@Autowired
	DemandStoreAPIResponse demandStoreAPIResponse;
	
	@Autowired
	DemandStoreAPIResponseHelper demandStoreAPIResponseHelper;
	
	@Autowired
	DeleteCartItemRepository deleteCartItemRepository;
	
	public DemandStoreAPIResponse deleteItemFromCart(DeleteCartItemRequest deleteCartItemRequest) {
		 List<MessageDetail> messageDetailList = new ArrayList<>();
		 
		 if(null !=  deleteCartItemRequest.getCustomerUuid() && !deleteCartItemRequest.getCustomerUuid().isEmpty()  && (null == deleteCartItemRequest.getCustomerId() || deleteCartItemRequest.getCustomerId()==0)){
			
			
			 if(deleteCartItemRepository.deleteItemFromRedis(CommonConstant.CACHE_CART_SERVICE + CommonConstant.ANON + deleteCartItemRequest.getCustomerUuid(), deleteCartItemRequest.getSku())) {
				 
				 deleteCartItemRepository.deleteItemFromRedis(CommonConstant.SHOW_SHOPPING_CART + CommonConstant.ANON + deleteCartItemRequest.getCustomerUuid(), deleteCartItemRequest.getSku());
				 LOGGER.info("sku : {} deleted from redis of customeruuId : {}",deleteCartItemRequest.getSku(), deleteCartItemRequest.getCustomerUuid() );
				 demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.ITEM_DELETED_SUCCESSFULLY, messageDetailList);
			     return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
			 }else {
				 LOGGER.info("cart is empty");
				 demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.CART_IS_EMPTY, messageDetailList);
			     return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
			 }
    	}
       if(deleteCartItemRequest.getCustomerId() !=0 && deleteCartItemRequest.getCustomerId()!= null) {
        	LOGGER.info("sku : {} deleted from  database of customerId : {}",deleteCartItemRequest.getSku(), deleteCartItemRequest.getCustomerId() );
        	if(!deleteCartItemRepository.deleteItemFromDB(deleteCartItemRequest.getCustomerId(), deleteCartItemRequest.getSku())) {
        		LOGGER.info("sku : {} deleted from redis of customeruuId : {}",deleteCartItemRequest.getSku(), deleteCartItemRequest.getCustomerId() );
				 demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.CART_IS_EMPTY, messageDetailList);
			     return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
        	}
        	if(null != deleteCartItemRequest.getCustomerUuid() && !deleteCartItemRequest.getCustomerUuid().isEmpty() ) {
        		deleteCartItemRepository.deleteItemFromRedis(CommonConstant.CACHE_CART_SERVICE + CommonConstant.ANON + deleteCartItemRequest.getCustomerUuid(), deleteCartItemRequest.getSku());
        	}
        	if(deleteCartItemRepository.deleteItemFromRedis(CommonConstant.CACHE_CART_SERVICE + CommonConstant.IDEN + deleteCartItemRequest.getCustomerId(), deleteCartItemRequest.getSku())) {
				 LOGGER.info("sku : {} deleted from redis of customeruuId : {}",deleteCartItemRequest.getSku(), deleteCartItemRequest.getCustomerId() );
				 demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.ITEM_DELETED_SUCCESSFULLY, messageDetailList);
			     return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
			 }else {
 				 demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.CART_IS_EMPTY, messageDetailList);
			     return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
			 }
		 }           
        else {
        	    LOGGER.error("uneccessary error occured while deleting  sku : {} deleted of customerId : {}",deleteCartItemRequest.getSku(), deleteCartItemRequest.getCustomerId());
		    	demandStoreAPIResponseHelper.setMessageDetailList(MessageType.ERROR, CommonConstant.ITEM_NOT_PRESENT_ERROR, messageDetailList);
 		     	return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
        }
    }
}
