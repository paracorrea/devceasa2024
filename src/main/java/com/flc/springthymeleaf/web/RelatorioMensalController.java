package com.flc.springthymeleaf.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.flc.springthymeleaf.DTO.RelatorioMensalDto;
import com.flc.springthymeleaf.domain.Propriedade;

import com.flc.springthymeleaf.service.RelatorioCotacaoMensalService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RelatorioMensalController {
	private static final Logger LOGGER = Logger.getLogger(CotacaoController.class.getName());

	@Autowired
	private RelatorioCotacaoMensalService relatorioMensalService;
	

	@GetMapping("cotacoes/relatorio-mensal")
	public String relatorio() {
		return "cotacao/relatorio_form";
	}
	
	// gerar relatorio mensal
    @PostMapping("/cotacoes/gerar-relatorio")
    public void gerarRelatorio(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        List<Integer> propriedadeIds = lerIdsDoCsv(file);
        
        // Agora vamos gerar o relatório
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=relatorioMensal.pdf");

        List<RelatorioMensalDto> relatorio = relatorioMensalService.gerarRelatorioMensal(propriedadeIds);
        gerarPdfRelatorio(response, relatorio);
    }
    
    private List<Integer> lerIdsDoCsv(MultipartFile file) throws IOException {
        List<Integer> ids = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                ids.add(Integer.parseInt(line.trim()));
            }
        }
        return ids;
    }
    
    private void gerarPdfRelatorio(HttpServletResponse response, List<RelatorioMensalDto> relatorio) throws IOException {
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        for (RelatorioMensalDto dto : relatorio) {
            Propriedade propriedade = dto.getPropriedade();
            if (propriedade != null) {
                document.add(new Paragraph(propriedade.getProduto().getNome() + " - " + propriedade.getVariedade()));

                Table table = new Table(new float[]{1, 1, 1});
                table.addHeaderCell("Mês");
                table.addHeaderCell("Média Valor Comum");

                for (Map.Entry<String, BigDecimal> entry : dto.getMediasPorMes().entrySet()) {
                    table.addCell(entry.getKey());
                    table.addCell(entry.getValue().toString());
                }

                document.add(table);
                document.add(new Paragraph("\n"));
            }
        }

        document.close();
    }

// término gerar relatório mensal
}
