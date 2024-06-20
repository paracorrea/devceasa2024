package com.flc.springthymeleaf.web;


import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flc.springthymeleaf.domain.Feira;

import com.flc.springthymeleaf.service.FeiraService;
import com.flc.springthymeleaf.service.exceptions.FeiraNaoEncontradaException;
import com.flc.springthymeleaf.service.exceptions.FeiraNaoPodeSerEncerradaException;
import com.flc.springthymeleaf.web.validator.FeiraValidator;

import enums.StatusFeira;
import jakarta.validation.Valid;

@Controller
public class FeiraController {

	
	private static final Logger logger = LoggerFactory.getLogger(Feira.class);
	
	
	@Autowired
	private FeiraService feiraService;
	
	/*
	 * @InitBinder public void initBinder(WebDataBinder binder) {
	 * binder.addValidators(new FeiraValidator(feiraService)); }
	 */

	 @GetMapping("/feiras/cadastrar")
	    public String cadastrar(Feira feira, Model model,@RequestParam(defaultValue = "0") int page) {
		
		 	int pageSize = 10;  // Número de itens por página
		
	        Pageable pageable = PageRequest.of(page, pageSize);
	        List<Feira> feiraPage = feiraService.findAll(pageable);

	        model.addAttribute("feiraPage", feiraPage);
	        return "feira/feira_cadastro";
	    }

	    // Endpoint para salvar a nova feira
	 @PostMapping("/feiras/salvar")
	    public String salvarFeira(@Valid @ModelAttribute("feira") Feira feira, BindingResult result, RedirectAttributes attr, Model model,@RequestParam(defaultValue = "0") int page) {
	        // Verifica se já existe uma feira com a mesma data
	      
		 int pageSize = 10;  // Número de itens por página
			
	        Pageable pageable = PageRequest.of(page, pageSize);
		 Feira feiraExistente = feiraService.findByDataFeira(feira.getDataFeira());
	        
	        if (feiraExistente != null) {
	            result.rejectValue("dataFeira", "error.feira", "Já existe uma feira marcada para esta data.");
	        }

	        // Verifica se a data da feira é posterior à data atual
	        if (feira.getDataFeira().isAfter(LocalDate.now())) {
	            result.rejectValue("dataFeira", "error.feira", "A data da feira não pode ser posterior à data atual.");
	        }

	        // Define o status como ABERTA por padrão
	        feira.setStatusFeira(StatusFeira.ABERTA);

	        if (result.hasErrors()) {
	        	 List<Feira> feiraPage = feiraService.findAll(pageable);
	            model.addAttribute("feiras", feiraPage);
	            return "feira/feira_cadastro";
	        }

	        // Salva a feira se não houver erros
	        feiraService.salvarFeira(feira);
	        attr.addFlashAttribute("success", "Feira cadastrada com sucesso!");
	        return "redirect:/feiras/cadastrar";
	    }
	
	 
		
}
