package com.flc.springthymeleaf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flc.springthymeleaf.service.ExportacaoProhortService;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;

@Controller
public class ExportacaoProhortController {

    @Autowired
    private ExportacaoProhortService exportacaoProhortService;

    // localhost:5001/exportacao/exportar?ano=2024&mes=10
    
    
    @GetMapping("/exportacao/abertura")
    public String aberturaProhort(Model model) {
    	
    	return "prohort/prohort_form_datas";
    }
    
    @GetMapping("/exportacao/exportar")
    public void exportarProhort(
            @RequestParam(value = "ano") int ano,
            @RequestParam(value = "mes") int mes,
            HttpServletResponse response) {

        List<Object[]> resultados = exportacaoProhortService.gerarArquivoProhort(ano, mes);
       
        if (ano < 2024) {
	         ano = 01;
	     }
	     if (mes < 01 && mes > 12) {
	         mes = 01;
	     }
        
        if (!resultados.isEmpty()) {
            System.out.println("A lista possui " + resultados.size() + " linhas");
        }
        
        resultados.sort(Comparator.comparing((Object[] linha) -> (String) linha[1])
                .thenComparing(linha -> (String) linha[0]));

        // Configuração do cabeçalho de resposta para download do arquivo
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=\"exportacao_prohort.txt\"");
        try (PrintWriter writer = response.getWriter()) {
            NumberFormat pesoFormatter = NumberFormat.getIntegerInstance(); // Formata peso sem decimais
            DecimalFormat precoFormatter = new DecimalFormat("#,##0.00");  // Formata preço com duas casas decimais

            for (Object[] linha : resultados) {
                String nomeProduto = (String) linha[0];
                String codigoMunicipio = (String) linha[1];
                Double pesoTotal = (Double) linha[2];
                BigDecimal precoMedio = (BigDecimal) linha[3];

                // Formata os valores
                String pesoTotalFormatado = pesoFormatter.format(pesoTotal);
                String precoMedioFormatado = precoFormatter.format(precoMedio);

                // Monta a linha de dados para o arquivo
                String linhaTxt = "343" + ";" + ano + ";" + mes + ";" + nomeProduto + ";" + 
                                  codigoMunicipio + ";" + "365" + ";" + pesoTotalFormatado + ";" + precoMedioFormatado;
                writer.println(linhaTxt); // Escreve no arquivo
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

