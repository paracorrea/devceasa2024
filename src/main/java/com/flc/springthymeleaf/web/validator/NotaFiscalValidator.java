package com.flc.springthymeleaf.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.flc.springthymeleaf.domain.NotaFiscal;
import com.flc.springthymeleaf.service.NotaFiscalService;
import org.springframework.beans.factory.annotation.Autowired;

public class NotaFiscalValidator implements Validator {

    @Autowired
    private NotaFiscalService notaFiscalService;

    public NotaFiscalValidator(NotaFiscalService notaFiscalService) {
		super();
		this.notaFiscalService = notaFiscalService;
	}

	@Override
    public boolean supports(Class<?> clazz) {
        return NotaFiscal.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NotaFiscal notaFiscal = (NotaFiscal) target;

        // Exemplo de validação para chave de acesso única
        if (notaFiscalService.existsByChaveAcesso(notaFiscal.getChaveAcesso())) {
            errors.rejectValue("chaveAcesso", "Chave de acesso já existe");
        }

        // Adicione outras validações conforme necessário
    }
}
