package com.ouat.notificationsender.request;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DeliveryAddressPlaceOrderAlert {
	
    private String fullName;

    private Integer pincode;

    private String address;

    private String landmark;

    private String city;

    private String state;

    private String mobile;
    
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

	public DeliveryAddressPlaceOrderAlert(String fullName, Integer pincode, String address, String landmark, String city,
			String state, String mobile) {
		super();
		this.fullName = fullName;
		this.pincode = pincode;
		this.address = address;
		this.landmark = landmark;
		this.city = city;
		this.state = state;
		this.mobile = mobile;
	 
	}

	public DeliveryAddressPlaceOrderAlert() {
		super();
		// TODO Auto-generated constructor stub
	}  
}
