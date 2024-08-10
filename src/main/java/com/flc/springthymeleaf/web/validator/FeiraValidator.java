package com.flc.springthymeleaf.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.flc.springthymeleaf.domain.Feira;
import com.flc.springthymeleaf.service.FeiraService;

public class FeiraValidator implements Validator {

	FeiraService feiraService;

	
	
	
	public FeiraValidator(FeiraService feiraService) {
		super();
		this.feiraService = feiraService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Feira.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		//Feira feira = (Feira) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dataFeira", "a Data não pode ser nulla");

	}

}