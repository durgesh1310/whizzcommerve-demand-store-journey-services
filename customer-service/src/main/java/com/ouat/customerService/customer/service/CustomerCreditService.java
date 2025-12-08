package com.ouat.customerService.customer.service;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ouat.customerService.constants.CommonConstant;
import com.ouat.customerService.customer.repository.CustomerCreditRepository;
import com.ouat.customerService.customer.request.CustomerCreditHistory;
import com.ouat.customerService.customer.response.CustomerCreditResponse;
import com.ouat.customerService.dto.CustomerCredit;
import com.ouat.customerService.response.DemandStoreAPIResponse;

@Service
public class CustomerCreditService {
	
	@Autowired
	CustomerCreditRepository customerCreditRepository;
	
	
	public ResponseEntity<DemandStoreAPIResponse> fetchCredits(Integer customerId) {
		
		List<CustomerCreditResponse> customerCreditResponseList = new ArrayList<>();
		List<CustomerCredit> creditList = customerCreditRepository.creditsByCustomerId(customerId);
		for(CustomerCredit credit : creditList) {
			if(credit.getCreditType().equals("M")) {
				CustomerCreditResponse customerCreditResponse = new CustomerCreditResponse();
				customerCreditResponse.setCreditName("Wallet");
				if(null != credit.getAmount() && credit.getAmount().intValue() < 0 ) {
					customerCreditResponse.setAmount(0);
				} else {
					customerCreditResponse.setAmount(credit.getAmount().intValue());
				}
				customerCreditResponse.setType("M");
				customerCreditResponseList.add(customerCreditResponse);
			} else if(credit.getCreditType().equals("L") && null != credit.getExpiry()) {
		            if(credit.getExpiry().after(new Date(Instant.now().toEpochMilli()))) {
			        	CustomerCreditResponse customerCreditResponse = new CustomerCreditResponse();
						customerCreditResponse.setCreditName("Rewards");
						if(null != credit.getAmount() && credit.getAmount().intValue() < 0 ) {
							customerCreditResponse.setAmount(0);
						} else {
							customerCreditResponse.setAmount(credit.getAmount().intValue());
						}
						customerCreditResponse.setType("L");
						customerCreditResponseList.add(customerCreditResponse);
			        }
		       
			}
		}
		return ResponseEntity.ok(
				new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, customerCreditResponseList));
	}

	@Transactional
	public ResponseEntity<DemandStoreAPIResponse> addInHistory(CustomerCreditHistory customerCreditHistory) {
			customerCreditRepository.addInHistory(customerCreditHistory);
			customerCreditRepository.updateCustomerCredit( customerCreditHistory, "Credit used against order : "+ customerCreditHistory.getOrderId());
			return ResponseEntity.ok(new DemandStoreAPIResponse(true, null, CommonConstant.SUCCESS_STATUS_CODE, null));
	}

}
