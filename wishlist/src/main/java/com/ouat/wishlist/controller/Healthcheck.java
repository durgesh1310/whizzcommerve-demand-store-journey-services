package com.ouat.wishlist.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Healthcheck {
	
	@GetMapping("/ping")
    public String ping(HttpServletRequest request) {
        return "pong";
    }


}
