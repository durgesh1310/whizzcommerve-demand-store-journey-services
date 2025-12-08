package com.ouat.authServiceDemandStore.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ouat.authServiceDemandStore.constant.CommonConstant;
import com.ouat.authServiceDemandStore.security.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {


	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");

		String deviceId = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				deviceId = jwtTokenUtil.getDeviceIdFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				LOGGER.info("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				LOGGER.info("JWT Token has expired");
			}
		} else {
			LOGGER.warn("JWT Token does not begin with Bearer String");
		}

		// Once we get the token validate it.
		if (deviceId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			if (! jwtTokenUtil.validateToken(jwtToken, deviceId)) {
				throw new RuntimeException(CommonConstant.USER_NOT_LOGGED_IN);
			
			}
			//request.setAttribute(CommonConstant.USER_ID, deviceId);
		}
		chain.doFilter(request, response);
	}

}