package com.ouat.orderService.cancel.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CancelReturnReasonDTO {
	
	private Integer reasonId;
	
	
	private String reason;


	public Integer getReasonId() {
		return reasonId;
	}


	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}


	public CancelReturnReasonDTO(Integer reasonId, String reason) {
		super();
		this.reasonId = reasonId;
		this.reason = reason;
	}


	public CancelReturnReasonDTO() {
		super();
	}
	
	
	
	
	
}
