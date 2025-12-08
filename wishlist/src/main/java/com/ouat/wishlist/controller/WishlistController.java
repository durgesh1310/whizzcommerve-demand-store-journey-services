
package com.ouat.wishlist.controller;


import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ouat.wishlist.constants.CommonConstant;
import com.ouat.wishlist.exception.BusinessProcessException;
import com.ouat.wishlist.interceptor.DemandStoreLoginRequired;
import com.ouat.wishlist.interceptor.RequestHeaders;
import com.ouat.wishlist.request.WishlistRequest;
import com.ouat.wishlist.response.CustomerDetailVO;
import com.ouat.wishlist.response.DemandStoreAPIResponse;
import com.ouat.wishlist.response.MessageDetail;
import com.ouat.wishlist.response.MessageType;
import com.ouat.wishlist.response.WishlistResponse;
import com.ouat.wishlist.service.WishlistService;


@RestController
@RequestMapping("/wishlist")
@CrossOrigin
public class WishlistController {
	
	@Autowired
	WishlistService wishlistService;
	
	@PostMapping
	@DemandStoreLoginRequired
	public ResponseEntity<DemandStoreAPIResponse> wishlist(@RequestBody @Valid WishlistRequest wishlistRequest, HttpServletRequest request)throws BusinessProcessException{
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);		
		if(wishlistService.addItemInWishlist(wishlistRequest, customerDetail) > 0) {
			return ResponseEntity.ok(new DemandStoreAPIResponse(true, Arrays.asList(new MessageDetail(MessageType.INFO, "Item Wishlisted !!", null, null)), CommonConstant.SUCCESS_STATUS_CODE));	
		}else {
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST_PARAM, CommonConstant.FAILURE_STATUS_CODE);
		}
	}
	
	@GetMapping
	@DemandStoreLoginRequired
	public ResponseEntity<DemandStoreAPIResponse> getWishlist(HttpServletRequest request) throws BusinessProcessException{
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		List<WishlistResponse> wishlistList = wishlistService.findByCustomerId(customerDetail);
		if(wishlistList.isEmpty()) {
			throw new BusinessProcessException(CommonConstant.WISHLIST_EMPTY, CommonConstant.FAILURE_STATUS_CODE);
		}else {
			return ResponseEntity.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, wishlistList));
		}
	}
	
	
	@DeleteMapping("/{productId}")
	@DemandStoreLoginRequired
	public ResponseEntity<DemandStoreAPIResponse> deleteWishlist(@PathVariable Long productId, HttpServletRequest request) throws BusinessProcessException{
		CustomerDetailVO customerDetail = (CustomerDetailVO) request.getAttribute(RequestHeaders.CUSTOMER_DETAIL);
		if(wishlistService.delete(productId, customerDetail)>0) {
			return ResponseEntity.ok(new DemandStoreAPIResponse(true, Arrays.asList(new MessageDetail(MessageType.INFO, "Item removed from wishlist !!", null, null)), CommonConstant.SUCCESS_STATUS_CODE));
		}else {
			throw new BusinessProcessException(CommonConstant.WISHLIST_ERROR, CommonConstant.FAILURE_STATUS_CODE);
		}
		
	}
	

}
