package com.flc.springthymeleaf.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flc.springthymeleaf.domain.Grupo;
import com.flc.springthymeleaf.domain.Subgrupo;
import com.flc.springthymeleaf.service.GrupoService;
import com.flc.springthymeleaf.service.SubgrupoService;
import com.flc.springthymeleaf.web.validator.SubgrupoValidator;

import jakarta.validation.Valid;

@Controller
public class SubgrupoController {

	
	@Autowired
	private SubgrupoService subgrupoService;
	
	
	@Autowired
	private GrupoService grupoService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
			
		binder.addValidators(new SubgrupoValidator(subgrupoService));
	}
	
	public SubgrupoController(SubgrupoService subgrupoService) {
		
		this.subgrupoService = subgrupoService;
	}

	
	@GetMapping("/subgrupos/cadastrar")
	public String cadastrar(Subgrupo subgrupos, Model model) {
		
		List<Subgrupo> lista = subgrupoService.findAll();
		model.addAttribute("subgrupos", lista);						
		return "subgrupo/subgrupo_cadastro";
	}
	
	
	@PostMapping("/subgrupos/salvar")
	public String salvar(@Valid Subgrupo subgrupo, BindingResult result, RedirectAttributes attr) {

		if (result.hasErrors()) {
			return "subgrupo/subgrupo_cadastro";
		}
		
		
		subgrupoService.insert(subgrupo);
		attr.addFlashAttribute("success", "Subgrupo cadastrado com sucesso");
		
		return "redirect:/subgrupos/cadastrar";
	}
	
	
	
	@GetMapping("/subgrupos/listar")
	public String findAll(ModelMap model) {
		
		
		
		List<Subgrupo> listSubgrupos = subgrupoService.findAll();
		model.addAttribute("subgrupos",listSubgrupos);
		
		return "/subgrupo/subgrupo_listar";
	}
	
	
	@GetMapping("/subgrupos/editar/{id}")
	public String preEditar(@PathVariable("id") Integer id, ModelMap model) {
		model.addAttribute("subgrupo", subgrupoService.findById(id));
		return "/subgrupo/subgrupo_cadastro";
	}
	
	@PostMapping("/subgrupos/editar")
	public String editar(@Valid Subgrupo subgrupo, BindingResult result,RedirectAttributes attr) {
		
		if (result.hasErrors()) {
			return "/subgrupo/subgrupo_cadastro";
		}
		
		subgrupoService.update(subgrupo);
		attr.addFlashAttribute("success", "Subgrupo editado com sucesso");
		return "redirect:/subgrupos/cadastrar";
	}
	
	
	@ModelAttribute("grupos")
	public List<Grupo> listaGrupos() {
		return grupoService.findAll();
	}
	
	

	@GetMapping("/subgrupos/excluir/{id}")
	public String excluir(@PathVariable("id") Integer id, RedirectAttributes attr)  {

		
		if (subgrupoService.subgrupoTemProdutos(id)) {
			attr.addFlashAttribute("fail", "subgrupo não removido. Possui Produtos");
			
			
		} else {
		subgrupoService.delete(id);
		
		attr.addFlashAttribute("success","Subgrupo excluído com sucesso")	;	
		

	}
		return "redirect:/subgrupos/cadastrar";
	}
	
	
	
	// Lista todos os grupos de um determinado subgrupo
	@GetMapping("/subgrupos/grupos/{id}")
	public String findGrupoPorSubgrupo(@PathVariable Integer id, ModelMap model) {
		
		List<Subgrupo> obj = subgrupoService.findGrupoSubgrupo(id);
		model.addAttribute("subgrupos", obj);
		return "/subgrupo/subgrupo_listar";	
	}
	
	
	
	
}
