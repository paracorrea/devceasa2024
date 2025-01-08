package com.flc.springthymeleaf.service;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import com.flc.springthymeleaf.repository.ItemDeNotaRepository;
import com.flc.springthymeleaf.web.CotacaoController;

@Service
public class ExportacaoProhortService {

    private final ItemDeNotaRepository itemDeNotaRepository;

    
    private static final Logger LOGGER = Logger.getLogger(ExportacaoProhortService.class.getName());
    
    
    public ExportacaoProhortService(ItemDeNotaRepository itemDeNotaRepository) {
        this.itemDeNotaRepository = itemDeNotaRepository;
    }

    public List<Object[]> gerarArquivoProhort1(int ano, int mes) {
        // Executa a query para obter os resultados
        return itemDeNotaRepository.findDadosParaProhort(ano, mes);

         }

    
    
    public List<Object[]> gerarArquivoProhort(int ano, int mes) {
        // Busca os produtos com cotações
        List<Object[]> produtosComCotacao = itemDeNotaRepository.findDadosParaProhort(ano, mes);
        LOGGER.info("Produtos com Cotação: {}"+ produtosComCotacao);
        // Busca os produtos sem cotações
        List<Object[]> produtosSemCotacao = itemDeNotaRepository.findProdutosSemCotacao(ano, mes);
        LOGGER.info("Produtos sem Cotação: {}"+ produtosSemCotacao);
        for (Object[] produto : produtosSemCotacao) {
            String codigoPropriedade = (String) produto[0];
            BigDecimal precoMedio = BigDecimal.ZERO;

            // Busca a média dos valores relacionados
            Object[] cotaçõesRelacionadas = itemDeNotaRepository.findCotaçõesRelacionadasReferenciais(codigoPropriedade, ano, mes);

            if (cotaçõesRelacionadas != null && cotaçõesRelacionadas.length == 1) {
                BigDecimal mediaValorComum = cotaçõesRelacionadas[0] != null 
                    ? new BigDecimal(cotaçõesRelacionadas[0].toString()) 
                    : BigDecimal.ZERO;

                if (mediaValorComum.compareTo(BigDecimal.ZERO) > 0) {
                    precoMedio = mediaValorComum; // Apenas atribuímos o valor médio diretamente
                }
            }

            // Atualiza o preço médio no produto sem cotação
            produto[3] = precoMedio;
            LOGGER.info("Produto atualizado sem cotação: {}"+ Arrays.toString(produto));
        }

        // Combina produtos com e sem cotação
        produtosComCotacao.addAll(produtosSemCotacao);
        return produtosComCotacao;
    }

	public List<Object[]> gerarArquivoProhortPorDia(int ano, int mes, int dia) {
		// TODO Auto-generated method stub
		  List<Object[]> produtosComCotacao = itemDeNotaRepository.findDadosParaProhortPorDia(ano, mes, dia);
	        LOGGER.info("Produtos com Cotação: {}"+ produtosComCotacao);
	        // Busca os produtos sem cotações
	        List<Object[]> produtosSemCotacao = itemDeNotaRepository.findProdutosSemCotacao(ano, mes);
	        LOGGER.info("Produtos sem Cotação: {}"+ produtosSemCotacao);
	        for (Object[] produto : produtosSemCotacao) {
	            String codigoPropriedade = (String) produto[0];
	            BigDecimal precoMedio = BigDecimal.ZERO;

	            // Busca a média dos valores relacionados
	            Object[] cotaçõesRelacionadas = itemDeNotaRepository.findCotaçõesRelacionadasReferenciais(codigoPropriedade, ano, mes);

	            if (cotaçõesRelacionadas != null && cotaçõesRelacionadas.length == 1) {
	                BigDecimal mediaValorComum = cotaçõesRelacionadas[0] != null 
	                    ? new BigDecimal(cotaçõesRelacionadas[0].toString()) 
	                    : BigDecimal.ZERO;

	                if (mediaValorComum.compareTo(BigDecimal.ZERO) > 0) {
	                    precoMedio = mediaValorComum; // Apenas atribuímos o valor médio diretamente
	                }
	            }

	            // Atualiza o preço médio no produto sem cotação
	            produto[3] = precoMedio;
	            LOGGER.info("Produto atualizado sem cotação: {}"+ Arrays.toString(produto));
	        }

	        // Combina produtos com e sem cotação
	        produtosComCotacao.addAll(produtosSemCotacao);
	        return produtosComCotacao;
	}

   

    
}

	
   
