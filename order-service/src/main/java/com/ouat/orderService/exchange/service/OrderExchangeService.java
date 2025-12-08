package com.ouat.orderService.exchange.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ouat.orderService.client.ExchangeSkuRequest;
import com.ouat.orderService.client.PlpPdpServiceClient;
import com.ouat.orderService.constants.CommonConstant;
import com.ouat.orderService.exception.BusinessProcessException;
import com.ouat.orderService.exchange.repository.OrderExchangeRepository;
import com.ouat.orderService.response.DemandStoreAPIResponse;

@Service
public class OrderExchangeService {

	public static final Logger log = LoggerFactory.getLogger(OrderExchangeService.class);

	@Autowired
	PlpPdpServiceClient plpPdpServiceClient;

	@Autowired
	OrderExchangeRepository orderExchangeRepository;

	public ResponseEntity<DemandStoreAPIResponse> getOrderItemId(Long orderItemId, Long customerId) throws BusinessProcessException {
		ExchangeSkuRequest orderExchangeDetail = new ExchangeSkuRequest();  
		String orderSku = orderExchangeRepository.getSku(orderItemId, customerId);
		orderExchangeDetail.setSku(orderSku);
		DemandStoreAPIResponse skuDetails = plpPdpServiceClient.getSkuDetails(orderExchangeDetail);
		log.info("Getting Sku Details for sku : {}",orderExchangeDetail.toString());
		if (skuDetails.isSuccess()) {
			log.info("Result Data for Sku : {}", skuDetails.getData());
			return ResponseEntity
					.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, skuDetails.getData()));
		} else {
			throw new BusinessProcessException(CommonConstant.EXCHANGE_NOT_AVAILABLE,
					CommonConstant.FAILURE_STATUS_CODE);
		}
	}

}
