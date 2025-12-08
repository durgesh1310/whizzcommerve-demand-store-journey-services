package com.ouat.inventoryservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouat.inventoryservice.request.InventoryUpdate;
import com.ouat.inventoryservice.request.InventoryUpdateRequest;
import com.ouat.inventoryservice.response.DemandStoreAPIResponse;
import com.ouat.inventoryservice.service.InventoryService;



/**
 * 
 * @author sourabh singh
 * @apiNote this microservice is build to fullfill inventory work
 *
 */
@RestController
@RequestMapping("/internal")
public class InventoryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryController.class);
	
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private ObjectMapper mapper ;
	
	@PostMapping("/update-inventory")
	public  ResponseEntity<DemandStoreAPIResponse> updateInventory(@RequestBody InventoryUpdateRequest inventoryUpdateRequest) throws JsonProcessingException {
		LOGGER.info("request recieved for inventory update with request : {}",  mapper.readValue(mapper.writeValueAsString(inventoryUpdateRequest), InventoryUpdateRequest.class));    
		return  ResponseEntity.ok(inventoryService.updateInventory(inventoryUpdateRequest));

	}
	
	@PostMapping("/add-inventory")
	public boolean addInventory(@RequestBody InventoryUpdateRequest inventoryUpdateRequest) {
		return inventoryService.addInventory(inventoryUpdateRequest);
	}
	
	@PostMapping("/v1/add-inventory")
	public ResponseEntity<Boolean> addInventoryV1(@RequestBody InventoryUpdateRequest inventoryUpdateRequest) {
		return ResponseEntity.ok(inventoryService.addInventory(inventoryUpdateRequest));
	}
	
	@PostMapping("v1/update-inventory")
	public boolean updateInventory(@RequestBody InventoryUpdate inventoryUpdateRequest) {
	            return inventoryService.updateInventory(inventoryUpdateRequest);

	}



}
