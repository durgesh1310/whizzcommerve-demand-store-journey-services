package com.ouat.checkout.builder;

import static com.ouat.checkout.constant.CacheConstant.CHECKOUT_CACHE_APPENDER;
import static com.ouat.checkout.constant.CacheConstant.GUEST_CHECKOUT_CACHE_APPENDER;
import static com.ouat.checkout.constant.CacheConstant.GUEST_USER;
import static com.ouat.checkout.constant.CacheConstant.IDENTIFIED_USER;
import static com.ouat.checkout.util.Utility.mapToDouble;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ouat.checkout.cache.CheckoutCache;
import com.ouat.checkout.cache.GuestCheckoutCache;
import com.ouat.checkout.cache.OrderedItemCache;
import com.ouat.checkout.controller.request.GuestCheckoutRequest;
import com.ouat.checkout.controller.response.CheckoutResponse;
import com.ouat.checkout.controller.response.GuestCheckoutResponse;
import com.ouat.checkout.dto.AddressDto;
import com.ouat.checkout.dto.CustomerCreditDto;
import com.ouat.checkout.dto.PricingSummaryDto;
import com.ouat.checkout.dto.PromoDto;
import com.ouat.checkout.dto.PromoDto.SkuPromoDisountMapping;
import com.ouat.checkout.dto.ShowShoppingCartItemDto;
import com.ouat.checkout.dto.SkuPricingInventoryCodDetailsDto.SkuPricingInventoryCodDetail;
import com.ouat.checkout.placeOrder.request.GuestAddress;
import com.ouat.checkout.redis.RedisUtil;
import com.ouat.checkout.service.CheckoutService;
import com.ouat.checkout.util.Utility;


@Component
public class CacheBuilder {

	private static final long CHECKOUT_CACHE_EXPIRY = 10;
    private static final long GUEST_CHECKOUT_CACHE_EXPIRY = 15;

    @Autowired
    private RedisUtil<String> checkoutCache;
    @Autowired 
    CheckoutService checkoutSerVice;

    public void buildCheckoutCache(CheckoutResponse ckoResponse, PromoDto promoDto,
            Integer customerId,
            Map<String, SkuPricingInventoryCodDetail> skuInventoryCodDetailsDtoDetails, AddressDto address) {
        CheckoutCache ckoCache = new CheckoutCache();
        ckoCache.setPricingSummary(ckoResponse.getPricingSummary());
        if (ckoResponse.getAddress() != null)
            ckoCache.setAddressId(ckoResponse.getAddress().getSelectedAddressId());
        ckoCache.setCreditApplied(buildMapOfCreditApplied(ckoResponse.getCredits(), ckoResponse.getPricingSummary().getTotalPrice(), getTotalOrderPayableWithoutCredit(ckoResponse.getPricingSummary())));
        ckoCache.setPaymentMode(ckoResponse.getPaymentMethod());
        ckoCache.setOrderSummary(getListOfOrderItemCache(ckoResponse.getOrderSummary(),
                buildSkuPromoDiscountMapping(promoDto), skuInventoryCodDetailsDtoDetails));
        ckoCache.setOrderConfirmationEmail(ckoResponse.getOrderConfirmationEmail());
        if (ckoResponse.getPricingSummary() != null)
            ckoCache.setPromoCode(ckoResponse.getPricingSummary().getPromoCode());
        ckoCache.setAddress(address);
        String key = String.format(CHECKOUT_CACHE_APPENDER, IDENTIFIED_USER, customerId);
        checkoutCache.putValueWithExpireTime(key, Utility.getJson(ckoCache), CHECKOUT_CACHE_EXPIRY,
                TimeUnit.MINUTES);
    }

    private Map<String, SkuPromoDisountMapping> buildSkuPromoDiscountMapping(PromoDto promoDto) {
    	Map<String, SkuPromoDisountMapping> mp = new HashMap<>();
        if (promoDto != null && promoDto.getSkus() != null && !promoDto.getSkus().isEmpty()) {
        	for(SkuPromoDisountMapping it : promoDto.getSkus()) {
            	mp.put(it.getSku(), it);
        	}
        }
           // return promoDto.getSkus().stream()
                  //  .collect(Collectors.toMap(SkuPromoDisountMapping::getSku, Function.identity()));
        return  mp;//new HashMap<String, SkuPromoDisountMapping>();
    }
    private List<OrderedItemCache> getListOfOrderItemCache(
            List<ShowShoppingCartItemDto> orderSummary,
            Map<String, SkuPromoDisountMapping> skuPromoDiscountMapping,
            Map<String, SkuPricingInventoryCodDetail> skuInventoryCodDetailsDtoDetails) {
    	
        return orderSummary.stream().map(itemDetail -> {
            OrderedItemCache orderedItemCache = new OrderedItemCache();
            if (itemDetail.getRegularPrice() > itemDetail.getRetailPrice())
                orderedItemCache.setOrderItemPlatFormOfferedDiscount(
                        itemDetail.getRegularPrice() - itemDetail.getRetailPrice());
            orderedItemCache.setQuantity(itemDetail.getQuantity());
            orderedItemCache.setRegularPrice(itemDetail.getRegularPrice());
            orderedItemCache.setRetailPrice(itemDetail.getRetailPrice());
            orderedItemCache.setSize(itemDetail.getSize());
            orderedItemCache.setSku(itemDetail.getSku());
            orderedItemCache.setEdd(itemDetail.getEdd());
            orderedItemCache.setDefaultImageUrl(itemDetail.getDefaultImageUrl());
            orderedItemCache.setProductName(itemDetail.getProductName());
            if (skuInventoryCodDetailsDtoDetails != null
                    && skuInventoryCodDetailsDtoDetails.get(itemDetail.getSku()) != null) {
                SkuPricingInventoryCodDetail skuDetails =
                        skuInventoryCodDetailsDtoDetails.get(itemDetail.getSku());
                orderedItemCache.setIsExchangeable(skuDetails.getIsExchangeable());
                orderedItemCache.setIsReturnable(skuDetails.getIsReturnable());
                orderedItemCache.setOuatMargin(skuDetails.getOuatMargin());
                orderedItemCache.setVendorPrice(skuDetails.getVendorPrice());
            }

            if (skuPromoDiscountMapping != null && !skuPromoDiscountMapping.isEmpty()) {
                SkuPromoDisountMapping discountMapping =
                        skuPromoDiscountMapping.get(itemDetail.getSku());
                if (discountMapping != null) {
                    orderedItemCache.setOrderItemPromoDiscount(discountMapping.getPromoValue());
                    orderedItemCache.setIsPromoApplicable(discountMapping.getIsPromoApplicable());
                }
            }
            return orderedItemCache;
        }).collect(Collectors.toList());
    }

    public Double getTotalOrderPayableWithoutCredit(PricingSummaryDto pricingSummary) {
        return Math.max(0.0,
                (mapToDouble(pricingSummary.getTotalPrice())
                        + mapToDouble(pricingSummary.getTotalDeliveryCharges()))
                        - (mapToDouble(pricingSummary.getTotalPromoDiscount())));
    }
    private Map<String, Double> buildMapOfCreditApplied(List<CustomerCreditDto> credits, Double totalRetailPrice, Double orderPayable) {
        if (credits == null || credits.isEmpty())
            return null;
        Map<String, Double> mp = new HashMap<>();
        long loyaltyCreditUsed = checkoutSerVice.evaluateLoyaltyCreditUsed(credits, totalRetailPrice);
        long merchCreditused = checkoutSerVice.evaluateMerchCreditUsed(credits, orderPayable);
        mp.put("L", (double)loyaltyCreditUsed);
        mp.put("M", (double)merchCreditused);
        return mp;
    }

    public CheckoutCache getCheckOutCache(Integer customerId) {
        String key = String.format(CHECKOUT_CACHE_APPENDER, IDENTIFIED_USER, customerId);
        String value = checkoutCache.getValue(key);
        return Utility.convertJsonToObject(value, CheckoutCache.class);
    }
    
    public void setCheckOutCach(CheckoutCache  chkout,Integer customerId) {
    	 String key = String.format(CHECKOUT_CACHE_APPENDER, IDENTIFIED_USER, customerId);
         checkoutCache.putValueWithExpireTime(key, Utility.getJson(chkout), CHECKOUT_CACHE_EXPIRY,
                 TimeUnit.MINUTES);
    }
    
    public void deleteCheckoutCache(Integer customerId) {
    	 String key = String.format(CHECKOUT_CACHE_APPENDER, IDENTIFIED_USER, customerId);
    	 checkoutCache.delete(key);
    	 return ;
    }

    public void buildGuestCheckoutCache(GuestCheckoutResponse guestCkoResponse,
            PromoDto promoResponse, GuestCheckoutRequest request,
            Map<String, SkuPricingInventoryCodDetail> details, String deviceId) {
        GuestCheckoutCache guestCheckoutCache = new GuestCheckoutCache();
        guestCheckoutCache.setPricingSummary(guestCkoResponse.getPricingSummary());
        guestCheckoutCache.setPaymentMode(guestCkoResponse.getPaymentMethod());
        if (!Objects.isNull(promoResponse))
            guestCheckoutCache.setPromoCode(promoResponse.getPromoCode());
        guestCheckoutCache
                .setOrderSummary(getListOfOrderItemCache(guestCkoResponse.getOrderSummary(),
                        buildSkuPromoDiscountMapping(promoResponse), details));
        guestCheckoutCache.setAddress(request.getAddress());
        guestCheckoutCache.setMobileNo(request.getMobileNo());
        String key = String.format(GUEST_CHECKOUT_CACHE_APPENDER, GUEST_USER, deviceId);
        checkoutCache.putValueWithExpireTime(key, Utility.getJson(guestCheckoutCache),
                GUEST_CHECKOUT_CACHE_EXPIRY, TimeUnit.MINUTES);
    }

    public com.ouat.checkout.placeOrder.request.GuestCheckoutCache getGuestCheckOutCache(String id) {
        String key = String.format(GUEST_CHECKOUT_CACHE_APPENDER, GUEST_USER, id);
        String value = checkoutCache.getValue(key);
        GuestCheckoutCache cache = Utility.convertJsonToObject(value, GuestCheckoutCache.class);
        return buildPlaceOrderGuestCheckoutCache(cache);
    }

    public void deleteGuestCheckOutCache(String id) {
        String key = String.format(GUEST_CHECKOUT_CACHE_APPENDER, GUEST_USER, id);
   	    checkoutCache.delete(key);
   	    return;
   
    }

    private com.ouat.checkout.placeOrder.request.GuestCheckoutCache buildPlaceOrderGuestCheckoutCache(
            GuestCheckoutCache cache) {
        com.ouat.checkout.placeOrder.request.GuestCheckoutCache cache2 = new com.ouat.checkout.placeOrder.request.GuestCheckoutCache();
        cache2.setAddress( buildGuestAddress( cache.getAddress()));
        cache2.setOrderSummary(cache.getOrderSummary());
        cache2.setMobileNo(cache.getMobileNo());
        cache2.setPaymentMode(cache.getPaymentMode());
        cache2.setPromoCode(cache.getPromoCode());
        cache2.setPricingSummary(cache.getPricingSummary());
        return cache2;
    }

    private GuestAddress buildGuestAddress(
            com.ouat.checkout.controller.request.GuestCheckoutRequest.GuestAddress address) {
        GuestAddress address2 = new GuestAddress();
        address2.setAddress(address.getAddress());
        address2.setPincode(address.getPincode());
        address2.setState(address.getState());
        address2.setCity(address.getCity());
        address2.setEmail(address.getEmail());
        address2.setFullName(address.getFullName());
        address2.setLandmark(address.getLandmark());
        return address2;
    }
}
