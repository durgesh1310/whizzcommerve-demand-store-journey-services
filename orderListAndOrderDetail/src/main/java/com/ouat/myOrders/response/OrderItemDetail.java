package com.ouat.myOrders.response;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderItemDetail {
	private Long orderItemId;
	private String orderStatus;
	private String orderStatusDate;
	private String addtionalMessage;
	private String  action;
	private ItemDetail itemDetail;
	private List<ActionButton> actionButton;
	private TrackingDetail trackingDetail;
	public TrackingDetail getTrackingDetail() {
		return trackingDetail;
	}
	public void setTrackingDetail(TrackingDetail trackingDetail) {
		this.trackingDetail = trackingDetail;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getOrderStatusDate() {
		return orderStatusDate;
	}
	public void setOrderStatusDate(String orderStatusDate) {
		this.orderStatusDate = orderStatusDate;
	}
	public Long getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getAddtionalMessage() {
		return addtionalMessage;
	}
	public void setAddtionalMessage(String addtionalMessage) {
		this.addtionalMessage = addtionalMessage;
	}
	public ItemDetail getItemDetail() {
		return itemDetail;
	}
	public void setItemDetail(ItemDetail itemDetail) {
		this.itemDetail = itemDetail;
	}
	public List<ActionButton> getActionButton() {
		return actionButton;
	}
	public void setActionButton(List<ActionButton> actionButton) {
		this.actionButton = actionButton;
	}
	
	public OrderItemDetail(Long orderItemId, String orderStatus, String orderStatusDate, String addtionalMessage,
			String action, ItemDetail itemDetail, List<ActionButton> actionButton, TrackingDetail trackingDetail) {
		super();
		this.orderItemId = orderItemId;
		this.orderStatus = orderStatus;
		this.orderStatusDate = orderStatusDate;
		this.addtionalMessage = addtionalMessage;
		this.action = action;
		this.itemDetail = itemDetail;
		this.actionButton = actionButton;
		this.trackingDetail = trackingDetail;
	}
	public OrderItemDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "OrderItemDetail [orderItemId=" + orderItemId + ", orderStatus=" + orderStatus + ", addtionalMessage="
				+ addtionalMessage + ", itemDetail=" + itemDetail + ", actionButton=" + actionButton + "]";
	}
	
	 @Override
	  public boolean equals(Object v) {
	        boolean retVal = false;

	        if (v instanceof OrderItemDetail){
	        	OrderItemDetail ptr = (OrderItemDetail) v;
	            retVal = ptr.orderItemId.longValue() == this.orderItemId;
	        }

	     return retVal;
	  }
	    @Override
	    public int hashCode() {
	        int hash = 7;
	        hash = 17 * hash + (this.orderItemId != null ? this.orderItemId.hashCode() : 0);
	        return hash;
	    }

}
