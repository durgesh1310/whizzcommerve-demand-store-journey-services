package com.ouat.customerService.customer.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LoginRepository {
	
	
	@Autowired
	JDBCTemplateWrapper jdbcTemplateWrapper;
	
	
	public int saveOTP(String signInId, String otp, String reason) {
		return jdbcTemplateWrapper.execute("insert into customers.customer_otp (signin_id, otp, otp_reason, expiry) values (?, ?, ?, DATE_ADD(NOW(), INTERVAL 2 MINUTE)) ON DUPLICATE KEY UPDATE otp = ?, otp_reason = ?, expiry = DATE_ADD(NOW(), INTERVAL 2 MINUTE), created_date = now()", signInId, otp, reason, otp, reason);
	}
	
	public boolean validateOTP(String signInId, String otp, String reason) {
		Integer flag = jdbcTemplateWrapper.findInteger("Select count(1) FROM customers.customer_otp where signin_id = ? AND otp = ? AND otp_reason = ? AND expiry > now()", signInId, otp, reason);
		return flag != null && flag == 1 ? true : false; 
	}

	public int deleteOTPEntry(String signinId) {
		return jdbcTemplateWrapper.execute("Delete FROM customers.customer_otp where signin_id = ?", signinId);
	}

}
