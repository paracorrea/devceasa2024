package com.flc.springthymeleaf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flc.springthymeleaf.domain.Embalagem;
import com.flc.springthymeleaf.enums.TipoEmbalagem;
import com.flc.springthymeleaf.enums.UnidadeMedida;
import com.flc.springthymeleaf.service.EmbalagemService;

import jakarta.validation.Valid;

import java.util.Optional;

@Controller
public class EmbalagemController {

    @Autowired
    private EmbalagemService embalagemService;

    @ModelAttribute("unidades")
    public UnidadeMedida[] listaUnidadesMedida() {
        return UnidadeMedida.values();
    }
    
    @ModelAttribute("tipos")
    public TipoEmbalagem[] listaTipoEmbalagem() {
        return TipoEmbalagem.values();
    }
    
    @GetMapping("/embalagens")
    public String listarEmbalagens(@RequestParam(defaultValue = "0") int page, Model model) {
    	  int pageSize = 10;  // Número de itens por página
          Pageable pageable = PageRequest.of(page, pageSize);
        Page<Embalagem> embalagens = embalagemService.findAll(pageable);
        model.addAttribute("embalagens", embalagens);
        return "embalagem/embalagem_lista";
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
            if (embalagem.getId() != null && embalagemService.findById(embalagem.getId()).isPresent()) {
                // Realiza um update
                embalagemService.salvarEmbalagem(embalagem);
            } else {
                // Realiza um insert
                embalagemService.salvarEmbalagem(embalagem);
            }
            attr.addFlashAttribute("success", "Embalagem salva com sucesso!");
        } catch (DataIntegrityViolationException e) {
            attr.addFlashAttribute("error", "Erro: Código já existe.");
            return "embalagem/embalagem_cadastro";
        }

        return "redirect:/embalagens";
    }
   
    @GetMapping("/embalagens/editar/{id}")
    public String editarEmbalagem(@PathVariable("id") Integer id, Model model) {
        Optional<Embalagem> embalagemOpt = embalagemService.findById(id);
        if (embalagemOpt.isPresent()) {
            model.addAttribute("embalagem", embalagemOpt.get());
            return "embalagem/embalagem_cadastro";
        } else {
            return "redirect:/embalagens";
        }
    }

    @GetMapping("/embalagens/excluir/{id}")
    public String excluirEmbalagem(@PathVariable("id") Integer id, RedirectAttributes attr) {
        embalagemService.excluirEmbalagem(id);
        attr.addFlashAttribute("success", "Embalagem excluída com sucesso!");
        return "redirect:/embalagens";
    }
}
