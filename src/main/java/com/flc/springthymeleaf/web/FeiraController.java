package com.flc.springthymeleaf.web;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.flc.springthymeleaf.domain.Feira;
import com.flc.springthymeleaf.service.FeiraService;
import enums.StatusFeira;
import jakarta.validation.Valid;

@Controller
public class FeiraController {
    private final FeiraService feiraService;

    public FeiraController(FeiraService feiraService) {
        this.feiraService = feiraService;
    }

    @GetMapping("/feiras/cadastrar")
    public String cadastrar(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 10;  // Número de itens por página
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Feira> feiraPage = feiraService.findAll(pageable);

        model.addAttribute("feiraPage", feiraPage);
        model.addAttribute("feira", new Feira());
        model.addAttribute("dataInicio", null); // Adicione null para distinguir a tela de cadastro
        model.addAttribute("dataFim", null);    // Adicione null para distinguir a tela de cadastro
        return "feira/feira_cadastro";
    }

    @PostMapping("/feiras/salvar")
    public String salvarFeira(@Valid @ModelAttribute("feira") Feira feira, BindingResult result, RedirectAttributes attr, Model model) {
        // Verifica se a data está presente
        if (feira.getDataFeira() == null) {
            result.rejectValue("dataFeira", "error.feira", "A data da feira é obrigatória.");
        } else {
            // Verifica se já existe uma feira com a mesma data
            Feira feiraExistente = feiraService.findByDataFeira(feira.getDataFeira());
            if (feiraExistente != null) {
                result.rejectValue("dataFeira", "error.feira", "Já existe uma feira marcada para esta data.");
            }

            // Verifica se a data da feira é posterior à data atual
            if (feira.getDataFeira().isAfter(LocalDate.now())) {
                result.rejectValue("dataFeira", "error.feira", "A data da feira não pode ser posterior à data atual.");
            }
        }

        // Define o status como ABERTA por padrão
        feira.setStatusFeira(StatusFeira.ABERTA);

        if (result.hasErrors()) {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Feira> feiraPage = feiraService.findAll(pageable);
            model.addAttribute("feiraPage", feiraPage);
            return "feira/feira_cadastro";
        }

        // Salva a feira se não houver erros
        feiraService.salvarFeira(feira);
        attr.addFlashAttribute("success", "Feira cadastrada com sucesso!");
        return "redirect:/feiras/cadastrar";
    }

    @PutMapping("/feiras/encerrar")
    public String encerrarFeira(@RequestParam Long id, RedirectAttributes attr) {
        Optional<Feira> feiraOptional = feiraService.findById(id);
        if (feiraOptional.isPresent()) {
            Feira feira = feiraOptional.get();
            feira.setStatusFeira(StatusFeira.FECHADA);
            feiraService.salvarFeira(feira);
            attr.addFlashAttribute("success", "Feira encerrada com sucesso!");
        } else {
            attr.addFlashAttribute("error", "Feira não encontrada!");
        }
        return "redirect:/feiras/cadastrar";
    }

    @PostMapping("/feiras/excluir")
    public String excluirFeira(@RequestParam Long id, RedirectAttributes attr) {
        Optional<Feira> feiraOptional = feiraService.findById(id);
        if (feiraOptional.isPresent()) {
            Feira feira = feiraOptional.get();
            if (feira.getStatusFeira() == StatusFeira.PUBLICADA || feira.getStatusFeira() == StatusFeira.ABERTA) {
                attr.addFlashAttribute("error", "Não é possível excluir uma feira que está aberta ou publicada.");
            } else {
                feiraService.excluirFeira(feira.getId());
                attr.addFlashAttribute("success", "Feira excluída com sucesso!");
            }
        } else {
            attr.addFlashAttribute("error", "Feira não encontrada!");
        }
        return "redirect:/feiras/cadastrar";
    }

    @GetMapping("/feiras/pesquisar")
    public String pesquisar(@RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
                            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
                            @RequestParam(defaultValue = "0") int page,
                            Model model) {
        int pageSize = 10;  // Número de itens por página
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Feira> feiraPage = feiraService.findByDataFeiraBetween(dataInicio, dataFim, pageable);

        model.addAttribute("feiraPage", feiraPage);
        model.addAttribute("feira", new Feira());
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);
        return "feira/feira_cadastro"; // Retorne para a mesma página com os resultados da pesquisa
    }
}