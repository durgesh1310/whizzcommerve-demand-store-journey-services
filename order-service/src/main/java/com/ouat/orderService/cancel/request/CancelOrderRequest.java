package com.ouat.orderService.cancel.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.orderService.cancel.dto.RefundType;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CancelOrderRequest {
	
	private Long orderItemId;
	
	private String sku;
	
	private RefundType refundType;
	
	private Integer reasonId;
	
	
	public Long getOrderItemId() {
		return orderItemId;
	}





	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}





	public String getSku() {
		return sku;
	}





	public void setSku(String sku) {
		this.sku = sku;
	}

	
	




	public RefundType getRefundType() {
		return refundType;
	}





	public void setRefundType(RefundType refundType) {
		this.refundType = refundType;
	}

	
	




	public Integer getReasonId() {
		return reasonId;
	}





	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}





	@Override
    public String toString() {
        String serialized = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            serialized = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException jpe) {
            jpe.printStackTrace();
        }
        return serialized;
    }

}
