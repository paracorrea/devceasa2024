package com.flc.springthymeleaf.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.flc.springthymeleaf.domain.Cotacao;
import com.flc.springthymeleaf.domain.Produto;
import com.flc.springthymeleaf.domain.Propriedade;
import com.flc.springthymeleaf.service.ProdutoService;
import com.flc.springthymeleaf.service.PropriedadeService;
import com.flc.springthymeleaf.web.validator.PropriedadeValidator;

import jakarta.validation.Valid;

@Controller
public class PropriedadeController {
	
	private static final Logger logger = LoggerFactory.getLogger(Cotacao.class);

	@Autowired
	private PropriedadeService propriedadeService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Value("${unidades}")
	private List<String> unidades;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
			
		binder.addValidators(new PropriedadeValidator(propriedadeService));
	}
	
	@ModelAttribute("produtos")
	public List<Produto> listaProdutos() {
		return produtoService.findAll();
	}
	
		
	@GetMapping("/propriedades/cadastrar")
	public String cadastrar(Propriedade propriedade, Model model) {
		
		List<Propriedade> lista = propriedadeService.findAll();
		model.addAttribute("propriedades", lista);		
		model.addAttribute("unidades", unidades);
		return "propriedade/propriedade_cadastro";
	}
	
	@PostMapping("/propriedades/salvar")
	public String salvar(@Valid Propriedade propriedade,BindingResult result, RedirectAttributes attr ) {
		
		
		if (result.hasErrors()) {
			return "propriedade/propriedade_cadastro";
		}
		
		if (propriedadeService.existsByCodigo(propriedade.getCodigo())) {
					attr.addFlashAttribute("success", "Código já existente");
					return "propriedade/propriedade_cadastro";
		} 
		
		propriedadeService.insert(propriedade);
		attr.addFlashAttribute("success", "Propriedade cadastradas com sucesso");
		
		return "redirect:/propriedades/cadastrar";
		}
	
	@GetMapping("/propriedades/editar/{id}")
	public String preEditar(@PathVariable("id") Integer id, ModelMap model) {
		model.addAttribute("unidades", unidades);
		model.addAttribute("propriedade", propriedadeService.findById(id));
		logger.info("id: de propriedade: ",id);
		return "propriedade/propriedade_cadastro";
	}
	
	@PostMapping("/propriedades/editar") 
	public String editar (@Valid Propriedade propriedade, BindingResult result, RedirectAttributes attr) {
		
		//if (result.hasErrors()) {
		//	return "propriedade/propriedade_cadastro";
		//}
		//if (propriedadeService.existsByCodigo(propriedade.getCodigo())) {
		//	attr.addFlashAttribute("success", "Código já existente");
		//	return "propriedade/propriedade_cadastro";
		//} 
		propriedadeService.update(propriedade);
		attr.addFlashAttribute("success", "Propriedade editada com sucesso");
		return "redirect:/propriedades/cadastrar";
	}
	
	@GetMapping("/propriedades/excluir/{id}")
	public String excluir(@PathVariable("id") Integer id, RedirectAttributes attr) {

		
		
			propriedadeService.delete(id);
			attr.addFlashAttribute("success","Propriedade excluída com sucesso");		
			return "redirect:/propriedades/cadastrar";
		} 
	
	
	@GetMapping("/propriedades/listar")
	public String findAll(ModelMap model) {
		
		
		
		List<Propriedade> list = propriedadeService.findAll();
		model.addAttribute("propriedades",list);
		
		return "propriedade/propriedade_listar";
	

	}
	
	
	@GetMapping("/propriedades/produtos/{id}")
	public String findPropriedadePorProduto(@PathVariable Integer id, ModelMap model) {
		
		List<Propriedade> obj = propriedadeService.findPropriedadePorProduto(id);
		model.addAttribute("propriedades", obj);
		return "propriedade/propriedade_listar";	}
	

}
