package com.ouat.authServiceDemandStore.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouat.authServiceDemandStore.constant.CommonConstant;
import com.ouat.authServiceDemandStore.exception.BusinessProcessException;
import com.ouat.authServiceDemandStore.request.CustomerRequest;
import com.ouat.authServiceDemandStore.response.DemandStoreAPIResponse;
import com.ouat.authServiceDemandStore.security.JwtTokenUtil;
import com.ouat.authServiceDemandStore.vo.CustomerDetailVO;
import com.ouat.authServiceDemandStore.vo.DeviceType;
import com.ouat.authServiceDemandStore.vo.Platform;

@Service
public class DemandStoreService {
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	RedisHelper redisHelper;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DemandStoreService.class);
	
	public ResponseEntity<DemandStoreAPIResponse> generateToken(CustomerRequest customer, HttpServletRequest request)
			throws BusinessProcessException {
		validateRequest(customer.toString(), request);
		//Date expiry = new Date(System.currentTimeMillis() + CommonConstant.JWT_TOKEN_VALIDITY);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, CommonConstant.JWT_TOKEN_VALIDITY_IN_DAYS);
		Date expiry = cal.getTime();
		CustomerDetailVO vo = CustomerDetailVO.builder().name(customer.getFirstName()).os(request.getHeader(CommonConstant.OS))
				.browser(request.getHeader(CommonConstant.BROWSER))
				.userClient(request.getHeader(CommonConstant.USER_CLIENT))
				.deviceId(request.getHeader(CommonConstant.DEVICE_ID))
				.deviceType(DeviceType.valueOf(request.getHeader(CommonConstant.DEVICE_TYPE)))
				.key(request.getHeader(CommonConstant.KEY))
				.platform(Platform.valueOf(request.getHeader(CommonConstant.PLATFORM)))
				.appVersion(request.getHeader(CommonConstant.APPVERSION))
				.apiVersion(request.getHeader(CommonConstant.APIVERSION)).customerId(customer.getCustomerId())
				.email(customer.getEmail()).mobile(customer.getMobile()).isActive(customer.getIsActive())
				.gender(customer.getGender()).expiry(expiry).build();
		Map<String, Object> claims = new HashMap<>();
		LOGGER.info("vo detail : {} ", vo.toString());
		claims.put(CommonConstant.USER_DETAIL, vo.toString());
		String token = jwtTokenUtil.generateToken(request.getHeader(CommonConstant.DEVICE_ID), claims, expiry);
		vo.setToken(token);
		return ResponseEntity.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, token));

	}
	
	
	private void validateRequest(String customerOrToken, HttpServletRequest request) throws BusinessProcessException {
		if(request.getHeader(CommonConstant.OS) == null || request.getHeader(CommonConstant.OS).trim().equals("")) {
			LOGGER.error("Invalid request :: OS is missing in header, customer request {} ", customerOrToken);
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST, CommonConstant.FAILURE_STATUS_CODE);
		} 
		if(request.getHeader(CommonConstant.BROWSER) == null || request.getHeader(CommonConstant.BROWSER).trim().equals("")) {
			LOGGER.error("Invalid request :: BROWSER is missing in header, customer request {} ", customerOrToken);
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST, CommonConstant.FAILURE_STATUS_CODE);
		} 
		if(request.getHeader(CommonConstant.USER_CLIENT) == null || request.getHeader(CommonConstant.USER_CLIENT).trim().equals("")) {
			LOGGER.error("Invalid request :: USER_CLIENT is missing in header, customer request {} ", customerOrToken);
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST, CommonConstant.FAILURE_STATUS_CODE);
		} 
		if(request.getHeader(CommonConstant.DEVICE_ID) == null || request.getHeader(CommonConstant.DEVICE_ID).trim().equals("")) {
			LOGGER.error("Invalid request :: DEVICE_ID is missing in header, customer request {} ", customerOrToken);
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST, CommonConstant.FAILURE_STATUS_CODE);
		} 
		if(request.getHeader(CommonConstant.DEVICE_TYPE) == null || request.getHeader(CommonConstant.DEVICE_TYPE).trim().equals("")) {
			LOGGER.error("Invalid request :: DEVICE_TYPE is missing in header, customer request {} ", customerOrToken);
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST, CommonConstant.FAILURE_STATUS_CODE);
		} 
		if(request.getHeader(CommonConstant.KEY) == null || request.getHeader(CommonConstant.KEY).trim().equals("")) {
			LOGGER.error("Invalid request :: KEY is missing in header, customer request {} ", customerOrToken);
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST, CommonConstant.FAILURE_STATUS_CODE);
		} 
		if(request.getHeader(CommonConstant.PLATFORM) == null || request.getHeader(CommonConstant.PLATFORM).trim().equals("")) {
			LOGGER.error("Invalid request :: PLATFORM is missing in header, customer request {} ", customerOrToken);
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST, CommonConstant.FAILURE_STATUS_CODE);
		}
		if("Android".equals(request.getHeader(CommonConstant.PLATFORM)) || "IOS".equals(request.getHeader(CommonConstant.PLATFORM))) {
			if(request.getHeader(CommonConstant.APPVERSION) == null || request.getHeader(CommonConstant.APPVERSION).trim().equals("")) {
				LOGGER.error("Invalid request :: APPVERSION is missing in header, customer request {} ", customerOrToken);
				throw new BusinessProcessException(CommonConstant.INVALID_REQUEST, CommonConstant.FAILURE_STATUS_CODE);
			} 
		}
		if(request.getHeader(CommonConstant.APIVERSION) == null || request.getHeader(CommonConstant.APIVERSION).trim().equals("")) {
			LOGGER.error("Invalid request :: APIVERSION is missing in header, customer request {} ", customerOrToken);
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST, CommonConstant.FAILURE_STATUS_CODE);
		}
	}


	public ResponseEntity<DemandStoreAPIResponse> validateToken(String token,
			HttpServletRequest request) throws BusinessProcessException {
		LOGGER.info("User token : {} ", token);
		LOGGER.info("Device Id token : {} ", request.getHeader(CommonConstant.DEVICE_ID));
		validateRequest(token, request);
		if (!jwtTokenUtil.validateToken(token, request.getHeader(CommonConstant.DEVICE_ID))) {
			LOGGER.error("Invalid request :: Token expired {} ", token);
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST, CommonConstant.FAILURE_STATUS_CODE);
		}
		Map<String, Object> claims = jwtTokenUtil.getAllClaimsFromToken(token);
		CustomerDetailVO customerDetail = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			customerDetail = mapper.readValue(claims.get(CommonConstant.USER_DETAIL).toString(), CustomerDetailVO.class);
		} catch (JsonProcessingException e) {
			LOGGER.error("Invalid request :: Error while fetching customer detail from token {} ", e.getMessage(), e);
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST, CommonConstant.FAILURE_STATUS_CODE);
		}
		return ResponseEntity.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, customerDetail));
	}


}
