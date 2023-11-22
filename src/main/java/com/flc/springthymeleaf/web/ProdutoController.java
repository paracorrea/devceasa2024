package com.flc.springthymeleaf.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flc.springthymeleaf.domain.Produto;
import com.flc.springthymeleaf.domain.Subgrupo;
import com.flc.springthymeleaf.service.ProdutoService;
import com.flc.springthymeleaf.service.SubgrupoService;
import com.flc.springthymeleaf.web.validator.ProdutoValidator;

import jakarta.validation.Valid;

@Controller
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private SubgrupoService subService;
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
			
		binder.addValidators(new ProdutoValidator(produtoService));
	}
	

	public ProdutoController(ProdutoService produtoService) {
		super();
		this.produtoService = produtoService;
	}
	
	@ModelAttribute("subgrupos")
	public List<Subgrupo> listaGrupos() {
		return subService.findAll();
	}
	
	@GetMapping("/produtos/page/{pageNo}")
	public String findPaginated(@PathVariable (value = "pageNo") int pageNo, 
				@RequestParam("sortField") String sortField,
				@RequestParam("sortDir") String sortDir,
				Model model) {
			int pageSize = 25;
			
			Page<Produto> page = produtoService.findPaginated(pageNo, pageSize, sortField, sortDir);
			List<Produto> listProdutos = page.getContent();
			
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("totalItems", page.getTotalElements());
			
			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
			
			model.addAttribute("produtos", listProdutos);
			return "produto/produto_cadastro";
		}
	
	
	
	@GetMapping("/produtos/cadastrar")
	public String listarProdutos(Produto produto , Model model) {
		//List<Produto> lista = produtoService.findAll();
		//model.addAttribute("produtos", lista);
		return findPaginated(1, "nome", "asc", model);
	
}
	
	@GetMapping("/produtos/formcadastro")
	public String formularioCadastrar(Produto produto , Model model) {
		List<Produto> lista = produtoService.findAll();
		model.addAttribute("produtos", lista);
		return "produto/produto_form";
	}
	
	@GetMapping("/produtos/editar/{id}")
	public String preEditar(@PathVariable("id") Integer id, ModelMap model) {

		model.addAttribute("produto", produtoService.findById(id));
		return "produto/produto_editar";

	}
	
		
	@PostMapping("/produtos/editar")
	public String editar (@Valid Produto produto,BindingResult result, RedirectAttributes attr) {

		if (result.hasErrors()) {
			return "produto/produto_form";
		}
		
		
		produtoService.update(produto);
		attr.addFlashAttribute("success", "Produto editado com sucesso");
		return "redirect:/produtos/cadastrar";

	}
	
	@PostMapping("/produtos/salvar")
	public String salvar(@Valid Produto produto,BindingResult result, RedirectAttributes attr) {

		

		if (result.hasErrors()) {
			return "produto/produto_form";
		}
		
		
		produtoService.insert(produto);
		attr.addFlashAttribute("success", "Produto cadastrado com sucesso");
		return "redirect:/produtos/cadastrar";
		
	
	}
	
	@GetMapping("/produtos/excluir/{id}")
	public String excluir(@PathVariable("id") Integer id, RedirectAttributes attr) {

		if (produtoService.produtoTemPropriedades(id)) {
			attr.addFlashAttribute("fail", "Produto não pode ser removido. Possui propriedades");
			
			
		} else {
		
		produtoService.delete(id);
		attr.addFlashAttribute("success","Produto excluído com sucesso");		
		
		}		
		return "redirect:/produtos/cadastrar";

	}
	
		
	@GetMapping("/produtos/listar")
	public String findAll(ModelMap model) {
		
		
		
		List<Produto> listSubgrupos = produtoService.findAll();
		model.addAttribute("produtos",listSubgrupos);
		
		return "produto/produto_listagem";
	

	}

	@GetMapping("/produtos/subgrupos/{id}")
	public String findProdutosPorSubgrupos(@PathVariable Integer id, ModelMap model) {
		
		List<Produto> obj = produtoService.findProdutoBySubgrupo(id);
		model.addAttribute("search", obj);
		return "produto/produto_listar";	}

	
	@GetMapping("/produtos/nomes")
	public String findPorNome(@RequestParam (value = "search", required = false) String search, Model model) {
		
		String nome = search.toUpperCase();
		List<Produto> obj = produtoService.findByNome(nome);
		model.addAttribute("search", obj);
		return "produto/produto_listar";
		}
}
	
