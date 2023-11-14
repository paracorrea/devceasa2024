package com.flc.springthymeleaf.web.validator;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.flc.springthymeleaf.domain.Subgrupo;
import com.flc.springthymeleaf.service.SubgrupoService;


public class SubgrupoValidator implements Validator{

	SubgrupoService subgrupoService;
	
	public SubgrupoValidator(SubgrupoService subgrupoService) {
	
		this.subgrupoService = subgrupoService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Subgrupo.class.equals(clazz);
	}

	@Override
	public void validate(Object object, Errors errors) {

		Subgrupo d = (Subgrupo) object;
		String nome = d.getNome();
		List<Subgrupo> subgrupos = subgrupoService.findByNome(nome);
	
		if (nome !=null) {
			if (subgrupos.size() >0 ) {
				errors.rejectValue("nome", "SubGrupoJaExiste.subgrupo.nome");
			}
		}
	
	}
	
}
