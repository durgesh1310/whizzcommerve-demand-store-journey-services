package com.ouat.checkout.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.ouat.checkout.apistore.clients.EmailSMSServiceClient;
import com.ouat.checkout.constant.CommonConstant;
import com.ouat.checkout.enums.MessageType;
import com.ouat.checkout.placeOrder.client.EmailSendRequest;
import com.ouat.checkout.response.DemandStoreAPIResponse;
import com.ouat.checkout.response.MessageDetail;


@ControllerAdvice
public class GenericExceptionMapper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericExceptionMapper.class);
	
	@Autowired
	EmailSMSServiceClient emailSMSServiceClient;
	
	@Value("${env}")
	private String env;
	
	@ExceptionHandler(Throwable.class)
    public ResponseEntity<DemandStoreAPIResponse> handleThrowable(Throwable ex) {
		DemandStoreAPIResponse response = new DemandStoreAPIResponse();
        response.setSuccess(false);
        response.setCode(CommonConstant.FAILURE_STATUS_CODE);
        MessageDetail messageDetail = new MessageDetail(MessageType.ERROR, CommonConstant.GENERIC_ERROR_MSG, null, null);
        List<MessageDetail> msg = new ArrayList<>();
        msg.add(messageDetail);
        response.setMessage(msg);
        HttpStatus httpStatus = HttpStatus.OK;
        LOGGER.error("Some error occured : {} ", ex.getMessage(), ex);
        sendEmail(ex);
        return new ResponseEntity<DemandStoreAPIResponse>(response, httpStatus);
    }
	
	@ExceptionHandler(BusinessProcessException.class)
    public ResponseEntity<DemandStoreAPIResponse> handleBusinessProcessException(BusinessProcessException ex) {
		DemandStoreAPIResponse response = new DemandStoreAPIResponse();
		MessageType type = MessageType.ERROR;
		if(ex.getStatusCode() != null && !ex.getStatusCode().trim().isEmpty() && ex.getStatusCode().equals(CommonConstant.SUCCESS_STATUS_CODE)) {
        	response.setCode(ex.getStatusCode());
        	response.setSuccess(true);
        	type = MessageType.INFO;
        } else {
        	response.setCode(CommonConstant.FAILURE_STATUS_CODE);
        	response.setSuccess(false);
        }
        LOGGER.error("BusinessProcessException : {} ", ex.getMessage(), ex);
        MessageDetail messageDetail = new MessageDetail(type, ex.getMessage(), null, null);
        List<MessageDetail> msg = new ArrayList<>();
        msg.add(messageDetail);
        response.setMessage(msg);
        HttpStatus httpStatus = HttpStatus.OK;
        
        if(! ex.getMessage().equalsIgnoreCase("Unauthorized")) {
        	sendEmail(ex);
        }
        
        return new ResponseEntity<DemandStoreAPIResponse>(response, httpStatus);
    }
	
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<DemandStoreAPIResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		DemandStoreAPIResponse response = new DemandStoreAPIResponse();
        response.setSuccess(false);
        response.setCode(CommonConstant.FAILURE_STATUS_CODE);
        MessageDetail messageDetail = new MessageDetail(MessageType.ERROR, CommonConstant.INVALID_REQUEST_PARAM, null, null);
        List<MessageDetail> msg = new ArrayList<>();
        msg.add(messageDetail);
        response.setMessage(msg);
        HttpStatus httpStatus = HttpStatus.OK;
        LOGGER.error("Some error occured : {} ", ex.getMessage(), ex);
        sendEmail(ex);
        return new ResponseEntity<>(response, httpStatus);
    }
	
	@Async
	public void sendEmail(Throwable exception) {
		if (env != null && env.equals("prod")) {
			StringWriter sw = new StringWriter();
			exception.printStackTrace(new PrintWriter(sw));
			List<String> toEmailId = Arrays.asList("sourabh@taggd.com", "geeta@taggd.com", "aman@taggd.com",
					"swapnil@taggd.com", "aksh@taggd.com");
			emailSMSServiceClient.sendEmail(new EmailSendRequest(toEmailId, "Exception in Checkout Service " + env,
					"noreply@mail.taggd.com", "Checkout Service", sw.toString()));
		}
	}
	


}
