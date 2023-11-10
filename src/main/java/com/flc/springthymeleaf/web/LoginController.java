package com.flc.springthymeleaf.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/showMyLoginPage") // Security configuration 
	public String showMyLoginPage() {
		
		return "plain-login";
	}
	
	
	@GetMapping("/access_denied") // Security configuration 
	public String showAccessDenied() {
		
		return "access_denied";
	}
	
	
}
