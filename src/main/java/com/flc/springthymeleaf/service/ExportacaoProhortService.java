package com.flc.springthymeleaf.service;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.flc.springthymeleaf.repository.ItemDeNotaRepository;

@Service
public class ExportacaoProhortService {

    private final ItemDeNotaRepository itemDeNotaRepository;

    public ExportacaoProhortService(ItemDeNotaRepository itemDeNotaRepository) {
        this.itemDeNotaRepository = itemDeNotaRepository;
    }

    public List<Object[]> gerarArquivoProhort(int ano, int mes) {
        // Executa a query para obter os resultados
        return itemDeNotaRepository.findDadosParaProhort(ano, mes);

        
      
        }

       
    
}

	
   
