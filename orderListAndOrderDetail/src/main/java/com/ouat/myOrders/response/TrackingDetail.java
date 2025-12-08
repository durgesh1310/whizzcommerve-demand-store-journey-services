package com.ouat.myOrders.response;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)

public class TrackingDetail {
	private List<MileStones> mileStones;
	private AdditionalDetail additionalDetail;
	public AdditionalDetail getAdditionalDetail() {
		return additionalDetail;
	}
	public void setAdditionalDetail(AdditionalDetail additionalDetail) {
		this.additionalDetail = additionalDetail;
	}
	@Override
	public String toString() {
		return "TrackingDetail [mileStones=" + mileStones + ", additionalDetail=" + additionalDetail + "]";
	}
	public TrackingDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	public List<MileStones> getMileStones() {
		return mileStones;
	}
	public void setMileStones(List<MileStones> mileStones) {
		this.mileStones = mileStones;
	}
	public TrackingDetail(List<MileStones> mileStones, AdditionalDetail additionalDetail) {
		super();
		this.mileStones = mileStones;
		this.additionalDetail = additionalDetail;
	}
 
	 
	

}
