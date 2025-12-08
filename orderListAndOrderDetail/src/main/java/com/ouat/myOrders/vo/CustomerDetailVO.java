package com.ouat.myOrders.vo;

import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
 
public class CustomerDetailVO {

	private String os;

	private String browser;

	private String userClient;

	private String deviceId;

	private DeviceType deviceType;

	private String key;

	private Platform platform;

	private String appVersion;

	private String apiVersion;

	private Long customerId;

	private String email;

	private String mobile;

	private String gender;

	private Integer isActive;
	
	private Date expiry;
	
	private String token;

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getUserClient() {
		return userClient;
	}

	public void setUserClient(String userClient) {
		this.userClient = userClient;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	
	
	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
	public CustomerDetailVO(String os, String browser, String userClient, String deviceId, DeviceType deviceType,
			String key, Platform platform, String appVersion, String apiVersion, Long customerId, String email,
			String mobile, String gender, Integer isActive, Date expiry, String token) {
		super();
		this.os = os;
		this.browser = browser;
		this.userClient = userClient;
		this.deviceId = deviceId;
		this.deviceType = deviceType;
		this.key = key;
		this.platform = platform;
		this.appVersion = appVersion;
		this.apiVersion = apiVersion;
		this.customerId = customerId;
		this.email = email;
		this.mobile = mobile;
		this.gender = gender;
		this.isActive = isActive;
		this.expiry = expiry;
		this.token = token;
	}
	
	

	public CustomerDetailVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Don't change this
	 */
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
