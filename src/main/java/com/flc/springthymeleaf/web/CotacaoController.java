package com.flc.springthymeleaf.web;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flc.springthymeleaf.domain.Cotacao;
import com.flc.springthymeleaf.domain.Propriedade;
import com.flc.springthymeleaf.service.CotacaoService;
import com.flc.springthymeleaf.service.PropriedadeService;
import com.flc.springthymeleaf.service.exceptions.DataIntegrityException;
import com.flc.springthymeleaf.web.validator.CotacaoValidator;

import enums.FatorSazonal;
import jakarta.validation.Valid;



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
	
	@ModelAttribute("fatores")
	public FatorSazonal[] getFatores() {
		return FatorSazonal.values();

	}
	
	@GetMapping("/cotacoes/cadastrar")
	public String cadastrar(Cotacao cotacao, Model model) {
		
		LocalDate dataAtual = LocalDate.now();
		
		List<Cotacao> lista = cotacaoService.findAll();
		
		model.addAttribute("dataAtual", dataAtual);
		model.addAttribute("cotacoes", lista);		
		return "cotacao/cotacao_cadastro";
	}
	
	
	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<String> handleDataIntegrityException(DataIntegrityException ex) {
	    return ResponseEntity.badRequest().body(ex.getMessage());
	}
	@PostMapping("/cotacoes/salvar")
	public String salvar(@Valid @ModelAttribute Cotacao cotacao, Model model,BindingResult result, RedirectAttributes attr ) {
		
		 LocalDate dataAtual = LocalDate.now();
		 
		 

	        // Exibe a data no terminal
	        System.out.println("Data Atual: " + dataAtual);
		 
		 if (!cotacao.getDataCotacao().equals(dataAtual)) {
			
			 result.rejectValue("dataCotacao", "error.cotacao", "A data da cotação deve ser a data atual. Data atual: " + dataAtual + ", Data informada: " + cotacao.getDataCotacao());
	        }
		 
		 
		 
		if (result.hasErrors()) {
			
			
			
			return "cotacao/cotacao_cadastro";
		}
		
		cotacaoService.insert(cotacao);
		attr.addFlashAttribute("success", "Cotação cadastrada com sucesso");
		
		return "redirect:/cotacoes/cadastrar";
		}
	
	@GetMapping("/cotacoes/editar/{id}")
	public String preEditar(@PathVariable("id") Integer id, ModelMap model) {
		model.addAttribute("cotacao", cotacaoService.findById(id));
		return "cotacao/cotacao_cadastro";
	}
	
	@PostMapping("/cotacoes/editar") 
	public String editar (@Valid Cotacao cotacao, BindingResult result, RedirectAttributes attr) {
		if (result.hasErrors()) {
			return "cotacao/cotacao_cadastro";
		}
		
		cotacaoService.update(cotacao);
		attr.addFlashAttribute("success", "Cotação editada com sucesso");
		return "redirect:/cotacoes/cadastrar";
	}
	
	@GetMapping("/cotacao/excluir/{id}")
	public String excluir(@PathVariable("id") Integer id, RedirectAttributes attr) {

		
		
			cotacaoService.delete(id);
			attr.addFlashAttribute("success","Cotação excluída com sucesso");		
			return "redirect:/cotacoes/cadastrar";
		} 
	
	
	@GetMapping("/cotacoes/listar")
	public String findAll(ModelMap model) {
		
		
		
		List<Cotacao> list = cotacaoService.findAll();
		model.addAttribute("cotacoes",list);
		
		return "cotacao/cotacao_listar";
	

	}
	
	
	/*
	 * @GetMapping("/propriedades/produtos/{id}") public String
	 * findPropriedadePorProduto(@PathVariable Integer id, ModelMap model) {
	 * 
	 * List<Propriedade> obj = propriedadeService.findPropriedadePorProduto(id);
	 * model.addAttribute("propriedades", obj); return
	 * "propriedade/propriedade_listar"; }
	 */
	
}
