package com.ouat.customerService.dto;

import java.sql.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomerCredit {
	
	
	private Double amount;
	
	
	private String creditType;
	
	
	private Date expiry;


	public Double getAmount() {
		return amount;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public String getCreditType() {
		return creditType;
	}


	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}


	public Date getExpiry() {
		return expiry;
	}


	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}
	
	
	
	
	public CustomerCredit(Double amount, String creditType, Date expiry) {
		super();
		this.amount = amount;
		this.creditType = creditType;
		this.expiry = expiry;
	}


	@Override
	public String toString() {
		String serialized = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			serialized = objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException jpe) {
			jpe.printStackTrace();
		}
		return serialized;
	}
	
	
	
	
}
