package com.ouat.orderService.returnExchangeCancelAlert.repository;

public enum RecordType {
	 
    CANCELLED("Cancelled"), RETURN("Return"), REJECTED("Rejected"), EXCHANGE("Exchange"),  Others("Others");
	
	private String value;
	private RecordType(String value) {
		this.value = value;
	}
	private String getValue() {
		return value;
	}
}

