package com.flc.springthymeleaf.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.flc.springthymeleaf.DTO.RelatorioMensalDto;
import com.flc.springthymeleaf.domain.Propriedade;

import com.flc.springthymeleaf.service.RelatorioCotacaoMensalService;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
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
	public void gerarRelatorio(
	    @RequestParam("file") MultipartFile file,
	    @RequestParam("mesAnoInicial") @DateTimeFormat(pattern = "yyyy-MM") String mesAnoInicialStr,
	    @RequestParam("quantidadeMeses") int quantidadeMeses,
	    HttpServletResponse response
	) throws IOException {
	    List<Integer> propriedadeIds = lerIdsDoCsv(file);

	    // Parse o mês e ano inicial
	    YearMonth mesAnoInicial = YearMonth.parse(mesAnoInicialStr);
	    LocalDate dataInicial = mesAnoInicial.atDay(1);

	    // Gerar o relatório com base nos parâmetros
	    List<RelatorioMensalDto> relatorio = relatorioMensalService.gerarRelatorioMensal(propriedadeIds, dataInicial, quantidadeMeses);

	    // Gerar PDF
	    response.setContentType("application/pdf");
	    response.setHeader("Content-Disposition", "attachment; filename=relatorioMensal.pdf");
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
        PageSize pageSize = PageSize.A4.rotate(); // Define o modo landscape
        Document document = new Document(pdf, pageSize);

        // Obter e ordenar os meses
        List<String> mesesOrdenados = relatorio.get(0).getMediasPorMes().keySet().stream()
            .sorted((m1, m2) -> {
                String[] parts1 = m1.split("-");
                String[] parts2 = m2.split("-");
                int year1 = Integer.parseInt(parts1[0]);
                int month1 = Integer.parseInt(parts1[1]);
                int year2 = Integer.parseInt(parts2[0]);
                int month2 = Integer.parseInt(parts2[1]);
                if (year1 != year2) {
                    return Integer.compare(year2, year1); // ordena por ano, descendente
                } else {
                    return Integer.compare(month2, month1); // ordena por mês, descendente
                }
            })
            .collect(Collectors.toList());

        // Definir a largura das colunas
        float[] columnWidths = new float[mesesOrdenados.size() + 1];
        columnWidths[0] = 200f; // largura da primeira coluna (produtos)
        for (int i = 1; i < columnWidths.length; i++) {
            columnWidths[i] = 100f; // largura das colunas de médias
        }

        // Criar tabela
        Table table = new Table(columnWidths);
        DeviceRgb customColor = new DeviceRgb(35, 107, 54); // RGB para verde
        DeviceRgb alternateRowColor = new DeviceRgb(173, 216, 230); // RGB para azul claro

        // Cabeçalho - Primeira célula "Produtos" e os meses nas colunas seguintes
        Cell headerCell = new Cell();
        headerCell.add(new Paragraph("Produtos").setFontSize(10).setBold().setFontColor(ColorConstants.WHITE));
        headerCell.setBackgroundColor(customColor); // Definindo a cor de fundo
        headerCell.setBorder(Border.NO_BORDER);
        table.addHeaderCell(headerCell);

        for (String mes : mesesOrdenados) {
            String[] parts = mes.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            String monthName = Month.of(month).getDisplayName(TextStyle.SHORT, new Locale("pt", "BR"));

            Cell monthHeaderCell = new Cell();
            monthHeaderCell.add(new Paragraph(monthName + "/" + year).setFontSize(10).setBold().setFontColor(ColorConstants.WHITE));
            monthHeaderCell.setBackgroundColor(customColor); // Definindo a cor de fundo
            monthHeaderCell.setBorder(Border.NO_BORDER);
            table.addHeaderCell(monthHeaderCell);
        }

        // Adicionar os produtos e suas médias
        for (int i = 0; i < relatorio.size(); i++) {
            RelatorioMensalDto dto = relatorio.get(i);

            // Determinar a cor de fundo da linha
            DeviceRgb rowColor = (i % 2 == 0) ? (DeviceRgb) ColorConstants.WHITE : alternateRowColor;

            // Primeira célula da linha (nome do produto)
            Cell productCell = new Cell().add(new Paragraph(dto.getPropriedade().getProduto().getNome() + " - " + dto.getPropriedade().getVariedade() + " - " +dto.getPropriedade().getSubvariedade()).setFontSize(8));
            productCell.setBackgroundColor(rowColor);
            productCell.setBorder(Border.NO_BORDER);
            table.addCell(productCell);

            // Colunas das médias
            for (String mes : mesesOrdenados) {
                BigDecimal media = dto.getMediasPorMes().get(mes);
                Cell mediaCell = new Cell().add(new Paragraph(media != null ? media.toString() : "N/A").setFontSize(10));
                mediaCell.setBackgroundColor(rowColor);
                mediaCell.setBorder(Border.NO_BORDER);
                table.addCell(mediaCell);
            }
        }

        document.add(table);
        document.close();
    }

}