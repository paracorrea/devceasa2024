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
    	
    	return "graficos/pesquisar_produtos";
    }
    
    @GetMapping("/graficos/produtos_preco")
    public String getGraficoVariacaoPreco(
            @RequestParam("propriedadeId")Integer propriedadeID,  
            @RequestParam(defaultValue = "90") int periodo, Model model, Cotacao cotacao, RedirectAttributes attr ) {

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minus(periodo, ChronoUnit.DAYS);

        System.out.println("Data de início: " + startDate);
        System.out.println("Data de término: " + endDate);
        
        // obter o código e jogar em codigoPropriedade
        Optional<Propriedade> propriedadeOpt = propriedadeService.findById(propriedadeID);
        
        if (propriedadeOpt.isPresent()) {
	                   	
        	Propriedade propriedade = propriedadeOpt.get();
        	String nomeProduto = propriedade.getProduto().getNome();
        	System.out.println("Codigo da Propriedade: "+ propriedade.getCodigo());
        	System.out.println("Nome do Produto " +nomeProduto);
        	
        	 Map<String, Object> result = cotacaoService.getMediaSemanalPorProduto(propriedadeID, startDate, endDate);
        	 
        	 
        	 System.out.println(result);
        	 model.addAttribute("result", result);
        }	
        	else { attr.addFlashAttribute("fail", "Não localizado ou não escolhido um produto");
        		return null;
        	}
		return "graficos/grafico_cotacao_produto";
        
        		
       
    }
}
