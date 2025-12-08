package com.ouat.checkout.service;

import static com.ouat.checkout.constant.CacheConstant.SHIPPING_CARGES_CACHE_APPENDER;
import static com.ouat.checkout.util.Utility.mapToDouble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.ouat.checkout.apistore.clients.APIStoreClient;
import com.ouat.checkout.apistore.clients.PaymentGatewayClient;
import com.ouat.checkout.builder.CacheBuilder;
import com.ouat.checkout.constant.CommonConstant;
import com.ouat.checkout.controller.request.CheckoutRequest;
import com.ouat.checkout.controller.request.GuestCheckoutRequest;
import com.ouat.checkout.controller.request.GuestCheckoutRequest.GuestAddress;
import com.ouat.checkout.controller.response.CheckoutResponse;
import com.ouat.checkout.controller.response.GuestCheckoutResponse;
import com.ouat.checkout.dto.AddressDetailDto;
import com.ouat.checkout.dto.AddressDto;
import com.ouat.checkout.dto.ApplyPromoRequestDto;
import com.ouat.checkout.dto.ApplyPromoRequestDto.CartItem;
import com.ouat.checkout.dto.CustomerCreditDto;
import com.ouat.checkout.dto.PricingSummaryDto;
import com.ouat.checkout.dto.PromoDto;
import com.ouat.checkout.dto.ShowShoppingCartItemDto;
import com.ouat.checkout.dto.SkuPricingInventoryCodDetailsDto;
import com.ouat.checkout.dto.SkuPricingInventoryCodDetailsDto.SkuPricingInventoryCodDetail;
import com.ouat.checkout.dto.redis.ShippingChargesDetailsRedisDto;
import com.ouat.checkout.enums.DiscountTypeEnum;
import com.ouat.checkout.enums.PaymentMode;
import com.ouat.checkout.exception.BusinessProcessException;
import com.ouat.checkout.exception.DownStreamException;
import com.ouat.checkout.placeOrder.DTO.ShippingChargesDetailDTO;
import com.ouat.checkout.placeOrder.repository.PlaceOrderRepository;
import com.ouat.checkout.redis.RedisUtil;
import com.ouat.checkout.repository.CheckoutRepository;
import com.ouat.checkout.response.CustomerDetailVO;
import com.ouat.checkout.response.DemandStoreAPIResponse;
import com.ouat.checkout.response.Platform;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private static final Logger log = LoggerFactory.getLogger(CheckoutService.class);

    private static final int MAX_RETRY = 3;

    private Boolean IS_COD_APPLICABLE = true;

    @Value("${max.cart.value.cod:#{0.0}}")
    private Double maxCartValueForCod;
    
    private Double MIN_VALUE_FOR_COD = 700.00;
    
    private Double LOYALTY_CREDIT_PERCENTAGE = 20.00;

    @Autowired
    private RedisUtil<ShippingChargesDetailsRedisDto> shippingChargesCache;

    @Autowired
    private CheckoutRepository repository;

    @Autowired
    private APIStoreClient apiStoreClient;

    @Autowired
    private CacheBuilder cacheBuilder;
    
    @Autowired 
    private PlaceOrderRepository placeOrderRepository;
    
    @Autowired
	private PaymentGatewayClient paymentGatewayClient;

    @Qualifier("threadPoolExecutor")
    private final ThreadPoolTaskExecutor taskExecutor;

    public ResponseEntity<DemandStoreAPIResponse> callCheckoutWorkFlow(Integer customerId,
            String promoCode, CustomerDetailVO customerDetails, String transactionId,
            HttpHeaders downStreamHeaders, Platform platform) throws BusinessProcessException, DownStreamException {

        List<ShowShoppingCartItemDto> cartItemResponse = getCustomerCartItems(String.valueOf(customerId),
                customerDetails.getUserClient(), transactionId);

        if (cartItemResponse == null || cartItemResponse.isEmpty()) {
            return ResponseEntity.ok(new DemandStoreAPIResponse());
        }
        List<String> skus = getListOfSkus(cartItemResponse);
        SkuPricingInventoryCodDetailsDto skuInventoryCodDetailsDto = apiStoreClient.getSkusInventoryCodDetailsResponse(skus, transactionId, downStreamHeaders);
        for (ShowShoppingCartItemDto cartResponse : cartItemResponse) {
            doPrecheck(cartResponse, skuInventoryCodDetailsDto.getDetails());
        }
        List<CustomerCreditDto> creditResponse = apiStoreClient.getCustomerCreditResponse(customerId, transactionId, downStreamHeaders);
        log.info("creditResponse : {}", creditResponse.toString());
        List<AddressDetailDto> customerAddress = apiStoreClient.getCustomerAddressResponse(customerId, transactionId, downStreamHeaders);
      PricingSummaryDto priceSummary = getPriceSummaryBeforApplyingPromo(cartItemResponse,
                creditResponse, customerDetails.getUserClient());
        
        PromoDto promoResponse = new PromoDto();
        if (null != promoCode && !promoCode.isEmpty()) {
            ApplyPromoRequestDto applyPromoRequestBody = buildApplyPromoRequest(cartItemResponse,
                    skuInventoryCodDetailsDto.getDetails(),
                    customerDetails.getCustomerId().intValue(), platform ,
                    priceSummary, promoCode, skuInventoryCodDetailsDto, null);
            promoResponse = apiStoreClient.getPromoCodeResponse(String.valueOf(customerId),
                    promoCode, transactionId, applyPromoRequestBody, downStreamHeaders);
        }
       
        AddressDto address = new AddressDto();
        if (customerAddress != null && !customerAddress.isEmpty()) {
            Map<Integer, AddressDetailDto> mapOfAddressResponse = customerAddress.stream()
                    .collect(Collectors.toMap(AddressDetailDto::getAddressId, Function.identity()));
            Integer selectedAddressId =
                    setSelectedFlagForDefaultAddressPopulation(mapOfAddressResponse, customerId);
            customerAddress = mapOfAddressResponse.values().stream().collect(Collectors.toList());
            address = buildAddressDto(customerAddress, selectedAddressId);
        }

      addPromoCodeInPrincingSummary(priceSummary, promoResponse, promoCode);
        Map<String, Object> pgResponseData = buildPaymentGatewayResponse(customerId, transactionId);
        
        return buildDemandStoreAPIResponse(cartItemResponse, creditResponse, customerAddress,
                priceSummary, address, promoResponse, customerId, customerDetails.getEmail(),
                skuInventoryCodDetailsDto.getDetails(), pgResponseData);
    }

	private Map<String, Object> buildPaymentGatewayResponse(Integer customerId, String transactionId) {
		String razorpayCustomerId = placeOrderRepository.fetchRazorpayCustomerId(new Long(customerId));
		Map<String, Object> pgResponseData = new HashMap<>();
		if (null != razorpayCustomerId) {
			pgResponseData.put("pg_customer_id", razorpayCustomerId);
			try {
				pgResponseData.put("saved_cards",
						paymentGatewayClient.fetchSavedCards(razorpayCustomerId, transactionId));
			} catch (DownStreamException e) {
				log.info("Error while fetching card details of customer : {} ", razorpayCustomerId);
			}
		}
		return (pgResponseData.isEmpty())?null : pgResponseData;
	}


   private PricingSummaryDto getPriceSummaryBeforApplyingPromo(
            List<ShowShoppingCartItemDto> cartItemResponse, List<CustomerCreditDto> creditResponse,
            String platform) {
        PricingSummaryDto pricingSummary = new PricingSummaryDto();
        pricingSummary.setTotalPlatformDiscount(evaluateTotalPlatformDiscount(cartItemResponse));
        pricingSummary.setTotalPrice(evaluateTotalPrice(cartItemResponse));
        pricingSummary.setTotalDeliveryCharges(getDelieveryCharges(platform, pricingSummary));
        pricingSummary.setTotalCreditValue(evaluateTotalCreditValue(creditResponse,evaluateTotalPrice(cartItemResponse), 
        		getTotalOrderPayable(pricingSummary)));
        pricingSummary.setTotalOrderPayable(getTotalOrderPayable(pricingSummary));
        return pricingSummary;
    }

    public ResponseEntity<DemandStoreAPIResponse> callRefreshCheckoutWorkflow(Integer customerId,
            CustomerDetailVO customerDetails, CheckoutRequest request, String transactionId,
            HttpHeaders downStreamHeaders, Platform platform, String uuid) throws DownStreamException, BusinessProcessException {
        List<ShowShoppingCartItemDto> cartItemResponse = getCustomerCartItems(String.valueOf(customerId),
                customerDetails.getUserClient(), transactionId);
        if (cartItemResponse == null || cartItemResponse.isEmpty()) {
            return ResponseEntity.ok(new DemandStoreAPIResponse());
        }
        List<String> skus = getListOfSkus(cartItemResponse);
        SkuPricingInventoryCodDetailsDto skuInventoryCodDetailsDto = apiStoreClient
                .getSkusInventoryCodDetailsResponse(skus, transactionId, downStreamHeaders);
        for (ShowShoppingCartItemDto cartResponse : cartItemResponse) {
            doPrecheck(cartResponse, skuInventoryCodDetailsDto.getDetails());
        }
        List<CustomerCreditDto> creditResponse = apiStoreClient
                .getCustomerCreditResponse(customerId, transactionId, downStreamHeaders);
        List<AddressDetailDto> customerAddress = apiStoreClient
                .getCustomerAddressResponse(customerId, transactionId, downStreamHeaders);

        PricingSummaryDto priceSummary = refreshPriceSummaryBeforApplyingPromo(cartItemResponse,
                creditResponse, customerDetails.getUserClient(), request.getAppliedCredits());

        ApplyPromoRequestDto applyPromoRequestBody =
                buildApplyPromoRequest(cartItemResponse, skuInventoryCodDetailsDto.getDetails(),
                        customerDetails.getCustomerId().intValue(), platform ,
                        priceSummary, request.getPromoCode(), skuInventoryCodDetailsDto, uuid);
        PromoDto promoResponse = new PromoDto();
		if (null != request.getPromoCode() && !request.getPromoCode().isEmpty()) {
			promoResponse = apiStoreClient.getPromoCodeResponse(String.valueOf(customerId), request.getPromoCode(),
					transactionId, applyPromoRequestBody, downStreamHeaders);
		}

        AddressDto address = new AddressDto();
        if (customerAddress != null && !customerAddress.isEmpty()) {
            Map<Integer, AddressDetailDto> mapOfAddressResponse = customerAddress.stream()
                    .collect(Collectors.toMap(AddressDetailDto::getAddressId, Function.identity()));
            Integer selectedAddressId = null;
            if (mapOfAddressResponse.containsKey(request.getAddressId())) {
                selectedAddressId = request.getAddressId();
            } else {
                selectedAddressId = setSelectedFlagForDefaultAddressPopulation(mapOfAddressResponse,
                        customerId);
            }
            customerAddress = mapOfAddressResponse.values().stream().collect(Collectors.toList());
            address = buildAddressDto(customerAddress, selectedAddressId);
        }
        addPromoCodeInPrincingSummary(priceSummary, promoResponse, request.getPromoCode());
        
        Map<String, Object> pgResponseData = buildPaymentGatewayResponse(customerId, transactionId);
        
        return buildDemandStoreAPIResponse(cartItemResponse, creditResponse, customerAddress,
                priceSummary, address, promoResponse, customerId,
                request.getOrderConfirmationEmail(), skuInventoryCodDetailsDto.getDetails(), pgResponseData);
    }
    private PricingSummaryDto refreshPriceSummaryBeforApplyingPromo(
            List<ShowShoppingCartItemDto> cartItemResponse, List<CustomerCreditDto> creditResponse,
            String platform, List<String> appliedCredits) throws BusinessProcessException {
        PricingSummaryDto pricingSummary = new PricingSummaryDto();
        pricingSummary.setTotalPlatformDiscount(evaluateTotalPlatformDiscount(cartItemResponse));
        pricingSummary.setTotalPrice(evaluateTotalPrice(cartItemResponse));
        pricingSummary.setTotalDeliveryCharges(getDelieveryCharges(platform, pricingSummary));
        pricingSummary.setTotalCreditValue(refreshTotalCreditValue(creditResponse, appliedCredits, evaluateTotalPrice(cartItemResponse), getTotalOrderPayable(pricingSummary)));
        pricingSummary.setTotalOrderPayable(getTotalOrderPayable(pricingSummary));
        return pricingSummary;
    }

    private List<ShowShoppingCartItemDto> getCustomerCartItems(String customerId, String platform,
            String transactionId) throws DownStreamException {
        return apiStoreClient.getCustomerShowShoppingCartItemResponse(customerId, platform,
                transactionId);
    }

    private List<String> getListOfSkus(List<ShowShoppingCartItemDto> cartItemResponse) {
        return cartItemResponse.stream().map(item -> item.getSku()).collect(Collectors.toList());
    }
    private void doPrecheck(ShowShoppingCartItemDto cartResponse,
            Map<String, SkuPricingInventoryCodDetail> map) throws BusinessProcessException {
        if (map == null || map.isEmpty())
            throw new BusinessProcessException("Something went wrong",
                    CommonConstant.FAILURE_STATUS_CODE);

        if (map.containsKey(cartResponse.getSku())) {
            SkuPricingInventoryCodDetail pricingInventoryCodDetail = map.get(cartResponse.getSku());
            if (pricingInventoryCodDetail == null || areSkuPricesDifferentOnCartAndDuringCheckout(
                    cartResponse, pricingInventoryCodDetail)) {
            	log.info("Issue with pricing : {} ", cartResponse.getSku());
                throw new BusinessProcessException("Something went wrong",
                        CommonConstant.FAILURE_STATUS_CODE);
            } else {
                if (pricingInventoryCodDetail.getInventory() < cartResponse.getQuantity()
                        || pricingInventoryCodDetail.getIsInventoryExpired())
                    throw new BusinessProcessException(
                            cartResponse.getProductName() + " " + "out of stock",
                            CommonConstant.FAILURE_STATUS_CODE);
                else if (!pricingInventoryCodDetail.getIsSkuActive()) {
                    throw new BusinessProcessException(
                            cartResponse.getProductName() + " " + "is not available",
                            CommonConstant.FAILURE_STATUS_CODE);
                }
            }
            // If on any of the sku cod is not applicable then payment method can't be COD;
            if (!pricingInventoryCodDetail.getIsCodApplicable())
                IS_COD_APPLICABLE = false;
        }

    }

    private boolean areSkuPricesDifferentOnCartAndDuringCheckout(
            ShowShoppingCartItemDto cartResponse,
            SkuPricingInventoryCodDetail pricingInventoryCodDetail) {
        return Double.compare(
                cartResponse.getQuantity()
                        * mapToDouble(pricingInventoryCodDetail.getRetailPrice()),
                mapToDouble(cartResponse.getRetailPrice())) != 0
                || Double.compare(
                        cartResponse.getQuantity()
                                * mapToDouble(pricingInventoryCodDetail.getRegularPrice()),
                        mapToDouble(cartResponse.getRegularPrice())) != 0;
    }

    private ApplyPromoRequestDto buildApplyPromoRequest(
            List<ShowShoppingCartItemDto> cartItemResponse,
            Map<String, SkuPricingInventoryCodDetail> details, Integer customerId, Platform platform,
            PricingSummaryDto pricingSummary, String promoCode,
            SkuPricingInventoryCodDetailsDto skuInventoryCodDetailsDto, String uuid) {
        ApplyPromoRequestDto applyPromoRequestDto = new ApplyPromoRequestDto();
        applyPromoRequestDto.setUuId(uuid);
        applyPromoRequestDto.setPromocode(promoCode);
        applyPromoRequestDto
                .setCustomerId(customerId);
        applyPromoRequestDto.setPlatform(platform);
        applyPromoRequestDto.setCartValue(mapToDouble(pricingSummary.getTotalPrice()));
        applyPromoRequestDto
                .setPaymentMode(
                        getAllPaymentModes(
                                mapToDouble(pricingSummary.getTotalPrice())
                                        + mapToDouble(pricingSummary.getTotalDeliveryCharges()),
                                null, pricingSummary.getTotalOrderPayable()).get(0));
        applyPromoRequestDto.setCartItemList(
                createCartItemList(cartItemResponse, details, skuInventoryCodDetailsDto));
        return applyPromoRequestDto;
    }

    private List<CartItem> createCartItemList(List<ShowShoppingCartItemDto> cartItemResponse,
            Map<String, SkuPricingInventoryCodDetail> details,
            SkuPricingInventoryCodDetailsDto skuInventoryCodDetailsDto) {
        List<CartItem> cartItemList = new ArrayList<ApplyPromoRequestDto.CartItem>();

        if (details != null && !details.isEmpty()) {
            for (ShowShoppingCartItemDto showShoppingCartItem : cartItemResponse) {
                CartItem item = new CartItem();
                SkuPricingInventoryCodDetail pricingInventoryCodDetailsDto =
                        details.get(showShoppingCartItem.getSku());
                item.setCategory(pricingInventoryCodDetailsDto.getCategory());
                item.setProductId(pricingInventoryCodDetailsDto.getProductId());
                item.setProductType(pricingInventoryCodDetailsDto.getProductType());
                item.setQuantity(showShoppingCartItem.getQuantity());
                item.setRetailPrice(pricingInventoryCodDetailsDto.getRetailPrice());
                item.setSubCategory(pricingInventoryCodDetailsDto.getSubCategory());
                item.setSku(showShoppingCartItem.getSku());
                cartItemList.add(item);
            }
        }

        return cartItemList;
    }

    private Integer setSelectedFlagForDefaultAddressPopulation(
            Map<Integer, AddressDetailDto> mapOfAddressResponse, Integer customerId) {
        if (mapOfAddressResponse == null || mapOfAddressResponse.isEmpty())
            return null;
        Integer lastShippedAddressId = repository.queryForFindingLastShippedAddress(customerId);
        if (lastShippedAddressId == null)
            lastShippedAddressId = mapOfAddressResponse.keySet().stream().findFirst().get();
        AddressDetailDto lastShippedAddress = mapOfAddressResponse.get(lastShippedAddressId);
        if (null == lastShippedAddress)
            return null;
        else
            lastShippedAddress.setIsSelected(Boolean.TRUE);
        return lastShippedAddressId;
    }

    private AddressDto buildAddressDto(List<AddressDetailDto> customerAddress,
            Integer selectedAddressId) {
        AddressDto address = new AddressDto();
        address.setAddresses(customerAddress);
        address.setSelectedAddressId(selectedAddressId);
        return address;
    }

    private double evaluateTotalPlatformDiscount(List<ShowShoppingCartItemDto> cartItemResponse) {
        return cartItemResponse.stream()
                .filter(item -> item.getRegularPrice() > item.getRetailPrice())
                .mapToDouble(price -> (price.getRegularPrice() - price.getRetailPrice())).sum();
    }
    
    private double refreshTotalCreditValue(List<CustomerCreditDto> creditResponse,
            List<String> appliedCredits, Double totalRetailPrice, Double orderPayable) throws BusinessProcessException {
        if (appliedCredits == null || appliedCredits.isEmpty())
            return 0.0;
        long loyaltyCreditUsed = evaluateLoyaltyCreditUsed(creditResponse, totalRetailPrice);
        long merchCreditUsed = evaluateMerchCreditUsed(creditResponse,orderPayable);
        log.info("L: {}, M:{}", loyaltyCreditUsed, merchCreditUsed);
        return  Math.max(loyaltyCreditUsed , merchCreditUsed);
    }
 
    public double evaluateTotalCreditValue(List<CustomerCreditDto> creditResponse, Double totalRetailPrice, Double orderPayable) {
        if (creditResponse == null || creditResponse.isEmpty())
            return 0.0;
        long loyaltyCreditUsed = evaluateLoyaltyCreditUsed(creditResponse, totalRetailPrice);
        long merchCreditUsed = evaluateMerchCreditUsed(creditResponse,orderPayable);
        log.info("L: {}, M:{}", loyaltyCreditUsed, merchCreditUsed);
        return  Math.max(loyaltyCreditUsed , merchCreditUsed);// max of merch or loyalty
    }

	public long evaluateMerchCreditUsed(List<CustomerCreditDto> creditResponse, Double orderPayable) {
		Double merchCredit = 0.00;
		log.info( "....................................retail price..................: {}", orderPayable);
		for(CustomerCreditDto it : creditResponse) {
        	if(it.getType().equals("M")) {
        		merchCredit = merchCredit +  it.getAmount();
        		break;
        	}
        }
		return  Math.round(Math.min(merchCredit, orderPayable));
	}
    
    public long evaluateLoyaltyCreditUsed(List<CustomerCreditDto> creditResponse, Double totalRetailPrice) {
    	Double loyaltyCredit = 0.00;
    	for(CustomerCreditDto it : creditResponse) {
        	if(it.getType().equals("L")) {
        		loyaltyCredit = loyaltyCredit +  Math.min(it.getAmount(), (totalRetailPrice*LOYALTY_CREDIT_PERCENTAGE)/100.00);
        		break;
        	}
        }
    	return Math.round(loyaltyCredit);
    }
    /*
     * @Note Removed multiplication of quantity because response which is getting in cartItem
     * response its retails price is multipication of quantity*retail_price already
     **/

    private double evaluateTotalPrice(List<ShowShoppingCartItemDto> cartItemResponse) {
        return cartItemResponse.stream().mapToDouble(item -> item.getRetailPrice()).sum();
    }

    /**
     * @implNote FREESHIPPING on each wednesday from 27th April 2022
     * */
    @Retryable(value = {RedisConnectionFailureException.class, QueryTimeoutException.class}, maxAttempts = MAX_RETRY)
    private Double getDelieveryCharges(String platform, PricingSummaryDto pricingSummary) {
        String key = String.format(SHIPPING_CARGES_CACHE_APPENDER, platform);
        log.info("fetching shipping charges from redis with key : {}", key);
        ShippingChargesDetailsRedisDto shippingChargesDto = shippingChargesCache.getValue(key);
        log.info("shipping charges fetched woth shipping charges detail : {} with key : {}",shippingChargesDto, key);
        if (shippingChargesDto != null) {
            return Double.valueOf(shippingChargesDto.getCartValue() < mapToDouble(pricingSummary.getTotalPrice()) ? shippingChargesDto.getShippingCharges(): 0.0);
        }
        
        ShippingChargesDetailDTO chargesDetailDTO = repository.getshippingCharge(platform);
//        Double shippingCharge = repository.queryForShippingCharges(platform.getValue(),
//                pricingSummary.getTotalPrice());
        log.info("shipping charges fetched from database shipping charges detail: {} ", chargesDetailDTO);
        Double shippingCharge = 0.0;
        if(chargesDetailDTO.getCartValue() > pricingSummary.getTotalPrice()) {
			   shippingCharge = chargesDetailDTO.getShippingCharges();
		   } 
         return shippingCharge;
    }

    private void addPromoCodeInPrincingSummary(PricingSummaryDto pricingSummary,
            PromoDto promoResponse, String promocode) {
        pricingSummary.setPromoCode(promocode);
        if (promoResponse != null) {
            pricingSummary.setTotalPromoDiscount(promoResponse.getTotal());
        }
        pricingSummary.setTotalOrderPayable(getTotalOrderPayable(pricingSummary));
    }

    public Double getTotalOrderPayable(PricingSummaryDto pricingSummary) {
        return Math.max(0.0,
                (mapToDouble(pricingSummary.getTotalPrice())
                        + mapToDouble(pricingSummary.getTotalDeliveryCharges()))
                        - (mapToDouble(pricingSummary.getTotalCreditValue())
                                + mapToDouble(pricingSummary.getTotalPromoDiscount())));
    }
    @Retryable(value = {RedisConnectionFailureException.class, QueryTimeoutException.class}, maxAttempts = MAX_RETRY)
    private ResponseEntity<DemandStoreAPIResponse> buildDemandStoreAPIResponse(
            List<ShowShoppingCartItemDto> cartItemResponse, List<CustomerCreditDto> creditResponse,
            List<AddressDetailDto> customerAddress, PricingSummaryDto priceSummary,
            AddressDto address, PromoDto promoResponse, Integer customerId,
            String orderConfirmationEmail,
            Map<String, SkuPricingInventoryCodDetail> skuInventoryCodDetailsDtoDetails, Map<String, Object> pgResponseData) {
        CheckoutResponse ckoResponse = buildCheckoutResponse(cartItemResponse, creditResponse,
                priceSummary, address, orderConfirmationEmail, promoResponse, pgResponseData);
        /*CompletableFuture.runAsync(() -> cacheBuilder.buildCheckoutCache(ckoResponse, promoResponse,
              customerId, skuInventoryCodDetailsDtoDetails, address), taskExecutor);*/
        cacheBuilder.buildCheckoutCache(ckoResponse, promoResponse,
                customerId, skuInventoryCodDetailsDtoDetails, address);
        DemandStoreAPIResponse demandStoreAPIResponse = new DemandStoreAPIResponse();
        demandStoreAPIResponse.setCode(CommonConstant.SUCCESS_STATUS_CODE);
        demandStoreAPIResponse.setData(ckoResponse);
        IS_COD_APPLICABLE = true;
        return ResponseEntity.ok(demandStoreAPIResponse);
    }

    private CheckoutResponse buildCheckoutResponse(List<ShowShoppingCartItemDto> cartItemResponse,
            List<CustomerCreditDto> creditResponse, PricingSummaryDto priceSummary,
            AddressDto address, String orderConfrimationEmail, PromoDto promoDto, Map<String, Object> pgResponseData) {
        CheckoutResponse ckoResponse = new CheckoutResponse();
        ckoResponse.setAddress(address);
        ckoResponse.setCredits(getCreditsHavingPositiveAmount(creditResponse, evaluateTotalPrice(cartItemResponse)));
        ckoResponse.setOrderSummary(cartItemResponse);
        updateDeliveryChargesBasedOnDiscountType(priceSummary, promoDto);
        ckoResponse.setPricingSummary(priceSummary);
        ckoResponse.setPaymentMethod(getAllPaymentModes(mapToDouble(priceSummary.getTotalPrice())
                + mapToDouble(priceSummary.getTotalDeliveryCharges()), promoDto, ckoResponse.getPricingSummary().getTotalOrderPayable()));
        ckoResponse.setOrderConfirmationEmail(orderConfrimationEmail);
        if(null == pgResponseData || pgResponseData.isEmpty()) {
        	 ckoResponse.setPgCustomerData(null);
        }else ckoResponse.setPgCustomerData(pgResponseData);
        
        return ckoResponse;
    }

  public List<CustomerCreditDto> getCreditsHavingPositiveAmount(
            List<CustomerCreditDto> creditResponse, Double totalRetailPrice) {
    	List<CustomerCreditDto> credits = new ArrayList<>();
    	Double loyalty =   (double) evaluateLoyaltyCreditUsed(creditResponse, totalRetailPrice);
        Double merch = 0.00;
    	for(CustomerCreditDto it : creditResponse) {
    		if(it.getType().equals("M")) {
    			merch =Double.valueOf(it.getAmount()); ;
    			break;
    		} 
    	}
    	CustomerCreditDto dto = new CustomerCreditDto();
    	if(merch >= loyalty) {
			dto.setAmount(Double.valueOf(merch));
			dto.setCreditName("Your Wallet");
			dto.setType("M");
		}else {
			dto.setAmount(Double.valueOf((loyalty)));
			dto.setCreditName("Your Rewards");
			dto.setType("L");
		}
    	credits.add(dto);
    	return credits;
    }

    private void updateDeliveryChargesBasedOnDiscountType(PricingSummaryDto priceSummary,
            PromoDto promoDto) {
        if (promoDto == null || priceSummary == null)
            return;
        Double updatedShippingCharges = DiscountTypeEnum.getPromoValue(promoDto.getDiscountType());
        if (updatedShippingCharges == null)
            return;
        priceSummary.setTotalDeliveryCharges(updatedShippingCharges);
    }
    
    /**
     * @param totalOrderPayable 
     * @implNote Included COD in check because there is no case in which company will provide
     * promocodes on COD orders
     * 
     * If orderPayable is zero then paymentMethod will be OWP (Order Without Paymnet)
     * */
    private List<PaymentMode> getAllPaymentModes(Double cartValue, PromoDto promoDto,
            Double totalOrderPayable) {
        List<PaymentMode> allModes = new ArrayList<PaymentMode>();

        if (Double.compare(totalOrderPayable, 0.0) == 0) {
            allModes.add(PaymentMode.OWP);
            return allModes;
        }
        if(null != promoDto)
        	log.info("promoDto : {} ", promoDto.toString());
        
        log.info("is_code_applicable = {}, cartValue = {}, maxCartValyeForCOD = {}", IS_COD_APPLICABLE, totalOrderPayable, maxCartValueForCod);        
        if ((IS_COD_APPLICABLE && totalOrderPayable < maxCartValueForCod) && ((IS_COD_APPLICABLE && totalOrderPayable > MIN_VALUE_FOR_COD))) {
            if ((promoDto == null || promoDto.getPaymentMode() == null)
                    || (promoDto.getPaymentMode() == PaymentMode.ALL)
                    || (promoDto.getPaymentMode() == PaymentMode.COD)) {
                allModes.add(PaymentMode.ALL);
            } else {
                allModes.add(PaymentMode.ONLINE);
            }
        }
        else {
            allModes.add(PaymentMode.ONLINE);
        }
        return allModes;
    }

    public ResponseEntity<DemandStoreAPIResponse> callGuestCheckoutWorkflow(String deviceId,
            String userClient, Platform platform, GuestCheckoutRequest request,
            String transactionId, HttpHeaders downStreamHeaders)
            throws BusinessProcessException, DownStreamException {
        List<ShowShoppingCartItemDto> cartItemResponse =
                getCustomerCartItems(deviceId, userClient, transactionId);
        if (cartItemResponse == null || cartItemResponse.isEmpty()) {
            return ResponseEntity.ok(new DemandStoreAPIResponse());
        }
        List<String> skus = getListOfSkus(cartItemResponse);
        SkuPricingInventoryCodDetailsDto skuInventoryCodDetailsDto = apiStoreClient
                .getSkusInventoryCodDetailsResponse(skus, transactionId, downStreamHeaders);
        for (ShowShoppingCartItemDto cartResponse : cartItemResponse) {
            doPrecheck(cartResponse, skuInventoryCodDetailsDto.getDetails());
        }
        PricingSummaryDto priceSummary =
                getPriceSummaryBeforApplyingPromo(cartItemResponse, null, userClient);

        PromoDto promoResponse = null;
        if (null != request.getPromoCode() && !request.getPromoCode().isEmpty()) {
            ApplyPromoRequestDto applyPromoRequestBody = buildApplyPromoRequest(cartItemResponse,
                    skuInventoryCodDetailsDto.getDetails(), null, platform, priceSummary,
                    request.getPromoCode(), skuInventoryCodDetailsDto, deviceId);
			try {
				promoResponse = apiStoreClient.getPromoCodeResponse(deviceId, request.getPromoCode(), transactionId,
						applyPromoRequestBody, downStreamHeaders);
			}catch (DownStreamException e) {
            	log.error("Error while making promo service : {} ", e.getMessage());
            	promoResponse = new PromoDto();
            }
        }

        addPromoCodeInPrincingSummary(priceSummary, promoResponse, request.getPromoCode());
        return buildDemandStoreAPIResponse(cartItemResponse, request,
                priceSummary, promoResponse, skuInventoryCodDetailsDto.getDetails(), deviceId);
    }

    @Retryable(value = {RedisConnectionFailureException.class, QueryTimeoutException.class},
            maxAttempts = MAX_RETRY)
    private ResponseEntity<DemandStoreAPIResponse> buildDemandStoreAPIResponse(
            List<ShowShoppingCartItemDto> cartItemResponse, GuestCheckoutRequest request,
            PricingSummaryDto priceSummary, PromoDto promoResponse,
            Map<String, SkuPricingInventoryCodDetail> details, String deviceId) {
        GuestCheckoutResponse guestCkoResponse = buildGuestCheckoutResponse(cartItemResponse,
                priceSummary, request.getAddress(), promoResponse, request.getMobileNo());

        // build checkout cache in seprate thread
        CompletableFuture.runAsync(() -> cacheBuilder.buildGuestCheckoutCache(guestCkoResponse,
                promoResponse, request, details, deviceId), taskExecutor);
        DemandStoreAPIResponse demandStoreAPIResponse = new DemandStoreAPIResponse();
        demandStoreAPIResponse.setCode(CommonConstant.SUCCESS_STATUS_CODE);
        demandStoreAPIResponse.setData(guestCkoResponse);
        IS_COD_APPLICABLE = true;
        return ResponseEntity.ok(demandStoreAPIResponse);
    }


    private GuestCheckoutResponse buildGuestCheckoutResponse(
            List<ShowShoppingCartItemDto> cartItemResponse, PricingSummaryDto priceSummary,
            GuestAddress address, PromoDto promoResponse, String mobileNo) {
        GuestCheckoutResponse guestCkoResponse = new GuestCheckoutResponse();
        guestCkoResponse.setAddress(address);
        guestCkoResponse.setOrderSummary(cartItemResponse);
        updateDeliveryChargesBasedOnDiscountType(priceSummary, promoResponse);
        guestCkoResponse.setPricingSummary(priceSummary);
        guestCkoResponse.setPaymentMethod(getAllPaymentModes(
                mapToDouble(priceSummary.getTotalPrice())
                        + mapToDouble(priceSummary.getTotalDeliveryCharges()),
                promoResponse, guestCkoResponse.getPricingSummary().getTotalOrderPayable()));
        guestCkoResponse.setMobileNo(mobileNo);
        return guestCkoResponse;
    }
    
}
