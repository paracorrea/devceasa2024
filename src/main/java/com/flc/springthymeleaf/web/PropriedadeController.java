package com.flc.springthymeleaf.web;

import com.flc.springthymeleaf.domain.Cotacao;
import com.flc.springthymeleaf.domain.Produto;
import com.flc.springthymeleaf.domain.Propriedade;
import com.flc.springthymeleaf.service.ProdutoService;
import com.flc.springthymeleaf.service.PropriedadeService;
import com.flc.springthymeleaf.web.validator.PropriedadeValidator;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public String salvar(@Valid Propriedade propriedade, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "propriedade/propriedade_cadastro";
        }

        if (propriedadeService.existsByCodigo(propriedade.getCodigo())) {
            attr.addFlashAttribute("success", "Código já existente");
            return "propriedade/propriedade_cadastro";
        }

        propriedadeService.insert(propriedade);
        attr.addFlashAttribute("success", "Propriedade cadastrada com sucesso");

        return "redirect:/propriedades/cadastrar";
    }

    @GetMapping("/propriedades/editar/{id}")
    public String preEditar(@PathVariable("id") Integer id, ModelMap model) {
        Optional<Propriedade> propriedadeOptional = propriedadeService.findById(id);
        if (propriedadeOptional.isPresent()) {
            model.addAttribute("unidades", unidades);
            model.addAttribute("propriedade", propriedadeOptional.get());
            return "propriedade/propriedade_cadastro";
        } else {
            model.addAttribute("error", "Propriedade não encontrada");
            return "redirect:/propriedades/listar";
        }
    }
    

    @PostMapping("/propriedades/editar")
    public String editar(@Valid Propriedade propriedade, BindingResult result, RedirectAttributes attr) {
        propriedadeService.update(propriedade);
        attr.addFlashAttribute("success", "Propriedade editada com sucesso");
        return "redirect:/propriedades/cadastrar";
    }

    @PostMapping("/propriedades/excluir")
    public String excluir(@RequestParam Integer id, RedirectAttributes attr) {
        Optional<Propriedade> propriedadeOptional = propriedadeService.findById(id);
        if (propriedadeOptional.isPresent()) {
            propriedadeService.delete(id);
            attr.addFlashAttribute("success", "Propriedade excluída com sucesso");
        } else {
            attr.addFlashAttribute("error", "Propriedade não encontrada");
        }
        return "redirect:/propriedades/cadastrar";
    }

    @GetMapping("/propriedades/listar")
    public String findAll(ModelMap model) {
        List<Propriedade> listaPropriedade = propriedadeService.findAll();
        List<Propriedade> listaOrdenada = listaPropriedade.stream()
                .sorted((p1, p2) -> p1.getProduto().getNome().compareToIgnoreCase(p2.getProduto().getNome()))
                .collect(Collectors.toList());
        model.addAttribute("propriedades", listaOrdenada);
        return "propriedade/propriedade_listar";
    }

    @GetMapping("/propriedades/produtos/{id}")
    public String findPropriedadePorProduto(@PathVariable Integer id, ModelMap model) {
        List<Propriedade> obj = propriedadeService.findPropriedadePorProduto(id);
        model.addAttribute("propriedades", obj);
        return "propriedade/propriedade_listar";
    }

    @GetMapping("/propriedades/listar1")
    public ResponseEntity<List<Propriedade>> findAllRest() {
        List<Propriedade> listaPropriedade = propriedadeService.findAll();
        List<Propriedade> listaOrdenada = listaPropriedade.stream()
                .sorted((p1, p2) -> p1.getProduto().getNome().compareToIgnoreCase(p2.getProduto().getNome()))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(listaOrdenada);
    }

    @GetMapping("/propriedades/produtos1/{id}")
    public ResponseEntity<List<Propriedade>> findPropriedadePorProduto1(@PathVariable Integer id, ModelMap model) {
        List<Propriedade> obj = propriedadeService.findPropriedadePorProduto(id);
        model.addAttribute("propriedades", obj);
        return ResponseEntity.ok().body(obj);
    }
    
    @GetMapping("/propriedades/search")
    @ResponseBody
    public List<Propriedade> searchPropriedades(@RequestParam("query") String query) {
        return propriedadeService.searchByQuery(query);
    }
}
