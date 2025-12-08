package com.ouat.cartService.shoppingCartResponse;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class DemandStoreAPIResponseHelper {
 
	public DemandStoreAPIResponse apiResponse(List<MessageDetail>messageDetailList,
			Boolean isSuccess,String successCode, Object data){
		
		DemandStoreAPIResponse promotionAPIResponse = new DemandStoreAPIResponse();
		promotionAPIResponse.setMessage(messageDetailList);
    	promotionAPIResponse.setSuccess(isSuccess);
    	promotionAPIResponse.setCode(successCode);
    	promotionAPIResponse.setData(data);
        return promotionAPIResponse;
	}
	
	public void setMessageDetailList(MessageType messageType, String msg, List<MessageDetail> messageDetails) {
		messageDetails.add(new MessageDetail(messageType, msg, null, null));
	}
}
