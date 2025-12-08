package com.ouat.customerService.customer.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.customerService.customer.request.Gender;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CustomerDetailResponse {
	
	
	private String firstName;
	
	private String lastName;
	
	private Gender gender;
	
	private String mobile;
	
	private String email;
	
	private Integer isActive;
	
	private Integer customerId;
	
	private String userType;
	
	private Integer isNewUser;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	
	
	
	
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	

	
	
	public Integer getIsNewUser() {
		return isNewUser;
	}

	public void setIsNewUser(Integer isNewUser) {
		this.isNewUser = isNewUser;
	}

	public CustomerDetailResponse(Integer customerId, String firstName, String lastName, Gender gender, String mobile, String email,
			Integer isActive, String userType, Integer isNewUser) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.mobile = mobile;
		this.email = email;
		this.isActive = isActive;
		this.customerId = customerId;
		this.userType = userType;
		this.isNewUser = isNewUser;
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
