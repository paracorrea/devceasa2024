package com.flc.springthymeleaf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flc.springthymeleaf.domain.Grupo;
import com.flc.springthymeleaf.service.GrupoService;
import com.flc.springthymeleaf.web.validator.GrupoValidator;

import jakarta.validation.Valid;

@Controller
public class GrupoController {
	
	@Autowired
	private GrupoService grupoService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
			
		binder.addValidators(new GrupoValidator(grupoService));
	}
	
	@GetMapping("/grupos/cadastrar")
	public String cadastrar(Grupo grupo, ModelMap model) {
		
		model.addAttribute("grupos", grupoService.findAll());
		return "grupo/grupo_cadastro";
	}
	
	@GetMapping("/grupos/listar")
	public String listar(Grupo grupo, ModelMap model) {
		
		model.addAttribute("grupos", grupoService.findAll());
		return "grupo/grupo_listagem";
	}	
	
	@PostMapping("/grupos/salvar")
	public String salvar(@Valid Grupo grupo, BindingResult result, RedirectAttributes attr) {
		
		if (result.hasErrors()) {
			return "grupo/grupo_cadastro";
		}
	
		grupoService.insert(grupo);
		attr.addFlashAttribute("success", "Cargo cadastrado com sucesso");
		return "redirect:/grupos/cadastrar";
	}
	
	
	@GetMapping("/grupos/editar/{id}")
	public String preEditar(@PathVariable("id") Integer id,   ModelMap model) {
		model.addAttribute("grupo", grupoService.findById(id));
		return "grupo/grupo_cadastro";
	}
	
	@PostMapping("/grupos/editar")
	public String editar(@Valid Grupo grupo, BindingResult result,RedirectAttributes attr) {
		
		if (result.hasErrors()) {
			return "grupo/grupo_cadastro";
		}
		
		grupoService.update(grupo);
		attr.addFlashAttribute("success", "Grupo editado com sucesso");
		return "redirect:/grupos/cadastrar";
	}
	
	@GetMapping("/grupos/excluir/{id}")
	public String excluir(@PathVariable("id") Integer id, RedirectAttributes attr){
		
		if (grupoService.grupoTemSubgrupo(id)) {
			attr.addFlashAttribute("fail", "Grupo não removido. Possui Subgrupos(s) vinculado(s)");
			
		} else {
			grupoService.delete(id);
			attr.addFlashAttribute("success","Grupo excluido com sucesso");
			
		}
			
		return "redirect:/grupos/cadastrar";
		
	}
	
	
	
}
