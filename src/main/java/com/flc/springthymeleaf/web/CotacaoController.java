package com.flc.springthymeleaf.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.flc.springthymeleaf.domain.Produto;
import com.flc.springthymeleaf.domain.Propriedade;
import com.flc.springthymeleaf.service.CotacaoService;
import com.flc.springthymeleaf.service.PropriedadeService;
import com.flc.springthymeleaf.web.validator.CotacaoValidator;



@Controller
public class CotacaoController {

	
	@Autowired
	private CotacaoService cotacaoService;
	
	@Autowired
	private PropriedadeService propriedadeService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
			
		binder.addValidators(new CotacaoValidator(cotacaoService));
	}
	
	@ModelAttribute("cotados")
	public List<Propriedade> listarCotados() {
		return propriedadeService.findPropriedadePorCotacao();
	}
	
	
	
	
	
}
