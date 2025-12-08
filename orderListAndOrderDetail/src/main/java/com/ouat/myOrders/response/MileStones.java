package com.ouat.myOrders.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)

public class MileStones {
	private Integer sequence;
	private String label;
	private String date;
	private Boolean isCurrentStatus;
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Boolean getIsCurrentStatus() {
		return isCurrentStatus;
	}
	public void setIsCurrentStatus(Boolean isCurrentStatus) {
		this.isCurrentStatus = isCurrentStatus;
	}
	public MileStones(Integer sequence, String label, String date, Boolean isCurrentStatus) {
		super();
		this.sequence = sequence;
		this.label = label;
		this.date = date;
		this.isCurrentStatus = isCurrentStatus;
	}
	public MileStones() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	

}
