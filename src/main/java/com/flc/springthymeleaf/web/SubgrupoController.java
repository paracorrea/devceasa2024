package com.flc.springthymeleaf.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SubgrupoController {

	@GetMapping("/subgrupos/cadastrar")
	public String subgrupoHome()
	{
		return "subgrupo/subgrupo_cadastro";
	}
}
