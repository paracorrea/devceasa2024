package com.flc.springthymeleaf.web;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.flc.springthymeleaf.domain.Nota;
import com.flc.springthymeleaf.service.NotaService;

import jakarta.validation.Valid;

@Controller
public class NotaController {

	private final NotaService notaService;
	
	public NotaController(NotaService notaService) {
		this.notaService = notaService;
	}
	
	@GetMapping("/notas/cadastrar")
	public String cadastrar(@RequestParam(defaultValue = "0") int page, Model model) {
		
		int pageSize = 10;  // Número de itens por página
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Nota> notaPage = notaService.findAll(pageable);

        model.addAttribute("notaPage", notaPage);
        model.addAttribute("nota", new Nota());
        return "nota/nota_cadastro";
	}
	
	 @PostMapping("/notas/salvar")
	    public String salvarFeira(@Valid @ModelAttribute("nota") Nota nota, BindingResult result, RedirectAttributes attr, Model model) {
	        // Verifica se a data está presente
	        if (nota.getDatacaptura() == null) {
	            result.rejectValue("dataCaptura", "error.nota", "A data da nota é obrigatória.");
	        } else {
	            

	            // Verifica se a data da feira é posterior à data atual
	            if (nota.getDatacaptura().isAfter(LocalDate.now())) {
	                result.rejectValue("dataCaptura", "error.nota", "A data da captura da nota não pode ser posterior à data atual.");
	            }
	        }

	      
	        if (result.hasErrors()) {
	            Pageable pageable = PageRequest.of(0, 10);
	            Page<Nota> notaPage = notaService.findAll(pageable);
	            model.addAttribute("notaPage", notaPage);
	            return "nota/nota_cadastro";
	        }

	        // Salva a feira se não houver erros
	        notaService.save(nota);
	        attr.addFlashAttribute("success", "Nota cadastrada com sucesso!");
	        return "redirect:/notas/cadastrar";
	    }
	 
	 @GetMapping("/notas/pesquisar")
	    public String pesquisar(@RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
	                            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
	                            @RequestParam(defaultValue = "0") int page,
	                            Model model) {
	        int pageSize = 10;  // Número de itens por página
	        Pageable pageable = PageRequest.of(page, pageSize);
	        Page<Nota> notaPage = notaService.findByDataNotaBetween(dataInicio, dataFim, pageable);

	        model.addAttribute("notaPage", notaPage);
	        model.addAttribute("dataInicio", dataInicio);
	        model.addAttribute("dataFim", dataFim);
	        model.addAttribute("nota", new Nota());
	        return "nota/nota_cadastro"; // Retorne para a mesma página com os resultados da pesquisa
	    }
	
}
