package com.flc.springthymeleaf.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.flc.springthymeleaf.domain.Cotacao;
import com.flc.springthymeleaf.service.CotacaoService;

public class CotacaoValidator implements Validator {

	
	CotacaoService cotacaoService;
	
	
	public CotacaoValidator(CotacaoService cotacaoService) {
		super();
		this.cotacaoService = cotacaoService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Cotacao.class.equals(clazz);
	}

	@Override
	public void validate(Object object, Errors errors) {
		// TODO Auto-generated method stub
		
		Cotacao dat = (Cotacao) object;
		
		
		
	}

	
	
	
}
