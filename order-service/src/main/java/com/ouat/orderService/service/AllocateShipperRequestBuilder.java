package com.ouat.orderService.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ouat.orderService.client.AllocateShipperRequest;
import com.ouat.orderService.client.ItemListRequest;
import com.ouat.orderService.dto.OrderItemDetailsDto;
import com.ouat.orderService.util.Utility;

@Component
public class AllocateShipperRequestBuilder {
	private static Logger log = LoggerFactory.getLogger(AllocateShipperRequestBuilder.class);

	@Value("${pickrr.auth.token}")
	private String authToken;

	private static final Double AMOUNT_FOR_PREPAID = 0.00;
	private static final Double GIFT_WRAP_CHARGES = 0.00;
	private static final String ORDER_PAYMENT_STATUS = "COD";
	private static final Double ITEM_BREADTH = 1.00;
	private static final Double OTEM_HEIGHT = 1.00;
	private static final Double ITEM_LENGTH = 1.00;
	private static final Double ITEM_WEIGHT = 0.5;
	private static final Double TRANSACTION_CHARGE = 0.00;
	private static final Boolean IS_REVERSE = false;

	public String allocateOrderItem(OrderItemDetailsDto orderItemDetail)
			throws JsonProcessingException, ParseException {

		AllocateShipperRequest request = new AllocateShipperRequest();

		request.setAuthToken(authToken);
		request.setItemName(orderItemDetail.getItemName());
		request.setItemList(buildItemListRequest(orderItemDetail));
		request.setFromName(orderItemDetail.getFromName());
		request.setFromPhoneNumber(orderItemDetail.getFromPhoneNumber());
		request.setFromAddress(orderItemDetail.getFromAddress());
		request.setFromPincode(orderItemDetail.getFromPincode());
		request.setPickupGstin(orderItemDetail.getPickupGstin());
		request.setToName(orderItemDetail.getToName());
		request.setToEmail(orderItemDetail.getToEmail());
		request.setToPhoneNumber(orderItemDetail.getToPhoneNumber());
		request.setToPincode(orderItemDetail.getToPincode());
		request.setToAddress(orderItemDetail.getToAddress());
		request.setQuantity(orderItemDetail.getQuantity());
		request.setInvoiceValue(orderItemDetail.getInvoiceValue());
		if (orderItemDetail.getPaymentMethod().equalsIgnoreCase(ORDER_PAYMENT_STATUS)) {
			request.setCodAmount(orderItemDetail.getCodAmount());
		} else {
			request.setCodAmount(AMOUNT_FOR_PREPAID);
		}
		request.setClientOrderId( orderItemDetail.getClientOrderId());
		request.setItemBreadth(ITEM_BREADTH);
		request.setItemLength(ITEM_LENGTH);
		request.setItemHeight(OTEM_HEIGHT);
		request.setItemWeight(ITEM_WEIGHT);
		request.setIsReverse(IS_REVERSE);
		request.setInvoiceNumber(orderItemDetail.getInvoiceNumber());
		request.setTotalDiscount(orderItemDetail.getTotalDiscount());
		request.setShippingCharge(orderItemDetail.getShippingCharge());
		request.setTransactionCharge(TRANSACTION_CHARGE);
		request.setGiftwrapCharge(GIFT_WRAP_CHARGES);
		String allocateOrderItemRequstBody = getJsonStringForAllocateBestShipperRequest(request);
		log.info("best shipper request : {}",allocateOrderItemRequstBody);	
		return allocateOrderItemRequstBody;

	}

	private String getJsonStringForAllocateBestShipperRequest(AllocateShipperRequest allocateBestShipperRequest)
			throws JsonProcessingException {
		Utility.getJson(allocateBestShipperRequest);
		return Utility.getJson(allocateBestShipperRequest);
	}

	public List<ItemListRequest> buildItemListRequest(OrderItemDetailsDto orderItemDetail) {
		List<ItemListRequest> itemRequest = new ArrayList<ItemListRequest>();
		ItemListRequest itemListRequest = new ItemListRequest();
		itemListRequest.setItemName(orderItemDetail.getItemName());
		itemListRequest.setItemTaxPercentage(0);
		itemListRequest.setPrice(orderItemDetail.getPrice());
		itemListRequest.setQuantity(orderItemDetail.getQuantity());
		itemListRequest.setSku(orderItemDetail.getSku());
		itemRequest.add(itemListRequest);
		return itemRequest;
	}

}
