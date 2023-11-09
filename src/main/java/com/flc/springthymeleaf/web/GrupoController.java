package com.flc.springthymeleaf.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GrupoController {
	
	@GetMapping("/grupos/cadastrar")
	public String cadastroPage() {
		
		return "grupo/grupo_cadastro";
	}
	

}
