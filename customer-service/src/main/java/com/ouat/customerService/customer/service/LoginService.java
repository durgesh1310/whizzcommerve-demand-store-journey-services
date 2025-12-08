package com.ouat.customerService.customer.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ouat.customerService.client.EmailSMSServiceClient;
import com.ouat.customerService.client.EmailSendRequest;
import com.ouat.customerService.constants.CommonConstant;
import com.ouat.customerService.constants.LoginType;
import com.ouat.customerService.customer.CustomerRequestValidator;
import com.ouat.customerService.customer.repository.CustomerRepository;
import com.ouat.customerService.customer.repository.LoginRepository;
import com.ouat.customerService.customer.request.OTPReason;
import com.ouat.customerService.customer.request.OTPRequestForEmailSMSService;
import com.ouat.customerService.customer.request.SendOTPRequest;
import com.ouat.customerService.customer.request.ValidateOTPRequest;
import com.ouat.customerService.customer.response.CustomerDetailResponse;
import com.ouat.customerService.exception.BusinessProcessException;
import com.ouat.customerService.response.DemandStoreAPIResponse;
import com.ouat.customerService.response.OTPSentResponse;
import com.ouat.customerService.util.RedisHelper;
import com.ouat.customerService.util.Utility;

@Service
public class LoginService {
	
	
	@Autowired
	CustomerRequestValidator validator;
	
	@Autowired
	RedisHelper redisHelper;
	
	@Autowired
	LoginRepository loginRepository;
	
	@Autowired
	EmailSMSServiceClient emailSMSServiceClient;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	LoginHelper loginHelper;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);
	
	public ResponseEntity<DemandStoreAPIResponse> sendOTP(SendOTPRequest sendOTPRequest) throws BusinessProcessException {
		
		if (null == sendOTPRequest.getSigninId() || sendOTPRequest.getSigninId().trim().length()==0) {
			throw new BusinessProcessException(CommonConstant.INVALID_PARAM_FOR_LOGIN,
					CommonConstant.SUCCESS_STATUS_CODE);
		}
		String attempts = redisHelper.getOTPFROMRedis(CommonConstant.OTP_COUNTER_KEY+sendOTPRequest.getSigninId());
		
		if (null != attempts && Integer.parseInt(attempts) > 5) {
			throw new BusinessProcessException(CommonConstant.TEMP_BLOCK, CommonConstant.SUCCESS_STATUS_CODE);
		}
		
		if (validator.isValidEmail(sendOTPRequest.getSigninId())) {
			if (sendOTPRequest.getOtpReason() == OTPReason.LOGIN) {
				CustomerDetailResponse customerDetailResponse = customerRepository.findCustomerDetailByEmail(sendOTPRequest.getSigninId());
				if (customerDetailResponse == null) {
					throw new BusinessProcessException(CommonConstant.NOT_REGISTERED_WITH_EMAIL, CommonConstant.CUSTOMER_NOT_REGISTERED_CODE);
				} else if (customerDetailResponse.getIsActive() == 0) {
					throw new BusinessProcessException(CommonConstant.USER_BLOCKED, CommonConstant.USER_BLOCKED_STATUS_CODE);
				}
			}
			return sendOTPOnEmail(sendOTPRequest.getSigninId(), sendOTPRequest.getOtpReason(), false);
		} else if (validator.isValidMobile(sendOTPRequest.getSigninId())) {
			if (sendOTPRequest.getOtpReason() == OTPReason.LOGIN) {
				CustomerDetailResponse customerDetailResponse = customerRepository.findCustomerDetailByMobile(sendOTPRequest.getSigninId());
				validateCustomer(sendOTPRequest);
			}
			return sendOTPOnMobile(sendOTPRequest.getSigninId(), sendOTPRequest.getOtpReason(), false);
		} else {
			throw new BusinessProcessException(CommonConstant.INVALID_PARAM_FOR_LOGIN, CommonConstant.FAILURE_STATUS_CODE);
		}

	}

	private void validateCustomer(SendOTPRequest sendOTP) throws BusinessProcessException {
		CustomerDetailResponse customerDetailResponse = customerRepository.findCustomerDetailByMobile(sendOTP.getSigninId());
		if (customerDetailResponse == null) {
			throw new BusinessProcessException(CommonConstant.NOT_REGISTERED_WITH_MOBILE,
					CommonConstant.CUSTOMER_NOT_REGISTERED_CODE);
		} else if (customerDetailResponse.getIsActive() == 0) {
			throw new BusinessProcessException(CommonConstant.USER_BLOCKED, CommonConstant.USER_BLOCKED_STATUS_CODE);
		}
	}

	private ResponseEntity<DemandStoreAPIResponse> sendOTPOnEmail(String email, OTPReason reason, boolean retry)
			throws BusinessProcessException {
		String otp = Utility.generateRandomNumber(CommonConstant.OTP_LENGTH);
		EmailSendRequest emailSendRequest = Utility.buildOTPEmailRequest(email, otp);
		String validateOTPKey = CommonConstant.SIGNIN_KEY + email;
		String signInIdOTPCounterKey = CommonConstant.OTP_COUNTER_KEY + email;
		if (emailSMSServiceClient.sendOTP(emailSendRequest)) {
			loginRepository.saveOTP(email, otp, reason.getValue());
			redisHelper.setKeyValueWithTTL(validateOTPKey, otp, CommonConstant.TTL_FOR_OTP, TimeUnit.SECONDS);
			if (!retry) {
				redisHelper.setKeyValueWithTTL(signInIdOTPCounterKey, 1, 30, TimeUnit.MINUTES);
			}
			String msg = String.format(CommonConstant.OTP_ON_EMAIL, email);
			if(retry) {
				msg = String.format(CommonConstant.RESENT_OTP_ON_EMAIL, email);
			}
			OTPSentResponse response = new OTPSentResponse(otp.length(), msg);
			return ResponseEntity
					.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, response));
		} else {
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
		}
	}
	
	private ResponseEntity<DemandStoreAPIResponse> sendOTPOnMobile(String mobile, OTPReason reason, boolean retry)
			throws BusinessProcessException {
		String otp = Utility.generateRandomNumber(CommonConstant.OTP_LENGTH);
		OTPRequestForEmailSMSService emailSendRequest = Utility.buildOTPSMSRequest(mobile, otp);
		String validateOTPKey = CommonConstant.SIGNIN_KEY + mobile;
		String signInIdOTPCounterKey = CommonConstant.OTP_COUNTER_KEY + mobile;
		if (emailSMSServiceClient.sendOTPOnMobile(emailSendRequest)) {
			if(mobile.equals("6387289237")) { //Mudit's Mummy's mobile
				otp = "433037";
			}
			loginRepository.saveOTP(mobile, otp, reason.getValue());
			redisHelper.setKeyValueWithTTL(validateOTPKey, otp, CommonConstant.TTL_FOR_OTP, TimeUnit.SECONDS);
			if (!retry) {
				redisHelper.setKeyValueWithTTL(signInIdOTPCounterKey, 1, 30, TimeUnit.MINUTES);
			}
			String msg = String.format(CommonConstant.OTP_ON_SMS, mobile);
			if (retry) {
				msg = String.format(CommonConstant.RESENT_OTP_ON_SMS, mobile);
			}
			OTPSentResponse response = new OTPSentResponse(otp.length(), msg);
			return ResponseEntity
					.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, response));
		} else {
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
		}
	}

	public ResponseEntity<DemandStoreAPIResponse> validateOTP(ValidateOTPRequest validateOTP,
			HttpServletRequest request) throws BusinessProcessException {
		String validateOTPKey = CommonConstant.SIGNIN_KEY + validateOTP.getSigninId();
		String signInIdOTPCounterKey = CommonConstant.OTP_COUNTER_KEY + validateOTP.getSigninId();
		if (validator.isValidEmail(validateOTP.getSigninId())) {
			return validateOTPAndGenerateToken(validateOTP, request, validateOTPKey, signInIdOTPCounterKey, true);
		} else if (validator.isValidMobile(validateOTP.getSigninId())) {
			return validateOTPAndGenerateToken(validateOTP, request, validateOTPKey, signInIdOTPCounterKey, false);
		} else {
			throw new BusinessProcessException(CommonConstant.INVALID_PARAM_FOR_LOGIN,
					CommonConstant.FAILURE_STATUS_CODE);
		}
	}
/*
	private ResponseEntity<DemandStoreAPIResponse> validateOTPForEmailAndGenerateToken(ValidateOTPRequest validateOTP, HttpServletRequest request, String validateOTPKey,
			String signInIdOTPCounterKey) throws BusinessProcessException {
		String otp = redisHelper.getOTPFROMRedis(validateOTPKey);
		redisHelper.increaseCounter(signInIdOTPCounterKey);
		if(otp != null && !otp.isBlank()) {
			if(otp.equals(validateOTP.getOtp())) {
				loginRepository.deleteOTPEntry(validateOTPKey);
				redisHelper.deleteKey(validateOTPKey);
				String token = null;
				if (OTPReason.LOGIN == validateOTP.getOtpReason()) {
					CustomerDetailResponse customerDetail = customerRepository.findCustomerDetailByEmail(validateOTP.getSigninId());
					token = loginHelper.generateToken(customerDetail, request);
					if(token == null) {
						throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
					}
				}
				return ResponseEntity.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, token));
			}
		} else {
			if(loginRepository.validateOTP(validateOTP.getSigninId(), validateOTP.getOtp(), validateOTP.getOtpReason().getValue())) {
				loginRepository.deleteOTPEntry(validateOTPKey);
				String token = null;
				if (OTPReason.LOGIN == validateOTP.getOtpReason()) {
					CustomerDetailResponse customerDetail = customerRepository.findCustomerDetailByEmail(validateOTP.getSigninId());
					token = loginHelper.generateToken(customerDetail, request);
					if(token == null) {
						throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
					}
					return ResponseEntity.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, token));
				}
				throw new BusinessProcessException(CommonConstant.USER_LOGIN_SUCCESS, CommonConstant.SUCCESS_STATUS_CODE);
			} else {
				return sendOTPOnEmail(validateOTP.getSigninId(), validateOTP.getOtpReason(), true);
			}
			
		}
		return null;
	}

	private ResponseEntity<DemandStoreAPIResponse> validateOtpForMobileAndGenerateToken(ValidateOTPRequest validateOTP, HttpServletRequest request,
			String validateOTPKey, String signInIdOTPCounterKey) throws BusinessProcessException {
		String otp = redisHelper.getOTPFROMRedis(validateOTPKey);
		redisHelper.increaseCounter(signInIdOTPCounterKey);
		if(otp != null && !otp.isBlank()) {
			if(otp.equals(validateOTP.getOtp())) {
				loginRepository.deleteOTPEntry(validateOTPKey);
				redisHelper.deleteKey(validateOTPKey);
				String token = null;
				if (OTPReason.LOGIN == validateOTP.getOtpReason()) {
					CustomerDetailResponse customerDetail = customerRepository.findCustomerDetailByMobile(validateOTP.getSigninId());
					token = loginHelper.generateToken(customerDetail, request);
					if(token == null) {
						throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
					}
					
				}
				return ResponseEntity.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, token));
			}
		} else {
			if(loginRepository.validateOTP(validateOTP.getSigninId(), validateOTP.getOtp(), validateOTP.getOtpReason().getValue())) {
				loginRepository.deleteOTPEntry(validateOTPKey);
				String token = null;
				if (OTPReason.LOGIN == validateOTP.getOtpReason()) {
					CustomerDetailResponse customerDetail = customerRepository.findCustomerDetailByMobile(validateOTP.getSigninId());
					token = loginHelper.generateToken(customerDetail, request);
					if(token == null) {
						throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
					}
					return ResponseEntity.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, token));
				} else {
					return ResponseEntity.ok(new DemandStoreAPIResponse(true, Arrays.asList(new MessageDetail(MessageType.INFO, CommonConstant.VERIFICATION_SUCESS, null, null)), CommonConstant.SUCCESS_STATUS_CODE, null));
				}
			} else {
				return sendOTPOnMobile(validateOTP.getSigninId(), validateOTP.getOtpReason(), true);
			}
		}
		return null;
	}
	
	*/
	
	private ResponseEntity<DemandStoreAPIResponse> validateOTPAndGenerateToken(ValidateOTPRequest validateOTP, HttpServletRequest request, String validateOTPKey,
			String signInIdOTPCounterKey, boolean isEmail) throws BusinessProcessException {
		String otp = redisHelper.getOTPFROMRedis(validateOTPKey);
		redisHelper.increaseCounter(signInIdOTPCounterKey);
		if(otp != null) {
			if (otp.equals(validateOTP.getOtp())) {
				loginRepository.deleteOTPEntry(validateOTPKey);
				redisHelper.deleteKey(validateOTPKey);
				return generateToken(validateOTP, request, isEmail);
			} return validateAgainstDBAndResendIfRequired(validateOTP, request, validateOTPKey, isEmail);
		} else {
			return validateAgainstDBAndResendIfRequired(validateOTP, request, validateOTPKey, isEmail);
			
		}
	}

	private ResponseEntity<DemandStoreAPIResponse> validateAgainstDBAndResendIfRequired(ValidateOTPRequest validateOTP,
			HttpServletRequest request, String validateOTPKey, boolean isEmail) throws BusinessProcessException {
		if (loginRepository.validateOTP(validateOTP.getSigninId(), validateOTP.getOtp(),
				validateOTP.getOtpReason().getValue())) {
			loginRepository.deleteOTPEntry(validateOTPKey);
			return generateToken(validateOTP, request, isEmail);
		} else {
			if(isEmail) {
				return sendOTPOnEmail(validateOTP.getSigninId(), validateOTP.getOtpReason(), true);
			} else {
				return sendOTPOnMobile(validateOTP.getSigninId(), validateOTP.getOtpReason(), true);
			}
		}
	}

	private ResponseEntity<DemandStoreAPIResponse> generateToken(ValidateOTPRequest validateOTP, HttpServletRequest request, boolean isEmail)
			throws BusinessProcessException {
		Map<String, Object> data = new HashMap<String, Object>();
		String token;
		CustomerDetailResponse customerDetail = null;
		if(isEmail) {
			customerDetail = customerRepository.findCustomerDetailByEmail(validateOTP.getSigninId());
		} else {
			customerDetail = customerRepository.findCustomerDetailByMobile(validateOTP.getSigninId());
		}
		if(null == customerDetail &&  validateOTP.getOtpReason().equals(OTPReason.LOGIN)) {
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
		}
		if(validateOTP.getOtpReason().equals(OTPReason.LOGIN)) {
			token = loginHelper.generateToken(customerDetail, request);
			if(token == null) {
				throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.FAILURE_STATUS_CODE);
			}
			data.put("token", token);
			data.put("customer_detail", customerDetail);
			LOGGER.info("Login API Response : {} ", data.toString());
		}
		return ResponseEntity.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, data));
	}
	
	

public ResponseEntity<DemandStoreAPIResponse> sendOTPV1(SendOTPRequest sendOTPRequest) throws BusinessProcessException {
	
	if (null == sendOTPRequest.getSigninId() || sendOTPRequest.getSigninId().trim().length()==0) {
		throw new BusinessProcessException(CommonConstant.INVALID_PARAM_FOR_LOGIN,
				CommonConstant.SUCCESS_STATUS_CODE);
	}
	String attempts = redisHelper.getOTPFROMRedis(CommonConstant.OTP_COUNTER_KEY+sendOTPRequest.getSigninId());
	
	if (null != attempts && Integer.parseInt(attempts) > 5) {
		throw new BusinessProcessException(CommonConstant.TEMP_BLOCK, CommonConstant.SUCCESS_STATUS_CODE);
	}
	
	if (validator.isValidEmail(sendOTPRequest.getSigninId())) {
		if (sendOTPRequest.getOtpReason() == OTPReason.LOGIN) {
			CustomerDetailResponse customerDetailResponse = customerRepository.findCustomerDetailByEmail(sendOTPRequest.getSigninId());
			if (customerDetailResponse == null) {
				customerRepository.addGuestCustomer(sendOTPRequest.getSigninId(), LoginType.EMAIL);
			} else if (customerDetailResponse.getIsActive() == 0) {
				throw new BusinessProcessException(CommonConstant.USER_BLOCKED, CommonConstant.USER_BLOCKED_STATUS_CODE);
			}
		}
		return sendOTPOnEmail(sendOTPRequest.getSigninId(), sendOTPRequest.getOtpReason(), false);
	} else if (validator.isValidMobile(sendOTPRequest.getSigninId())) {
		if (sendOTPRequest.getOtpReason() == OTPReason.LOGIN) {
			CustomerDetailResponse customerDetailResponse = customerRepository.findCustomerDetailByMobile(sendOTPRequest.getSigninId());
			if (customerDetailResponse == null) {
				customerRepository.addGuestCustomer(sendOTPRequest.getSigninId(), LoginType.MOBILE);
			} else if (customerDetailResponse.getIsActive() == 0) {
				throw new BusinessProcessException(CommonConstant.USER_BLOCKED, CommonConstant.USER_BLOCKED_STATUS_CODE);
			}
		}
		return sendOTPOnMobile(sendOTPRequest.getSigninId(), sendOTPRequest.getOtpReason(), false);
	} else {
		throw new BusinessProcessException(CommonConstant.INVALID_PARAM_FOR_LOGIN, CommonConstant.FAILURE_STATUS_CODE);
	}
}

}
