package com.ouat.notificationsender.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.notificationsender.client.EmailSendRequest;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CashbackAlert {
	 private String name;
	 private Integer cashbackPercentage;
	 private Integer amount;
	 private Long orderNumber;
	 private EmailSendRequest email;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCashbackPercentage() {
		return cashbackPercentage;
	}
	public void setCashbackPercentage(Integer cashbackPercentage) {
		this.cashbackPercentage = cashbackPercentage;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	 
	public EmailSendRequest getEmail() {
		return email;
	}
	public void setEmail(EmailSendRequest email) {
		this.email = email;
	}
	public CashbackAlert(String name, Integer cashbackPercentage, Integer amount, Long orderNumber,
			EmailSendRequest email) {
		super();
		this.name = name;
		this.cashbackPercentage = cashbackPercentage;
		this.amount = amount;
		this.orderNumber = orderNumber;
		this.email = email;
	}
	public Long getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}
	public CashbackAlert() {
		super();
		// TODO Auto-generated constructor stub
	}
	 
	 
	 

}
