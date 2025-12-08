package com.ouat.myOrders.service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ouat.myOrders.CommonConstant.CommonConstant;
import com.ouat.myOrders.DTO.OrderDetailDTO;
import com.ouat.myOrders.DTO.OrderListDTO;
import com.ouat.myOrders.DTO.ProductNameAndProductImgUrlDTO;
import com.ouat.myOrders.client.CustomerServiceClient;
import com.ouat.myOrders.client.CuttlyShortUrlClient;
import com.ouat.myOrders.client.PlpPdpServiceClient;
import com.ouat.myOrders.exception.BusinessProcessException;
import com.ouat.myOrders.helper.OrderStatusAndOrderStatusDate;
import com.ouat.myOrders.repository.MyOrdersRepository;
import com.ouat.myOrders.response.ActionButton;
import com.ouat.myOrders.response.ActionType;
import com.ouat.myOrders.response.AdditionalDetail;
import com.ouat.myOrders.response.Address;
import com.ouat.myOrders.response.ItemDetail;
import com.ouat.myOrders.response.MileStones;
import com.ouat.myOrders.response.MyOrders;
import com.ouat.myOrders.response.OrderConfirmation;
import com.ouat.myOrders.response.OrderDetailResponse;
import com.ouat.myOrders.response.OrderItemDetail;
import com.ouat.myOrders.response.OrderItemDetailForOrderConfirmation;
import com.ouat.myOrders.response.OrderListItemDetail;
import com.ouat.myOrders.response.OrderListResponse;
import com.ouat.myOrders.response.OrderStatus;
import com.ouat.myOrders.response.PriceSummary;
import com.ouat.myOrders.response.RecordType;
import com.ouat.myOrders.response.TrackingDetail;
import com.ouat.myOrders.vo.CustomerDetailVO;

@Service
public class MyOrderService{
	public static final Logger LOGGER = LoggerFactory.getLogger(MyOrderService.class);
	
	@Value("${order.confirmation.banner}")
	private String bannerImage;
	@Autowired
	MyOrdersRepository  myOrdersRepository;
	@Autowired
	PlpPdpServiceClient plpPdpServiceClient;
	@Autowired
	CustomerServiceClient customerServiceClient;
	
	@Autowired
	CuttlyShortUrlClient cuttlyClient;
	
	@Value("${is.referral}")
	private boolean referralFlag;
	
	public MyOrders orderListDetail(long customerId, Integer pageNo) throws BusinessProcessException {
		Map<Integer, List<OrderListDTO>> orderList = myOrdersRepository.getOrderListDetail(customerId, pageNo);
		if(orderList == null || orderList.isEmpty()) {
			LOGGER.warn("ordder list is empty for the customer Id  : {}", customerId);
			return null;
		}
		Map<String, ProductNameAndProductImgUrlDTO> map = getProductNameAndProductImageUrl(orderList);
		if(map.isEmpty() || map==null) {
			return null;
		}
 		Map<String, ProductNameAndProductImgUrlDTO> productNameAndProductImgUrl = getProductNameAndProductImageUrl(orderList);
		List<OrderListResponse> orderListResponse = new ArrayList<OrderListResponse>();
		boolean isOrderTypeAdded = false;
		for(Entry<Integer, List<OrderListDTO>> entry : orderList.entrySet()) {
			OrderListResponse orderListDetailResponse = new OrderListResponse();
			orderListDetailResponse.setOrderId(entry.getKey());
			orderListDetailResponse.setOrderNumber(entry.getValue().get(0).getOrderNumber());
			orderListDetailResponse.setOrderDate( setPlaceOrderDate(entry.getValue().get(0).getOrderDate())); 
			orderListDetailResponse.setOrderTotalPayAmount(entry.getValue().get(0).getOrderPayAmount());
			List<OrderListItemDetail> orderDetailResponseList = new ArrayList<OrderListItemDetail>();
			List<OrderListDTO> list = entry.getValue();
			for(OrderListDTO orderListIterator : list) {
				OrderListItemDetail orderItemDetailResponse = new OrderListItemDetail(); 
				if(productNameAndProductImgUrl.get(orderListIterator.getSku())!=null) {
					orderItemDetailResponse.setItemName(productNameAndProductImgUrl.get(orderListIterator.getSku()).getItemName());
				}
				if(productNameAndProductImgUrl.get(orderListIterator.getSku())!= null) {
					orderItemDetailResponse.setThumbNailImageUrl(productNameAndProductImgUrl.get(orderListIterator.getSku()).getItemImageUrl());
				}
				orderItemDetailResponse.setItemPayable(orderListIterator.getOrderItemPayAmount());
				OrderStatusAndOrderStatusDate orderStatusAndOrderStatusDate = setOrderStatusAndOrderStatusDate(orderListIterator.getOrderStatus(),orderListIterator.getIsRefundRequired(),orderListIterator.getIsRefunded(),orderListIterator.getRecordType(),
						orderListIterator.getOrderReceavedDate(),orderListIterator.getOrderCancelledOrRefundRaisedDate(),orderListIterator.getOrderRefundedDate(),orderListIterator.getOrderDate(),orderListIterator.getOrderShippedDate(),orderListIterator.getOrderDeliveredDate(), orderListIterator.getRtoDate(), orderListIterator.getRtoDeliverDate(),orderListIterator.getRtoTrackingNumber());
				orderItemDetailResponse.setOrderStatus(orderStatusAndOrderStatusDate.getOrderStatus());
				orderItemDetailResponse.setOrderStatusDate(orderStatusAndOrderStatusDate.getOrderStatusDate());
				orderItemDetailResponse.setQty(orderListIterator.getQty());
				if(!isOrderTypeAdded) {
					orderListDetailResponse.setOrderType(orderListIterator.getOrderType());
					isOrderTypeAdded = true;
				}
				orderItemDetailResponse.setOrderType(orderListIterator.getOrderType());
				orderDetailResponseList.add(orderItemDetailResponse);
			}
			orderListDetailResponse.setOrderResponseList(orderDetailResponseList);
			orderListResponse.add(orderListDetailResponse);
			isOrderTypeAdded = false;
		}
		MyOrders myorder = new MyOrders();
		LOGGER.info("setting order list detail into my order with order list : {}", orderListResponse);
		myorder.setOrderListDetailResponse(orderListResponse.stream().sorted(Comparator.comparing(OrderListResponse::getOrderId).reversed()).collect(Collectors.toList()));
 		myorder.setNumberOfOrder(myOrdersRepository.getOrderListCount(customerId));
 		return myorder;
 	}
	public String setPlaceOrderDate(Date date){
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MMMM-yyyy");
         String dateString = format2.format(date);
        dateString = dateString.replace("-", " "); 
        return (dateString);
    }
	private Map<String, ProductNameAndProductImgUrlDTO> getProductNameAndProductImageUrl(Map<Integer, List<OrderListDTO>> orderList) throws BusinessProcessException {
		List<String> skus = new ArrayList<String>();
		for(Entry<Integer, List<OrderListDTO>> entry : orderList.entrySet()) {
			List<OrderListDTO> list = entry.getValue();
			for(OrderListDTO orderListIterator : list) {
				if(!skus.contains( orderListIterator.getSku())) {
					skus.add(orderListIterator.getSku());
				}
			}
		}
		LOGGER.info("calling the downstrem api  plp pdp for the product name and product image url  for skus : {} ", skus);
		Map<String, ProductNameAndProductImgUrlDTO> productNameAndProductImgUrl = plpPdpServiceClient.getProudctNameAndProductImgUrl(skus);
		if(productNameAndProductImgUrl.isEmpty() || productNameAndProductImgUrl==null) {
			LOGGER.info("product name and product img url is empty  or null");
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST, CommonConstant.FAILURE_STATUS_CODE);
		}
		LOGGER.info("downstream api plp pdp service has ben called successfully with response : {}", productNameAndProductImgUrl);
		return productNameAndProductImgUrl;
	}
	public OrderDetailResponse orderDetail(Integer customerId, Integer orderId, String orderNumber) throws BusinessProcessException{
		List<OrderDetailDTO> orderItemDetailDTOList = myOrdersRepository.getOrderDetail(customerId, orderId, orderNumber);
		List<String> skus = new ArrayList<String>();
		Boolean flag = true;
		Integer shippingAddresId = null;
		for (OrderDetailDTO orderDetailIterator : orderItemDetailDTOList  ) {
				if(!skus.contains( orderDetailIterator.getSku())) {
					skus.add(orderDetailIterator.getSku()); 
				}
				if(null != orderDetailIterator.getShippingAddressId()&& flag == true) {
					shippingAddresId= orderDetailIterator.getShippingAddressId();
				}
		}
		LOGGER.info("calling the plp pdp service  for the product name and product image url for skus : {} ", skus);
		Map<String, ProductNameAndProductImgUrlDTO> productNameAndProductImgUrlMap = plpPdpServiceClient.getProudctNameAndProductImgUrl(skus);
		
		if(null == productNameAndProductImgUrlMap || productNameAndProductImgUrlMap.isEmpty()) {
			LOGGER.info("calling the plp pdp service  for the product name and product image url is not successfull ");
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST, CommonConstant.FAILURE_STATUS_CODE);
		}
		LOGGER.info("calling the plp pdp service  for the product name and product image url is successfull with response : {} ",productNameAndProductImgUrlMap);
	
		LOGGER.info("calling the customer service for the shipping address ");
		Address shippingAddress = customerServiceClient.getCustomerShippingAddressDetail(customerId, shippingAddresId);
 		if(shippingAddress==null || shippingAddress.getAddressId()==0 || null ==shippingAddress.getAddressId()) {
			LOGGER.info("shipping address is null or zero or address id is null or zero");
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST, CommonConstant.FAILURE_STATUS_CODE);
		}
		LOGGER.info("customer service called successfully called for the shipping address :{}",shippingAddress);
		OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
		List<OrderItemDetail> orderItemDetailList = new ArrayList<OrderItemDetail>();
		PriceSummary priceSummary = new PriceSummary();
		List<Long>productItemId = new ArrayList<Long>();
	    for(OrderDetailDTO orderDetailIterator : orderItemDetailDTOList) {
	    	if(productItemId.contains(orderDetailIterator.getOrderItemId())) {
	    		continue;
	    	}
	    	orderDetailResponse.setOrderType(orderItemDetailDTOList.get(0).getOrderType());
	    	productItemId.add(orderDetailIterator.getOrderItemId());
	    	OrderItemDetail orderItemDetail = buildOrderItemDetail(productNameAndProductImgUrlMap, orderDetailIterator);
	    	buildActionButton(orderDetailIterator, orderItemDetail);
	    	orderItemDetailList.add(orderItemDetail);
 		}
	    buildPriceSummary(orderItemDetailDTOList, priceSummary);
	    orderDetailResponse.setShippingAddress(shippingAddress);
	    orderDetailResponse.setOrderNumber(orderItemDetailDTOList.get(0).getOrderId());
	    orderDetailResponse.setCustomOrderNumber(orderItemDetailDTOList.get(0).getOrderNumber());
		String formatedDate = dateConvert(orderItemDetailDTOList.get(0).getOrderDate());
	    orderDetailResponse.setOrderDate(formatedDate);
	    orderDetailResponse.setOrderItem(orderItemDetailList);
	    orderDetailResponse.setPriceSummary(priceSummary);
		return  orderDetailResponse;
	}
	private OrderItemDetail buildOrderItemDetail(Map<String, ProductNameAndProductImgUrlDTO> productNameAndProductImgUrlMap, OrderDetailDTO orderDetailIterator) {
		ItemDetail itemDetail = new ItemDetail();
		if(productNameAndProductImgUrlMap.get(orderDetailIterator.getSku())!=null && productNameAndProductImgUrlMap.get(orderDetailIterator.getSku()).getItemName()!=null ) {
			itemDetail.setItemName(productNameAndProductImgUrlMap.get(orderDetailIterator.getSku()).getItemName());
		}
		if(productNameAndProductImgUrlMap.get(orderDetailIterator.getSku()) != null) {
			itemDetail.setImageUrl(productNameAndProductImgUrlMap.get(orderDetailIterator.getSku()).getItemImageUrl());
		}
		itemDetail.setItemPayable(orderDetailIterator.getOrderItemPayable());
		itemDetail.setSku(orderDetailIterator.getSku());
		itemDetail.setQuantity(orderDetailIterator.getQuantity());
 		itemDetail.setProductId(productNameAndProductImgUrlMap.get(orderDetailIterator.getSku()).getProductId());
 		if((null != productNameAndProductImgUrlMap.get(orderDetailIterator.getSku()).getAttributeName() && productNameAndProductImgUrlMap.get(orderDetailIterator.getSku()).getAttributeName().equals("Size")) || (null != productNameAndProductImgUrlMap.get(orderDetailIterator.getSku()).getAttributeName() && productNameAndProductImgUrlMap.get(orderDetailIterator.getSku()).getAttributeName().equals("Kids Size"))) {
 			itemDetail.setSize(productNameAndProductImgUrlMap.get(orderDetailIterator.getSku()).getSkuAttributevalue());
 		}
		OrderItemDetail orderItemDetail = new OrderItemDetail();
		orderItemDetail.setItemDetail(itemDetail);
		orderItemDetail.setOrderItemId(orderDetailIterator.getOrderItemId());
		OrderStatusAndOrderStatusDate orderStatusAndOrderStatusDate = setOrderStatusAndOrderStatusDate(orderDetailIterator.getOrderItemStatus(),orderDetailIterator.getIsRefundRequired(),orderDetailIterator.getIsRefunded(),//check is refund required in the pojo
				orderDetailIterator.getRecordType(),orderDetailIterator.getOrderItemCreatedDate(),orderDetailIterator.getCancelExchangeRequestedDate(),orderDetailIterator.getRefundDate(),orderDetailIterator.getOrderDate(),orderDetailIterator.getShippedDate(),orderDetailIterator.getDeliveredDate(), orderDetailIterator.getRtoDate(), orderDetailIterator.getRtoDeliverDate(), orderDetailIterator.getRtoTrackingNumber());
		orderItemDetail.setOrderStatus(orderStatusAndOrderStatusDate.getOrderStatus());
		orderItemDetail.setOrderStatusDate(orderStatusAndOrderStatusDate.getOrderStatusDate());
		orderItemDetail.setAction("/onceuponatrun/product/{}");
		orderItemDetail.setTrackingDetail(builTrackingDetail(orderDetailIterator, orderStatusAndOrderStatusDate));
		
		return orderItemDetail;
	}
	private TrackingDetail builTrackingDetail(OrderDetailDTO orderDetailIterator,
			OrderStatusAndOrderStatusDate orderStatusAndOrderStatusDate) {
		TrackingDetail trackingDetail = new TrackingDetail();
		trackingDetail.setAdditionalDetail(buildAdditionalDetail(orderDetailIterator.getAwb(), orderDetailIterator.getCourrierPartner() ));
		trackingDetail.setMileStones(buildMileStoneList(orderDetailIterator.getOrderItemStatus(),orderDetailIterator.getOrderDate(), orderDetailIterator.getShippedDate(),orderDetailIterator.getDeliveredDate()) );
		return trackingDetail;
	}
	private AdditionalDetail buildAdditionalDetail(String awb, String courrierCompany) {
		AdditionalDetail additionalDetail = new AdditionalDetail();
		additionalDetail.setAwb(awb);
		additionalDetail.setCourierCompany(courrierCompany);
		if(awb != null)
			additionalDetail.setMessage("Track on : "+ courrierCompany +", your AWB number "+ awb);
		return additionalDetail;
	}
	private List<MileStones> buildMileStoneList(OrderStatus orderStatus , Date orderRecievedDate, Date orderShippeddate, Date orderDeliveredDate) {
		List<MileStones> milesToneList = new ArrayList<MileStones>();
	
 	    MileStones mileStonesOne = new MileStones();
 		mileStonesOne.setIsCurrentStatus(true);
 		if(null != orderRecievedDate)
 			mileStonesOne.setDate(dateConvert(orderRecievedDate));
	    mileStonesOne.setSequence(1);
	    mileStonesOne.setLabel("Order Received");
		milesToneList.add(mileStonesOne);
	    MileStones mileStonesTwo = new MileStones();
		if (orderStatus == OrderStatus.SD && orderShippeddate != null) {
	    	mileStonesTwo.setIsCurrentStatus(true);

	    }else {
	    	mileStonesTwo.setIsCurrentStatus(false);
	    }
	    if(null != orderShippeddate)
	    	mileStonesTwo.setDate(dateConvert(orderShippeddate));
	    mileStonesTwo.setSequence(2);
	    mileStonesTwo.setLabel("Shipped");
		milesToneList.add(mileStonesTwo);

	    MileStones mileStonesThree = new MileStones();
	    if(orderStatus == OrderStatus.DL  &&  orderDeliveredDate!=null) {
	    	mileStonesThree.setIsCurrentStatus(true);
	    }else {
	    	mileStonesThree.setIsCurrentStatus(false);
	    }	    
	    if(null != orderDeliveredDate)
	    	mileStonesThree.setDate(dateConvert(orderDeliveredDate));
	    mileStonesThree.setSequence(3);
	    mileStonesThree.setLabel("Delivered");
		milesToneList.add(mileStonesThree);
	   return  milesToneList;
	}
 
	private void buildActionButton(OrderDetailDTO orderDetailIterator, OrderItemDetail orderItemDetail) {
		List<ActionButton>actionButtonList = new ArrayList<ActionButton>();
		if(orderDetailIterator.getOrderItemStatus() == OrderStatus.OR) {
			//LOGGER.info("setting the action button for the order date for order date : {}",checkCancelTat(orderDetailIterator.getOrderDate() ));
			ActionButton actionButton = new  ActionButton();
			actionButton.setActionType(ActionType.CANCEL);
			actionButton.setActionUrl("/why are  you cancelling the order");
			actionButtonList.add(actionButton);
		}
		else if(orderDetailIterator.getOrderItemStatus() == OrderStatus.SD) {
			ActionButton actionButton3 = new  ActionButton();
			actionButton3.setActionType(ActionType.TRACK);
			actionButton3.setActionUrl(orderDetailIterator.getTrackingUrl());
			actionButtonList.add(actionButton3);
			
		}else if (orderDetailIterator.getOrderItemStatus() == OrderStatus.DL && null != orderDetailIterator.getDeliveredDate()) { 
			// till Delivery Date + 7
			LOGGER.info("setting the action button for the order date for order deliver date : {}",orderDetailIterator.getDeliveredDate() );
			if(orderDetailIterator.getIsReturnable() == 1 && checkExchangeReturnTat(orderDetailIterator.getDeliveredDate())) {
				ActionButton actionButton = new  ActionButton();
				actionButton.setActionType(ActionType.RETURN);
				actionButton.setActionUrl("/why are  you returnning the order");
				actionButtonList.add(actionButton);	
			}
			if(orderDetailIterator.getIsExchangable() == 1 && checkExchangeReturnTat(orderDetailIterator.getDeliveredDate())) {
				ActionButton actionButton1 = new  ActionButton();
				actionButton1.setActionType(ActionType.EXCHANGE);
				actionButton1.setActionUrl("/why are  you cancelling the order");
				actionButtonList.add(actionButton1);
			}
		}
		orderItemDetail.setActionButton(actionButtonList);
	}
	private boolean checkExchangeReturnTat(Date deliveredDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(deliveredDate);
		c.add(Calendar.DATE, CommonConstant.RETURN_EXCHANGE_WINDOW);
		java.util.Date  deliveredDateCasted = c.getTime();
		return deliveredDateCasted.after(new java.util.Date());
	}
	private boolean checkCancelTat(Date orderDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(orderDate);
		c.add(Calendar.DATE, CommonConstant.ORDER_CANCEL_BUTTON_TIME_PERIOD);
		java.util.Date  deliveredDateCasted = c.getTime();
		return deliveredDateCasted.after(new java.util.Date());
	}
	
	private void buildPriceSummary(List<OrderDetailDTO> orderItemDetailDTOList, PriceSummary priceSummary) {
		priceSummary.setOderTotal( orderItemDetailDTOList.get(0).getOrderTotalAmount());
	    priceSummary.setOrderPayable(orderItemDetailDTOList.get(0).getOrderPayable());
	    priceSummary.setPlatformDiscount(orderItemDetailDTOList.get(0).getOrderPlatformDiscount());
	    priceSummary.setPromoDiscount(orderItemDetailDTOList.get(0).getOrderPromoDiscount());
	    priceSummary.setCreditApplied(orderItemDetailDTOList.get(0).getOrderCreditApplied());
	    priceSummary.setShippingCharge(orderItemDetailDTOList.get(0).getShippingCharged());
	    priceSummary.setPromocode(orderItemDetailDTOList.get(0).getPromoCodeApplied());
	    priceSummary.setOderTotal(orderItemDetailDTOList.get(0).getOrderTotalAmount());
	}
	public String dateConvert(Date requestedDate){
		if(null==requestedDate) return "";
		String orderDate = requestedDate.toString();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MMMM-yyyy");
        java.util.Date date = null;
        try {
            date = format1.parse(orderDate);
        } catch (ParseException e) {
        }
        String dateString = format2.format(date);
        dateString = dateString.replace("-", " "); 
        return (dateString);
    }
	public OrderStatusAndOrderStatusDate setOrderStatusAndOrderStatusDate(OrderStatus orderStatus,Integer isRefundRequired,Integer isRefunded,RecordType recordType, 
			Date OrderRecievedDate,Date cancelExchangeRequested,Date refundDate,Date orderDate,Date shippedDate,Date deliveredDate,
			Date rtoBookedDate, Date rtoDeliverdDate, String trackingUrl
			) {
		OrderStatusAndOrderStatusDate orderStatusAndOrderStatusDate = new OrderStatusAndOrderStatusDate();
  		if(orderStatus.equals(OrderStatus.OR)) {
			String formatedDate = dateConvert(OrderRecievedDate);
			orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate);
			orderStatusAndOrderStatusDate.setOrderStatus("Order Received");
		} 
  		else if (orderStatus.equals(OrderStatus.SV)) {
			String formatedDate = dateConvert(OrderRecievedDate);
			orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate);
			orderStatusAndOrderStatusDate.setOrderStatus("Shared with Vendor");
		}
		else if(orderStatus.equals(OrderStatus.CD)) {
			if(isRefundRequired == 1) {
				if(isRefunded ==1) {
					if(null != refundDate) {
						String formatedDate = dateConvert(refundDate);
						orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate);
					}
					orderStatusAndOrderStatusDate.setOrderStatus("Cancel Refunded");
				}else {
					if(null != cancelExchangeRequested) {
						String formatedDate = dateConvert(cancelExchangeRequested);
						orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate); 
					}
					orderStatusAndOrderStatusDate.setOrderStatus("Order Cancelled");
				}
			}else {
				if(null != cancelExchangeRequested) {
					String formatedDate = dateConvert(cancelExchangeRequested);
					orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate);
				}
				orderStatusAndOrderStatusDate.setOrderStatus("Order Cancelled");
			}
		}
		else if (orderStatus.equals(OrderStatus.SD)) {
			if(shippedDate != null) {
				String formatedDate = dateConvert(shippedDate);
				orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate);
			}
			orderStatusAndOrderStatusDate.setOrderStatus("Order Shipped");
		}
		else if (orderStatus.equals(OrderStatus.DL)){
			if(null != deliveredDate) {
				String formatedDate = dateConvert(deliveredDate);
				orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate);
			}
			orderStatusAndOrderStatusDate.setOrderStatus("Order Delivered");
		}
		else if(orderStatus.equals(OrderStatus.RR)  && recordType.equals(RecordType.RETURN ) && isRefunded == 0) {
			String formatedDate = dateConvert(cancelExchangeRequested);
			orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate);
			orderStatusAndOrderStatusDate.setOrderStatus("Returned Raised");
				
		}else if (orderStatus.equals(OrderStatus.RF )  ) {
			if( isRefunded == 1) {
				if(null != refundDate) {
					String formatedDate = dateConvert(refundDate);
					orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate);
					if(recordType.equals(RecordType.RETURN)) {
						orderStatusAndOrderStatusDate.setOrderStatus("Returned - Refunded");
					} else if(recordType.equals(RecordType.CANCELLED))  {
						orderStatusAndOrderStatusDate.setOrderStatus("Cancelled - Refunded");
					}
				}
			}else {
				if(null != cancelExchangeRequested) {
					String formatedDate = dateConvert(cancelExchangeRequested);
					orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate); 
				}
				if(recordType.equals(RecordType.RETURN)) {
					orderStatusAndOrderStatusDate.setOrderStatus("Returned");
				} else if(recordType == RecordType.CANCELLED)  {
					orderStatusAndOrderStatusDate.setOrderStatus("Cancelled");
				}
			}
		} 
		else if(orderStatus.equals(OrderStatus.EXR) && recordType.equals( RecordType.EXCHANGE)) {
			if(null != cancelExchangeRequested) {
				String formatedDate = dateConvert(cancelExchangeRequested);
				orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate);
			}
			orderStatusAndOrderStatusDate.setOrderStatus("Order Exchanaged");
		}
		else if (orderStatus== OrderStatus.WP && null != OrderRecievedDate ) {
			String formatedDate = dateConvert(OrderRecievedDate);
			orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate); 
			orderStatusAndOrderStatusDate.setOrderStatus("Warehouse Processing");
		}
		else if (orderStatus.equals(OrderStatus.RTO)) {
			String formatedDate = dateConvert(rtoBookedDate);
			orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate); 
			orderStatusAndOrderStatusDate.setOrderStatus("Return to Origin Booked");
		}
		else if (orderStatus.equals(OrderStatus.RTOD)) {
			String formatedDate = dateConvert(rtoDeliverdDate);
			orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate); 
			orderStatusAndOrderStatusDate.setOrderStatus("Return to Origin Delivered");
		}
		else if(orderStatus.equals(OrderStatus.CDV)) {
			String formatedDate = dateConvert(cancelExchangeRequested);
			orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate); 
			orderStatusAndOrderStatusDate.setOrderStatus("Cancelled  by Vendor");
		} 
		else if (orderStatus.equals(OrderStatus.RA)) {
			String formatedDate = dateConvert(cancelExchangeRequested);
			orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate);
			orderStatusAndOrderStatusDate.setOrderStatus("Pickup Assigned");
		} else if (orderStatus.equals(OrderStatus.RC)) {
			String formatedDate = dateConvert(cancelExchangeRequested);
			orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate);
			orderStatusAndOrderStatusDate.setOrderStatus("Pickup Cancelled");
		} else if (orderStatus.equals(OrderStatus.RCP)) {
			String formatedDate = dateConvert(cancelExchangeRequested);
			orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate);
			orderStatusAndOrderStatusDate.setOrderStatus("Pickup Cancelled by Courier Company");
		}
		else {
			if(null != OrderRecievedDate && !orderStatus.equals(OrderStatus.PF) ) {
				String formatedDate = dateConvert(OrderRecievedDate);
				orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate); 
				orderStatusAndOrderStatusDate.setOrderStatus("Order Recieved");
			}
			else if(orderStatus.equals(OrderStatus.PF) && null != OrderRecievedDate) {
				String formatedDate = dateConvert(OrderRecievedDate);
				orderStatusAndOrderStatusDate.setOrderStatusDate(formatedDate); 
				orderStatusAndOrderStatusDate.setOrderStatus("Payment Failed");
			} 
		}
		return orderStatusAndOrderStatusDate;
	}

	public OrderConfirmation buildOrderConfirmation(OrderDetailResponse orderDetailResponse, CustomerDetailVO customerDetail) {
		OrderConfirmation orderConfirmation = new OrderConfirmation();
		  orderConfirmation.setBannerImage(bannerImage);
		  orderConfirmation.setOrderDate(orderDetailResponse.getOrderDate());
		  Long customerId = customerDetail.getCustomerId();
		  String shortUrl = cuttlyClient.createShortLink(customerId);
		  List<OrderItemDetailForOrderConfirmation> orderItemDetailForOrderConfirmationList = new ArrayList<OrderItemDetailForOrderConfirmation>();
		  for(OrderItemDetail it : orderDetailResponse.getOrderItem()){
			   OrderItemDetailForOrderConfirmation detailForOrderConfirmation = new OrderItemDetailForOrderConfirmation();
			   detailForOrderConfirmation.setItemDetail(it.getItemDetail());
			   detailForOrderConfirmation.setOrderItemId(it.getOrderItemId());
			   detailForOrderConfirmation.setOrderStatus(it.getOrderStatus());
			   orderItemDetailForOrderConfirmationList.add(detailForOrderConfirmation);
		   }
		  orderConfirmation.setPriceSummary(orderDetailResponse.getPriceSummary());
		  orderConfirmation.setShippingAddress(orderDetailResponse.getShippingAddress());
		  orderConfirmation.setCustomOrderNumber(orderDetailResponse.getCustomOrderNumber());
		  orderConfirmation.setOrderNumber(orderDetailResponse.getOrderNumber());
		  orderConfirmation.setOrderItem(orderItemDetailForOrderConfirmationList);
		  if(referralFlag) {
			  orderConfirmation.setCampaignUrl(shortUrl); 
		  };
		return orderConfirmation;
	}

}