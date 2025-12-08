package com.ouat.orderService.returnExchangeCancelAlert.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ouat.orderService.client.EmailAlertType;
import com.ouat.orderService.client.EmailSendRequest;
import com.ouat.orderService.client.ReturnExchangeCancelAlert;
import com.ouat.orderService.response.CustomerDetailVO;
import com.ouat.orderService.returnExchangeCancelAlert.repository.ReturnExchangeCancelAlertDto;
import com.ouat.orderService.returnExchangeCancelAlert.repository.ReturnExchangeCancelAlertRepository;

@Service
public class ReturnCancelExchangeAlert {
	@Autowired
	ReturnExchangeCancelAlertRepository returnExchangeCancewlAlertRepository;
	
	public String setSetExchangeAlertCancelRequestDate(){
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MMMM-yyyy");
     	Date date = new Date();
        String dateString = format2.format(date);
        dateString = dateString.replace("-"," "); 
        return (dateString);
    }
	public ReturnExchangeCancelAlert buildReturnExchangeCancelRequest(Long orderItemId,  String sku,
 			CustomerDetailVO customerDetail,  EmailAlertType emailAlert) {
		String recordType = null;
		if(emailAlert == EmailAlertType.CANCEL) {
			recordType = "CANCELLED";
		} else if (emailAlert == EmailAlertType.RETURN) {
			recordType = "RETURN";
		} else if (emailAlert == EmailAlertType.EXCHNAGE) {
			recordType = "EXCHANGE";
		}
		ReturnExchangeCancelAlertDto returnExchangeCancelAlertDto = returnExchangeCancewlAlertRepository.getAlertAttribute(sku  , orderItemId, recordType );
		ReturnExchangeCancelAlert returnExchangeCancelAlert= new ReturnExchangeCancelAlert();
		returnExchangeCancelAlert.setCustomerName(customerDetail.getName());
		returnExchangeCancelAlert.setOrderNumber(returnExchangeCancelAlertDto.getOrderNumber());
 		returnExchangeCancelAlert.setReturnExchangeCancelDate(setSetExchangeAlertCancelRequestDate());
		returnExchangeCancelAlert.setImgUrl(returnExchangeCancelAlertDto.getImgUrl());
		returnExchangeCancelAlert.setItemName(returnExchangeCancelAlertDto.getItemName());
		returnExchangeCancelAlert.setSku(returnExchangeCancelAlertDto.getSku());
		returnExchangeCancelAlert.setQty(returnExchangeCancelAlertDto.getQty());
		returnExchangeCancelAlert.setReason(returnExchangeCancelAlertDto.getReason());
		returnExchangeCancelAlert.setRecordType(returnExchangeCancelAlertDto.getRecordType());
 		returnExchangeCancelAlert.setAlertType(emailAlert);
		returnExchangeCancelAlert.setEmailSendRequest(buildEmailSentRequestForOrderAlert(customerDetail.getEmail(), emailAlert));
 
		return returnExchangeCancelAlert;
	}
	private EmailSendRequest buildEmailSentRequestForOrderAlert(String customerEmail, EmailAlertType emailAlertType) {
		EmailSendRequest emailSendRequest = new EmailSendRequest();
     	emailSendRequest.setFromEmail("info@mail.taggd.com");
     	emailSendRequest.setFromNickName("taggd");
     	List<String> toEmailAddressList = new ArrayList<String>();
     	if(emailAlertType.equals(EmailAlertType.CANCEL)) {
     	 	emailSendRequest.setMessageBody("Your order is cancel from our side");
         	emailSendRequest.setSubject("ORDER CANCEL");
     	}else if(emailAlertType.equals(EmailAlertType.RETURN)){
     	 	emailSendRequest.setMessageBody("Your Order Return Initiated Successfully ");
         	emailSendRequest.setSubject("ORDER RETURN");
     	}
     	else if(emailAlertType.equals(EmailAlertType.EXCHNAGE)){
     		emailSendRequest.setMessageBody("Your order exchange request has been placed from our side");
         	emailSendRequest.setSubject("ORDER EXCHANGE");
     	}
    
     	toEmailAddressList.add(customerEmail);
     	emailSendRequest.setToEmailAddress(toEmailAddressList);
		return emailSendRequest;
	}
}
