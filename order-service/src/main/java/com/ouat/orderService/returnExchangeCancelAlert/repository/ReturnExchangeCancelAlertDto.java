package com.ouat.orderService.returnExchangeCancelAlert.repository;

public class ReturnExchangeCancelAlertDto {
	private String itemName;
	private String sku;
	private String imgUrl;
	private Integer qty;
 	private String reason;
	private RecordType recordType;
	private String orderNumber;

 	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderId(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public RecordType getRecordType() {
		return recordType;
	}
	public void setRecordType(RecordType recordType) {
		this.recordType = recordType;
	}
 	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imggUrl) {
		this.imgUrl = imggUrl;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
 
	 
	public ReturnExchangeCancelAlertDto(String itemName, String sku, String imgUrl, Integer qty, String reason,
			RecordType recordType, String orderNumber) {
		super();
		this.itemName = itemName;
		this.sku = sku;
		this.imgUrl = imgUrl;
		this.qty = qty;
		this.reason = reason;
		this.recordType = recordType;
		this.orderNumber = orderNumber;
	}
 
	
	
	
	@Override
	public String toString() {
		return "ReturnExchangeCancelAlertDto [itemName=" + itemName + ", sku=" + sku + ", imgUrl=" + imgUrl + ", qty="
				+ qty + ", reason=" + reason + ", recordType=" + recordType + ", orderNumber=" + orderNumber + "]";
	}
	public ReturnExchangeCancelAlertDto() {
		super();
		// TODO Auto-generated constructor stub
	}
}
