package com.ouat.customerService.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ouat.customerService.constants.CommonConstant;
import com.ouat.customerService.exception.BusinessProcessException;
import com.ouat.customerService.response.AuthServiceDemandStoreAPIResponse;
import com.ouat.customerService.response.Platform;


public class SessionRequiredInterceptor implements HandlerInterceptor {
	
	@Autowired
	AuthenticationServiceDemandStoreClient authserviceClient;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SessionRequiredInterceptor.class);
	
	

	Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			SessionRequired session = ControllerHelper.findMethodOrClassLevelAnnotation(handler,
					SessionRequired.class);
			if (session == null)
				return true;

			final String requestTokenHeader = request.getHeader("Authorization");
			
			try {
				if (null == requestTokenHeader) {
					validateRequest(request.toString(), request);
				} else {
					final String jwtToken = requestTokenHeader.substring(7);
					if (null != jwtToken && !jwtToken.isEmpty()) {
						AuthServiceDemandStoreAPIResponse userDetail = authserviceClient.validateAccessToken(jwtToken,
								request);
						if (userDetail.isSuccess()) {
							request.setAttribute(RequestHeaders.CUSTOMER_DETAIL, userDetail.getData());
						}
					}
				}
			} catch (Exception e) {
				log.error("Error occured while making API call to auth service : {} ", e.getMessage(), e);
				throw new BusinessProcessException("Unauthorized", "401");
			}
		}
		return true;
	}
	
	private void validateRequest(String requestAsString, HttpServletRequest request) throws BusinessProcessException {
		if(request.getHeader(RequestHeader.OS) == null || request.getHeader(RequestHeader.OS).trim().equals("")) {
			LOGGER.error("Invalid request :: OS is missing in header, customer request {} ", requestAsString);
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST_PARAM, CommonConstant.FAILURE_STATUS_CODE);
		} 
		if(request.getHeader(RequestHeader.BROWSER) == null || request.getHeader(RequestHeader.BROWSER).trim().equals("")) {
			LOGGER.error("Invalid request :: BROWSER is missing in header, customer request {} ", requestAsString);
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST_PARAM, CommonConstant.FAILURE_STATUS_CODE);
		} 
		if(request.getHeader(RequestHeader.USER_CLIENT) == null || request.getHeader(RequestHeader.USER_CLIENT).trim().equals("")) {
			LOGGER.error("Invalid request :: USER_CLIENT is missing in header, customer request {} ", requestAsString);
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST_PARAM, CommonConstant.FAILURE_STATUS_CODE);
		} 
		if(request.getHeader(RequestHeader.DEVICE_ID) == null || request.getHeader(RequestHeader.DEVICE_ID).trim().equals("")) {
			LOGGER.error("Invalid request :: DEVICE_ID is missing in header, customer request {} ", requestAsString);
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST_PARAM, CommonConstant.FAILURE_STATUS_CODE);
		} 
		if(request.getHeader(RequestHeader.DEVICE_TYPE) == null || request.getHeader(RequestHeader.DEVICE_TYPE).trim().equals("")) {
			LOGGER.error("Invalid request :: DEVICE_TYPE is missing in header, customer request {} ", requestAsString);
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST_PARAM, CommonConstant.FAILURE_STATUS_CODE);
		} 
		if(request.getHeader(RequestHeader.KEY) == null || request.getHeader(RequestHeader.KEY).trim().equals("")) {
			LOGGER.error("Invalid request :: KEY is missing in header, customer request {} ", requestAsString);
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST_PARAM, CommonConstant.FAILURE_STATUS_CODE);
		}
		
		if (!Platform.contains(request.getHeader(RequestHeader.PLATFORM))) {
			LOGGER.error("Invalid request :: PLATFORM is invalid in header, customer request {} ", requestAsString);
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST_PARAM, CommonConstant.FAILURE_STATUS_CODE);
		}
		
		if(request.getHeader(RequestHeader.PLATFORM) == null || request.getHeader(RequestHeader.PLATFORM).trim().equals("")) {
			LOGGER.error("Invalid request :: PLATFORM is missing in header, customer request {} ", requestAsString);
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST_PARAM, CommonConstant.FAILURE_STATUS_CODE);
		}
		if("Android".equals(request.getHeader(RequestHeader.PLATFORM)) || "IOS".equals(request.getHeader(RequestHeader.PLATFORM))) {
			if(request.getHeader(RequestHeader.APPVERSION) == null || request.getHeader(RequestHeader.APPVERSION).isEmpty()) {
				LOGGER.error("Invalid request :: APPVERSION is missing in header, customer request {} ", requestAsString);
				throw new BusinessProcessException(CommonConstant.INVALID_REQUEST_PARAM, CommonConstant.FAILURE_STATUS_CODE);
			} 
		}
		if(request.getHeader(RequestHeader.APIVERSION) == null || request.getHeader(RequestHeader.APIVERSION).trim().equals("")) {
			LOGGER.error("Invalid request :: APIVERSION is missing in header, customer request {} ", requestAsString);
			throw new BusinessProcessException(CommonConstant.INVALID_REQUEST_PARAM, CommonConstant.FAILURE_STATUS_CODE);
		}
	}

	

}
