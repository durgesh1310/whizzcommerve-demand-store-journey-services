package com.ouat.homepage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ouat.homepage.service.HomepageComponentService;


@RestController
@RequestMapping("/internal")
public class InternalController {
	
	
	@Autowired
	private HomepageComponentService service;
	
	@GetMapping("/delete-key")
	public void deleteKey(@RequestParam String key) {
		service.deleteKey(key);
		
	}

}
