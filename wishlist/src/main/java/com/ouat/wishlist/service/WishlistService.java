package com.ouat.wishlist.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ouat.wishlist.constants.CommonConstant;
import com.ouat.wishlist.entity.Wishlist;
import com.ouat.wishlist.exception.BusinessProcessException;
import com.ouat.wishlist.interceptor.WishlistServiceClient;
import com.ouat.wishlist.repository.WishlistRepository;
import com.ouat.wishlist.request.WishlistRequest;
import com.ouat.wishlist.response.CustomerDetailVO;
import com.ouat.wishlist.response.ProductDetailResponse;
import com.ouat.wishlist.response.WishlistResponse;

@Service
public class WishlistService {
	
	@Autowired
	WishlistRepository wishlistRepository;
	
	@Autowired
	WishlistServiceClient wishlistServiceClient;
	
	public int addItemInWishlist(WishlistRequest wishlistRequest, CustomerDetailVO customerDetail) throws BusinessProcessException {
		Wishlist wishlist = new Wishlist();
		BeanUtils.copyProperties(wishlistRequest, wishlist);
		wishlist.setCustomerId(customerDetail.getCustomerId());
		wishlist.setCreatedBy(customerDetail.getEmail());
		wishlist.setCreatedDate(new Date());
		wishlist.setUpdatedDate(new Date());
		wishlist.setUpdatedBy(customerDetail.getEmail());
		if(wishlistRepository.countWislistAddedItem(customerDetail.getCustomerId())<CommonConstant.WISHLIST_LIMIT) {
			wishlistRepository.save(wishlist);
			return 1;
		}else {
			throw new BusinessProcessException(CommonConstant.LIMIT_EXCCED, CommonConstant.SUCCESS_STATUS_CODE);
		}
	}
	
	public List<WishlistResponse> findByCustomerId(CustomerDetailVO customerDetail) throws BusinessProcessException {
		List<Long> wishlists = wishlistRepository.findProductIdByCustomer(customerDetail.getCustomerId());
		List<WishlistResponse> wishlistResponseList = new ArrayList<>();
		if(wishlists.isEmpty()) {
			return wishlistResponseList;
		}
		List<ProductDetailResponse> productDetailList = wishlistServiceClient.makePostCall(wishlists);
		Map<Integer, ProductDetailResponse> mapOfProductDetailResponse = productDetailList.stream().collect(Collectors.toMap(ProductDetailResponse::getProductId, Function.identity()));
		
        for (int i = 0; i < wishlists.size(); i++) {
            ProductDetailResponse response = mapOfProductDetailResponse
                    .get(wishlists.get(i) == null ? -1 : wishlists.get(i).intValue());
            if (response != null) {
                WishlistResponse wishlistResponse = new WishlistResponse();
                wishlistResponse.setProductId((long) response.getProductId());
                wishlistResponse.setProductName(response.getProductName());
                wishlistResponse.setProductImage(response.getProductImage());
                if (response.getFromPrice().equals(response.getToPrice())) {
                    wishlistResponse.setPrice(response.getFromPrice().toString());
                } else {
                    wishlistResponse
                            .setPrice(response.getFromPrice() + "-" + response.getToPrice());
                }
                wishlistResponse.setSalePrice(response.getSalePrice().toString());
                wishlistResponse.setDiscount(Double.valueOf(response.getFromPrice()) - Double.valueOf(response.getSalePrice()));
                wishlistResponseList.add(wishlistResponse);
            }
        }
        return wishlistResponseList;
	}
	
	public int delete(Long productId, CustomerDetailVO customerDetail) {
		return wishlistRepository.deleteByproductId(productId, customerDetail.getCustomerId());
	}

	public Map<String, List<Long>> getProductId(Long customerId) {
		Map<String, List<Long>> map = new HashMap<>();
		map.put("products", wishlistRepository.findProductIdByCustomer(customerId));
		return map;
	}
	
    private String discountBuilder(Double regularPrice, Double salePrice) {
        Long toDiscount = Math.round(((regularPrice - salePrice) * 100) / regularPrice);
        if (Long.compare(toDiscount, 0L) == 0L)
            return null;
        return toDiscount + "% OFF";
    }
	
	
	
}
