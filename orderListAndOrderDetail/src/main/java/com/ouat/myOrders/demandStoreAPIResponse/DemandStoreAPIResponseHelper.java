package com.ouat.myOrders.demandStoreAPIResponse;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ouat.myOrders.response.MessageDetail;
import com.ouat.myOrders.response.MessageType;

@Component
public class DemandStoreAPIResponseHelper {
 
	public DemandStoreAPIResponse apiResponse(List<MessageDetail>messageDetailList,
			Boolean isSuccess,String successCode, Object data){
		
		DemandStoreAPIResponse promotionAPIResponse = new DemandStoreAPIResponse();
		promotionAPIResponse.setMessage(messageDetailList);
    	promotionAPIResponse.setSuccess(isSuccess);
    	promotionAPIResponse.setData(data);
    	promotionAPIResponse.setCode(successCode);
        return promotionAPIResponse;
	}
	
	public void setMessageDetailList(MessageType messageType, String msg, List<MessageDetail> messageDetails) {
		messageDetails.add(new MessageDetail(messageType, msg, null, null));
	}
}
