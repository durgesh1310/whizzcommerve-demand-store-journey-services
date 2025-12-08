package com.ouat.checkout.placeOrder.client;

import java.util.Arrays;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ouat.checkout.constant.CommonConstant;
import com.ouat.checkout.exception.BusinessProcessException;
import com.ouat.checkout.placeOrder.request.GuestAddress;
import com.ouat.checkout.response.DemandStoreAPIResponse;
import com.ouat.checkout.util.Utility;

@Service
public class CustomerServiceClient {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceClient.class);
	private static final String API_KEY_AUTH_HEADER_NAME = "AccessKey";
	
	@Autowired
	private RestTemplate restTemplate;

	@Value("${customer.service.customer.history.url}")
	private String customerCreditUpdate;
	
	@Value("${customer.service.add.customer.url}")
	private String addCustomerAndAddress;
	
	@Value("${internal.secure.accessKey}")
    private String accessKey;
	
	public Boolean customerCreditUpdate(CustomerCreditHistory customerCreditHistory) {
		LOGGER.info("customer credit history : {}", customerCreditHistory);
		HttpHeaders headers = new HttpHeaders();
		headers.add(API_KEY_AUTH_HEADER_NAME, accessKey);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<CustomerCreditHistory> request = new HttpEntity<CustomerCreditHistory>(
				customerCreditHistory, headers);
		try {
			DemandStoreAPIResponse demandStoreAPIResponse  = restTemplate.exchange(customerCreditUpdate, HttpMethod.POST, request, DemandStoreAPIResponse.class).getBody();
			return demandStoreAPIResponse.isSuccess();
		} catch (Exception e) {
			LOGGER.error("Error while calling customer service for product detail : {} ", e.getMessage(), e);
		}
		return false;
	}
	
	
    public AddGuestUserResponse addGuestUser(GuestAddress address, String mobile) throws BusinessProcessException {
        address.setMobile(mobile);
        if(null == address.getEmail() || "".equals(address.getEmail().trim())) {
        	LOGGER.info("customer  address request invalid : {}", Utility.getJson(address));
        	throw new BusinessProcessException("Email id missing", CommonConstant.FAILURE_STATUS_CODE);
        } 
        HttpHeaders headers = new HttpHeaders();
        headers.add(API_KEY_AUTH_HEADER_NAME, accessKey);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<GuestAddress> request = new HttpEntity<GuestAddress>(address, headers);
        try {
            LOGGER.info("customer  address request recieve : {}", Utility.getJson(address));
            String responseBody =
                    restTemplate.exchange(addCustomerAndAddress, HttpMethod.POST, request,
                            String.class).getBody();
            DemandStoreAPIResponse demandStoreAPIResponse = Utility.convertJsonToObject(responseBody, DemandStoreAPIResponse.class);
            LOGGER.info("Customer Service Response = {}", Utility.getJson(demandStoreAPIResponse));
            if (Objects.isNull(demandStoreAPIResponse.getData())) {
                throw new RuntimeException("Empty Response from customer service");
            }
           return Utility.convertJsonToObject(Utility.getJson(demandStoreAPIResponse.getData()), AddGuestUserResponse.class);
        } catch (Exception e) {
            LOGGER.error("Error while calling customer service for product detail : {} ",
                    e.getMessage(), e);
            throw e;
        }
    }
}

