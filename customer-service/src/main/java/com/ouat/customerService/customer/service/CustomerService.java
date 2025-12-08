package com.ouat.customerService.customer.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ouat.customerService.constants.CommonConstant;
import com.ouat.customerService.customer.CustomerRequestValidator;
import com.ouat.customerService.customer.repository.CustomerCreditRepository;
import com.ouat.customerService.customer.repository.CustomerRepository;
import com.ouat.customerService.customer.request.AddGuestUserRequest;
import com.ouat.customerService.customer.request.CustomerAddressRequest;
import com.ouat.customerService.customer.request.CustomerRequest;
import com.ouat.customerService.customer.request.EmailAddRequest;
import com.ouat.customerService.customer.request.Gender;
import com.ouat.customerService.customer.response.AddGuestUserResponse;
import com.ouat.customerService.customer.response.AddressDetailResponse;
import com.ouat.customerService.customer.response.CustomerDetailResponse;
import com.ouat.customerService.exception.BusinessProcessException;
import com.ouat.customerService.response.CustomerDetailVO;
import com.ouat.customerService.response.DemandStoreAPIResponse;
import com.ouat.customerService.response.MessageDetail;
import com.ouat.customerService.response.MessageType;

@Service
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

	
	@Autowired
	private CustomerRequestValidator validator;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CustomerCreditRepository customerCreditRepository;
	
	@Autowired 
	CustomerRequestValidator customerRequestValidator;
	
	@Transactional
	public ResponseEntity<DemandStoreAPIResponse> addCustomer(CustomerRequest customerRequest, String platform)
			throws BusinessProcessException {
		validator.validateCustomerRequest(customerRequest);
		int customerId = customerRepository.signup(customerRequest); 
		if (customerId > 0) {
			/*
			if("WEB".equalsIgnoreCase(platform)) {
				processCreditsForSignup(new Long(customerId), platform);
			}
			*/
			return ResponseEntity.ok(new DemandStoreAPIResponse(true,
					Arrays.asList(new MessageDetail(MessageType.INFO, CommonConstant.CUSTOMER_ADDED, null, null)),
					CommonConstant.SUCCESS_STATUS_CODE, customerId));
		} else {
			return  ResponseEntity.ok(new DemandStoreAPIResponse(false,
					Arrays.asList(new MessageDetail(MessageType.ERROR, CommonConstant.SOMETHING_WENT_WRONG, null, null)),
					CommonConstant.FAILURE_STATUS_CODE, null));
		}
	}
	
	public ResponseEntity<DemandStoreAPIResponse> updateCustomer(CustomerRequest customerRequest)
			throws BusinessProcessException {
		validator.validateCustomerRequest(customerRequest);
		if (customerRepository.addCustomer(customerRequest) > 0) {
			return ResponseEntity.ok(new DemandStoreAPIResponse(true,
					Arrays.asList(new MessageDetail(MessageType.INFO, CommonConstant.CUSTOMER_ADDED, null, null)),
					CommonConstant.SUCCESS_STATUS_CODE, null));
		} else {
			return  ResponseEntity.ok(new DemandStoreAPIResponse(false,
					Arrays.asList(new MessageDetail(MessageType.ERROR, CommonConstant.SOMETHING_WENT_WRONG, null, null)),
					CommonConstant.FAILURE_STATUS_CODE, null));
		}
	}


	public ResponseEntity<DemandStoreAPIResponse> findCustomerDetailByEmail(String email) throws BusinessProcessException {
		CustomerDetailResponse customerDetail = customerRepository.findCustomerDetailByEmail(email);
		if(customerDetail == null) {
			throw new BusinessProcessException(CommonConstant.EMAIL_NOT_REGISTERED, CommonConstant.SUCCESS_STATUS_CODE);
		}
		return ResponseEntity.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, customerDetail));
	}


	public ResponseEntity<DemandStoreAPIResponse> findCustomerDetailByMobile(String mobile) throws BusinessProcessException {
		CustomerDetailResponse customerDetail = customerRepository.findCustomerDetailByMobile(mobile);
		if(customerDetail == null) {
			throw new BusinessProcessException(CommonConstant.MOBILE_NOT_REGISTERED, CommonConstant.SUCCESS_STATUS_CODE);
		}
		return ResponseEntity.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, customerDetail));
	}


	public ResponseEntity<DemandStoreAPIResponse> addAddress(CustomerAddressRequest customerAddressRequest)
			throws BusinessProcessException {
		if (!customerRepository.isCustomerExists(customerAddressRequest.getCustomerId())) {
			throw new BusinessProcessException(CommonConstant.CUSTOMER_NOT_REGISTERED,
					CommonConstant.SUCCESS_STATUS_CODE);
		}
		int flag = 0;
		try {
			List<AddressDetailResponse> addressList = customerRepository.findAddress(customerAddressRequest.getCustomerId());
			if(addressList == null || addressList.isEmpty()) {
				customerRepository.updateCustomerName(customerAddressRequest.getFullName(), customerAddressRequest.getCustomerId());
				
			}
			flag = customerRepository.addCustomerAddress(customerAddressRequest);
		} catch (Exception e) {
			flag = -1;
		}
		/*
		if (flag == -1) {
			return ResponseEntity.ok(new DemandStoreAPIResponse(false,
					Arrays.asList(
							new MessageDetail(MessageType.ERROR, CommonConstant.CUSTOMER_ADDRESS_EXISTS_REGISTERED, null, null)),
					CommonConstant.SUCCESS_STATUS_CODE, null));
		}
		*/
		if (flag > 0) {
			return ResponseEntity.ok(new DemandStoreAPIResponse(true,
					Arrays.asList(new MessageDetail(MessageType.INFO, CommonConstant.CUSTOMER_ADDRESS_CREATED, null, null)),
					CommonConstant.SUCCESS_STATUS_CODE, null));
		} else {
			return ResponseEntity.ok(new DemandStoreAPIResponse(false,
					Arrays.asList(new MessageDetail(MessageType.ERROR, CommonConstant.SOMETHING_WENT_WRONG, null, null)),
					CommonConstant.SUCCESS_STATUS_CODE, null));
		}
	}


	public ResponseEntity<DemandStoreAPIResponse> updateAddress(CustomerAddressRequest customerAddressRequest)
			throws BusinessProcessException {
		if (!customerRepository.isCustomerExists(customerAddressRequest.getCustomerId())) {
			throw new BusinessProcessException(CommonConstant.CUSTOMER_NOT_REGISTERED,
					CommonConstant.SUCCESS_STATUS_CODE);
		}

		int flag = 0;
		try {
			flag = customerRepository.updateAddress(customerAddressRequest);
		} catch (Exception e) {
			e.printStackTrace();
			flag = -1;
		}
		/*
		if (flag == -1) {
			return ResponseEntity.ok(new DemandStoreAPIResponse(false,
					Arrays.asList(
							new MessageDetail(MessageType.ERROR, CommonConstant.CUSTOMER_ADDRESS_EXISTS_REGISTERED, null, null)),
					CommonConstant.SUCCESS_STATUS_CODE, null));
		}
		*/
		if (flag > 0) {
			return ResponseEntity.ok(new DemandStoreAPIResponse(true,
					Arrays.asList(new MessageDetail(MessageType.INFO, CommonConstant.CUSTOMER_ADDRESS_UPDATED, null, null)),
					CommonConstant.SUCCESS_STATUS_CODE, null));
		} else {
			return ResponseEntity.ok(new DemandStoreAPIResponse(false,
					Arrays.asList(new MessageDetail(MessageType.ERROR, CommonConstant.SOMETHING_WENT_WRONG, null, null)),
					CommonConstant.SUCCESS_STATUS_CODE, null));
		}
	}

	public ResponseEntity<DemandStoreAPIResponse> findAddress(Integer customerId) throws BusinessProcessException {
		if (!customerRepository.isCustomerExists(customerId)) {
			throw new BusinessProcessException(CommonConstant.CUSTOMER_NOT_REGISTERED,
					CommonConstant.SUCCESS_STATUS_CODE);
		}
		List<AddressDetailResponse> addressList = customerRepository.findAddress(customerId);
		if (null == addressList || addressList.isEmpty()) {

			return ResponseEntity.ok(new DemandStoreAPIResponse(false,
					Arrays.asList(new MessageDetail(MessageType.INFO, CommonConstant.PLZ_ADD_ADDRESS, null, null)),
					CommonConstant.SUCCESS_STATUS_CODE, null));

		} else {
			return ResponseEntity.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, addressList));
		}
	}
	
	public ResponseEntity<DemandStoreAPIResponse> findAddressByAddressId(Integer customerId, Integer addressId) throws BusinessProcessException {
		if (!customerRepository.isCustomerExists(customerId)) {
			throw new BusinessProcessException(CommonConstant.CUSTOMER_NOT_REGISTERED,
					CommonConstant.SUCCESS_STATUS_CODE);
		}
		AddressDetailResponse address = customerRepository.findAddress(customerId, addressId);
		if (null == address) {

			return ResponseEntity.ok(new DemandStoreAPIResponse(false,
					Arrays.asList(new MessageDetail(MessageType.INFO, CommonConstant.PLZ_ADD_ADDRESS, null, null)),
					CommonConstant.SUCCESS_STATUS_CODE, null));

		} else {
			return ResponseEntity.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, address));
		}
	}

	public ResponseEntity<DemandStoreAPIResponse> deleteAddress(Long customerId, Integer addressId)
			throws BusinessProcessException {
		if (!customerRepository.isCustomerAddressExists(customerId, addressId)) {
			throw new BusinessProcessException(CommonConstant.INVALID_ADDRESS, CommonConstant.SUCCESS_STATUS_CODE);
		}
		try {
		if (customerRepository.deleteAddress(customerId, addressId) == 1) {
			return ResponseEntity.ok(new DemandStoreAPIResponse(true,
					Arrays.asList(new MessageDetail(MessageType.INFO, "Address removed !!", null, null)),
					CommonConstant.SUCCESS_STATUS_CODE, null));
		} else {
			throw new BusinessProcessException(CommonConstant.SOMETHING_WENT_WRONG, CommonConstant.SUCCESS_STATUS_CODE);
		}
		} catch (Exception e) {
			return ResponseEntity.ok(new DemandStoreAPIResponse(true,
					Arrays.asList(new MessageDetail(MessageType.INFO, "This address you can not remove !!", null, null)),
					CommonConstant.SUCCESS_STATUS_CODE, null));
		}
	}
	
	@Transactional
	public ResponseEntity<DemandStoreAPIResponse> processCreditsForFirstInstall(Long customerId, String platfrom) throws BusinessProcessException {
		/*
		if(customerRepository.hasCustomerAvailedAppInstallCredits(customerId) || customerRepository.hasCustomerAvailedSignupCredits(customerId)) {
			throw new BusinessProcessException("", CommonConstant.SUCCESS_STATUS_CODE);
		}
		customerCreditRepository.insertIntoCustomerCredit(customerId, 200.00, "App Install Credits", "M");
		customerCreditRepository.addInHistorySingle(customerId, 0, 0, 200.00, "ADDED", "Install Credits");
		customerRepository.addAppInstallCredits(customerId, platfrom);
		*/
		throw new BusinessProcessException("", CommonConstant.SUCCESS_STATUS_CODE);
	}
	
	
	public void processCreditsForSignup(Long customerId, String platfrom) throws BusinessProcessException {
		
		if(customerRepository.hasCustomerAvailedSignupCredits(customerId)) {
			return;
		}
		customerCreditRepository.insertIntoCustomerCredit(customerId, 100.00, "Sign up Credits", "M");
		customerCreditRepository.addInHistorySingle(customerId, 0, 0, 100.00, "ADDED", "Welcome Credits");
		customerRepository.userSignupCredits(customerId, platfrom);
	}
	
	@Transactional
	public ResponseEntity<DemandStoreAPIResponse> addAddressGuest(CustomerAddressRequest customerAddressRequest, CustomerDetailVO customerDetailVO) throws BusinessProcessException {
		customerRequestValidator.validateCustomerRequest(customerAddressRequest);
		if(null != customerDetailVO.getMobile() && ! customerDetailVO.getMobile().trim().equals("")) {
			if(! customerAddressRequest.getMobile().equals(customerDetailVO.getMobile())) {
				return ResponseEntity.ok(new DemandStoreAPIResponse(true,
						Arrays.asList(new MessageDetail(MessageType.ERROR, "Logedin mobile number and mobile number while adding address is not same", null, null)),
						CommonConstant.SUCCESS_STATUS_CODE, null));
			}
			CustomerDetailResponse customerDetail = customerRepository.findCustomerDetailByMobile(customerDetailVO.getMobile());
			if(customerDetail == null) {
				throw new BusinessProcessException(CommonConstant.INVALID_GUEST_USER, CommonConstant.SUCCESS_STATUS_CODE);
			}
			CustomerDetailResponse customerDetailEmail = customerRepository.findCustomerDetailByEmail(customerAddressRequest.getEmail());
			if(null != customerDetailEmail && ! customerDetailEmail.getCustomerId().equals(customerDetail.getCustomerId())) {
				throw new BusinessProcessException(CommonConstant.EMAIL_INVLAID, CommonConstant.SUCCESS_STATUS_CODE);
			}
			
			customerRepository.updateCustomerAndAddress(customerDetailVO, customerAddressRequest);
			return ResponseEntity.ok(new DemandStoreAPIResponse(true,
					Arrays.asList(new MessageDetail(MessageType.INFO, CommonConstant.CUSTOMER_ADDRESS_CREATED, null, null)),
					CommonConstant.SUCCESS_STATUS_CODE, null));
		 } else if(null != customerDetailVO.getEmail() && ! customerDetailVO.getEmail().trim().equals("")) {
			if(! customerAddressRequest.getEmail().equals(customerDetailVO.getEmail())) {
				return ResponseEntity.ok(new DemandStoreAPIResponse(true,
						Arrays.asList(new MessageDetail(MessageType.ERROR, "Logedin email and email while adding address is not same", null, null)),
						CommonConstant.SUCCESS_STATUS_CODE, null));
			}
			CustomerDetailResponse customerDetail = customerRepository.findCustomerDetailByEmail(customerDetailVO.getEmail());
			if(customerDetail == null) {
				throw new BusinessProcessException(CommonConstant.INVALID_GUEST_USER, CommonConstant.SUCCESS_STATUS_CODE);
			}
				
			CustomerDetailResponse customerDetailMobile = customerRepository.findCustomerDetailByMobile(customerAddressRequest.getMobile());
			if(null != customerDetailMobile && ! customerDetailMobile.getCustomerId().equals(customerDetail.getCustomerId())) {
				throw new BusinessProcessException(CommonConstant.EMAIL_INVLAID, CommonConstant.SUCCESS_STATUS_CODE);
			}
			
			customerRepository.updateCustomerAndAddress(customerDetailVO, customerAddressRequest);
			return ResponseEntity.ok(new DemandStoreAPIResponse(true,
					Arrays.asList(new MessageDetail(MessageType.INFO, CommonConstant.CUSTOMER_ADDRESS_CREATED, null, null)),
					CommonConstant.SUCCESS_STATUS_CODE, null));
		} else {
			throw new BusinessProcessException(CommonConstant.INVALID_GUEST_USER, CommonConstant.SUCCESS_STATUS_CODE);
		}
	}
	public ResponseEntity<DemandStoreAPIResponse> addCustomerForGuestCheckout(AddGuestUserRequest addGuestUserRequest)
			throws BusinessProcessException {
	   Integer  addressId = null, customerId = null ;
		/*LOGGER.info(" firing query for email and phone number ");
		String  email = customerRepository.getCustomerEmailFromPhone(addGuestUserRequest.getMobile());
		LOGGER.info(" email successfully revieved with response : {} and requst : {} ", email,addGuestUserRequest.getMobile() );

		String  phone = customerRepository.getCustomerPhoneAndFromEmail(addGuestUserRequest.getEmail());
		LOGGER.info(" phone successfully revieved with response : {} and request : {} ", phone,addGuestUserRequest.getEmail() );

		//happy case
  		if(null!= email && !email.trim().equals("") && null!= phone && !phone.trim().equals("") && email.equals(addGuestUserRequest.getEmail()) && phone.equals(addGuestUserRequest.getMobile())) {
	    	customerId = customerRepository.getCustomerId( addGuestUserRequest.getMobile());
   			if(null!= customerId && customerId !=0 && customerRepository.addCustomerAddress(buildCustomerAddressRequest(addGuestUserRequest, customerId))>0) {
			       addressId = customerRepository.getAddressId(email, phone, customerId);
		       }else {
					throw new BusinessProcessException(CommonConstant.INVALID_GUEST_USER, CommonConstant.SUCCESS_STATUS_CODE);
		    }
		}
  		//happy case
		else if(null!= email && !email.trim().equals("") && null!= phone && !phone.trim().equals("") 
				&& !email.equals(addGuestUserRequest.getEmail()) && !phone.equals(addGuestUserRequest.getMobile())) {
			return ResponseEntity.ok(new DemandStoreAPIResponse(true,
					Arrays.asList(new MessageDetail(MessageType.WARNING, CommonConstant.MOBILE_ALREADY_EXIST_WITH_DIFF_ACC, null, null)),
					CommonConstant.SUCCESS_STATUS_CODE, null));
		}
		else if((null == email || email.trim().equals("")) && null != phone && phone.trim().equals("") && !phone.equals(addGuestUserRequest.getMobile())) {
			return ResponseEntity.ok(new DemandStoreAPIResponse(true,
					Arrays.asList(new MessageDetail(MessageType.WARNING, CommonConstant.MOBILE_ALREADY_EXIST_WITH_DIFF_ACC, null, null)),
					CommonConstant.SUCCESS_STATUS_CODE, null));
		}
 		else if((null == phone || phone.trim().equals("")) && null != email  &&  !email.trim().equals("") && !(email.equals(addGuestUserRequest.getEmail()))) {
			customerId = customerRepository.getCustomerId( addGuestUserRequest.getMobile());
 			  if(customerRepository.addCustomerAddress(buildCustomerAddressRequest(addGuestUserRequest, customerId))>0){
			      addressId = customerRepository.getAddressId(email, phone, customerId);
			  }else {
					throw new BusinessProcessException(CommonConstant.INVALID_GUEST_USER, CommonConstant.SUCCESS_STATUS_CODE);
			  }
 			return ResponseEntity.ok(new DemandStoreAPIResponse(true,
					Arrays.asList(new MessageDetail(MessageType.WARNING, CommonConstant.MOBILE_ALREADY_EXIST_WITH_DIFF_ACC, null, null)),
					CommonConstant.SUCCESS_STATUS_CODE, null));
 		}
  	    
 		else if((null == phone || phone.trim().equals("")) && (null == email || email.trim().equals(""))) {
 			   if(customerRepository.addCustomer(buildCustomerRequest(addGuestUserRequest))>0) {
   				   customerId = customerRepository.getCustomerId(addGuestUserRequest.getMobile());
 				   if(null != customerId && customerId != 0 && customerRepository.addCustomerAddress(buildCustomerAddressRequest(addGuestUserRequest, customerId))>0){
 	 			      addressId = customerRepository.getAddressId(email, phone, customerId);
 				   }else {
 						throw new BusinessProcessException(CommonConstant.INVALID_GUEST_USER, CommonConstant.SUCCESS_STATUS_CODE);
 				   }
 			   }else {
					throw new BusinessProcessException(CommonConstant.INVALID_GUEST_USER, CommonConstant.SUCCESS_STATUS_CODE);
 			}
		}
 		*/
	    customerRequestValidator.validateCustomerRequest(addGuestUserRequest);
		LOGGER.info("hitting the db for customerdetail by mobile  with mobile : {}", addGuestUserRequest.getMobile());
		CustomerDetailResponse customerDetailByMobile = customerRepository.findCustomerDetailByMobile(addGuestUserRequest.getMobile());
		LOGGER.info("customer detail by mobile successfully fethed with response  : {}", customerDetailByMobile);

		LOGGER.info("hitting the db for customerdetail by email  with email : {}", addGuestUserRequest.getEmail());
		CustomerDetailResponse customerDetailByEmail = customerRepository.findCustomerDetailByEmail(addGuestUserRequest.getEmail());
		LOGGER.info("customer detail by email successfully fethed with response  : {}", customerDetailByEmail);

		if(null == customerDetailByMobile && null == customerDetailByEmail) {
			LOGGER.info("adding the customer in db with reuqest body : {}", addGuestUserRequest);
			if(customerRepository.addCustomer(buildCustomerRequest(addGuestUserRequest))>0) {
				LOGGER.info("getting customer_id with mobilem: {}" , addGuestUserRequest.getMobile());
				   customerId = customerRepository.getCustomerId(addGuestUserRequest.getMobile());
					LOGGER.info("getting customer_id with mobilem: {} fetched with response: {}" , addGuestUserRequest.getMobile(), customerId);
					LOGGER.info("inserting address in address table with request :{}and customer_id : {} " , addGuestUserRequest, customerId);
				   if(null != customerId && customerId != 0 && customerRepository.addCustomerAddress(buildCustomerAddressRequest(addGuestUserRequest, customerId))>0){
						LOGGER.info("getting address_id with mobile: {} and email: {} and customer_id : {}" , addGuestUserRequest.getMobile(), addGuestUserRequest.getEmail(), customerId);
					    addressId = customerRepository.getAddressId(addGuestUserRequest.getMobile(), addGuestUserRequest.getEmail(), customerId);
						LOGGER.info("address_id successfully fetched with response : {}" , addressId);

				   }else {
					   	LOGGER.info("Invalid Guest User :  (null == customerDetailByMobile && null == customerDetailByEmail)");
						throw new BusinessProcessException(CommonConstant.INVALID_GUEST_USER, CommonConstant.SUCCESS_STATUS_CODE);
				   }
			   }else {
				   	LOGGER.info("Invalid Guest User :  (null == customerDetailByMobile && null == customerDetailByEmail)");
					throw new BusinessProcessException(CommonConstant.INVALID_GUEST_USER, CommonConstant.SUCCESS_STATUS_CODE);
			}
		}
 		else if(null != customerDetailByMobile && null == customerDetailByEmail) {
			LOGGER.info("updating email by mobile : {}",addGuestUserRequest.getEmail(), addGuestUserRequest.getMobile());
			if((null == customerDetailByMobile.getEmail() || customerDetailByMobile.getEmail().trim().isEmpty()) && customerRepository.updateEmailByMobile(addGuestUserRequest.getMobile(), addGuestUserRequest.getEmail())>0) {
				LOGGER.info("getting customer_id with mobilem: {}" , addGuestUserRequest.getMobile());
				customerId = customerRepository.getCustomerId(addGuestUserRequest.getMobile());
				LOGGER.info("getting customer_id with mobilem: {} fetched with response: {}" , addGuestUserRequest.getMobile(), customerId);
				LOGGER.info("adding the address with request body : {} and customer_id : {}" , addGuestUserRequest, customerId);
				if(null != customerId && customerId != 0 && customerRepository.addCustomerAddress(buildCustomerAddressRequest(addGuestUserRequest, customerId))>0){
						LOGGER.info("getting address_id with mobile: {} and email: {} and customer_id : {}" , addGuestUserRequest.getMobile(), addGuestUserRequest.getEmail(), customerId);
					 addressId = customerRepository.getAddressId(addGuestUserRequest.getMobile(), addGuestUserRequest.getEmail(), customerId);
						LOGGER.info("address_id successfully fetched with response : {}" , addressId);
				 }else {
					 	LOGGER.info("Invalid Guest User :  (null != customerDetailByMobile && null == customerDetailByEmail) : {} ", addGuestUserRequest.toString());
						throw new BusinessProcessException(CommonConstant.INVALID_GUEST_USER, CommonConstant.FAILURE_STATUS_CODE);
				   }
			}else if(null != customerDetailByMobile.getEmail() &&  !customerDetailByMobile.getEmail().trim().isEmpty() && !customerDetailByMobile.getEmail().equals(addGuestUserRequest.getEmail()) ){
				 customerId = customerRepository.getCustomerId(addGuestUserRequest.getMobile());
					LOGGER.info("getting customer_id with mobilem: {} fetched with response: {}" , addGuestUserRequest.getMobile(), customerId);
					LOGGER.info("adding the address with request body : {} and customer_id : {}" , addGuestUserRequest, customerId);
					if(null != customerId && customerId != 0 && customerRepository.addCustomerAddress(buildCustomerAddressRequest(addGuestUserRequest, customerId))>0){
						LOGGER.info("getting address_id with mobile: {} and email: {} and customer_id : {}" , addGuestUserRequest.getMobile(), addGuestUserRequest.getEmail(), customerId);
					 addressId = customerRepository.getAddressId(addGuestUserRequest.getMobile(), addGuestUserRequest.getEmail(), customerId);
						LOGGER.info("address_id successfully fetched with response : {}" , addressId);

				 }else {
					 	LOGGER.info("Invalid Guest User :  (null != customerDetailByMobile.getEmail() &&  !customerDetailByMobile.getEmail().trim().isEmpty() && !customerDetailByMobile.getEmail().equals(addGuestUserRequest.getEmail())  ) : {} ", addGuestUserRequest.toString());
						throw new BusinessProcessException(CommonConstant.INVALID_GUEST_USER, CommonConstant.FAILURE_STATUS_CODE);
				   }
			}
		}
		else if(null == customerDetailByMobile && null != customerDetailByEmail) {
			LOGGER.info("updating mobile : {} by email : {}",addGuestUserRequest.getMobile(), addGuestUserRequest.getEmail());
			if((null == customerDetailByEmail.getMobile() || customerDetailByEmail.getMobile().trim().isEmpty())&& customerRepository.updateMobileByEmail(addGuestUserRequest.getMobile(), addGuestUserRequest.getEmail())>0) {
				LOGGER.info("getting customer_id with mobilem: {}" , addGuestUserRequest.getMobile());
				customerId = customerRepository.getCustomerId(addGuestUserRequest.getMobile());
				LOGGER.info("getting customer_id with mobilem: {} fetched with response: {}" , addGuestUserRequest.getMobile(), customerId);
				LOGGER.info("adding the address with request body : {} and customer_id : {}" , addGuestUserRequest, customerId);
				if(null != customerId && customerId != 0 && customerRepository.addCustomerAddress(buildCustomerAddressRequest(addGuestUserRequest, customerId))>0){
					LOGGER.info("getting address_id with mobile: {} and email: {} and customer_id : {}" , addGuestUserRequest.getMobile(), addGuestUserRequest.getEmail(), customerId);
					if(customerRepository.addCustomerAddress(buildCustomerAddressRequest(addGuestUserRequest, customerId))>0) {
						addressId = customerRepository.getAddressId(addGuestUserRequest.getMobile(), addGuestUserRequest.getEmail(), customerId);
					    LOGGER.info("address_id successfully fetched with response : {}" , addressId);
				}else {
						LOGGER.info("Invalid Guest User :  (null == customerDetailByMobile && null != customerDetailByEmail) : {} ", addGuestUserRequest.toString());
						throw new BusinessProcessException(CommonConstant.INVALID_GUEST_USER, CommonConstant.FAILURE_STATUS_CODE);
				   }
			}
			else if(null != customerDetailByEmail.getMobile() && !customerDetailByEmail.getMobile().equals(addGuestUserRequest.getMobile())) {
				return ResponseEntity.ok(new DemandStoreAPIResponse(true,
						Arrays.asList(new MessageDetail(MessageType.WARNING, CommonConstant.MOBILE_ALREADY_EXIST_WITH_DIFF_EMAIL, null, null)),
						CommonConstant.FAILURE_STATUS_CODE, null));
			}
		}else {
			LOGGER.info("Invalid Guest User :  From 2nd last else : {} ", addGuestUserRequest.toString());
			throw new BusinessProcessException(CommonConstant.INVALID_GUEST_USER, CommonConstant.FAILURE_STATUS_CODE);
		}
	}
    else if(null != customerDetailByMobile && null != customerDetailByEmail) {
			if(null != customerDetailByMobile.getEmail() &&	null != customerDetailByEmail.getMobile() && customerDetailByMobile.getCustomerId().equals(customerDetailByEmail.getCustomerId() )) {
				     customerId = customerDetailByEmail.getCustomerId();
				LOGGER.info("adding the address with request body : {} and customer_id : {}" , addGuestUserRequest, customerId);
				if(customerRepository.addCustomerAddress(buildCustomerAddressRequest(addGuestUserRequest, customerId))>0) {
  						LOGGER.info("getting address_id with mobile: {} and email: {} and customer_id : {}" , addGuestUserRequest.getMobile(), addGuestUserRequest.getEmail(), customerDetailByMobile.getCustomerId());
  						addressId = customerRepository.getAddressId(addGuestUserRequest.getMobile(), addGuestUserRequest.getEmail(),customerDetailByMobile.getCustomerId() );
  					}
				 }
			else if(null != customerDetailByMobile.getEmail() && null != customerDetailByEmail.getMobile() && !customerDetailByEmail.getCustomerId().equals(customerDetailByMobile.getCustomerId())) {
				return ResponseEntity.ok(new DemandStoreAPIResponse(true,
						Arrays.asList(new MessageDetail(MessageType.WARNING, CommonConstant.EMAIL_ALREADY_EXIST_WITH_DIFF_MOBILE, null, null)),
						CommonConstant.FAILURE_STATUS_CODE, null));
		   }	
    }else {
    	LOGGER.info("Invalid Guest User :  From last else : {} ", addGuestUserRequest.toString());
		throw new BusinessProcessException(CommonConstant.INVALID_GUEST_USER, CommonConstant.FAILURE_STATUS_CODE);
    }
		AddGuestUserResponse addGuestUserResponse = new  AddGuestUserResponse();
		addGuestUserResponse.setCustomerId(customerId);
		addGuestUserResponse.setAddressId(addressId);
		LOGGER.info(" add customer for quest checkout successfully done with response :{}, returning to place order service" , addGuestUserResponse);
		return ResponseEntity.ok(new DemandStoreAPIResponse(true,
				Arrays.asList(new MessageDetail(MessageType.INFO, CommonConstant.ADD_USER_FOR_GUEST_CHECKOUT, null, null)),
				CommonConstant.SUCCESS_STATUS_CODE, addGuestUserResponse));
	}
	private CustomerAddressRequest buildCustomerAddressRequest(AddGuestUserRequest addGuestUserRequest, Integer customerId) {
		CustomerAddressRequest customerAddressRequest = new CustomerAddressRequest();
  		customerAddressRequest.setCustomerId(customerId);
  		customerAddressRequest.setFullName(addGuestUserRequest.getFullName());
		customerAddressRequest.setPincode(addGuestUserRequest.getPincode());
		customerAddressRequest.setAddress(addGuestUserRequest.getAddress());
		customerAddressRequest.setLandmark(addGuestUserRequest.getLandmark());
		customerAddressRequest.setCity(addGuestUserRequest.getCity());
		customerAddressRequest.setState( addGuestUserRequest.getState());
 		customerAddressRequest.setEmail(addGuestUserRequest.getEmail());
		customerAddressRequest.setMobile(addGuestUserRequest.getMobile());
 		return customerAddressRequest;
	}
	private CustomerRequest buildCustomerRequest(AddGuestUserRequest addGuestUserRequest  ) {
		CustomerRequest customerRequest  =  new CustomerRequest();
		customerRequest.setFirstName(addGuestUserRequest.getFullName());
		customerRequest.setLastName("");
		customerRequest.setGender(Gender.OTHERS);
		customerRequest.setMobile(addGuestUserRequest.getMobile());
		customerRequest.setEmailId(addGuestUserRequest.getEmail());
		customerRequest.setSource("");
		customerRequest.setCampaign("");
		customerRequest.setMedium("");
		return customerRequest;	
	}

	public ResponseEntity<DemandStoreAPIResponse> findCustomerDetailByMoileAndEmail(String email, String mobile) {
		CustomerDetailResponse customerDetailMobile = customerRepository.findCustomerDetailByMobile(mobile);
		CustomerDetailResponse customerDetailByEmail = customerRepository.findCustomerDetailByEmail(email);
		
		if(null != customerDetailByEmail && (null == customerDetailByEmail.getMobile() || "".equals(customerDetailByEmail.getMobile().trim()))) {
			LOGGER.info("Error case 1 : "+ email + " - mobile "+ mobile);
			return ResponseEntity.ok(new DemandStoreAPIResponse(true, Arrays.asList(new MessageDetail(MessageType.INFO, CommonConstant.EMAIL_EXISTS_WITHOUT_MOBILE, null, null)), CommonConstant.SUCCESS_STATUS_CODE, null));
		}
		
		if(null != customerDetailMobile && null != customerDetailByEmail && ! customerDetailByEmail.getCustomerId().equals(customerDetailMobile.getCustomerId())) {
			LOGGER.info("Error case 2 : "+ email + " - mobile "+ mobile);
			return ResponseEntity.ok(new DemandStoreAPIResponse(true, Arrays.asList(new MessageDetail(MessageType.INFO, String.format(CommonConstant.THIS_MOBILE_BELONGS_TO_SOMEONE_ELSE, email, mobile), null, null)), CommonConstant.SUCCESS_STATUS_CODE, null));
		}
		
		if(customerDetailMobile == null && null != customerDetailByEmail && null!= customerDetailByEmail.getMobile() && !customerDetailByEmail.getMobile().trim().isEmpty() &&  ! mobile.equals(customerDetailByEmail.getMobile())) {
			LOGGER.info("Error case 3 : "+ email + " - mobile "+ mobile);
			return ResponseEntity.ok(new DemandStoreAPIResponse(true, Arrays.asList(new MessageDetail(MessageType.INFO, String.format(CommonConstant.THIS_MOBILE_BELONGS_TO_SOMEONE_ELSE, email, mobile), null, null)), CommonConstant.SUCCESS_STATUS_CODE, null));
		}
		
		if(customerDetailMobile != null && customerDetailMobile.getEmail() != null && ! customerDetailMobile.getEmail().trim().isEmpty() && ! customerDetailMobile.getEmail().equalsIgnoreCase(email)) {
			return ResponseEntity.ok(new DemandStoreAPIResponse(true, Arrays.asList(new MessageDetail(MessageType.INFO, String.format(CommonConstant.THIS_MOBILE_BELONGS_TO_SOMEONE_ELSE, email, mobile), null, null)), CommonConstant.SUCCESS_STATUS_CODE, null));
		}
		
		
		return ResponseEntity.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, null));
	}
	public ResponseEntity<DemandStoreAPIResponse> customerCareAddCustomer(CustomerRequest customerRequest)
			throws BusinessProcessException {
		if (!(customerRequestValidator.isValidEmail(customerRequest.getEmailId())) ){
			throw new BusinessProcessException(CommonConstant.INVALID_EMAIL, CommonConstant.SUCCESS_STATUS_CODE);
		}
		if (!(customerRequestValidator.isValidMobile(customerRequest.getMobile()))) {
			throw new BusinessProcessException(CommonConstant.INVALID_MOBILE, CommonConstant.SUCCESS_STATUS_CODE);
		}
		Integer customerId = customerRepository.getCustomerId(customerRequest.getMobile());
		if(null!=customerId && customerId !=0) {
			return ResponseEntity.ok(new DemandStoreAPIResponse(true,
					Arrays.asList(new MessageDetail(MessageType.INFO, CommonConstant.ACCOUNT_ALREADY_EXISTS_WITH_MOBILE, null, null)),
					CommonConstant.SUCCESS_STATUS_CODE, customerId));
		}
		else if (customerRepository.addCustomer(customerRequest) > 0) {
			return ResponseEntity.ok(new DemandStoreAPIResponse(true,
					Arrays.asList(new MessageDetail(MessageType.INFO, CommonConstant.CUSTOMER_ADDED, null, null)),
					CommonConstant.SUCCESS_STATUS_CODE, customerRepository.getCustomerId(customerRequest.getMobile()) ));
		} else {
			return  ResponseEntity.ok(new DemandStoreAPIResponse(false,
					Arrays.asList(new MessageDetail(MessageType.ERROR, CommonConstant.SOMETHING_WENT_WRONG , null, null)),
					CommonConstant.FAILURE_STATUS_CODE,null));
		}
	}


	public ResponseEntity<DemandStoreAPIResponse> updateCustomer(EmailAddRequest emailAddRequest, Long customerId)
			throws BusinessProcessException {
		if (!customerRequestValidator.isValidEmail(emailAddRequest.getEmail()) || emailAddRequest.getEmail() == null || "".equals(emailAddRequest.getEmail()) ) {
			throw new BusinessProcessException(CommonConstant.INVALID_EMAIL, CommonConstant.FAILURE_STATUS_CODE);
		}
		CustomerDetailResponse customerDetail = customerRepository
				.findCustomerDetailByEmail(emailAddRequest.getEmail());

		CustomerDetailResponse customerDetailById = customerRepository.findCustomerDetailByCustomerId(customerId);

		if (null == customerDetail) {
			if (customerRepository.updateCustomerEmail(customerId, emailAddRequest.getEmail()) == 1) {
				return ResponseEntity.ok(new DemandStoreAPIResponse(true,
						Arrays.asList(new MessageDetail(MessageType.INFO, null, null, null)),
						CommonConstant.SUCCESS_STATUS_CODE, 0));

			} else {
				return ResponseEntity.ok(new DemandStoreAPIResponse(true,
						Arrays.asList(new MessageDetail(MessageType.INFO, CommonConstant.INVALID_EMAIL, null, null)),
						CommonConstant.FAILURE_STATUS_CODE, customerId));
			}
		} else {
			if (!customerDetail.getCustomerId().equals(customerDetailById.getCustomerId())) {
				return ResponseEntity.ok(new DemandStoreAPIResponse(true,
						Arrays.asList(new MessageDetail(MessageType.INFO,
								String.format(CommonConstant.ACCOUNT_ALREADY_EXISTS_WITH_EMAIL_USE_DIFFERENT_EMAIL, emailAddRequest.getEmail()), null, null)),
						CommonConstant.FAILURE_STATUS_CODE, customerId));
			}
			return ResponseEntity
					.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, customerId));
		}
	}
	
	
	public ResponseEntity<DemandStoreAPIResponse> participantRanking(Long customerId) throws BusinessProcessException {
		Long rank = customerRepository.getParticipantRank(customerId);
		if (null != rank) {
			return ResponseEntity
					.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, rank));
		} else
			throw new BusinessProcessException(CommonConstant.NO_RANK, CommonConstant.SUCCESS_STATUS_CODE);
	}

 
}
