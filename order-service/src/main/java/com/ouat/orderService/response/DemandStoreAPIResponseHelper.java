package com.ouat.orderService.response;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Component
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DemandStoreAPIResponseHelper {
 
	public DemandStoreAPIResponse apiResponse(List<MessageDetail>messageDetailList,
			Boolean isSuccess,String successCode, Object data){
		
		DemandStoreAPIResponse exchangeAPIResponse = new DemandStoreAPIResponse();
		exchangeAPIResponse.setMessage(messageDetailList);
		exchangeAPIResponse.setSuccess(isSuccess);
		exchangeAPIResponse.setData(data);
		exchangeAPIResponse.setCode(successCode);
        return exchangeAPIResponse;
	}
	
	public void setMessageDetailList(MessageType messageType, String msg, List<MessageDetail> messageDetails) {
		messageDetails.add(new MessageDetail(messageType, msg, null, null));
	}
}