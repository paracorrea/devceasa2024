package com.flc.springthymeleaf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flc.springthymeleaf.service.ExportacaoProhortService;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/exportacao")
public class ExportacaoProhortController {

    @Autowired
    private ExportacaoProhortService exportacaoProhortService;

    
   
    
    @GetMapping("/prohort")
    public void exportarProhort(
            @RequestParam("ano") int ano,
            @RequestParam("mes") int mes,
            HttpServletResponse response) {

        List<Object[]> resultados = exportacaoProhortService.gerarArquivoProhort(ano, mes);
        
        

        if (!resultados.isEmpty()) {
            System.out.println("A lista possui " + resultados.size() + " linhas");
        }
        
        resultados.sort(Comparator.comparing((Object[] linha) -> (String) linha[1])
                .thenComparing(linha -> (String) linha[0]));

        // Configuração do cabeçalho de resposta para download do arquivo
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=\"exportacao_prohort.txt\"");

        try (PrintWriter writer = response.getWriter()) {
            for (Object[] linha : resultados) {
                String nomeProduto = (String) linha[0];
                String codigoMunicipio = (String) linha[1];
                Double pesoTotal = (Double) linha[2];
                BigDecimal precoMedio = (BigDecimal) linha[3];

                // Monta a linha de dados para o arquivo
                String linhaTxt = nomeProduto + ";" + codigoMunicipio + ";" + pesoTotal + ";" + precoMedio;
                writer.println(linhaTxt); // Escreve no arquivo
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

