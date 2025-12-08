package com.ouat.wishlist.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ouat.wishlist.constants.CommonConstant;
import com.ouat.wishlist.exception.BusinessProcessException;
import com.ouat.wishlist.response.DemandStoreAPIResponse;
import com.ouat.wishlist.service.WishlistService;

@RestController
@RequestMapping("/customers")
public class InternalController {
	
	@Autowired
	WishlistService wishlistService;
	
	@GetMapping("/ping")
    public String ping(HttpServletRequest request) {
        return "pong";
    }
	
	@GetMapping("/{customerId}")
	public ResponseEntity<DemandStoreAPIResponse> getProductId(@PathVariable Long customerId) throws BusinessProcessException{
		Map<String, List<Long>> map = wishlistService.getProductId(customerId);
		if(! map.isEmpty()) {
			return ResponseEntity.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, map));
		}else {
			return ResponseEntity.ok(new DemandStoreAPIResponse(false, null, CommonConstant.FAILURE_STATUS_CODE));
		}
		
	}

}
