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

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class ExportacaoProhortController {

    @Autowired
    private ExportacaoProhortService exportacaoProhortService;

    // localhost:5001/exportacao/exportar?ano=2024&mes=10
    
    private static final Logger LOGGER = Logger.getLogger(ExportacaoProhortController.class.getName());
    
    
    @GetMapping("/exportacao/abertura")
    public String aberturaProhort(Model model) {
    	
    	return "prohort/prohort_form_datas";
    }
    
	
    @GetMapping("/exportacao/exportar")
    public void exportarProhort1(
            @RequestParam(value = "ano") int ano,
            @RequestParam(value = "mes") int mes,
            HttpServletResponse response) {

        List<Object[]> resultados = exportacaoProhortService.gerarArquivoProhort(ano, mes);

        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=\"exportacao_prohort.txt\"");
        try (PrintWriter writer = response.getWriter()) {
            DecimalFormat pesoFormatter = new DecimalFormat();
            DecimalFormat precoFormatter = new DecimalFormat("0.00");
            
            for (Object[] linha : resultados) {
                String nomeProduto = (String) linha[0];
                String codigoMunicipio = (String) linha[1];
                
                Double pesoTotal = (Double) linha[2];
                
                System.out.println("Peso total" + pesoTotal);
                
                BigDecimal precoMedio = (BigDecimal) linha[3];

                long pesoTotalFormatado = Math.round(pesoTotal);
                
                System.out.println("Peso total" + pesoTotalFormatado);
               String precoMedioFormatado = precoFormatter.format(precoMedio);
               
               LOGGER.info("Peso Total: {}, Preço Médio: {}"+ pesoTotal + " " + precoMedio);
               
               LOGGER.info("Produto com cotação: {}" + linha[0]);
               LOGGER.info("Produto com cotação: {}" + linha[1]);
               LOGGER.info("Produto com cotação: {}" + linha[2]);
               LOGGER.info("Produto com cotação: {}" + linha[3]);
               
               
              
                String linhaTxt = "343" + ";" + ano + ";" + mes + ";" + nomeProduto + ";" + 
                                  codigoMunicipio + ";" + "365" + ";" + pesoTotalFormatado + ";" + precoMedioFormatado;
                writer.println(linhaTxt);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    @GetMapping("/exportacao/teste-dia")
    public void exportarProhortPorDia(
    		 @RequestParam(value = "ano") int ano,
             @RequestParam(value = "mes") int mes,
             @RequestParam(value = "dia") int dia,
             HttpServletResponse response) {

         List<Object[]> resultados = exportacaoProhortService.gerarArquivoProhortPorDia(ano, mes, dia);

         response.setContentType("text/plain");
         response.setHeader("Content-Disposition", "attachment; filename=\"exportacao_prohort.txt\"");
         try (PrintWriter writer = response.getWriter()) {
             DecimalFormat pesoFormatter = new DecimalFormat();
             DecimalFormat precoFormatter = new DecimalFormat();

             for (Object[] linha : resultados) {
                 String nomeProduto = (String) linha[0];
                 String codigoMunicipio = (String) linha[1];
                 
                 Double pesoTotal = (Double) linha[2];
                 
                 System.out.println("Peso total" + pesoTotal);
                 
                 BigDecimal precoMedio = (BigDecimal) linha[3];

                 long pesoTotalFormatado = Math.round(pesoTotal);
                 
                 System.out.println("Peso total" + pesoTotalFormatado);
                String precoMedioFormatado = precoFormatter.format(precoMedio);
                
                LOGGER.info("Peso Total: {}, Preço Médio: {}"+ pesoTotal + " " + precoMedio);
                
                LOGGER.info("Produto com cotação: {}" + linha[0]);
                LOGGER.info("Produto com cotação: {}" + linha[1]);
                LOGGER.info("Produto com cotação: {}" + linha[2]);
                LOGGER.info("Produto com cotação: {}" + linha[3]);
                
                
               
                 String linhaTxt = "343" + ";" + ano + ";" + mes + ";" + dia + ";" + nomeProduto + ";" + 
                                   codigoMunicipio + ";" + "365" + ";" + pesoTotalFormatado + ";" + precoMedioFormatado;
                 writer.println(linhaTxt);
             }
             writer.flush();
         } catch (IOException e) {
             e.printStackTrace();
         }
    }

}

