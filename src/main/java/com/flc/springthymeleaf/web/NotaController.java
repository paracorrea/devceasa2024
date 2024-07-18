// NOTA CONTROLLER 2
package com.flc.springthymeleaf.web;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.flc.springthymeleaf.domain.ItemDeNota;
import com.flc.springthymeleaf.domain.Municipio;
import com.flc.springthymeleaf.domain.Nota;
import com.flc.springthymeleaf.domain.Propriedade;
import com.flc.springthymeleaf.enums.FaixaHorario;
import com.flc.springthymeleaf.enums.LocalDestino;
import com.flc.springthymeleaf.enums.Portaria;
import com.flc.springthymeleaf.enums.TipoVeiculo;
import com.flc.springthymeleaf.enums.UnidadeMedida;
import com.flc.springthymeleaf.service.MunicipioService;
import com.flc.springthymeleaf.service.NotaService;
import com.flc.springthymeleaf.service.PropriedadeService;

import jakarta.validation.Valid;

@Controller
public class NotaController {

    private final NotaService notaService;
    private final MunicipioService municipioService;
    private final PropriedadeService propriedadeService;

    public NotaController(NotaService notaService, MunicipioService municipioService, 
                          PropriedadeService propriedadeService) {
        this.notaService = notaService;
        this.municipioService = municipioService;
        this.propriedadeService = propriedadeService;
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
    public TipoVeiculo[] listaTiposVeiculos() {
        return TipoVeiculo.values();
    }

    @ModelAttribute("locaisDestinos")
    public LocalDestino[] listaLocaisDestinos() {
        return LocalDestino.values();
    }

    @ModelAttribute("unidadesMedida")
    public UnidadeMedida[] listaUnidadesMedida() {
        return UnidadeMedida.values();
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
        if (result.hasErrors()) {
            return "nota/nota_cadastro";
        }

        // Buscar o municipio pelo IBGE para garantir que é uma entidade gerenciada
        Municipio municipio = municipioService.findByIbge(nota.getMunicipio().getIbge()).orElse(null);
        if (municipio == null) {
            result.rejectValue("municipio", "error.nota", "Município não encontrado.");
            return "nota/nota_cadastro";
        }
        nota.setMunicipio(municipio);

        // Remover itens não preenchidos e buscar propriedades do banco
        nota.getItens().removeIf(item -> item.getPropriedade() == null || item.getQuantidade() == null || item.getUnidadeMedida() == null);

        for (ItemDeNota item : nota.getItens()) {
            Propriedade propriedade = propriedadeService.findById(item.getPropriedade().getId()).orElse(null);
            if (propriedade == null) {
                result.rejectValue("itens", "error.nota", "Propriedade não encontrada.");
                return "nota/nota_cadastro";
            }
            item.setPropriedade(propriedade);
            item.setNota(nota);
        }

        // Logs para depuração
        System.out.println("Nota pronta para salvar: " + nota);
        System.out.println("Municipio associado: " + nota.getMunicipio());
        for (ItemDeNota item : nota.getItens()) {
            System.out.println("Item: " + item);
        }

        // Tentar salvar a nota novamente
        try {
            notaService.save(nota);
            attr.addFlashAttribute("success", "Nota cadastrada com sucesso!");
        } catch (Exception e) {
            // Capturar e logar a exceção
            System.err.println("Erro ao salvar a nota: " + e.getMessage());
            e.printStackTrace();
            attr.addFlashAttribute("error", "Erro ao salvar a nota. Verifique os logs para mais detalhes.");
        }
        
        return "redirect:/notas/cadastrar";
    }



}

