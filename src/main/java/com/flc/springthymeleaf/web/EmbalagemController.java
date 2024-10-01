package com.flc.springthymeleaf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flc.springthymeleaf.domain.Embalagem;
import com.flc.springthymeleaf.domain.Propriedade;
import com.flc.springthymeleaf.enums.TipoEmbalagem;
import com.flc.springthymeleaf.enums.UnidadeMedida;
import com.flc.springthymeleaf.service.EmbalagemService;
import com.flc.springthymeleaf.service.PropriedadeService;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
public class EmbalagemController {

    @Autowired
    private EmbalagemService embalagemService;
    
    @Autowired
    private PropriedadeService propriedadeService;

    @ModelAttribute("unidades")
    public UnidadeMedida[] listaUnidadesMedida() {
        return UnidadeMedida.values();
    }
    
    @ModelAttribute("tipos")
    public TipoEmbalagem[] listaTipoEmbalagem() {
        return TipoEmbalagem.values();
    }
    
    @GetMapping("/embalagens/listar")
    public String listarEmbalagens( Model model, Embalagem embalagem) {
    	
        List<Embalagem> embalagensResults = embalagemService.findAll();
        Collections.sort(embalagensResults, Comparator
        		.comparing((Embalagem e) -> e.getCodigo())
        		.thenComparing(e -> e.getCodigo()));
        
        model.addAttribute("embalagens", embalagensResults);
        return "embalagem/embalagem_lista";
    }

    @GetMapping("/embalagens/pesquisar")
    public String pesquisar(Model model) {
    	return "embalagem/embalagem_pesquisar_propriedade";
    }
    @GetMapping("/embalagens/associar")
    public String associarEmbalagemComPropriedade(@RequestParam("propriedadeId") Integer propriedadeId, Model model) {
        Optional<Propriedade> propriedadeOpt = propriedadeService.findById(propriedadeId);

        if (propriedadeOpt.isPresent()) {
            Propriedade propriedade = propriedadeOpt.get();

            // Carregar as embalagens e associá-las à propriedade
            List<Embalagem> embalagens = embalagemService.findAll();
            List<Embalagem> embalagensAssociadas = propriedade.getEmbalagens();
            
            model.addAttribute("propriedade", propriedade);
            model.addAttribute("embalagens", embalagens);
            model.addAttribute("embalagensAssociadas", embalagensAssociadas);
        }

        return "embalagem/embalagem_propriedade";
    }
    
    @PostMapping("/embalagens/associar")
    public String salvarAssociacaoEmbalagens(@RequestParam("propriedadeId") Integer propriedadeId, 
                                             @RequestParam("embalagens") List<Integer> embalagemIds,
                                             RedirectAttributes redirectAttributes) {
        // Buscar a propriedade
        Optional<Propriedade> propriedadeOpt = propriedadeService.findById(propriedadeId);
        if (propriedadeOpt.isPresent()) {
            Propriedade propriedade = propriedadeOpt.get();

            // Buscar as embalagens selecionadas
            List<Embalagem> embalagensSelecionadas = embalagemService.findAllById(embalagemIds);
            
            // Associar as embalagens à propriedade
            propriedade.setEmbalagens(embalagensSelecionadas);
            
            // Salvar a propriedade com as novas embalagens associadas
            propriedadeService.insert(propriedade);

            // Adicionar mensagem de sucesso
            redirectAttributes.addFlashAttribute("success", "Embalagens associadas com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Propriedade não encontrada.");
        }

        return "redirect:/embalagens/listar";
    }
    
    @GetMapping("/embalagens/nova")
    public String novaEmbalagem(@RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("embalagem", new Embalagem());
        return "embalagem/embalagem_cadastro";
    }

    @PostMapping("/embalagens/salvar")
    public String salvarEmbalagem(@Valid @ModelAttribute("embalagem") Embalagem embalagem, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "embalagem/embalagem_cadastro";
        }

        
        try {
            // Verifica se a embalagem existe no banco (baseado no ID)
            if (embalagem.getId() != null ) {
                // Realiza um update
                embalagemService.atualizarAtributos(embalagem);
            } else {
                // Realiza um insert
                embalagemService.salvarEmbalagem(embalagem);
            }
            attr.addFlashAttribute("success", "Embalagem salva com sucesso!");
        } catch (DataIntegrityViolationException e) {
            attr.addFlashAttribute("error", "Erro: Código já existe.");
            return "embalagem/embalagem_cadastro";
        }

        return "redirect:/embalagens/listar";
    }
   
    @GetMapping("/embalagens/editar/{id}")
    public String editarEmbalagem(@PathVariable("id") Integer id, Model model) {
        Optional<Embalagem> embalagemOpt = embalagemService.findById(id);
        if (embalagemOpt.isPresent()) {
            model.addAttribute("embalagem", embalagemOpt.get());
            return "embalagem/embalagem_cadastro";
        } else {
            return "redirect:/embalagens/listar";
        }
    }

    @GetMapping("/embalagens/excluir/{id}")
    public String excluirEmbalagem(@PathVariable("id") Integer id, RedirectAttributes attr) {
        embalagemService.excluirEmbalagem(id);
        attr.addFlashAttribute("success", "Embalagem excluída com sucesso!");
        return "redirect:/embalagens";
    }
    
    @GetMapping("/embalagens/search")
    @ResponseBody
    public List<Propriedade> searchPropriedades(@RequestParam("query") String query) {
        return propriedadeService.searchByQuery(query);
    }
    
    @GetMapping("/embalagens/searchPropertyByProductName")
    public ResponseEntity<List<Propriedade>> searchPropertyByProductName(@RequestParam String productName) {
        List<Propriedade> propriedades = propriedadeService.findByProdutoNome(productName);
        
        return ResponseEntity.ok(propriedades);
    }
    
    @GetMapping("/embalagens/searchPropertyByCode")
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
