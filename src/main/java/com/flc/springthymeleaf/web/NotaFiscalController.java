package com.flc.springthymeleaf.web;

import com.flc.springthymeleaf.domain.NotaFiscal;
import com.flc.springthymeleaf.service.NotaFiscalService;
import com.flc.springthymeleaf.service.MunicipioService;
import com.flc.springthymeleaf.service.MercadoService;
import com.flc.springthymeleaf.service.PermissionarioService;
import com.flc.springthymeleaf.web.validator.NotaFiscalValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/notas-fiscais")
public class NotaFiscalController {

    @Autowired
    private NotaFiscalService notaFiscalService;

    @Autowired
    private MunicipioService municipioService;

    @Autowired
    private MercadoService mercadoService;

    @Autowired
    private PermissionarioService permissionarioService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(new NotaFiscalValidator(notaFiscalService));
    }

    @GetMapping("/cadastrar")
    public String showForm(Model model) {
        model.addAttribute("notaFiscal", new NotaFiscal());
        model.addAttribute("municipios", municipioService.findAll());
        model.addAttribute("mercados", mercadoService.findAll());
        model.addAttribute("permissionarios", permissionarioService.findAll());
        return "notafiscal/notas_fiscais_cadastro";
    }

    @PostMapping("/salvar")
    public String saveNotaFiscal(@Valid @ModelAttribute("notaFiscal") NotaFiscal notaFiscal, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("municipios", municipioService.findAll());
            model.addAttribute("mercados", mercadoService.findAll());
            model.addAttribute("permissionarios", permissionarioService.findAll());
            return "notafiscal/notas_fiscais_cadastro";
        }

        notaFiscalService.save(notaFiscal);
        return "redirect:/notas-fiscais/cadastrar";
    }
}
