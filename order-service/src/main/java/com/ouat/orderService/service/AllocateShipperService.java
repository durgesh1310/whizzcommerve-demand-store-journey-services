package com.ouat.orderService.service;


import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ouat.orderService.client.AllocateShipperResponse;
import com.ouat.orderService.client.CourierResponse;
import com.ouat.orderService.client.PickrrClient;
import com.ouat.orderService.constants.CourierDetailOrderStatus;
import com.ouat.orderService.dto.OrderItemDetailsDto;
import com.ouat.orderService.exception.DownStreamException;
import com.ouat.orderService.repository.OrderRepository;
import com.ouat.orderService.util.Utility;

@Service
public class AllocateShipperService {

	private static Logger log = LoggerFactory.getLogger(AllocateShipperService.class);

	@Autowired
	private OrderRepository bestShipperRepository;

	@Autowired
	private AllocateShipperRequestBuilder builder;

	@Autowired
	private PickrrClient client;
	
	public CourierResponse allocateOrderToBestShipperWorkFlow(String orderItemIds)
			throws JsonProcessingException, DownStreamException, ParseException {
		OrderItemDetailsDto orderItemDetail = bestShipperRepository.queryForOrderItemsDetails(orderItemIds);
		log.info("OrderItemDetail = {}", orderItemDetail);
		return allocateOrderItemWorkFlow(orderItemDetail);
	}
	
	public CourierResponse allocateOrderItemWorkFlow(OrderItemDetailsDto orderItemDetail)
			throws JsonProcessingException, DownStreamException, ParseException {
		AllocateShipperResponse response = client.getTrackingIdAndCourierFromPickrr(builder.allocateOrderItem(orderItemDetail));
		log.info("Response Received from pickrr RequestType = allocateBestShipperRequest,  Response = {}", Utility.getJson(response));
		if (response != null && response.getSuccess()) {
			log.info("Going to perform  order item ship operations for RequestType = allocateBestShipperRequest");
			bestShipperRepository.updateBestShipperOrderStatus(response);
			bestShipperRepository.updateShipmentDetailStatus(response);
	        bestShipperRepository.updateForOrderItem(orderItemDetail.getOrderId());
			return buildCourierDetailResponse(response.getTrackingId(), response.getCourier());

		} else {
			log.info("Fail to get AWB number, Response= {}", response);
		}

		return null;
	}
	
	public CourierResponse buildCourierDetailResponse(String awb , String courier ) throws ParseException {
		CourierResponse courierDetailResponse = new CourierResponse();
		courierDetailResponse.setAwbNo(awb);
		courierDetailResponse.setStatus(CourierDetailOrderStatus.AVAILABLE);
		courierDetailResponse.setCourierCode(courier);//said by kailash joshi
		courierDetailResponse.setCourierName(courier);
		courierDetailResponse.setAdditionalInfo("Your awb no. is " + awb);
		log.info("CourierDetailResponse = {}", courierDetailResponse);
		return courierDetailResponse;
	}

 
}

