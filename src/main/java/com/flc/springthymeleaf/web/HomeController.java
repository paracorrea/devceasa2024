package com.flc.springthymeleaf.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping("/ceasa")
	public String pageStart(Model model) {
		
		
		
		model.addAttribute("text", " benvindo");
		
		return "indexpage";
	}

}
