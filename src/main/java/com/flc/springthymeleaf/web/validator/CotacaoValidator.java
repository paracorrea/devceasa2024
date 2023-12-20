package com.flc.springthymeleaf.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
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
		
		 Cotacao cotacao = (Cotacao) object;

		
		 ValidationUtils.rejectIfEmptyOrWhitespace(errors, "valor1", "field.required", "O campo Valor1 é obrigatório.");
		 ValidationUtils.rejectIfEmptyOrWhitespace(errors, "valor2", "field.required", "O campo Valor2 é obrigatório.");
		 ValidationUtils.rejectIfEmptyOrWhitespace(errors, "valor3", "field.required", "O campo Valor3 é obrigatório.");
	     ValidationUtils.rejectIfEmptyOrWhitespace(errors, "precoMinimo", "field.required", "O campo Preço Mínimo é obrigatório.");
	     ValidationUtils.rejectIfEmptyOrWhitespace(errors, "precoMaximo", "field.required", "O campo Preço Máximo é obrigatório.");
	     ValidationUtils.rejectIfEmptyOrWhitespace(errors, "precoMedio", "field.required", "O campo Preço Médio é obrigatório.");

	      
	        
	        if (cotacao.getPrecoMinimo() != null && cotacao.getPrecoMaximo() != null && cotacao.getPrecoMinimo().compareTo(cotacao.getPrecoMaximo()) > 0) {
	            errors.rejectValue("precoMaximo", "precoMaximo.invalid", "O Preço Máximo deve ser maior ou igual ao Preço Mínimo.");
	        }
		
	}
}
	
	

