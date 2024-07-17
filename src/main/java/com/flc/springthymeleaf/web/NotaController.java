package com.flc.springthymeleaf.web;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

import com.flc.springthymeleaf.domain.ItemDeNota;
import com.flc.springthymeleaf.domain.Municipio;
import com.flc.springthymeleaf.domain.Nota;
import com.flc.springthymeleaf.domain.Permissionario;
import com.flc.springthymeleaf.domain.Produto;
import com.flc.springthymeleaf.domain.Propriedade;
import com.flc.springthymeleaf.enums.FaixaHorario;
import com.flc.springthymeleaf.enums.LocalDestino;
import com.flc.springthymeleaf.enums.Portaria;
import com.flc.springthymeleaf.enums.TipoVeiculo;

import com.flc.springthymeleaf.service.MunicipioService;
import com.flc.springthymeleaf.service.NotaService;
import com.flc.springthymeleaf.service.PermissionarioService;
import com.flc.springthymeleaf.service.ProdutoService;
import com.flc.springthymeleaf.service.PropriedadeService;

import jakarta.validation.Valid;

@Controller
public class NotaController {

	private final NotaService notaService;
	private final MunicipioService municipioService;
	private final PermissionarioService permissionarioService;
	private final PropriedadeService propriedadeService;
	

	public NotaController(NotaService notaService, MunicipioService municipioService, PermissionarioService permissionarioService,
			PropriedadeService propriedadeService) {
		this.notaService = notaService;
		this.municipioService = municipioService;
		this.permissionarioService = permissionarioService;
		this.propriedadeService = propriedadeService;
		
	}

	@ModelAttribute("municipios")
	public List<Municipio> listaMunicipios() {
		return municipioService.findAll();
	}

	@ModelAttribute("permissionarios")
	public List<Permissionario> listaPermissionarios() {
		return permissionarioService.findAll();
	}

	@ModelAttribute("propriedades")
	public List<Propriedade> listaPropriedades() {
	    return propriedadeService.findPropriedadesNotNull();
	}
	@ModelAttribute("portarias")
	public Portaria[] listaPortarias() {
		return Portaria.values();
	}

	@ModelAttribute("faixasHorarios")
	public FaixaHorario[] listaFaixasHorarios() {
		return FaixaHorario.values();
	}

	@ModelAttribute("tiposVeiculos")
	public TipoVeiculo[] listaTiposVeiculos() {
		return TipoVeiculo.values();
	}

	@ModelAttribute("locaisDestinos")
	public LocalDestino[] listaLocaisDestinos() {
		return LocalDestino.values();
	}


	@GetMapping("/notas/cadastrar")
	public String cadastrar(@RequestParam(defaultValue = "0") int page, Model model) {
	    int pageSize = 10;  // Número de itens por página
	    Pageable pageable = PageRequest.of(page, pageSize);
	    Page<Nota> notaPage = notaService.findAll(pageable);

	    Nota nota = new Nota();
	    nota.getItens().add(new ItemDeNota()); // Adiciona um item à nota

	    model.addAttribute("notaPage", notaPage);
	    model.addAttribute("nota", nota); // Adiciona a nota inicializada
	    return "nota/nota_cadastro";
	}
	
	@PostMapping("/notas/salvar")
	public String salvarNota(@Valid @ModelAttribute("nota") Nota nota, BindingResult result, RedirectAttributes attr, Model model) {
	    if (result.hasErrors()) {
	        System.out.println("Erros de validação: " + result.getAllErrors());
	        Pageable pageable = PageRequest.of(0, 10);
	        Page<Nota> notaPage = notaService.findAll(pageable);
	        model.addAttribute("notaPage", notaPage);
	        return "nota/nota_cadastro";
	    }

	    System.out.println("IBGE do município: " + nota.getMunicipio().getIbge());
	    Municipio municipio = municipioService.findByIbge(nota.getMunicipio().getIbge());

	    if (municipio == null) {
	        result.rejectValue("municipio", "error.nota", "Município não encontrado.");
	        Pageable pageable = PageRequest.of(0, 10);
	        Page<Nota> notaPage = notaService.findAll(pageable);
	        model.addAttribute("notaPage", notaPage);
	        return "nota/nota_cadastro";
	    }
	    nota.setMunicipio(municipio);

	    for (ItemDeNota item : nota.getItens()) {
	        Optional<Propriedade> optionalPropriedade = propriedadeService.findById(item.getPropriedade().getId());
	        if (!optionalPropriedade.isPresent()) {
	            result.rejectValue("itens", "error.nota", "Propriedade não encontrada.");
	            Pageable pageable = PageRequest.of(0, 10);
	            Page<Nota> notaPage = notaService.findAll(pageable);
	            model.addAttribute("notaPage", notaPage);
	            return "nota/nota_cadastro";
	        }
	        item.setPropriedade(optionalPropriedade.get());
	        item.setNota(nota);  // Certifica que o item tem referência à nota
	    }

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
