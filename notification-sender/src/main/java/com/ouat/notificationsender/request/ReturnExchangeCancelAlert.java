package com.ouat.notificationsender.request;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.notificationsender.client.EmailSendRequest;
import com.ouat.notificationsender.response.EmailAlertType;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReturnExchangeCancelAlert {
	private String customerName;
	private String orderNumber;
	private String returnExchangeCancelDate;
	private String imgUrl;
	private String itemName;
	private String sku;
	private Integer qty;
	private String reason;
 	private RecordType recordType;
	private EmailAlertType alertType;
	private EmailSendRequest emailSendRequest;
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getReturnExchangeCancelDate() {
		return returnExchangeCancelDate;
	}
	public void setReturnExchangeCancelDate(String returnExchangeCancelDate) {
		this.returnExchangeCancelDate = returnExchangeCancelDate;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
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
	public RecordType getRecordType() {
		return recordType;
	}
	public void setRecordType(RecordType recordType) {
		this.recordType = recordType;
	}
	public EmailAlertType getAlertType() {
		return alertType;
	}
	public void setAlertType(EmailAlertType alertType) {
		this.alertType = alertType;
	}
	public EmailSendRequest getEmailSendRequest() {
		return emailSendRequest;
	}
	public void setEmailSendRequest(EmailSendRequest emailSendRequest) {
		this.emailSendRequest = emailSendRequest;
	}
	@Override
	public String toString() {
		return "ReturnExchangeCancelAlert [customerName=" + customerName + ", orderNumber=" + orderNumber
				+ ", returnExchangeCancelDate=" + returnExchangeCancelDate + ", imgUrl=" + imgUrl + ", itemName="
				+ itemName + ", sku=" + sku + ", qty=" + qty + ", reason=" + reason + ", recordType=" + recordType
				+ ", alertType=" + alertType + ", emailSendRequest=" + emailSendRequest + "]";
	}
	public ReturnExchangeCancelAlert(String customerName, String orderNumber, String returnExchangeCancelDate, String imgUrl,
			String itemName, String sku, Integer qty, String reason, RecordType recordType, EmailAlertType alertType,
			EmailSendRequest emailSendRequest) {
		super();
		this.customerName = customerName;
		this.orderNumber = orderNumber;
		this.returnExchangeCancelDate = returnExchangeCancelDate;
		this.imgUrl = imgUrl;
		this.itemName = itemName;
		this.sku = sku;
		this.qty = qty;
		this.reason = reason;
		this.recordType = recordType;
		this.alertType = alertType;
		this.emailSendRequest = emailSendRequest;
	}
	public ReturnExchangeCancelAlert() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	 
	 
	

}
