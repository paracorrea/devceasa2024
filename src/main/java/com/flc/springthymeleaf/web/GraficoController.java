package com.flc.springthymeleaf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flc.springthymeleaf.domain.Cotacao;
import com.flc.springthymeleaf.domain.Propriedade;
import com.flc.springthymeleaf.service.CotacaoService;
import com.flc.springthymeleaf.service.PropriedadeService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class GraficoController {

    @Autowired
    private CotacaoService cotacaoService;

	@Autowired
	private PropriedadeService propriedadeService;
    
	@GetMapping("/graficos/produto_pesquisar")
	public String startGraficoProdutoPorCotacao(Model model) {
	    LocalDate endDate = LocalDate.now();
	    LocalDate startDate = endDate.minusDays(90);
	    
	    // Carrega e ordena as propriedades cotadas nos últimos 90 dias
	    List<Propriedade> propriedadesCotadas = propriedadeService.findPropriedadesComCotacaoNoPeriodo(startDate, endDate);
	    model.addAttribute("propriedadesCotadas", propriedadesCotadas);

	    // Define a primeira propriedade como padrão, se a lista não estiver vazia
	    if (!propriedadesCotadas.isEmpty()) {
	        Propriedade propriedadePadrao = propriedadesCotadas.get(0);
	        Map<String, Object> result = cotacaoService.getMediaSemanalPorProduto(propriedadePadrao.getId(), startDate, endDate);
	        model.addAttribute("result", result);
	        model.addAttribute("nomeProduto", propriedadePadrao.getProduto().getNome());
	    }

	    return "graficos/grafico_cotacao_produto";
	}

	@GetMapping("/graficos/produto_preco_dados")
	public String getGraficoVariacaoPreco(
	        @RequestParam("propriedadeId") Integer propriedadeId,
	        @RequestParam(defaultValue = "90") int periodo,
	        Model model, RedirectAttributes attr) {

	    LocalDate endDate = LocalDate.now();
	    LocalDate startDate = endDate.minusDays(periodo);

	    // Reutiliza a lista de propriedades
	    List<Propriedade> propriedadesCotadas = propriedadeService.findPropriedadesComCotacaoNoPeriodo(startDate, endDate);
	    model.addAttribute("propriedadesCotadas", propriedadesCotadas);

	    // Verifica se a propriedade selecionada existe e carrega o gráfico
	    Optional<Propriedade> propriedadeOpt = propriedadeService.findById(propriedadeId);
	    if (propriedadeOpt.isPresent()) {
	        Propriedade propriedade = propriedadeOpt.get();
	        String nomeProduto = propriedade.getProduto().getNome();

	        Map<String, Object> result = cotacaoService.getMediaSemanalPorProduto(propriedadeId, startDate, endDate);
	        model.addAttribute("result", result);
	        model.addAttribute("nomeProduto", nomeProduto);
	        model.addAttribute("periodo",periodo);
	    } else {
	        attr.addFlashAttribute("fail", "Propriedade não encontrada ou não selecionada.");
	        return "redirect:/graficos/produto_pesquisar";
	    }

	    return "graficos/grafico_cotacao_produto";
	}
}
