package com.flc.springthymeleaf.service;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;
import com.flc.springthymeleaf.repository.ItemDeNotaRepository;

@Service
public class ExportacaoProhortService {

    private final ItemDeNotaRepository itemDeNotaRepository;

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

        // Busca os produtos sem cotações
        List<Object[]> produtosSemCotacao = itemDeNotaRepository.findProdutosSemCotacao(ano, mes);

        for (Object[] produto : produtosSemCotacao) {
            String codigoPropriedade = (String) produto[0];
            BigDecimal precoMedio = BigDecimal.ZERO;
            

            // Busca cotações relacionadas (códigos derivados)
            Object[] cotaçõesRelacionadas = itemDeNotaRepository.findCotaçõesRelacionadas(codigoPropriedade, ano, mes);

            if (cotaçõesRelacionadas != null && cotaçõesRelacionadas.length == 2) {
                BigDecimal media = cotaçõesRelacionadas[0] != null 
                    ? new BigDecimal(cotaçõesRelacionadas[0].toString()) 
                    : BigDecimal.ZERO;

                BigDecimal volume = cotaçõesRelacionadas[1] != null 
                    ? new BigDecimal(cotaçõesRelacionadas[1].toString()) 
                    : BigDecimal.ZERO;

                if (volume.compareTo(BigDecimal.ZERO) > 0) {
                    // Define os limites de 20% acima e abaixo
                    BigDecimal limiteSuperior = media.multiply(BigDecimal.valueOf(1.2));
                    BigDecimal limiteInferior = media.multiply(BigDecimal.valueOf(0.8));

                    // Verifica se a média está dentro dos limites
                    if (media.compareTo(limiteInferior) >= 0 && media.compareTo(limiteSuperior) <= 0) {
                        precoMedio = media;
                    }
                }
            }

            // Atualiza o preço médio no produto sem cotação
            produto[3] = precoMedio;
        }

        
        // Combina produtos com e sem cotação
        produtosComCotacao.addAll(produtosSemCotacao);
        return produtosComCotacao;
    }

    
}

	
   
