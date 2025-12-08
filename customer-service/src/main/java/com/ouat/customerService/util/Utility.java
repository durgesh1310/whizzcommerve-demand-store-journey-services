package com.ouat.customerService.util;

import java.util.Arrays;
import java.util.SplittableRandom;

import com.ouat.customerService.client.EmailSendRequest;
import com.ouat.customerService.constants.CommonConstant;
import com.ouat.customerService.customer.request.OTPRequestForEmailSMSService;

public class Utility {

	public static String generateRandomNumber(Integer length) {
		StringBuilder generatedOTP = new StringBuilder();
		SplittableRandom splittableRandom = new SplittableRandom();
		for (int i = 0; i < length; i++) {

		    int randomNumber = splittableRandom.nextInt(0, 9);
		    generatedOTP.append(randomNumber);
		}
		return generatedOTP.toString();
		/*
		if (null == length || length < CommonConstant.OTP_LENGTH)
			throw new RuntimeException("length must be less than or equals to "+ CommonConstant.OTP_LENGTH);
		int suffixLength = length - CommonConstant.OTP_LENGTH;
		int setp = (int) Math.round((double) suffixLength / 18);
		int mod = suffixLength % 18;
		StringBuilder randomNumber = new StringBuilder(String.valueOf(System.currentTimeMillis()));
		for (int k = 0; k < setp; k++) {
			randomNumber.append(String.valueOf((long) (Math.random() * Math.pow(10, 18))));
		}
		if (mod != 0)
			randomNumber.append(String.valueOf((long) (Math.random() * Math.pow(10, mod))));
		
		return randomNumber.toString().substring(0, 6); */
	}
	
	
	public static EmailSendRequest buildOTPEmailRequest(String email, String otp) {
		return new EmailSendRequest(Arrays.asList(email), CommonConstant.SUBJECT, CommonConstant.FROM_EMAIL, CommonConstant.FROM_NAME, String.format(CommonConstant.MESSAGE_BODY, otp));
	}
	
	
	public static OTPRequestForEmailSMSService buildOTPSMSRequest(String mobile, String otp) {
		return new OTPRequestForEmailSMSService(mobile, otp);

	}
}
