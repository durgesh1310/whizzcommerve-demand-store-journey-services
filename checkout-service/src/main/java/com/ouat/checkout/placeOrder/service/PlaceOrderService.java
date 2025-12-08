package com.ouat.checkout.placeOrder.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouat.checkout.apistore.clients.APIStoreClient;
import com.ouat.checkout.apistore.clients.PaymentGatewayClient;
import com.ouat.checkout.builder.CacheBuilder;
import com.ouat.checkout.builder.PaymentRequestBuilder;
import com.ouat.checkout.cache.CheckoutCache;
import com.ouat.checkout.cache.OrderedItemCache;
import com.ouat.checkout.dto.AddressDetailDto;
import com.ouat.checkout.dto.AddressDto;
import com.ouat.checkout.dto.CustomerCreditDto;
import com.ouat.checkout.dto.payments.RazorPayCreateOrderRequestDto;
import com.ouat.checkout.dto.payments.RazorPayCreateOrderResponseDto;
import com.ouat.checkout.enums.MessageType;
import com.ouat.checkout.exception.BusinessProcessException;
import com.ouat.checkout.exception.DownStreamException;
import com.ouat.checkout.lock.Lock;
import com.ouat.checkout.lock.RedisLock;
import com.ouat.checkout.placeOrder.DTO.OrderDTO;
import com.ouat.checkout.placeOrder.DTO.OrderIdAndOrderPayable;
import com.ouat.checkout.placeOrder.DTO.OrderItemDTO;
import com.ouat.checkout.placeOrder.DTO.OrderShippmentDetailDTO;
import com.ouat.checkout.placeOrder.client.AddGuestUserResponse;
import com.ouat.checkout.placeOrder.client.AddInLogRequest;
import com.ouat.checkout.placeOrder.client.CartServiceClient;
import com.ouat.checkout.placeOrder.client.CustomerCreditHistory;
import com.ouat.checkout.placeOrder.client.CustomerServiceClient;
import com.ouat.checkout.placeOrder.client.DeliveryAddressPlaceOrderAlert;
import com.ouat.checkout.placeOrder.client.EmailSendRequest;
import com.ouat.checkout.placeOrder.client.InventoryServiceClient;
import com.ouat.checkout.placeOrder.client.InventoryServiceRequest;
import com.ouat.checkout.placeOrder.client.NotificationSenderClient;
import com.ouat.checkout.placeOrder.client.OrderItemCreditDetail;
import com.ouat.checkout.placeOrder.client.PaymentMethodForPlaceOrderAlert;
import com.ouat.checkout.placeOrder.client.PlaceOrderAlertRequest;
import com.ouat.checkout.placeOrder.client.PriceSummaryForPlaceOrderAlert;
import com.ouat.checkout.placeOrder.client.ProductItemDetailForPlaceOrderAlert;
import com.ouat.checkout.placeOrder.client.PromotionServiceClient;
import com.ouat.checkout.placeOrder.client.SkuAndQty;
import com.ouat.checkout.placeOrder.commonConstant.CommonConstant;
import com.ouat.checkout.placeOrder.repository.PlaceOrderRepository;
import com.ouat.checkout.placeOrder.request.GuestCheckoutCache;
import com.ouat.checkout.placeOrder.request.PaymentGatewayCustomerDetail;
import com.ouat.checkout.placeOrder.request.PaymentGatewayDetailRequest;
import com.ouat.checkout.placeOrder.request.PaymentGatewayFailedRequest;
import com.ouat.checkout.placeOrder.request.Utm;
import com.ouat.checkout.placeOrder.response.PlaceOrderResponse;
import com.ouat.checkout.response.CustomerDetailVO;
import com.ouat.checkout.response.DemandStoreAPIResponse;
import com.ouat.checkout.response.DemandStoreAPIResponseHelper;
import com.ouat.checkout.response.DeviceType;
import com.ouat.checkout.response.MessageDetail;
import com.ouat.checkout.service.CheckoutService;
import com.ouat.checkout.util.DateTimeUtil;
import com.ouat.checkout.util.Utility;

@Service
public class PlaceOrderService {
	   
    public Logger LOGGER = LoggerFactory.getLogger(PlaceOrderService.class);
    
    @Autowired
    private CacheBuilder  checkoutCacheData;
    
    @Autowired 
    private PlaceOrderRepository placeOrderRepository;
	
	@Autowired
	private DemandStoreAPIResponseHelper demandStoreAPIResponseHelper;
	
	@Autowired 
	private InventoryServiceClient inventoryServiceClient;
	
	@Autowired
	private CustomerServiceClient customerServiceClient;
	
	@Autowired
	private NotificationSenderClient emailServiceClient;
	
	@Autowired
	private PromotionServiceClient promotionServiceClient;
	
	@Autowired
	private CartServiceClient cartServiceClient;
	
 	@Autowired
	private PaymentRequestBuilder paymentRequestBuilder;
	
	@Autowired
	private PaymentGatewayClient paymentGatewayClient;
	
	@Autowired
	private CheckoutService checkoutService;
	
	@Autowired
	APIStoreClient apiStoreClient;
	
	private String transactionId;
	
	 @Autowired
	 private RedisLock lock;
	
	@PostConstruct
	public void init() {
	    transactionId = UUID.randomUUID().toString();
	}
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = {BusinessProcessException.class})
	public DemandStoreAPIResponse placeOrder(String paymentMode, CustomerDetailVO customerDetail, Utm utm, HttpHeaders downStreamHeaders) throws Exception {
		List<String>key = new ArrayList<>();
		List<MessageDetail> messageDetailList = new ArrayList<>();
 		Integer orderStatusId = paymentMode.equals("COD") ? 11 : 1; // 11 is for OR and 1 is WP
 		key.add(customerDetail.getCustomerId().toString());
 	   	DemandStoreAPIResponse response  = null;
 	   	LOGGER.info("getting item from checkout cache");
		try {
			if(!lock.acquireLock(key,Lock.PLACEORDER)) {
				 LOGGER.info("lock not acquire for key : {}", key);
				throw new BusinessProcessException( CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
			}
            LOGGER.info("lock acquire");
            LOGGER.info("getting item from checkout cache");
    		CheckoutCache checkoutCache = checkoutCacheData.getCheckOutCache(customerDetail.getCustomerId().intValue());
    		LOGGER.info("Checkout Cache : {} ", checkoutCache);
    		if(!checkOutValidation(checkoutCache, customerDetail.getCustomerId().intValue())) {
    			demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.SOMETHING_WENT_WRONG, messageDetailList);
    		    return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, null);
    		}
    		OrderIdAndOrderPayable orderIdAndOrderPayable = dtoBuilderAndInsertInOrdersSchema(paymentMode, customerDetail.getCustomerId().intValue(), checkoutCache, orderStatusId,utm, customerDetail.getDeviceType(), downStreamHeaders);
    		downStreamApiCAllForinventoryUpdate(checkoutCache, customerDetail, false);
    		if(null != paymentMode && paymentMode.equals("COD")){
    		    String orderNumber = placeOrderRepository.fetchOrderNumberFromOrderId(orderIdAndOrderPayable.getOrderId());
                downStreamAPIcall(checkoutCache, orderIdAndOrderPayable.getOrderId(), "COD" ,customerDetail, true, orderNumber);
    		}
          demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.ORDER_PLACED, messageDetailList);
          response = demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, buildPlaceOrderResponse(paymentMode, orderIdAndOrderPayable));
             
        }finally {
        	lock.releaseLock(key);
            LOGGER.info("lock release");
        }
		return response;
	}
	private PlaceOrderResponse buildPlaceOrderResponse(String paymentMode,
			OrderIdAndOrderPayable orderIdAndOrderPayable) throws BusinessProcessException, DownStreamException {
		PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
		placeOrderResponse.setOrderId(orderIdAndOrderPayable.getOrderId());
		LOGGER.info("setting order status for the order id : {}", orderIdAndOrderPayable.getOrderId());
		if((orderIdAndOrderPayable.getOrderPayable()) == 0.00) {
			LOGGER.info("order status set to : OWP for order payable : {}", orderIdAndOrderPayable.getOrderPayable());
			placeOrderResponse.setOrderStatus("OWP");
			paymentMode = "OWP";
		}
		else if((orderIdAndOrderPayable.getOrderPayable())>0.00) {
			LOGGER.info("order status set to : {}",( paymentMode.equals("COD")?"COD":"PW"));
			placeOrderResponse.setOrderStatus((paymentMode.equals("COD")?"COD":"PW"));
		}
		else {
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
		}
		if(null != paymentMode && "ONLINE".equals(paymentMode)   && !(("OWP").equals(paymentMode))) {
           RazorPayCreateOrderRequestDto razorPayCreateOrderRequest = paymentRequestBuilder.getRazorPayCreateOrderRequest(
                    Math.round(orderIdAndOrderPayable.getOrderPayable()*100),
                    orderIdAndOrderPayable.getOrderId());
           RazorPayCreateOrderResponseDto razorCreateOrderResponse = paymentGatewayClient.createOrderOnRazorpayResponse(razorPayCreateOrderRequest, transactionId);
           PaymentGatewayDetailRequest request = new PaymentGatewayDetailRequest();
           request.setRazorpayOrderId(razorCreateOrderResponse.getId());
           placeOrderRepository.savePaymentGatewayResponse(orderIdAndOrderPayable.getOrderId(), request);
           placeOrderResponse.setThirdPartyOrderId(razorCreateOrderResponse.getId());
		}
		
		LOGGER.info("place order response successfully build with place  order response : {}", placeOrderResponse);
		return placeOrderResponse;
 	}
	
	public OrderIdAndOrderPayable dtoBuilderAndInsertInOrdersSchema(String paymentMode, Integer customerId, CheckoutCache checkoutCache, Integer orderStatusId, Utm utm , DeviceType deviceType, HttpHeaders downStreamHeaders) throws BusinessProcessException, DownStreamException {
		OrderDTO orderDTO = buildOrderDTO(checkoutCache, customerId, paymentMode, orderStatusId,utm , deviceType, downStreamHeaders);
 		LOGGER.info("inserting into order table with orderDTO : {}", orderDTO);
		Integer orderId = placeOrderRepository.insertIntoOrderTable(orderDTO);
		if(orderId == 0) {
			LOGGER.info("orders.order table not updated successfully");
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
		}
		List<OrderItemDTO> orderItemDTOList = buildOrderItemDto(checkoutCache, orderId,orderStatusId );
		LOGGER.info("inserting order item list into orderItem table with orderitemDTO : {}" , orderItemDTOList);
		int orderItemTableRowEffected = placeOrderRepository.insertIntoOrderItemTable(orderItemDTOList);
		if(orderItemTableRowEffected != orderItemDTOList.size() || orderItemTableRowEffected==0) {
			LOGGER.info("orders.order table not updated successfully");
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
		}
		OrderShippmentDetailDTO orderShippmentDetailDTO = buildOrderShippmentDetailDTO(checkoutCache.getAddressId(), orderId);
		LOGGER.info("Inserting into ordershipmentdetail table with shipment DTO : {} ", orderShippmentDetailDTO);
		int orderShippmentTableRowEffected = placeOrderRepository.insertIntoOrderShippmentDetail(orderShippmentDetailDTO);
		if(orderShippmentTableRowEffected == 0) {
			LOGGER.info("Inserting into ordershipmentdetail table not done successfully");
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
		}
		OrderIdAndOrderPayable orderIdAndOrderPayable = new OrderIdAndOrderPayable();
		orderIdAndOrderPayable.setOrderId(orderId);
		orderIdAndOrderPayable.setOrderPayable(orderDTO.getOrderPayable());
		orderIdAndOrderPayable.setOrderNumber(placeOrderRepository.fetchOrderNumberFromOrderId(orderId));
		return orderIdAndOrderPayable;
	}
	private void downStreamAPIcall(CheckoutCache checkoutCache, Integer orderId, String paymentMode, CustomerDetailVO customerDetail, boolean deleteCache, String orderNumber) throws BusinessProcessException {
		//will run in different thread 
		if(calculateTotalCreditUsed(checkoutCache) != 0.0 && calculateTotalCreditUsed(checkoutCache) != 0.00 && calculateTotalCreditUsed(checkoutCache) != 0) {
			creditUsedUpdate(checkoutCache, orderId, customerDetail);
		}
		
	    new Thread(() -> {
				try {
  					//1 : customer credit api call
					//2 : promo log update api call
					updateInpromoHistory(checkoutCache, orderId, customerDetail);
					//4 : customer cart clear dpi
					LOGGER.info("clear cart history with cId :{} and deviceID : {}",customerDetail.getCustomerId(),customerDetail.getDeviceId());
					StopWatch watch = new StopWatch();
					watch.start();
					
					cartServiceClient.clearCustomerCart(customerDetail.getCustomerId().intValue(), customerDetail.getDeviceId());
					watch.stop();
					LOGGER.info("Api call to cart service ended, total time taken : {} ", watch.getTotalTimeSeconds());
					LOGGER.info("calling the internal service Api to send emailer");
					watch.start();
					//3 : email and sms dpi
					sentEmailAndMessageAlert(checkoutCache, orderId, paymentMode, customerDetail, orderNumber);
					LOGGER.info("API call to send emailer took : {} ", watch.getTotalTimeSeconds());
					if(deleteCache) {
						LOGGER.info(" calling the deleting the checkout cache of customer : {} with  customer_id : {} ", customerDetail.getName(), customerDetail.getCustomerId());
				 		checkoutCacheData.deleteCheckoutCache(customerDetail.getCustomerId().intValue());
				 		checkoutCacheData.deleteGuestCheckOutCache(customerDetail.getDeviceId());
				 		LOGGER.info("deleting the checkout cache of customer : {} with  customer_id : {} ", customerDetail.getName(), customerDetail.getCustomerId());
					}
 				 		
				} catch (Exception e) {
					LOGGER.error("Error while performing downstream activities : {} ", e.getMessage(), e);
				}
			}).start();
	}
	
	private void creditUsedUpdate(CheckoutCache checkoutCache, Integer orderId, CustomerDetailVO customerDetail) throws BusinessProcessException {
		StopWatch watch = new StopWatch();
		if(null != checkoutCache && null!=checkoutCache.getCreditApplied()  && !checkoutCache.getCreditApplied().isEmpty()) {
			LOGGER.info("calling the down stream Api call to update the customer credit used : {} for order total : {} ",calculateTotalCreditUsed(checkoutCache), checkoutCache.getPricingSummary().getTotalPrice());						
			watch.start();
			if(!(customerServiceClient.customerCreditUpdate(buildCustomerCreditHistoryRequest(checkoutCache, customerDetail.getCustomerId().intValue(), orderId)))) {
				watch.stop();
		 		LOGGER.info("internal service Api called for customer credit history update with total time in second : {} ", watch.getTotalTimeSeconds());
				throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
			}
		}
 	}
	
	private void updateInpromoHistory(CheckoutCache checkoutCache, Integer orderId, CustomerDetailVO customerDetail) throws BusinessProcessException {
 		if(null !=  checkoutCache && null != checkoutCache.getPromoCode() && !"".equals(checkoutCache.getPromoCode().trim())) {
			boolean ifPromocodeSuccessfullyApplied = true;
			if(ifPromocodeSuccessfullyApplied) {
				LOGGER.info("calling the internal service Api add In log to add customer promo applied history with customer id : {} checkoutCache : {}, orderId : {}",customerDetail.getCustomerId().intValue(), checkoutCache, orderId);
				StopWatch watch = new StopWatch();
				watch.start();
				if(promotionServiceClient.addInPromoLog(buildAddInPromoLog(customerDetail.getCustomerId().intValue(), checkoutCache, orderId))) {
					watch.stop();
 					LOGGER.info("internal service Api successfully called with total time in second : {}", watch.getTotalTimeSeconds());
				}else {
					LOGGER.error("internal service Api to promotion service failed");
 					throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
				}
 			}
		}
	}

	private void sentEmailAndMessageAlert(CheckoutCache checkoutCache, Integer orderId, String paymentMode,
			CustomerDetailVO customerDetail, String orderNumber) {
			LOGGER.info("calling the down stream api call for notification service service Api call to send email and SMS with customerId: {}, checkoutchache:{},orderId : {}, paymentMode : {}, customeName : {}, customerEmailId : {}", customerDetail.getCustomerId().intValue(),checkoutCache, orderId, paymentMode, customerDetail.getName(),customerDetail.getEmail());
			StopWatch watch = new StopWatch();
			watch.start();
		    boolean emailServiceResponse = emailServiceClient.sentEmailForOderConformation(buildPlaceOrderAlert(customerDetail, checkoutCache, orderId, paymentMode, orderNumber));
		    watch.stop();
			if(!emailServiceResponse) {
		 		LOGGER.info("internal notification service service Api call failed to send email and SMS with customerId: {}, checkoutchache:{},orderId : {}, paymentMode : {}, customeName : {}, customerEmailId : {}", customerDetail.getCustomerId().intValue(),checkoutCache, orderId, paymentMode, customerDetail.getName(),customerDetail.getEmail());
				//throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
			}
 			LOGGER.info("down stream api call for notification sender service successfully called with total time in second : {} ", watch.getTotalTimeSeconds());
	}

	private void downStreamApiCAllForinventoryUpdate(CheckoutCache checkoutCache, CustomerDetailVO customerDetail, boolean deleteCache) throws BusinessProcessException {
   		InventoryServiceRequest inventoryServiceRequest = getSkuAndQty(checkoutCache);
   		boolean inventoryServiceResponse = inventoryServiceClient.updateInventory(inventoryServiceRequest.getSkuAndQtyList());
   		if(deleteCache) {
   			checkoutCacheData.deleteCheckoutCache(customerDetail.getCustomerId().intValue());
   		}
   		if(!inventoryServiceResponse){
   			LOGGER.info("Error response from inventory service : {} ", inventoryServiceResponse);
			throw new BusinessProcessException(CommonConstant.OUT_OF_STOCK, CommonConstant.FAILURE_STATUS_CODE);
		}
	}
	
	private void downStreamApiCAllForrevertInventory(CheckoutCache checkoutCache, CustomerDetailVO customerDetail, boolean deleteCache) throws BusinessProcessException {
   		InventoryServiceRequest inventoryServiceRequest = getSkuAndQty(checkoutCache);
   		boolean inventoryServiceResponse = inventoryServiceClient.addInventory(inventoryServiceRequest.getSkuAndQtyList());
   		if(deleteCache) {
   			checkoutCacheData.deleteCheckoutCache(customerDetail.getCustomerId().intValue());
   		}
   		if(!inventoryServiceResponse){
   			LOGGER.info("Error response from inventory service : {} ", inventoryServiceResponse);
			throw new BusinessProcessException(CommonConstant.OUT_OF_STOCK, CommonConstant.FAILURE_STATUS_CODE);
		}
	}
	private AddInLogRequest buildAddInPromoLog(Integer customerId, CheckoutCache checkoutCache, Integer orderId) {
		AddInLogRequest addInLogRequest = new AddInLogRequest();
		addInLogRequest.setCustomerId(customerId);
		addInLogRequest.setPromocode(checkoutCache.getPromoCode());
		addInLogRequest.setCurrentDateTime(null);
		addInLogRequest.setOrderId(orderId);
		addInLogRequest.setCartValue(checkoutCache.getPricingSummary().getTotalPrice());
		addInLogRequest.setDiscount(checkoutCache.getPricingSummary().getTotalPromoDiscount());
		return addInLogRequest;
	}
    private OrderDTO buildOrderDTO(CheckoutCache checkoutCache, Integer customerId, String paymentMode, Integer orderStatusId, Utm utm , DeviceType deviceType, HttpHeaders downStreamHeaders) throws BusinessProcessException, DownStreamException {
		  OrderDTO orderDTO = new OrderDTO();
		  orderDTO.setOrderNumber(generateOrderNumber());
		  orderDTO.setCustomerId(customerId);
		  orderDTO.setOrderStatusId((checkoutCache.getPricingSummary().getTotalOrderPayable()==0.00)?11:orderStatusId);
		  orderDTO.setTotalAmount(checkoutCache.getPricingSummary().getTotalPrice());
		  orderDTO.setOrderPayable(checkoutCache.getPricingSummary().getTotalOrderPayable());
		  orderDTO.setPlatformOfferedDiscount(checkoutCache.getPricingSummary().getTotalPlatformDiscount());
		  orderDTO.setPromoDiscount(checkoutCache.getPricingSummary().getTotalPromoDiscount());
		  
	      List<CustomerCreditDto> creditResponse = apiStoreClient.getCustomerCreditResponse(customerId, transactionId, downStreamHeaders);
		  LOGGER.info("down stream successfully called with response available : {}",  creditResponse);
		  List<CustomerCreditDto> credits =  checkoutService.getCreditsHavingPositiveAmount(creditResponse,checkoutCache.getPricingSummary().getTotalPrice());
          double creditAvailable =  credits.get(0).getAmount();
		  LOGGER.info(" credit available : {} and credit for order : {}", creditAvailable, calculateTotalCreditUsed(checkoutCache));
          if(creditAvailable >= calculateTotalCreditUsed(checkoutCache)) {
			  orderDTO.setCreditApplied(calculateTotalCreditUsed(checkoutCache));
		  }else {
				throw new BusinessProcessException(CommonConstant.CREDIT_VALIDATION, CommonConstant.FAILURE_STATUS_CODE);
		  }
		  orderDTO.setShippingCharges(checkoutCache.getPricingSummary().getTotalDeliveryCharges());
		  orderDTO.setOrderDate(null);
		  orderDTO.setOrderType("DemandStore");
		  orderDTO.setCartCreatedDate(null);
		  orderDTO.setPaymentMethod((checkoutCache.getPricingSummary().getTotalOrderPayable()==0.00)? "OWP" : paymentMode); 
		  orderDTO.setPromocode(checkoutCache.getPromoCode());
		  orderDTO.setPlatform(null == deviceType ? "" : deviceType.toString());
		  orderDTO.setUtmCampaign(utm.getUtmCampaign());
		  orderDTO.setUtmMedium(utm.getUtmMedium());
		  orderDTO.setUtmSource(utm.getUtmSource());
		 return orderDTO;
	}
    
	private String generateOrderNumber() {
	    String currentTimeInMillis = DateTimeUtil.getCurrentTimeInMillis();
	    return currentTimeInMillis;
    }

    private List<OrderItemDTO> buildOrderItemDto(CheckoutCache checkoutCache, Integer orderId, Integer orderStatusId) {
		    List<OrderedItemCache> orderItemList = checkoutCache.getOrderSummary() ;
		    List<OrderItemDTO> orderItemDTOList =  new ArrayList<OrderItemDTO>();
		    
			LOGGER.info("calling the split discount fuction for item level split for  shipping charges: {}  on total price :{}", checkoutCache.getPricingSummary().getTotalDeliveryCharges(), checkoutCache.getPricingSummary().getTotalPrice());
	        Map<String, Double> orderItemShippingChargesMap = discountSplitterOnEachProduct(
	        		((null !=checkoutCache.getPricingSummary().getTotalDeliveryCharges() && checkoutCache.getPricingSummary().getTotalDeliveryCharges() !=0.00)? checkoutCache.getPricingSummary().getTotalDeliveryCharges():0.00)
	        		, orderItemList, checkoutCache.getPricingSummary().getTotalPrice());
			LOGGER.info("the split discount fuction for item level split for  successfully called with response : {} ", orderItemShippingChargesMap);

			LOGGER.info("calling the split discount fuction for item level split for promo  discount : {} , on total price : {} ", checkoutCache.getPricingSummary().getTotalPromoDiscount(), checkoutCache.getPricingSummary().getTotalPrice());
 			Map<String, Double> promoDiscountSplitOnEachItem = discountSplitterOnEachProduct((null!=checkoutCache.getPricingSummary().getTotalPromoDiscount() && checkoutCache.getPricingSummary().getTotalPromoDiscount()!=0.00)? checkoutCache.getPricingSummary().getTotalPromoDiscount() : 0.00, orderItemList,checkoutCache.getPricingSummary().getTotalPrice());
 			LOGGER.info("split discount function return promo amount split on each sku  is : {}",promoDiscountSplitOnEachItem);
			
			LOGGER.info("calling the split discount fuction for platform discount split with order payable : {} and platform apllied discount: {}", checkoutCache.getPricingSummary().getTotalPrice(), checkoutCache.getPricingSummary().getTotalPlatformDiscount());
			Map<String, Double> platformDiscountSplitOnEachItem = discountSplitterOnEachProduct((null !=  checkoutCache.getPricingSummary().getTotalPlatformDiscount())? checkoutCache.getPricingSummary().getTotalPlatformDiscount():0.00, orderItemList,checkoutCache.getPricingSummary().getTotalPrice());
			LOGGER.info(" credit apllied : {} and merchandised credit applied is  {}", checkoutCache.getPricingSummary().getTotalCreditValue(), (null != checkoutCache.getCreditApplied()&& null != checkoutCache.getCreditApplied().get("Merchadise") )?checkoutCache.getCreditApplied().get("Merchandise") :0.00);
			
		LOGGER.info("calling the credit splitamont function and calculating the credit used on credit available: {}, order total : {} , promodiscount : {}, shipping charges: {}",checkoutCache.getPricingSummary().getTotalCreditValue(),checkoutCache.getPricingSummary().getTotalPromoDiscount() ,checkoutCache.getPricingSummary().getTotalPrice(),checkoutCache.getPricingSummary().getTotalDeliveryCharges());
		Map<String, Double> creditAmountSplitOnEachItem = discountSplitterOnEachProduct(
				   (null!=checkoutCache.getPricingSummary().getTotalCreditValue())
				   ? calculateTotalCreditUsed(checkoutCache):0.00, orderItemList,checkoutCache.getPricingSummary().getTotalPrice());
	       for(OrderedItemCache orderedItemCacheIt : orderItemList) {
			  OrderItemDTO orderItemDTO= new  OrderItemDTO();
			  orderItemDTO.setOrderId(orderId);
			  orderItemDTO.setSku(orderedItemCacheIt.getSku());
			  orderItemDTO.setQty(orderedItemCacheIt.getQuantity());
			  orderItemDTO.setOrderItemTotalAmount(orderedItemCacheIt.getRetailPrice());
		      LOGGER.info(" setting price retail price : {} , shipping charges : {} , promotion charges : {} , and credit apllied on item level : {} ",orderedItemCacheIt.getRetailPrice(),orderItemShippingChargesMap.get(orderedItemCacheIt.getSku()),promoDiscountSplitOnEachItem.get(orderedItemCacheIt.getSku()), creditAmountSplitOnEachItem.get(orderedItemCacheIt.getSku()));
			  orderItemDTO.setOrderItemPayable(orderedItemCacheIt.getRetailPrice()+ orderItemShippingChargesMap.get(orderedItemCacheIt.getSku()) - (promoDiscountSplitOnEachItem.get(orderedItemCacheIt.getSku())+creditAmountSplitOnEachItem.get(orderedItemCacheIt.getSku())));
			  orderItemDTO.setOrderItemPlatformOfferedDiscount(platformDiscountSplitOnEachItem.get(orderedItemCacheIt.getSku()));
			  orderItemDTO.setOrderItemCreditApplied(creditAmountSplitOnEachItem.get(orderedItemCacheIt.getSku()));
			  orderItemDTO.setOrderItemPromoDiscount(promoDiscountSplitOnEachItem.get(orderedItemCacheIt.getSku()));
			  orderItemDTO.setVendorPrice(orderedItemCacheIt.getVendorPrice()); 
			  orderItemDTO.setOuatPrice(orderedItemCacheIt.getOuatMargin());
			  orderItemDTO.setOrderStatusId((checkoutCache.getPricingSummary().getTotalOrderPayable()==0.00)?11:orderStatusId);
			  orderItemDTO.setOrderCreatedDate(null);
			  int edd = 0;
				try {
					edd = Integer.parseInt(orderedItemCacheIt.getEdd());
				} catch (Exception e) {
					edd = 1;
				}
			  orderItemDTO.setEstimatedShippedDate(calculateEstimatedShippedAndDeliveryDate((edd==0)?1:edd));
			  orderItemDTO.setEstimatedDeliveryDate(calculateEstimatedShippedAndDeliveryDate(((edd==0)?1:edd) + 4));
					
			  orderItemDTO.setOrderCreatedBy("DemandStore");
			  orderItemDTO.setUpdatedAt(null);
			  orderItemDTO.setUpdatedBy("PlaceOrderService");
			  orderItemDTO.setIsReturnable(orderedItemCacheIt.getIsReturnable());
			  orderItemDTO.setIsExchangable(orderedItemCacheIt.getIsExchangeable());
			  orderItemDTO.setParentOrderItemId(null);
			  orderItemDTO.setOrderItemShippingCharges(orderItemShippingChargesMap.get(orderedItemCacheIt.getSku()));
 			  orderItemDTOList.add(orderItemDTO);
		  }
		  return orderItemDTOList;
	  }
	
	
	private Double calculateTotalCreditUsed(CheckoutCache checkoutCache) {
		 return  Math.min(
				(null != checkoutCache.getPricingSummary().getTotalCreditValue())?
				   checkoutCache.getPricingSummary().getTotalCreditValue():0.00,
						   (checkoutCache.getPricingSummary().getTotalPrice() + (( null!=checkoutCache.getPricingSummary().getTotalDeliveryCharges() &&  checkoutCache.getPricingSummary().getTotalDeliveryCharges() !=0.00) ?	   
								   checkoutCache.getPricingSummary().getTotalDeliveryCharges():0.00)- ((( null != checkoutCache.getPricingSummary().getTotalPromoDiscount() && checkoutCache.getPricingSummary().getTotalPromoDiscount() !=0.00)?
										   checkoutCache.getPricingSummary().getTotalPromoDiscount() :0.00))
						   ));
	}
	
	public java.sql.Date calculateEstimatedShippedAndDeliveryDate(Integer edd) {
		java.sql.Date now = new java.sql.Date( new java.util.Date().getTime() );
		LOGGER.info("setting shipped date or delivery delevry date for the edd : {}" , edd);	
		java.sql.Date eddDate= new java.sql.Date(now.getTime() + (edd*24*60*60*1000));
		LOGGER.info("shipped date or delivery delevry date  set : {} for the edd : {}" ,eddDate, edd);	
		return eddDate;
	}
	private OrderShippmentDetailDTO buildOrderShippmentDetailDTO(Integer addressId, Integer orderId ) {
		  OrderShippmentDetailDTO orderShippmentDetailDTO = new OrderShippmentDetailDTO();
		  orderShippmentDetailDTO.setAddressId(addressId);
		  orderShippmentDetailDTO.setOrderId(orderId);
		  return orderShippmentDetailDTO;
	  }
    public Map<String, Double>discountSplitterOnEachProduct(Double discountAmount, List<OrderedItemCache> orderSummary, Double totalOrder ) {
        Double totalPercentOndiscountAmount = (discountAmount*(100))/(totalOrder );
        Map<String, Double> discountSplitMap = new HashMap<String, Double>();
        for(OrderedItemCache it : orderSummary) {
        	Double splittedAmount = (totalPercentOndiscountAmount*(it.getRetailPrice()))/100.0;
        	discountSplitMap.put(it.getSku(), splittedAmount);
        }
        return discountSplitMap;
    }
	public InventoryServiceRequest getSkuAndQty(CheckoutCache checkoutCache) {
		InventoryServiceRequest inventoryServiceRequest = new InventoryServiceRequest();
		List<SkuAndQty>skuAndQtyList = new ArrayList<SkuAndQty>();
		for(OrderedItemCache it : checkoutCache.getOrderSummary()) {
			SkuAndQty skuandQty = new SkuAndQty();
			skuandQty.setQty(it.getQuantity());
			skuandQty.setSku(it.getSku());
			skuAndQtyList.add(skuandQty);
		}
		inventoryServiceRequest.setSkuAndQtyList(skuAndQtyList);
		return inventoryServiceRequest;
	}
    @Transactional(rollbackFor = {BusinessProcessException.class})
	public DemandStoreAPIResponse orderStatus(Integer orderId, CustomerDetailVO customerDetail, String paymentStatus, PaymentGatewayDetailRequest paymentGatewayDetailRequest) throws BusinessProcessException {
    	CheckoutCache checkoutCache = checkoutCacheData.getCheckOutCache(customerDetail.getCustomerId().intValue());
		if("PF".equals(paymentStatus)) {
			downStreamApiCAllForrevertInventory(checkoutCache, customerDetail, true);
		}
		
		String orderNumber =  placeOrderRepository.fetchOrderNumberFromOrderId(orderId);
    	Integer rowAffected = placeOrderRepository.orderStatus(orderId, ("PF".equals(paymentStatus))?2:11, customerDetail.getCustomerId().intValue()); 
    	if (rowAffected > 0) {
			if(null != paymentGatewayDetailRequest) {
				placeOrderRepository.savePaymentGatewayResponse(orderId , paymentGatewayDetailRequest);
			}
			if(!("PF".equals(paymentStatus))) {
				downStreamAPIcall(checkoutCache, orderId ,"ONLINE" ,customerDetail, true, orderNumber);
 			}
			return new DemandStoreAPIResponse(CommonConstant.SUCCESS_FLAG, null, CommonConstant.SUCCESS_STATUS_CODE,orderId);
		} else {
			LOGGER.info("Some error occured while updating order status : {} ", orderId);
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG_WHILE_UPDATING_ORDER_STATUS,CommonConstant.FAILURE_STATUS_CODE);
		}
	}
	
  
	public CustomerCreditHistory buildCustomerCreditHistoryRequest(CheckoutCache checkoutCache, Integer customerId,
			Integer orderId) {
		CustomerCreditHistory customerCreditHistory = new CustomerCreditHistory();
		List<String> skuList = new ArrayList<>();
		for(OrderedItemCache orderitem: checkoutCache.getOrderSummary()) {
			skuList.add(orderitem.getSku());
		}
		List<OrderItemCreditDetail> orderItemCreditDetailList = placeOrderRepository.getOrderItemDetail(skuList,orderId);
		LOGGER.info(" order item credit list fetched from db : {} ", orderItemCreditDetailList.toString());
		customerCreditHistory.setOrderItemCreditDetail(orderItemCreditDetailList);
		customerCreditHistory.setCustomerId((long) customerId);
		customerCreditHistory.setOrderId((long) orderId);
		customerCreditHistory.setTotalCreditUsed(calculateTotalCreditUsed(checkoutCache));
		customerCreditHistory.setType(getTypeOfCreditUsed(checkoutCache));
		return customerCreditHistory;
	}
	private String getTypeOfCreditUsed(CheckoutCache checkoutCache) {
	 	 Map<String, Double> creditApplied = checkoutCache.getCreditApplied();
 		 if(creditApplied.get("M") != 0) return "M";
		 else if (creditApplied.get("L") != 0)return "L";
		 LOGGER.info("credit applied : {}", creditApplied);
		 return null;
    }
	private boolean checkOutValidation(CheckoutCache checkoutCache, Integer customerId)
			throws BusinessProcessException {
		if (null == checkoutCache) {
			LOGGER.info("checout is empty for the customer_id : {}", customerId);
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG_WHILE_UPDATING_ORDER_STATUS,
					CommonConstant.FAILURE_STATUS_CODE);
		} else if (null == checkoutCache.getAddressId() || checkoutCache.getAddressId() == 0) {
			LOGGER.info("address_id is 0 or null for the customer_id : {} ", customerId);
			throw new BusinessProcessException(CommonConstant.ADDRESS_IS_NOT_SELECTED, CommonConstant.FAILURE_STATUS_CODE);
		} else if (null == checkoutCache.getOrderSummary() || checkoutCache.getOrderSummary().isEmpty()) {
			LOGGER.info("order summary is null or empty for the customer _id : {} ", customerId);
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG_WHILE_UPDATING_ORDER_STATUS,
					CommonConstant.FAILURE_STATUS_CODE);

		}else if (null != checkoutCache.getOrderSummary() && !checkoutCache.getOrderSummary().isEmpty()) {
				for(OrderedItemCache it : checkoutCache.getOrderSummary()) {
				if(it.getQuantity()<=0) {
					LOGGER.info("qty is negative for the sku  : {} ",  it.getSku());
					throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG_WHILE_UPDATING_ORDER_STATUS,
							CommonConstant.FAILURE_STATUS_CODE);
				}
			}
		}
		else if (null == checkoutCache.getPaymentMode() || checkoutCache.getPaymentMode().isEmpty()) {
			LOGGER.info("paymentmode is null or empty for the customer_id: {} ", customerId);
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG_WHILE_UPDATING_ORDER_STATUS,
					CommonConstant.FAILURE_STATUS_CODE);
		}
		
		if (checkoutCache.getPricingSummary().getTotalOrderPayable() <= 0.0 && ( null == checkoutCache.getPromoCode() || "".equals(checkoutCache.getPromoCode().trim()))) {
			Map<String, Double> creditCache = checkoutCache.getCreditApplied();
			if(creditCache == null || creditCache.isEmpty()) {
				LOGGER.info("Issue with pricing summary for cusotmer id : {} ", customerId);
				throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG_WHILE_UPDATING_ORDER_STATUS, CommonConstant.FAILURE_STATUS_CODE);
			}
//			if(null == creditCache.get("Merchandise") || 0.0 == creditCache.get("Merchandise")) {
//				LOGGER.info("Issue with pricing summary for cusotmer id : {} ", customerId);
//				throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG_WHILE_UPDATING_ORDER_STATUS, CommonConstant.FAILURE_STATUS_CODE);
//      }			
 
		}
		
		return true;
	}
	public String setPlaceOrderDate(){
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MMMM-yyyy");
     	Date date = new Date();
        String dateString = format2.format(date);
        dateString = dateString.replace("-", " "); 
        return (dateString);
    }
    public PlaceOrderAlertRequest buildPlaceOrderAlert(CustomerDetailVO customerDetail,CheckoutCache checkoutCache, Integer orderId , String paymentMode, String orderNumber) {
    	PlaceOrderAlertRequest placeOrderAlert = new PlaceOrderAlertRequest();
    	placeOrderAlert.setCustomerId(customerDetail.getCustomerId());
    	DeliveryAddressPlaceOrderAlert address = buildDeliveryAddressForPlaceOrderAlert(checkoutCache);
      	placeOrderAlert.setBillingAndShippingDetail(address);
    	placeOrderAlert.setCustomerName((null == customerDetail.getName()) ? address.getFullName(): customerDetail.getName());
    	placeOrderAlert.setMobile((null == customerDetail.getMobile())?address.getMobile():customerDetail.getMobile());
    	placeOrderAlert.setOrderId(orderNumber);  
    	placeOrderAlert.setPlaceOrderDateTime(setPlaceOrderDate());
    	if(checkoutCache.getPromoCode()==null || checkoutCache.getPromoCode().isEmpty()) {
        	placeOrderAlert.setCoupon("");
    	}else {
        	placeOrderAlert.setCoupon(checkoutCache.getPromoCode());
    	}
     	placeOrderAlert.setPaymentMethod((checkoutCache.getPricingSummary().getTotalOrderPayable()==0.00)?  PaymentMethodForPlaceOrderAlert.OWP: PaymentMethodForPlaceOrderAlert.valueOf(paymentMode));
     	placeOrderAlert.setProductItemDetailList(buildOrderItemDetailForPlaceOrder(checkoutCache));
       	placeOrderAlert.setPriceSummary(buildPriceSummaryForOrderAlert(checkoutCache));
       	String email = customerDetail.getEmail();
       	if(email == null) {
       		email = checkoutCache.getOrderConfirmationEmail();
       	}
      	placeOrderAlert.setEmailSendRequest(buildEmailSentRequestForOrderAlert(email , orderNumber));
      	LOGGER.info("place order request : {}", placeOrderAlert.toString());
    	return placeOrderAlert;
    }

	private EmailSendRequest buildEmailSentRequestForOrderAlert(String customerEmail, String orderNumber) {
		EmailSendRequest emailSendRequest = new EmailSendRequest();
     	emailSendRequest.setFromEmail("info@mail.taggd.com");
     	emailSendRequest.setFromNickName("taggd");
     	emailSendRequest.setMessageBody("Congratulations your order has been placed..!");
     	emailSendRequest.setSubject("ORDER CONFIRMATION Your Order Number "+ orderNumber);
     	List<String> toEmailAddressList = new ArrayList<String>();
     	toEmailAddressList.add(customerEmail);
     	emailSendRequest.setToEmailAddress(toEmailAddressList);
		return emailSendRequest;
	}

	private PriceSummaryForPlaceOrderAlert buildPriceSummaryForOrderAlert(CheckoutCache checkoutCache) {
		PriceSummaryForPlaceOrderAlert priceSummaryForPlaceOrder = new PriceSummaryForPlaceOrderAlert();
		Long promoDiscount = 0L;
		if(checkoutCache.getPromoCode()==null || checkoutCache.getPromoCode().isEmpty() || null== checkoutCache.getPricingSummary().getTotalPromoDiscount()) {
	     	priceSummaryForPlaceOrder.setCouponDiscount(0.00);
    	}else {
    		promoDiscount = Math.round(checkoutCache.getPricingSummary().getTotalPromoDiscount());
    	}
     	priceSummaryForPlaceOrder.setCouponDiscount(promoDiscount.doubleValue());
     	Long grandtotal = 0L;
     	if(null != checkoutCache.getPricingSummary().getTotalOrderPayable()) {
     		grandtotal = Math.round(checkoutCache.getPricingSummary().getTotalOrderPayable());
     	}
     	priceSummaryForPlaceOrder.setGrandTotal(grandtotal.doubleValue());
     	if(null==checkoutCache.getPricingSummary().getTotalDeliveryCharges()) {
         	priceSummaryForPlaceOrder.setShippingAndHandling(0.00);
     	}else {     	
     		priceSummaryForPlaceOrder.setShippingAndHandling(checkoutCache.getPricingSummary().getTotalDeliveryCharges());
     	}
     	if(null != checkoutCache.getCreditApplied() && ! checkoutCache.getCreditApplied().isEmpty()) {
     		priceSummaryForPlaceOrder.setCreditApllied(calculateTotalCreditUsed(checkoutCache));
     	}
     	if(null != checkoutCache.getPricingSummary().getTotalPrice()) {
     		priceSummaryForPlaceOrder.setSubTotal(new Long(Math.round(checkoutCache.getPricingSummary().getTotalPrice())).doubleValue());
     	}
		return priceSummaryForPlaceOrder;
	}
	private List<ProductItemDetailForPlaceOrderAlert> buildOrderItemDetailForPlaceOrder(CheckoutCache checkoutCache) {
		List<ProductItemDetailForPlaceOrderAlert>productItemDetailList= new ArrayList<>();
    	for(OrderedItemCache it : checkoutCache.getOrderSummary()) {
    		ProductItemDetailForPlaceOrderAlert productItemDetailForPlaceOrderAlert = new ProductItemDetailForPlaceOrderAlert();
    		productItemDetailForPlaceOrderAlert.setItemImageUrl(it.getDefaultImageUrl());
    		productItemDetailForPlaceOrderAlert.setProductName(it.getProductName());
    		productItemDetailForPlaceOrderAlert.setSku(it.getSku());
    		productItemDetailForPlaceOrderAlert.setOuatMargin(it.getOuatMargin());
    		if(it.getSize() == null ) {
        		productItemDetailForPlaceOrderAlert.setSize("N/A");
    		}else {
        		productItemDetailForPlaceOrderAlert.setSize(it.getSize());
    		}
    		productItemDetailForPlaceOrderAlert.setQty(it.getQuantity());
    		productItemDetailForPlaceOrderAlert.setPrice(it.getRetailPrice());
    		productItemDetailList.add(productItemDetailForPlaceOrderAlert);
    	}
		return productItemDetailList;
	}
 
	private DeliveryAddressPlaceOrderAlert buildDeliveryAddressForPlaceOrderAlert(CheckoutCache checkoutCache) {
		DeliveryAddressPlaceOrderAlert deliveryAddressPlaceOrderAlert = new DeliveryAddressPlaceOrderAlert();
		Integer addressId = checkoutCache.getAddress().getSelectedAddressId();
		for(AddressDetailDto address : checkoutCache.getAddress().getAddresses()) {
			if(address.getAddressId().equals(addressId)) {
				deliveryAddressPlaceOrderAlert.setFullName(address.getFullName());
		    	deliveryAddressPlaceOrderAlert.setPincode(address.getPincode());
		    	deliveryAddressPlaceOrderAlert.setLandmark(address.getLandmark());
		    	deliveryAddressPlaceOrderAlert.setCity(address.getCity());
		    	deliveryAddressPlaceOrderAlert.setState(address.getState());
		    	deliveryAddressPlaceOrderAlert.setMobile(address.getMobile());
		    	deliveryAddressPlaceOrderAlert.setAddress(address.getAddress());
		    	return deliveryAddressPlaceOrderAlert;
			}
		}
    	return null;
	}
    
	@Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = {BusinessProcessException.class})
	public DemandStoreAPIResponse placeOrderForGuestCheckout(String paymentMode, CustomerDetailVO customerDetail, Utm utm,  HttpHeaders downStreamHeaders, String uuid) throws Exception {
		List<MessageDetail> messageDetailList = new ArrayList<>();
 		Integer orderStatusId =  paymentMode.equals("COD") ? 11 : 1;//11 is for OR and 1 is WP
		List<String>key = new ArrayList<>();
 		key.add(null != customerDetail.getCustomerId()? customerDetail.getCustomerId().toString() : uuid);
		DemandStoreAPIResponse  response = null;
		try {
			if(!lock.acquireLock(key,Lock.PLACEORDER)) {
				throw new BusinessProcessException( CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
			}
            LOGGER.info("lock acquire");
    		LOGGER.info("getting item from checkout cache");
    		GuestCheckoutCache guestCheckoutCache = checkoutCacheData.getGuestCheckOutCache(customerDetail.getDeviceId());
    		
    		AddGuestUserResponse addGuestUserResponse = customerServiceClient.addGuestUser(guestCheckoutCache.getAddress(), guestCheckoutCache.getMobileNo());
    		if(null == addGuestUserResponse || null == addGuestUserResponse.getAddressId() || null == addGuestUserResponse.getCustomerId() || addGuestUserResponse.getAddressId()==0 || addGuestUserResponse.getCustomerId()==0) {
    			throw new BusinessProcessException(CommonConstant.MOBILE_ALREADY_EXIST_WITH_DIFF_ACC,CommonConstant.FAILURE_STATUS_CODE);
    		}
    		setCustomerDetailImportantAttribute(customerDetail, guestCheckoutCache, addGuestUserResponse);
    		CheckoutCache checkoutCache = buildCheckoutCache(guestCheckoutCache, addGuestUserResponse);
    		checkoutCacheData.setCheckOutCach(checkoutCache, addGuestUserResponse.getCustomerId());
     		LOGGER.info("Checkout Cache : {} ", checkoutCache.toString());
    		if(!checkOutValidation(checkoutCache, customerDetail.getCustomerId().intValue())) {
    			demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.SOMETHING_WENT_WRONG, messageDetailList);
    		    return demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.FAILURE_STATUS_CODE, null);
    		}
    		OrderIdAndOrderPayable orderIdAndOrderPayable = dtoBuilderAndInsertInOrdersSchema(paymentMode, customerDetail.getCustomerId().intValue(), checkoutCache, orderStatusId, utm, customerDetail.getDeviceType(), downStreamHeaders);
    		
    		downStreamApiCAllForinventoryUpdate(checkoutCache, customerDetail, false);
    		if(null != paymentMode && paymentMode.equals("COD")){
    		    String orderNumber = placeOrderRepository
                        .fetchOrderNumberFromOrderId(orderIdAndOrderPayable.getOrderId());
                downStreamAPIcall(checkoutCache, orderIdAndOrderPayable.getOrderId(), "COD",
                        customerDetail, true, orderNumber);
    		}
    		demandStoreAPIResponseHelper.setMessageDetailList(MessageType.INFO, CommonConstant.ORDER_PLACED, messageDetailList);
    		response =  demandStoreAPIResponseHelper.apiResponse(messageDetailList,CommonConstant.SUCCESS_FLAG,CommonConstant.SUCCESS_STATUS_CODE, buildPlaceOrderResponse(paymentMode, orderIdAndOrderPayable));
        }finally {
            lock.releaseLock(key);
            LOGGER.info("lock release");
        }
		return response;
	}
	private void setCustomerDetailImportantAttribute(CustomerDetailVO customerDetail,
			GuestCheckoutCache guestCheckoutCache, AddGuestUserResponse addGuestUserResponse) {
		customerDetail.setCustomerId(addGuestUserResponse.getCustomerId().longValue());
		customerDetail.setName(guestCheckoutCache.getAddress().getFullName());
		customerDetail.setMobile(guestCheckoutCache.getMobileNo());
		customerDetail.setEmail(guestCheckoutCache.getAddress().getEmail());
	}
	private CheckoutCache buildCheckoutCache(GuestCheckoutCache guestCheckoutCache,
			AddGuestUserResponse addGuestUserResponse) {
		CheckoutCache checkoutCache = new CheckoutCache();
		checkoutCache.setAddressId(addGuestUserResponse.getAddressId());
		checkoutCache.setOrderSummary(guestCheckoutCache.getOrderSummary());
		checkoutCache.setPricingSummary(guestCheckoutCache.getPricingSummary());
		checkoutCache.setOrderConfirmationEmail(guestCheckoutCache.getAddress().getEmail());
		checkoutCache.setCreditApplied(null);
		checkoutCache.setPromoCode(guestCheckoutCache.getPromoCode());
		checkoutCache.setPaymentMode(guestCheckoutCache.getPaymentMode());
		AddressDto addressdto = buildAddressDto(guestCheckoutCache, addGuestUserResponse);
		checkoutCache.setAddress(addressdto);
		return checkoutCache;
	}
	private AddressDto buildAddressDto(GuestCheckoutCache guestCheckoutCache,
			AddGuestUserResponse addGuestUserResponse) {
		AddressDto addressdto = new AddressDto();
		addressdto.setSelectedAddressId(addGuestUserResponse.getAddressId());
		addressdto.setAddresses(buildAddressDetailDto(guestCheckoutCache, addGuestUserResponse.getAddressId()));
		return addressdto;
	}

	private List<AddressDetailDto> buildAddressDetailDto(GuestCheckoutCache guestCheckoutCache, Integer AddressId) {
		AddressDetailDto addressDetailDto = new AddressDetailDto();		
	    addressDetailDto.setFullName(guestCheckoutCache.getAddress().getFullName());
	    addressDetailDto.setPincode(guestCheckoutCache.getAddress().getPincode());
	    addressDetailDto.setAddress(guestCheckoutCache.getAddress().getAddress());
	    addressDetailDto.setLandmark(guestCheckoutCache.getAddress().getLandmark());
	    addressDetailDto.setCity(guestCheckoutCache.getAddress().getCity());
	    addressDetailDto.setState(guestCheckoutCache.getAddress().getState());
	    addressDetailDto.setMobile(guestCheckoutCache.getAddress().getMobile());
	    addressDetailDto.setAddressId(AddressId);
	    addressDetailDto.setIsSelected(true);
	    
		List<AddressDetailDto> addressDetailList = new ArrayList<>();
		addressDetailList.add(addressDetailDto);
	    return addressDetailList;
	}
	
	  @Transactional(rollbackFor = {BusinessProcessException.class})
	   	public DemandStoreAPIResponse orderV1status(Integer orderId,String deviceID, String paymentStatus, PaymentGatewayDetailRequest paymentGatewayDetailRequest) throws BusinessProcessException, DownStreamException {
		  	LOGGER.info("order ID  : {}, deviceID : {}, paymentStatus: {} , request Body : {}", orderId, deviceID, paymentStatus,  paymentGatewayDetailRequest);
	       	if(!validateOrderstatusV1Req(deviceID,orderId, paymentStatus, paymentGatewayDetailRequest)) {
	   			return new DemandStoreAPIResponse(CommonConstant.SUCCESS_FLAG, null, CommonConstant.FAILURE_STATUS_CODE,null);
	       	}
		  	Integer customerId = placeOrderRepository.getCustomer(orderId);
		  	LOGGER.info("customer id : {}", customerId);

		  	if(null == customerId) {
	   			return new DemandStoreAPIResponse(CommonConstant.SUCCESS_FLAG, null, CommonConstant.FAILURE_STATUS_CODE,null);
		  	}
		  	
		  	CustomerDetailVO customerDetail = new CustomerDetailVO();
		  	customerDetail.setCustomerId(customerId.longValue());
		  	customerDetail.setDeviceId(deviceID);
		  	
	       	CheckoutCache checkoutCache = checkoutCacheData.getCheckOutCache(customerId);
		  	LOGGER.info("cache : {}", checkoutCache);
	       	if(null == checkoutCache) {
	   			return new DemandStoreAPIResponse(CommonConstant.SUCCESS_FLAG, null, CommonConstant.FAILURE_STATUS_CODE,null);
	       	}
	       	Boolean isSuccess = fetchRazorpaymentStatus(orderId, Utility.generateTransactionId());
	       	if(isSuccess ) {
	       		Integer rowAffected = placeOrderRepository.orderStatusV1(orderId, ("PF".equals(paymentStatus))?2:11, customerId); 
	       		
		       	if (rowAffected > 0) {
		       		if(validateOrderStatusv1ReqBody(paymentGatewayDetailRequest)) {
		   				placeOrderRepository.savePaymentGatewayResponse(orderId , paymentGatewayDetailRequest);
 
		       		}
		       		String orderNumber = null;
                    if ("PS".equals(paymentStatus)) {
                          orderNumber = placeOrderRepository
                                .fetchOrderNumberFromOrderId(orderId);
                        downStreamAPIcall(checkoutCache, orderId, "ONLINE", customerDetail, true, orderNumber);
                    }
        		  	LOGGER.info("order status updated successfully");
		   			return new DemandStoreAPIResponse(CommonConstant.SUCCESS_FLAG, null, CommonConstant.SUCCESS_STATUS_CODE, orderNumber);
		   		} else {
		   			LOGGER.info("Some error occured while updating order status : {} ", orderId);
		   			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG_WHILE_UPDATING_ORDER_STATUS,CommonConstant.FAILURE_STATUS_CODE);
		   		}
	       	}else {
	       		downStreamApiCAllForrevertInventory(checkoutCache, customerDetail, true);
	       	}
		  	LOGGER.info("order status not updated successfully");
   			return new DemandStoreAPIResponse(CommonConstant.SUCCESS_FLAG, null, CommonConstant.FAILURE_STATUS_CODE,null);
	   	}

	private boolean validateOrderStatusv1ReqBody(PaymentGatewayDetailRequest paymentGatewayDetailRequest) {
		return (null != paymentGatewayDetailRequest &&  
				null != paymentGatewayDetailRequest.getRazorpayOrderId() && !paymentGatewayDetailRequest.getRazorpayOrderId().isEmpty() &&
				null != paymentGatewayDetailRequest.getRazorpayPaymentId() && !paymentGatewayDetailRequest.getRazorpayPaymentId().isEmpty()&& 
			    null != paymentGatewayDetailRequest.getRazorpaySignature() && !paymentGatewayDetailRequest.getRazorpaySignature().isEmpty());
	}

	private Boolean validateOrderstatusV1Req(String deviceID, Integer orderId, String paymentStatus, PaymentGatewayDetailRequest paymentGatewayDetailRequest) {
		if( 
				null != deviceID  && !deviceID.isEmpty() &&
				null != orderId && orderId != 0 &&
				null != paymentStatus && !paymentStatus.isEmpty()	
				) {
			LOGGER.info("request successfully validated");
			return true;
		}
		LOGGER.info("request not validated");
	return false;
	}
	
 	public Boolean fetchRazorpaymentStatus(Integer orderId, String trid) throws DownStreamException {
		try {
			if(orderId.equals("")) {
				return Boolean.FALSE;
			}
			String razorPayorderId = placeOrderRepository.fetchUnattemptedRazorpayOrders(orderId);
			if(razorPayorderId == null) {
				return Boolean.FALSE;
			}
			LOGGER.info("Going to request Razorpay : {}", orderId);
			 RazorPayCreateOrderResponseDto response = paymentGatewayClient.checkStatusOfOrderOnRazorpayResponse(razorPayorderId, trid);
			if (response.getStatus().equals("paid")) {
				LOGGER.info("Payment success : {} ", orderId);
				 return Boolean.TRUE;
			} else {
				LOGGER.error("Payment failed : {} ", orderId);
				return Boolean.FALSE;
			}
		} catch (DownStreamException e) {
			LOGGER.error("Error while making api call : {} ", orderId);
		}
		return Boolean.FALSE;		 
		 
	}


	public DemandStoreAPIResponse createCustomerOnRazorpay(PaymentGatewayCustomerDetail paymentGatewayCustomerDetail,
			Long customerId, String transactionId) throws BusinessProcessException { 
		String razorpayCustomerId  = placeOrderRepository.fetchRazorpayCustomerId(customerId);
		LOGGER.info("razorpayId : {}", razorpayCustomerId);
		if (null == razorpayCustomerId) {
			validateCustomerRequest(paymentGatewayCustomerDetail.getEmail(), paymentGatewayCustomerDetail.getContact());
			RazorPayCreateOrderResponseDto razorCreateOrderResponse = null;
			try {
				razorCreateOrderResponse = paymentGatewayClient.createCustomerOnRazorpay(paymentGatewayCustomerDetail,
						transactionId);
				LOGGER.info("razorCreateOrderResponse : {}", razorCreateOrderResponse);
			} catch (DownStreamException e) {
				throw new BusinessProcessException("Oh no ! Looks like payment gateway not working",
						CommonConstant.FAILURE_STATUS_CODE);
			}

			PaymentGatewayDetailRequest request = new PaymentGatewayDetailRequest();
			request.setRazorpayOrderId(razorCreateOrderResponse.getId());
			placeOrderRepository.savePaymentGatewayResponse(customerId, razorCreateOrderResponse.getId(),
					transactionId);
			razorpayCustomerId = razorCreateOrderResponse.getId();
		}
		Map<String, Object> responseData = new HashMap<>();
		responseData.put("pg_customer_id", razorpayCustomerId);
		return new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, responseData);
	}
	
	
	public void validateCustomerRequest(String email, String mobile) throws BusinessProcessException {

		if (!isValidEmail(email)) {
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST_PARAM, CommonConstant.SUCCESS_STATUS_CODE);
		}

		if (!isValidMobile(mobile)) {
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST_PARAM, CommonConstant.SUCCESS_STATUS_CODE);
		}
		
	}
	
	private boolean isValidMobile(String emailOrPhone) {
		return CommonConstant.MOBILE_NUMBER_PATTERN.matcher(emailOrPhone).matches();
	}


	private boolean isValidEmail(String email) {
		return CommonConstant.EMAIL_PATTERN.matcher(email).matches();
	}
	
	public String callBackUrlForRazorPay(HttpServletRequest request) throws IOException {

		String bodyInStringFormat = readInputStreamInStringFormat(request.getInputStream(), Charset.forName(request.getCharacterEncoding()));

    	List<String> razorpayBodyList = new ArrayList<>();

    	String[] bodyArr = bodyInStringFormat.split("&");
    	for (int i = 0; i < bodyArr.length; i++) {
    		razorpayBodyList.add(bodyArr[i]);
    	}
    	
    	if(!razorpayBodyList.get(0).split("=")[1].equals("BAD_REQUEST_ERROR")) {
    		String orderid = placeOrderRepository.fetchRazorpayOrders(razorpayBodyList.get(1).split("=")[1]);
    		return "razorpay_payment_id="+razorpayBodyList.get(0).split("=")[1]+"&razorpay_order_id="+razorpayBodyList.get(1).split("=")[1]+"&razorpay_signature="+
    		razorpayBodyList.get(2).split("=")[1]+"&orderid="+orderid+"&status=success";
    		
    	}else {
    		String failedBody = razorpayBodyList.get(5).split("=")[1];
    		ObjectMapper mapper = new ObjectMapper();
    	
    		PaymentGatewayFailedRequest failedRequest = mapper.readValue(URLDecoder.decode(failedBody), PaymentGatewayFailedRequest.class);
    		
    		String orderid = placeOrderRepository.fetchRazorpayOrders(failedRequest.getOrderId());
    		return "razorpay_payment_id="+razorpayBodyList.get(0).split("=")[1]+"&razorpay_order_id="+razorpayBodyList.get(1).split("=")[1]+"&razorpay_signature="+
    		razorpayBodyList.get(2).split("=")[1]+"&orderid="+orderid+"&status=failed";
    	}
	}
	
	private String readInputStreamInStringFormat(InputStream stream, Charset charset) throws IOException {
        final int MAX_BODY_SIZE = 4096;
        final StringBuilder bodyStringBuilder = new StringBuilder();
        if (!stream.markSupported()) {
          stream = new BufferedInputStream(stream);
        }

        stream.mark(MAX_BODY_SIZE + 1);
        final byte[] entity = new byte[MAX_BODY_SIZE + 1];
        final int bytesRead = stream.read(entity);

        if (bytesRead != -1) {
          bodyStringBuilder.append(new String(entity, 0, Math.min(bytesRead, MAX_BODY_SIZE), charset));
          if (bytesRead > MAX_BODY_SIZE) {
            bodyStringBuilder.append("...");
          }
        }
        stream.reset();

        return bodyStringBuilder.toString();
      }
    


}
