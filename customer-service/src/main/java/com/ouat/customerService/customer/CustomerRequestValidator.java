package com.ouat.customerService.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ouat.customerService.constants.CommonConstant;
import com.ouat.customerService.customer.repository.CustomerRepository;
import com.ouat.customerService.customer.request.AddGuestUserRequest;
import com.ouat.customerService.customer.request.CustomerAddressRequest;
import com.ouat.customerService.customer.request.CustomerRequest;
import com.ouat.customerService.customer.request.Gender;
import com.ouat.customerService.exception.BusinessProcessException;

@Service
public class CustomerRequestValidator {
	
	
	@Autowired
	private CustomerRepository customerRepository;
	
	
	public boolean isValidMobile(String emailOrPhone) {
		return CommonConstant.MOBILE_NUMBER_PATTERN.matcher(emailOrPhone).matches();
	}


	public boolean isValidEmail(String email) {
		return CommonConstant.EMAIL_PATTERN.matcher(email).matches();
	}


	public void validateCustomerRequest(CustomerRequest customerRequest) throws BusinessProcessException {

		if (!isValidEmail(customerRequest.getEmailId())) {
			throw new BusinessProcessException(CommonConstant.INVALID_EMAIL, CommonConstant.SUCCESS_STATUS_CODE);
		}

		if (!isValidMobile(customerRequest.getMobile())) {
			throw new BusinessProcessException(CommonConstant.INVALID_MOBILE, CommonConstant.SUCCESS_STATUS_CODE);
		}
		
		if(customerRepository.isCustomerAlreadyExistsWithEmail(customerRequest.getEmailId())) {
			throw new BusinessProcessException(String.format(CommonConstant.ACCOUNT_ALREADY_EXISTS_WITH_EMAIL, customerRequest.getEmailId()), CommonConstant.SUCCESS_STATUS_CODE);
		}
		
		if(customerRepository.isCustomerAlreadyExistsWithMobile(customerRequest.getMobile())) {
			throw new BusinessProcessException(String.format(CommonConstant.ACCOUNT_ALREADY_EXISTS_WITH_MOBILE, customerRequest.getMobile()), CommonConstant.SUCCESS_STATUS_CODE);
		}
		
		if(customerRequest.getGender() == null) {
			customerRequest.setGender(Gender.UNKNOWN);
		}
	}
	
	
	public void validateCustomerRequest(CustomerAddressRequest customerRequest) throws BusinessProcessException {
		
		if(customerRequest.getEmail() == null || "".equals(customerRequest.getEmail())) {
			throw new BusinessProcessException(CommonConstant.INVALID_EMAIL, CommonConstant.SUCCESS_STATUS_CODE);
		}

		if (!isValidEmail(customerRequest.getEmail())) {
			throw new BusinessProcessException(CommonConstant.INVALID_EMAIL, CommonConstant.SUCCESS_STATUS_CODE);
		}

		if (!isValidMobile(customerRequest.getMobile())) {
			throw new BusinessProcessException(CommonConstant.INVALID_MOBILE, CommonConstant.SUCCESS_STATUS_CODE);
		}
	}


	public void validateCustomerRequest(AddGuestUserRequest addGuestUserRequest) throws BusinessProcessException {
		if (!isValidEmail(addGuestUserRequest.getEmail())) {
			throw new BusinessProcessException(CommonConstant.INVALID_EMAIL, CommonConstant.SUCCESS_STATUS_CODE);
		}

		if (!isValidMobile(addGuestUserRequest.getMobile())) {
			throw new BusinessProcessException(CommonConstant.INVALID_MOBILE, CommonConstant.SUCCESS_STATUS_CODE);
		}
	}

}
