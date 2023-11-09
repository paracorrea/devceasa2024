package com.flc.springthymeleaf.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PropriedadeController {
	
	
	@GetMapping("/propriedades/cadastrar")
	public String subgrupoHome()
	{
		return "propriedade/propriedade_cadastro";
	}
}
