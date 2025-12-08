package com.ouat.myOrders.response;

public class ActionButton {
	private ActionType actionType;
	private String actionUrl;
	public ActionType getActionType() {
		return actionType;
	}
	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public ActionButton(ActionType actionType, String actionUrl) {
		super();
		this.actionType = actionType;
		this.actionUrl = actionUrl;
	}
	public ActionButton() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ActionButton [actionType=" + actionType + ", actionUrl=" + actionUrl + "]";
	}
	

}
