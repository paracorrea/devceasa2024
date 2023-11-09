package com.flc.springthymeleaf.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProdutoController {

	
	@GetMapping("/produtos/cadastrar")
	public String subgrupoHome()
	{
		return "produto/produto_cadastro";
	}
	
}
