package com.ouat.notificationsender.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ouat.notificationsender.request.CashbackAlert;
import com.ouat.notificationsender.request.PlaceOrderAlertRequest;
import com.ouat.notificationsender.request.ReturnExchangeCancelAlert;
import com.ouat.notificationsender.response.DemandStoreAPIResponse;
import com.ouat.notificationsender.service.OrderAlertService;

@RestController
public class OrderAlertController {
	public static final Logger LOGGER = LoggerFactory.getLogger(OrderAlertController.class);

	@Autowired 
	OrderAlertService orderAlertService;
	/**
	 * This API sends email and SMS to customers
	 * @param placeOrderAlert
	 * @return
	 */
 	@PostMapping("/send-place-alert")
	public ResponseEntity<DemandStoreAPIResponse> sendPlaceOrderEmailAlert(@RequestBody PlaceOrderAlertRequest placeOrderAlert ){
		LOGGER.info("place order alert request has been recieve with place order request : {}, with order item detail : {} ", placeOrderAlert, placeOrderAlert.getProductItemDetailList().get(0).getProductName());
 		return ResponseEntity.ok(new DemandStoreAPIResponse(orderAlertService.placeOrderAlert(placeOrderAlert),null ,null ,null));
	}

	@PostMapping("/cancel-return-exchange-alert")
    public ResponseEntity<DemandStoreAPIResponse> sendReturnExchnageCanceEmailAlert(@RequestBody ReturnExchangeCancelAlert alert){
		LOGGER.info("send-cancel-return-exchange-aler request has been recieve with request : {}", alert);
		return ResponseEntity.ok(new DemandStoreAPIResponse(orderAlertService.returnExchangeCancelAlert(alert),null ,null ,null));
	}
	
	@PostMapping("/cashback-alert")
    public ResponseEntity<DemandStoreAPIResponse> cashbackAlert(@RequestBody CashbackAlert alert){
		LOGGER.info("send-cancel-return-exchange-aler request has been recieve with request : {}", alert);
		return ResponseEntity.ok(new DemandStoreAPIResponse(orderAlertService.cashbackAlert(alert),null ,null ,null));
	}
	 
}
