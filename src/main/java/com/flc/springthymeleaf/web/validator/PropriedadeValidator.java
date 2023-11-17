package com.flc.springthymeleaf.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.flc.springthymeleaf.domain.Propriedade;
import com.flc.springthymeleaf.service.PropriedadeService;

public class PropriedadeValidator implements Validator {

	
	PropriedadeService propriedadeService;
	
	
	
	
	
	public PropriedadeValidator(PropriedadeService propriedadeService) {
		this.propriedadeService = propriedadeService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Propriedade.class.equals(clazz);
	}

	@Override
	public void validate(Object object, Errors errors) {
		
		Propriedade p = (Propriedade) object;
		
		String pVariedade = p.getVariedade();
		String pSubvariedade = p.getSubvariedade();
		String pClassificacao = p.getClassificacao();
		Integer id = p.getProduto().getId();
		
		
		
		//Propriedade propriedade = propriedadeService.findById(id);
		
		//if (propriedade==null) {
			
			//	errors.rejectValue("nome", "Propriedade.naopode.nome");
		//	}
		
		//if ( propriedades.size()>0) {
		//	errors.rejectValue("nome", "GrupoJaExiste.grupo.nome");
		//}
		
	}

}
