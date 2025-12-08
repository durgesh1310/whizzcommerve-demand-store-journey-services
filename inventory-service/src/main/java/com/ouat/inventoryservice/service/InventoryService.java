package com.ouat.inventoryservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ouat.inventoryservice.repository.InventoryRepository;
import com.ouat.inventoryservice.request.InventoryUpdate;
import com.ouat.inventoryservice.request.InventoryUpdateRequest;
import com.ouat.inventoryservice.request.SkuAndQty;
import com.ouat.inventoryservice.response.DemandStoreAPIResponse;
import com.ouat.inventoryservice.response.MessageDetail;
import com.ouat.inventoryservice.response.MessageType;

@Service
public class InventoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryService.class);


	@Autowired
	private InventoryRepository inventoryRepository;

	@Transactional
	public DemandStoreAPIResponse updateInventory(InventoryUpdateRequest inventoryUpdateRequest) {
		List<MessageDetail> messagesList = doPrecheckForInventoryUpdate(inventoryUpdateRequest, inventoryRepository.getInventory(buildSkuList(inventoryUpdateRequest)));
		if(messagesList!=null && messagesList.isEmpty() && inventoryRepository.updateInventory(inventoryUpdateRequest.getSkuAndQtyList())>0) {
			ArrayList<MessageDetail> messageList = new ArrayList<>();
			messageList.add(new MessageDetail(MessageType.INFO, "Inventory Updated Successfully", null, null));
			return new DemandStoreAPIResponse(true, messageList , "200", null);
		}
		else {
			return new DemandStoreAPIResponse(false,messagesList, "500", null );
		}
	}
	private List<MessageDetail>  doPrecheckForInventoryUpdate(InventoryUpdateRequest inventoryUpdateRequest,
			  Map<String, Integer> inventoryMap) {
		List<MessageDetail> messagesList  = new ArrayList<MessageDetail>();
		for(SkuAndQty it : inventoryUpdateRequest.getSkuAndQtyList()) {
			if(it.getQty()>inventoryMap.get(it.getSku())) {
				messagesList.add(new MessageDetail(MessageType.INFO, it.getSku() + " got out of stock", null, null));
			}
 		}
		if(inventoryUpdateRequest.getSkuAndQtyList().size()!= inventoryMap.size()) {
			messagesList.add(new MessageDetail(MessageType.INFO, "some item may not exist in inventory", null, null));
		}
		return messagesList;
	}
	


	private List<String> buildSkuList(InventoryUpdateRequest inventoryUpdateRequest) {
		List<String>skus = new ArrayList<>();
		for(SkuAndQty it : inventoryUpdateRequest.getSkuAndQtyList()) {
			skus.add(it.getSku());
		}
		return skus;
	}
	
	
	@Transactional
	public boolean addInventory(InventoryUpdateRequest inventoryUpdateRequest) {
		if (inventoryRepository.addInventory(inventoryUpdateRequest.getSkuAndQtyList()) != inventoryUpdateRequest
				.getSkuAndQtyList().size()) {
			return false;
		}
		return true;

	}
	
	
	@Transactional
	public boolean updateInventory(InventoryUpdate inventoryUpdateRequest) {
		return inventoryRepository.updateInventoryV1(inventoryUpdateRequest) > 0;
	}

}
