package com.ouat.wishlist.exception;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.ouat.wishlist.constants.CommonConstant;
import com.ouat.wishlist.response.DemandStoreAPIResponse;
import com.ouat.wishlist.response.MessageDetail;
import com.ouat.wishlist.response.MessageType;


@ControllerAdvice
public class GenericExceptionMapper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericExceptionMapper.class);
	
	@ExceptionHandler(Throwable.class)
    public ResponseEntity<DemandStoreAPIResponse> handleThrowable(Throwable ex) {
		DemandStoreAPIResponse response = new DemandStoreAPIResponse();
        response.setSuccess(false);
        response.setCode(CommonConstant.FAILURE_STATUS_CODE);
        MessageDetail messageDetail = new MessageDetail(MessageType.ERROR, CommonConstant.GENERIC_ERROR_MSG, null, null);
        List<MessageDetail> msg = new ArrayList<MessageDetail>();
        msg.add(messageDetail);
        response.setMessage(msg);
        HttpStatus httpStatus = HttpStatus.OK;
        LOGGER.error("Some error occured : {} ", ex.getMessage(), ex);
        return new ResponseEntity<DemandStoreAPIResponse>(response, httpStatus);
    }
	
	@ExceptionHandler(BusinessProcessException.class)
    public ResponseEntity<DemandStoreAPIResponse> handleBusinessProcessException(BusinessProcessException ex) {
		DemandStoreAPIResponse response = new DemandStoreAPIResponse();
        if(ex.getStatusCode() != null && !ex.getStatusCode().trim().isEmpty() && ex.getStatusCode().equals(CommonConstant.SUCCESS_STATUS_CODE)) {
        	response.setCode(ex.getStatusCode());
        	response.setSuccess(true);
        } else {
        	response.setCode(CommonConstant.FAILURE_STATUS_CODE);
        }
        LOGGER.error("BusinessProcessException : {} ", ex.getMessage(), ex);
        MessageDetail messageDetail = new MessageDetail(MessageType.ERROR, ex.getMessage(), null, null);
        List<MessageDetail> msg = new ArrayList<MessageDetail>();
        msg.add(messageDetail);
        response.setMessage(msg);
        HttpStatus httpStatus = HttpStatus.OK;
        return new ResponseEntity<DemandStoreAPIResponse>(response, httpStatus);
    }
	
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<DemandStoreAPIResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		DemandStoreAPIResponse response = new DemandStoreAPIResponse();
        response.setSuccess(false);
        response.setCode(CommonConstant.FAILURE_STATUS_CODE);
        MessageDetail messageDetail = new MessageDetail(MessageType.ERROR, CommonConstant.INVALID_REQUEST_PARAM, null, null);
        List<MessageDetail> msg = new ArrayList<MessageDetail>();
        msg.add(messageDetail);
        response.setMessage(msg);
        HttpStatus httpStatus = HttpStatus.OK;
        LOGGER.error("Some error occured : {} ", ex.getMessage(), ex);
        return new ResponseEntity<>(response, httpStatus);
    }
	
	


}
