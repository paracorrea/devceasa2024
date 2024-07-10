package com.flc.springthymeleaf.web;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flc.springthymeleaf.domain.Nota;

import com.flc.springthymeleaf.enums.FaixaHorario;
import com.flc.springthymeleaf.enums.TipoVeiculo;
import com.flc.springthymeleaf.enums.LocalDestino;
import com.flc.springthymeleaf.domain.Municipio;
import com.flc.springthymeleaf.domain.Permissionario;
import com.flc.springthymeleaf.service.MunicipioService;
import com.flc.springthymeleaf.service.PermissionarioService;

import enums.Portaria;

import com.flc.springthymeleaf.service.NotaService;

import jakarta.validation.Valid;

@Controller
public class NotaController {

    private final NotaService notaService;
    private final MunicipioService municipioService;
    private final PermissionarioService permissionarioService;

    public NotaController(NotaService notaService, MunicipioService municipioService, PermissionarioService permissionarioService) {
        this.notaService = notaService;
        this.municipioService = municipioService;
        this.permissionarioService = permissionarioService;
    }

    @GetMapping("/notas/cadastrar")
    public String cadastrar(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 10;  // Número de itens por página
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Nota> notaPage = notaService.findAll(pageable);

        List<Municipio> municipios = municipioService.findAll();
        List<Permissionario> permissionarios = permissionarioService.findAll();

        model.addAttribute("notaPage", notaPage);
        model.addAttribute("nota", new Nota());
        model.addAttribute("portarias", Portaria.values());
        model.addAttribute("faixasHorarios", FaixaHorario.values());
        model.addAttribute("tiposVeiculos", TipoVeiculo.values());
        model.addAttribute("locaisDestinos", LocalDestino.values());
        model.addAttribute("municipios", municipios);
        model.addAttribute("permissionarios", permissionarios);
        return "nota/nota_cadastro";
    }

    @PostMapping("/notas/salvar")
    public String salvarNota(@Valid @ModelAttribute("nota") Nota nota, BindingResult result, RedirectAttributes attr, Model model) {
               
        	
             

        // Salva a feira se não houver erros
        notaService.save(nota);
        attr.addFlashAttribute("success", "Nota cadastrada com sucesso!");
        return "redirect:/notas/cadastrar";
    }
}
