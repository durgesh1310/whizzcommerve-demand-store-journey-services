package com.ouat.cartService.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ouat.cartService.exception.BusinessProcessException;
import com.ouat.cartService.interceptor.DemandStoreLoginRequired;
import com.ouat.cartService.interceptor.RequestHeaders;
import com.ouat.cartService.interceptor.SessionRequired;
import com.ouat.cartService.response.CustomerDetailVO;
import com.ouat.cartService.shoppingCartHelper.CommonConstant;
import com.ouat.cartService.shoppingCartHelper.Platform;
import com.ouat.cartService.shoppingCartRequest.AddToCartRequest;
import com.ouat.cartService.shoppingCartRequest.DeleteCartItemRequest;
import com.ouat.cartService.shoppingCartRequest.ShowShoppingCartRequest;
import com.ouat.cartService.shoppingCartResponse.DemandStoreAPIResponse;
import com.ouat.cartService.shoppingCartService.AddToCartService;
import com.ouat.cartService.shoppingCartService.DeleteCartItemService;
import com.ouat.cartService.shoppingCartService.MergeCartService;
import com.ouat.cartService.shoppingCartService.ShowShoppingCartService;
import com.ouat.cartService.shoppingCartService.ShowShoppingCartServiceInternal;

/**
 * 
 * @author : Sourabh singh
 *
 */
@RestController
@CrossOrigin
public class ShoppingCartController {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartController.class);
	
	@Autowired
	AddToCartService addToCartService;

	@Autowired
	DeleteCartItemService deleteCartItemService;

	@Autowired
	ShowShoppingCartService showShoppingCartService;
	
	@Autowired
	ShowShoppingCartServiceInternal showShoppingCartServiceInternal;

	@Autowired
	MergeCartService mergeCartService;
	
	@PostMapping("/add-to-cart")
	@SessionRequired
	public ResponseEntity<DemandStoreAPIResponse> apsurtItemToCart(@RequestBody AddToCartRequest addToCartRequest, HttpServletRequest request) throws BusinessProcessException {
		LOGGER.info("Add to cart request recieved successfully  with  request body : {}", addToCartRequest);
		LOGGER.info("ADD_TO_CART_DEVICE | {} " , request.getHeader(RequestHeaders.DEVICE_TYPE));
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		LOGGER.info("customer detail data has been fetched from the servelet request with customer detail : {}", customerDetail);
		if (null != customerDetail) {
			addToCartRequest.setCustomerId(customerDetail.getCustomerId().intValue());
		}
		String uuid = request.getHeader(RequestHeaders.DEVICE_ID);
		String platform = request.getHeader(RequestHeaders.USER_CLIENT);
		addToCartRequest.setCustomerUuid(uuid);
		addToCartRequest.setPlatform(Platform.valueOf(platform));
		LOGGER.info("calling the upsert cart function for add to cart request with request detail : {} and customer detail : {}", addToCartRequest, customerDetail);
		return ResponseEntity.ok(addToCartService.upsertCart(addToCartRequest));
	}
	@PostMapping("/delete-cart-item")
	@SessionRequired
	public ResponseEntity<DemandStoreAPIResponse> deleteItemFromCart(@RequestBody DeleteCartItemRequest deleteCartItemRequest, HttpServletRequest request) {
		LOGGER.info("delete item from cart request recieved successfully with request : {}", deleteCartItemRequest);
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		if (null != customerDetail) {
			deleteCartItemRequest.setCustomerId(customerDetail.getCustomerId().intValue());
		}
		String uuid = request.getHeader(RequestHeaders.DEVICE_ID);
		deleteCartItemRequest.setCustomerUuid(uuid);
		LOGGER.info("customer uuid : {}", uuid);
		LOGGER.info("calling the delete_item_from_cart function with request : {} and customer detail : {}", deleteCartItemRequest, customerDetail);
		return ResponseEntity.ok(deleteCartItemService.deleteItemFromCart(deleteCartItemRequest));
	}
	
	@GetMapping("/show-shopping-cart-detail")
	@SessionRequired
	public ResponseEntity<DemandStoreAPIResponse> showShoppingCart(HttpServletRequest request) throws BusinessProcessException {
		LOGGER.info("Request Received for show shopping Cart with no request body");
		LOGGER.info("SHOW_SHOPPING_CART_DEVICE | {} " , request.getHeader(RequestHeaders.DEVICE_TYPE));
		ShowShoppingCartRequest showShoppingcartRequest = new ShowShoppingCartRequest();
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		LOGGER.info("fetched the customer detail from the http servelet request with customer detail : {}", customerDetail);
		if (null != customerDetail) {
			showShoppingcartRequest.setCustomerId(customerDetail.getCustomerId().intValue());
		}
		String uuid = request.getHeader(RequestHeaders.DEVICE_ID);
		String platform = request.getHeader(RequestHeaders.USER_CLIENT);
		showShoppingcartRequest.setCustomerUuid(uuid);
		showShoppingcartRequest.setPlatform(Platform.valueOf(platform));
		LOGGER.info("show shopping cart request build and calling the show shopping Cart function with  with no request body : {} and customer detail", showShoppingcartRequest, customerDetail);
		return ResponseEntity.ok(showShoppingCartService.showShoppingCart(showShoppingcartRequest));
	}
	@GetMapping("/merge-cart")
	@DemandStoreLoginRequired
	public void mergeCart(HttpServletRequest request) throws BusinessProcessException{
		LOGGER.info("merge cart request recieved with no request body");
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		LOGGER.info("customer detail : {} \n fetched from the request servelet for merge cart request ");
 		if (null == customerDetail) {
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
		}
		String uuid = request.getHeader(RequestHeaders.DEVICE_ID);
		Integer customerId = customerDetail.getCustomerId().intValue();
		LOGGER.info("calling the merge cart function with customer Id:{}  or customer UUId :{} to merge the cart ",customerId ,uuid);
	    mergeCartService.mergeCart(uuid, customerId );
	}
	
	@GetMapping("/internal/{customerId}/show-shopping-cart/{platform}")
	public  ResponseEntity<DemandStoreAPIResponse> showShoppingCartInternal(@PathVariable("customerId") String customerId, @PathVariable String platform) throws BusinessProcessException {
     	LOGGER.info("show shopping cart internal called with Id  :{} and platform : {}",customerId ,platform);
        if(NumberUtils.isNumber(customerId)){
     		return ResponseEntity.ok(showShoppingCartServiceInternal.showShoppingCartInternal(new ShowShoppingCartRequest(null, Platform.valueOf(platform),  Integer.parseInt(customerId) )));
         }else {
     		return ResponseEntity.ok(showShoppingCartServiceInternal.showShoppingCartInternal(new ShowShoppingCartRequest(customerId, Platform.valueOf(platform),  null)));
         }
	}
	
	@DeleteMapping("/internal/shopping-cart")
	public  ResponseEntity<DemandStoreAPIResponse> deleteShoppingCart(@RequestParam(name = "customer_id") Integer customerId, @RequestParam(name = "device_id") String deviceId) throws BusinessProcessException {
		LOGGER.info("delete shopping cart after placing the order has been recieved  with customer Id: {} and device id :{}",customerId ,deviceId);
		return ResponseEntity.ok(showShoppingCartServiceInternal.deleteShoppingCart(customerId, deviceId));
	}
	
	@GetMapping("/cart-item")
	@SessionRequired
	public  ResponseEntity<DemandStoreAPIResponse> getNumberOfCartItem (HttpServletRequest request){
		LOGGER.info("Request for number of cart item is successfully recieved ");
		ShowShoppingCartRequest showShoppingcartRequest = new ShowShoppingCartRequest();
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		LOGGER.info("customer detail for number of cart item is fethced successfully with customer detail : {}", customerDetail);

		if (null != customerDetail) {
			showShoppingcartRequest.setCustomerId(customerDetail.getCustomerId().intValue());
		}
		String uuid = request.getHeader(RequestHeaders.DEVICE_ID);
		String platform = request.getHeader(RequestHeaders.USER_CLIENT);
		showShoppingcartRequest.setCustomerUuid(uuid);
		showShoppingcartRequest.setPlatform(Platform.valueOf(platform));
		LOGGER.info("calling the get number of cart item in shopping cart with request : {}", showShoppingcartRequest);
		return ResponseEntity.ok(showShoppingCartServiceInternal.getNumberOfCartItem(showShoppingcartRequest));
	}
	@GetMapping("/shipping-charges/{cartValue}")
	@SessionRequired
	public ResponseEntity<DemandStoreAPIResponse> getShippingCharges(@PathVariable(name = "cartValue") Double cartValue,
			HttpServletRequest request) {
		LOGGER.info("shipping cart request has been successfully recieved with cart value: {}", cartValue);
		String platform = request.getHeader(RequestHeaders.USER_CLIENT);
		LOGGER.info("calling the shipping charg function with cart value: {} and platform : {}", cartValue);
		return ResponseEntity.ok(showShoppingCartService.getShippingCharges( cartValue,  platform));
	}
	
	@GetMapping("/v1/shipping-charges/{cartValue}")
	@SessionRequired
	public ResponseEntity<DemandStoreAPIResponse> getV1ShippingCharges(@PathVariable(name = "cartValue") Double cartValue,
			HttpServletRequest request) {
		LOGGER.info("shipping cart request has been successfully recieved with cart value: {}", cartValue);
		String platform = request.getHeader(RequestHeaders.USER_CLIENT);
		LOGGER.info("calling the shipping charg function with cart value: {} and platform : {}", cartValue);
		return ResponseEntity.ok(showShoppingCartService.getV1ShippingCharges( cartValue,  platform));
	}
}
 