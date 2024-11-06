package com.flc.springthymeleaf.web;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flc.springthymeleaf.DTO.ProdutoPesoDTO;
import com.flc.springthymeleaf.DTO.SubgrupoPesoDTO;
import com.flc.springthymeleaf.domain.Produto;
import com.flc.springthymeleaf.domain.Subgrupo;
import com.flc.springthymeleaf.service.ItemDeNotaService;
import com.flc.springthymeleaf.service.ProdutoService;
import com.flc.springthymeleaf.service.SubgrupoService;
import com.flc.springthymeleaf.service.exceptions.ObjectNotFoundException;
import com.flc.springthymeleaf.web.validator.ProdutoValidator;

import jakarta.validation.Valid;

@Controller
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private SubgrupoService subService;
	
	@Autowired
	private ItemDeNotaService itemDeNotaService;
	
	
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
	@GetMapping("/produtos/pesquisar")
    public String pesquisarPorId(@RequestParam (required= false) Integer id, 
    							@RequestParam (required= false) String nome, Model model,
    							RedirectAttributes attr) {
        // Lógica para buscar o produto por ID
       

		   try {
		        if (id != null) {
		            // Lógica para buscar o produto por ID
		            Produto produto = produtoService.findById(id);

		            if (produto != null) {
		                model.addAttribute("produtos", Collections.singletonList(produto));
		            } else {
		                // Produto não encontrado, redirecionar ou fornecer uma mensagem de erro
		                return "redirect:/produtos/listar";
		                // ou
		                // model.addAttribute("error", "Produto não encontrado");
		                // return "sua-pagina-de-erro";
		            }
		        } else if (nome != null && !nome.isEmpty()) {
		            // Lógica para buscar o produto por nome
		            List<Produto> produtos = produtoService.findByNome(nome.toUpperCase());

		            model.addAttribute("produtos", produtos);
		        } else {
		            // Caso nenhum parâmetro seja fornecido, retorne à página inicial ou forneça todos os produtos
		            model.addAttribute("produtos", produtoService.findAll());
		        }
		    } catch (ObjectNotFoundException e) {
		        // Trate a exceção ObjectNotFoundException
		    	attr.addFlashAttribute("success","Produto inexistente");
		        // ou
		        // redirecione para uma página de erro
		        return "produto/produto_listagem";
		    }

		    // Adicione aqui a lógica para preencher o restante dos dados necessários na página

		   return "produto/produto_listagem";
		}
		
	@GetMapping("/produtos/listar")
	public String findAll(ModelMap model) {
		
		
		
		List<Produto> listProdutos = produtoService.findAll();
		
		List<Produto> listaOrdenada = listProdutos.stream()
		        .sorted((p1, p2) -> p1.getNome().compareToIgnoreCase(p2.getNome()))
		        .collect(Collectors.toList());
		
		model.addAttribute("produtos",listaOrdenada);
		
		return "produto/produto_listagem";
	

	}

	@GetMapping("/produtos/subgrupos/{id}")
	public String findProdutosPorSubgrupos(@PathVariable Integer id, ModelMap model) {
		
		List<Produto> listaProdutoPorSubgrupo = produtoService.findProdutoBySubgrupo(id);
		
		List<Produto> listaOrdenada = listaProdutoPorSubgrupo.stream()
		        .sorted((p1, p2) -> p1.getNome().compareToIgnoreCase(p2.getNome()))
		        .collect(Collectors.toList());
		
		
		model.addAttribute("search", listaOrdenada);
		return "produto/produto_listar";	}

	
	@GetMapping("/produtos/nomes")
	public String findPorNome(@RequestParam (value = "search", required = false) String search, Model model) {
		
		String nome = search.toUpperCase();
		List<Produto> obj = produtoService.findByNome(nome);
		model.addAttribute("search", obj);
		return "produto/produto_listar";
		}
	
	 @GetMapping("/produtos/{id}")
	    public ResponseEntity<Produto> getProdutoById(@PathVariable Integer id) {
	        Produto produto = produtoService.findById(id);
	        if (produto != null) {
	            return ResponseEntity.ok(produto);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	 
	 @GetMapping("/")
	 public String mostrarGraficoProdutos(
			 @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	         @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
	         Model model) throws Exception {

	     // Define as datas padrão caso nenhuma data seja fornecida
	     if (startDate == null) {
	         startDate = LocalDate.now().minusWeeks(1);
	     }
	     if (endDate == null) {
	         endDate = LocalDate.now();
	     }

	     // Obtém dados do primeiro gráfico (produtos)
	     List<ProdutoPesoDTO> produtosDTO = itemDeNotaService.getTop5ProdutosByPeso(startDate, endDate);

	     // Obtém dados do segundo gráfico (subgrupos)
	     List<SubgrupoPesoDTO> subgrupoDTO = itemDeNotaService.getTop5SubgruposByPeso(startDate, endDate);

	     Double volumeTotal = itemDeNotaService.findVolumeTotalEntreDatas(startDate, endDate);
	     
	     // Converte os dados em JSON
	     ObjectMapper objectMapper = new ObjectMapper();
	     String produtosJson = objectMapper.writeValueAsString(produtosDTO);
	     String subgrupoJson = objectMapper.writeValueAsString(subgrupoDTO);

	     // Adiciona os dados e datas ao modelo
	     model.addAttribute("produtosDTO", produtosJson);
	     model.addAttribute("subgrupoDTO", subgrupoJson);
	     model.addAttribute("startDate", startDate);
	     model.addAttribute("endDate", endDate);
	     model.addAttribute("volumeTotal", volumeTotal);

	     return "graficos/grafico_combinado";  // Nome do template Thymeleaf
	 }
	 
	 @GetMapping("produtos/graficos1")
	 public String mostrarGraficoProdutos1(
	         @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	         @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
	         Model model) throws Exception {

	     // Definindo as datas padrão
	     if (startDate == null) {
	         startDate = LocalDate.now().minusWeeks(1); // Última semana como padrão
	     }
	     if (endDate == null) {
	         endDate = LocalDate.now(); // Data atual como padrão
	     }

	     List<ProdutoPesoDTO> produtosDTO = itemDeNotaService.getTop5ProdutosByPeso(startDate, endDate);
	    

	     ObjectMapper objectMapper = new ObjectMapper();
	     String produtosJson = objectMapper.writeValueAsString(produtosDTO);

	    
	     
	     // Adiciona as datas ao modelo
	     model.addAttribute("produtosDTO", produtosJson);
	     model.addAttribute("startDate", startDate);
	     model.addAttribute("endDate", endDate);

	     return "produto/grafico_produtos";
	 }
	 
	 
	 
	 @GetMapping("produtos/graficos2")
	 public String mostrarGraficosubgruopos(
	         @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	         @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
	         Model model) throws Exception {

	     // Definindo as datas padrão
	     if (startDate == null) {
	         startDate = LocalDate.now().minusWeeks(1); // Última semana como padrão
	     }
	     if (endDate == null) {
	         endDate = LocalDate.now(); // Data atual como padrão
	     }

	     List<SubgrupoPesoDTO> subgrupoDTO = itemDeNotaService.getAllSubgruposByPeso(startDate, endDate);

	     ObjectMapper objectMapper = new ObjectMapper();
	     String produtosJson = objectMapper.writeValueAsString(subgrupoDTO);

	     // Adiciona as datas ao modelo
	     model.addAttribute("subgrupoDTO", produtosJson);
	     model.addAttribute("startDate", startDate);
	     model.addAttribute("endDate", endDate);

	     return "subgrupo/subgrupo_grafico";
	 }
	 
	 @GetMapping("/produtos/grafico3")
	 public String mostrarGraficosCombinados(
	         @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	         @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
	         Model model) throws Exception {

	     // Define as datas padrão caso nenhuma data seja fornecida
	     if (startDate == null) {
	         startDate = LocalDate.now().minusWeeks(1);
	     }
	     if (endDate == null) {
	         endDate = LocalDate.now();
	     }

	     // Obtém dados do primeiro gráfico (produtos)
	     List<ProdutoPesoDTO> produtosDTO = itemDeNotaService.getTop5ProdutosByPeso(startDate, endDate);

	     // Obtém dados do segundo gráfico (subgrupos)
	     List<SubgrupoPesoDTO> subgrupoDTO = itemDeNotaService.getTop5SubgruposByPeso(startDate, endDate);

	     // Converte os dados em JSON
	     ObjectMapper objectMapper = new ObjectMapper();
	     String produtosJson = objectMapper.writeValueAsString(produtosDTO);
	     String subgrupoJson = objectMapper.writeValueAsString(subgrupoDTO);

	     // Adiciona os dados e datas ao modelo
	     model.addAttribute("produtosDTO", produtosJson);
	     model.addAttribute("subgrupoDTO", subgrupoJson);
	     model.addAttribute("startDate", startDate);
	     model.addAttribute("endDate", endDate);

	     return "graficos/grafico_combinado";  // Nome do template Thymeleaf
	 }
}
	
