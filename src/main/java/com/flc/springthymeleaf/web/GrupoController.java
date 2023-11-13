package com.flc.springthymeleaf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;

import com.flc.springthymeleaf.domain.Grupo;
import com.flc.springthymeleaf.service.GrupoService;
import com.flc.springthymeleaf.web.validator.GrupoValidator;

@Controller
public class GrupoController {
	
	@Autowired
	private GrupoService grupoService;
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
			
		binder.addValidators(new GrupoValidator(grupoService));
	}
	
	
	@GetMapping("/grupos/cadastrar")
	public String cadastrar(Grupo grupo, ModelMap model) {
		
		model.addAttribute("grupos", grupoService.findAll());
		return "grupo/grupo_cadastro";
	
	}
	
	
		
		
}
