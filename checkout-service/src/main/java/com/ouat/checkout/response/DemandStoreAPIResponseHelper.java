package com.ouat.checkout.response;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.checkout.enums.MessageType;

@Component
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
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