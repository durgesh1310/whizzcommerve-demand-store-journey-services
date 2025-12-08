package com.ouat.cartService.interceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ouat.cartService.exception.BusinessProcessException;
import com.ouat.cartService.response.AuthServiceDemandStoreAPIResponse;


public class DemandStoreLoginInterceptor implements HandlerInterceptor {
	
	@Autowired
	AuthenticationServiceDemandStoreClient authserviceClient;
	Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			DemandStoreLoginRequired loginRequired = ControllerHelper.findMethodOrClassLevelAnnotation(handler,
					DemandStoreLoginRequired.class);
			if (loginRequired == null) {
				return true;
			}
				
			final String requestTokenHeader = request.getHeader("Authorization");
			
			if(requestTokenHeader == null) {
				log.error("Token is missing");
				throw new BusinessProcessException("Unauthorized", "401");
			}
			
			try {
				final String jwtToken = requestTokenHeader.substring(7);
				AuthServiceDemandStoreAPIResponse userDetail = authserviceClient.validateAccessToken(jwtToken, request);
				if (userDetail.isSuccess())
					request.setAttribute(RequestHeaders.CUSTOMER_DETAIL, userDetail.getData());
				else {
					throw new BusinessProcessException("Unauthorized", "401");
				}
			} catch (Exception e) {
				log.error("Error occured while making API call to auth service : {} ", e.getMessage(), e);
				throw new BusinessProcessException("Unauthorized", "401");
			}
		}
		return true;
	}
	

}
