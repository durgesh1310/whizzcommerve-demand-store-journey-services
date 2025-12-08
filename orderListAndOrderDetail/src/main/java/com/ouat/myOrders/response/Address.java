package com.ouat.myOrders.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Address {
	
	private String fullName;
	
	private Integer pincode;
	
	private String address;
	
	private String landmark;
	
	private String city;
	
	private String state;
	
	private String mobile;
	
	private Integer addressId;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getPincode() {
		return pincode;
	}

	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	@Override
	public String toString() {
		return "Address [fullName=" + fullName + ", pincode=" + pincode + ", address=" + address + ", landmark="
				+ landmark + ", city=" + city + ", state=" + state + ", mobile=" + mobile + ", addressId=" + addressId
				+ "]";
	}

	public Address(String fullName, Integer pincode, String address, String landmark, String city, String state,
			String mobile, Integer addressId) {
		super();
		this.fullName = fullName;
		this.pincode = pincode;
		this.address = address;
		this.landmark = landmark;
		this.city = city;
		this.state = state;
		this.mobile = mobile;
		this.addressId = addressId;
	}

	public Address() {
		super();
		// TODO Auto-generated constructor stub
	}
	 
	 
	 
}
