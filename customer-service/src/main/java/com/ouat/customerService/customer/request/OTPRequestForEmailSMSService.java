package com.ouat.customerService.customer.request;

import java.io.Serializable;

public class OTPRequestForEmailSMSService implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mobile;
    
    private String otp;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public OTPRequestForEmailSMSService(String mobile, String otp) {
		super();
		this.mobile = mobile;
		this.otp = otp;
	}
	
	
    
}
