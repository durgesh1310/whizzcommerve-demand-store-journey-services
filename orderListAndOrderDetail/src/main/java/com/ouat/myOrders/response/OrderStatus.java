
package com.ouat.myOrders.response;

public enum OrderStatus {
	
	CDV("Cancelled  by Vendor"),PF("Payment Failed"), OR("Ordered Recieved"), EX("Exchanged"),SD("Shipped"),SV("Shared with Vendor"), DL("Delivered"),CD("Cancelled"), RR("Return Raised"), EXR("Exchange Raised"), RF("Refunded"), PW("Payment Waiting"), WP("Warehouse Processing"),Others("Others"), RTO("Return To Origin Booked"), RTOD("Return To Origin Delivered"), RA("Pickup Assigned"), RC("Pickup Cancelled"), RCP("Pickup Cancelled by Courrier");
	
	private String value;
	private OrderStatus(String value) {
		this.value = value;
		
	}
	private String getValue() {
		return value;
	}
}
