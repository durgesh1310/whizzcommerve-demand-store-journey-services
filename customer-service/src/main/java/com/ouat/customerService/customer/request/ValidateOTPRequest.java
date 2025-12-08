package com.ouat.customerService.customer.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ValidateOTPRequest {
	
	private String signinId;
	
	
	private String otp;
	
	private OTPReason otpReason;


	public String getSigninId() {
		return signinId;
	}


	public void setSigninId(String signinId) {
		this.signinId = signinId;
	}


	public String getOtp() {
		return otp;
	}


	public void setOtp(String otp) {
		this.otp = otp;
	}
	
	
	


	public OTPReason getOtpReason() {
		return otpReason;
	}


	public void setOtpReason(OTPReason otpReason) {
		this.otpReason = otpReason;
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
