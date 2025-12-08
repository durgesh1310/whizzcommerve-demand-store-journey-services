package com.ouat.orderService.cancel.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderCancelReturnRecordDto {
	
	private Integer orderId;
	
	private Long orderItemId;
	
	private Integer qty;
	
	private String requestedDate;
	
	private boolean isRefundRequired;
	
	private boolean isRefunded;
	
	private Double refundAmount;
	
	private RecordType recordType;
	
	private RefundType refundType;
	
	private Integer reasonId;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public String getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}

	public boolean isRefundRequired() {
		return isRefundRequired;
	}

	public void setRefundRequired(boolean isRefundRequired) {
		this.isRefundRequired = isRefundRequired;
	}

	public boolean isRefunded() {
		return isRefunded;
	}

	public void setRefunded(boolean isRefunded) {
		this.isRefunded = isRefunded;
	}

	public Double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public RecordType getRecordType() {
		return recordType;
	}

	public void setRecordType(RecordType recordType) {
		this.recordType = recordType;
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


	public OrderCancelReturnRecordDto(Integer orderId, Long orderItemId, Integer qty, String requestedDate,
			boolean isRefundRequired, boolean isRefunded, Double refundAmount, RecordType recordType,
			RefundType refundType, Integer reasonId) {
		super();
		this.orderId = orderId;
		this.orderItemId = orderItemId;
		this.qty = qty;
		this.requestedDate = requestedDate;
		this.isRefundRequired = isRefundRequired;
		this.isRefunded = isRefunded;
		this.refundAmount = refundAmount;
		this.recordType = recordType;
		this.refundType = refundType;
		this.reasonId = reasonId;
	}

	public OrderCancelReturnRecordDto() {
		super();
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
