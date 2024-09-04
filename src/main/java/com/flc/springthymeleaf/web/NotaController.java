// NOTA CONTROLLER 2
package com.flc.springthymeleaf.web;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flc.springthymeleaf.domain.Embalagem;
import com.flc.springthymeleaf.domain.ItemDeNota;
import com.flc.springthymeleaf.domain.Municipio;
import com.flc.springthymeleaf.domain.Nota;
import com.flc.springthymeleaf.domain.Propriedade;
import com.flc.springthymeleaf.enums.FaixaHorario;
import com.flc.springthymeleaf.enums.LocalDestino;
import com.flc.springthymeleaf.enums.Portaria;
import com.flc.springthymeleaf.enums.TipoVeiculo;
import com.flc.springthymeleaf.enums.UnidadeMedida;
import com.flc.springthymeleaf.service.EmbalagemService;
import com.flc.springthymeleaf.service.MunicipioService;
import com.flc.springthymeleaf.service.NotaService;
import com.flc.springthymeleaf.service.PropriedadeService;

import jakarta.validation.Valid;

@Controller
public class NotaController {

    private final NotaService notaService;
    private final MunicipioService municipioService;
    private final PropriedadeService propriedadeService;
    private EmbalagemService embalagemService;

    public NotaController(NotaService notaService, MunicipioService municipioService, 
                          PropriedadeService propriedadeService, EmbalagemService embalagemService) {
        this.notaService = notaService;
        this.municipioService = municipioService;
        this.propriedadeService = propriedadeService;
        this.embalagemService = embalagemService;
    }

    @ModelAttribute("embalagens")
    public List<Embalagem> listaEmbalagens() {
        return embalagemService.findAll();
    }
    
    @ModelAttribute("municipios")
    public List<Municipio> listaMunicipios() {
        return municipioService.findAll();
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
    public List<String> listaTiposVeiculos() {
        return Arrays.stream(TipoVeiculo.values())
                     .map(tv -> tv.getCodigo() + " - " + tv.getDescricao())
                     .collect(Collectors.toList());
    }

    @ModelAttribute("locaisDestinos")
    public List<String> listaLocaisDestinos() {
    	
  
        return Arrays.stream(LocalDestino.values())
        		.map(ld -> ld.getCodigo() + " - " + ld.getDescricao())
        		.collect(Collectors.toList());
    }

  

    @GetMapping("/notas/cadastrar")
    public String cadastrar(Model model) {
        Nota nota = new Nota();
        nota.setItens(List.of(new ItemDeNota())); // Adiciona um item vazio para a nota
        model.addAttribute("nota", nota);
        return "nota/nota_cadastro";
    }

    @PostMapping("/notas/salvar")
    public String salvarNota(@Valid @ModelAttribute("nota") Nota nota, BindingResult result, RedirectAttributes attr) {
        // Log para depuração
        System.out.println("Iniciando o salvamento da nota");

        if (result.hasErrors()) {
            // Log para depuração
            System.out.println("Erro de validação encontrado: " + result.getAllErrors());
            return "nota/nota_cadastro";
        }

        // Buscar o municipio pelo IBGE para garantir que é uma entidade gerenciada
        Municipio municipio = municipioService.findById(nota.getMunicipio().getId()).orElse(null);
        if (municipio == null) {
            result.rejectValue("municipio", "error.nota", "Município não encontrado.");
            // Log para depuração
            System.out.println("Município não encontrado: " + nota.getMunicipio().getIbge());
            return "nota/nota_cadastro";
        }
        nota.setMunicipio(municipio);

        // Remover itens não preenchidos e buscar propriedades do banco
        nota.getItens().removeIf(item -> item.getPropriedade() == null || item.getQuantidade() == null || item.getEmbalagem() == null );

        for (ItemDeNota item : nota.getItens()) {
            Propriedade propriedade = propriedadeService.findById(item.getPropriedade().getId()).orElse(null);
            if (propriedade == null) {
                result.rejectValue("itens", "error.nota", "Propriedade não encontrada.");
                // Log para depuração
                System.out.println("Propriedade não encontrada: " + item.getPropriedade().getId());
                return "nota/nota_cadastro";
            }
            item.setPropriedade(propriedade);
            item.setNota(nota);
        }

        // Log para depuração
        System.out.println("Nota pronta para salvar: " + nota);
        System.out.println("Municipio associado: " + nota.getMunicipio());
        for (ItemDeNota item : nota.getItens()) {
            System.out.println("Item: " + item);
        }

        // Salvar a nota
        notaService.save(nota);
        attr.addFlashAttribute("success", "Nota preparada com sucesso! Verifique os logs para detalhes.");
        return "redirect:/notas/cadastrar";
    }
    @GetMapping("/notas/listar")
    public String listarNotas(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Nota> notas = notaService.findAll(PageRequest.of(page, 10));
        model.addAttribute("notas", notas);
        return "nota/nota_listagem";
    }

    @GetMapping("/notas/editar/{id}")
    public String editarNota(@PathVariable Integer id, Model model) {
        Nota nota = notaService.findById(id).orElseThrow(() -> new IllegalArgumentException("Nota inválida: " + id));
        model.addAttribute("nota", nota);
        return "nota/nota_editar";
    }
    
    // Mapeamento para requisições POST
    @PostMapping("/notas/excluir/{id}")
    public String excluirNotaPost(@PathVariable("id") Integer id, RedirectAttributes attr) {
        return excluirNota(id, attr);
    }

    // Mapeamento para requisições DELETE
    @DeleteMapping("/notas/excluir/{id}")
    public String excluirNotaDelete(@PathVariable("id") Integer id, RedirectAttributes attr) {
        return excluirNota(id, attr);
    }

    private String excluirNota(Integer id, RedirectAttributes attr) {
        try {
            notaService.deleteById(id);
            attr.addFlashAttribute("success", "Nota excluída com sucesso!");
        } catch (DataIntegrityViolationException e) {
            attr.addFlashAttribute("error", "Erro ao excluir a nota. A nota possui itens associados.");
        } catch (Exception e) {
            attr.addFlashAttribute("error", "Erro ao excluir a nota: " + e.getMessage());
        }
        return "redirect:/notas/listar";
    }

    @GetMapping("/notas/search")
    @ResponseBody
    public List<Propriedade> searchPropriedades(@RequestParam("query") String query) {
        return propriedadeService.searchByQuery(query);
    }
    
    @GetMapping("/notas/searchPropertyByProductName")
    public ResponseEntity<List<Propriedade>> searchPropertyByProductName(@RequestParam String productName) {
        List<Propriedade> propriedades = propriedadeService.findByProdutoNome(productName);
        
        return ResponseEntity.ok(propriedades);
    }
    
    @GetMapping("/notas/searchPropertyByCode")
    public ResponseEntity<?> searchPropertyByCode(@RequestParam String code) {
       
    	String codigoTrimmed = code.trim();
    	Propriedade propriedade = propriedadeService.findByCodigo(codigoTrimmed);
        if (propriedade != null) {
            return ResponseEntity.ok(propriedade);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Propriedade não encontrada.");
        }
    }

}

