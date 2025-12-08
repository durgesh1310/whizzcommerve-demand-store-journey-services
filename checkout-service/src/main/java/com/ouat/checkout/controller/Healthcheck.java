package com.ouat.checkout.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ouat.checkout.placeOrder.client.CustomerServiceClient;
import com.ouat.checkout.placeOrder.request.GuestAddress;
import com.ouat.checkout.response.DemandStoreAPIResponse;

@RestController
public class Healthcheck {
	
	@GetMapping("/ping")
	public String ping() {
		return "pong";
	}
	@Autowired
	CustomerServiceClient cuuuc;
	
	/*@PostMapping("/test-guest-address")
	public DemandStoreAPIResponse checkGuestAddress() {
		GuestAddress ssaxs = new GuestAddress();
		ssaxs.setFullName("gaurav");
		ssaxs.setAddress("dwqqdnqwkndnewqjkj  edcwead");
		ssaxs.setLandmark("delasxasjkxkn");
		ssaxs.setCity("delhi");
		ssaxs.setPincode(201014);
		ssaxs.setState("delhi");
		ssaxs.setLandmark("qwer");
		ssaxs.setMobile("9716246348");
		ssaxs.setEmail("sourabhsingh2828277@gmail.com");
		return cuuuc.addGuestUser(ssaxs);
  	}*/
}
