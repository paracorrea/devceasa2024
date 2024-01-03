package com.flc.springthymeleaf.web;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flc.springthymeleaf.domain.Cotacao;
import com.flc.springthymeleaf.domain.Propriedade;
import com.flc.springthymeleaf.service.CotacaoService;
import com.flc.springthymeleaf.service.PropriedadeService;
import com.flc.springthymeleaf.web.validator.CotacaoValidator;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import enums.FatorSazonal;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;



@Controller
public class CotacaoController {

	
	@Autowired
	private CotacaoService cotacaoService;
	
	@Autowired
	private PropriedadeService propriedadeService;
	
	
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
			
		binder.addValidators(new CotacaoValidator(cotacaoService));
	}
	
	@ModelAttribute("cotados")
	public List<Propriedade> listarCotados() {
		
		List<Propriedade> lista = propriedadeService.findPropriedadePorCotacao();
		
		  Collections.sort(lista, (p1, p2) -> p1.getProduto().getNome().compareToIgnoreCase(p2.getProduto().getNome()));

	
		return lista;
	}
	
	@ModelAttribute("fatores")
	public FatorSazonal[] getFatores() {
		return FatorSazonal.values();

	}
	
	@GetMapping("/cotacoes/cadastrar")
	public String cadastrar(Cotacao cotacao, Model model) {
		
		
		LocalDate dataCotacao = LocalDate.now();
		
		
		List<Cotacao> lista = cotacaoService.findAll();
		
		model.addAttribute("dataCotacao", dataCotacao);
		model.addAttribute("cotacoes", lista);
				
		
		
		return "cotacao/cotacao_cadastro";
	}
	
	@PostMapping("/cotacoes/salvar")
	public String salvar(@Valid @ModelAttribute Cotacao cotacao, Model model,BindingResult result, RedirectAttributes attr ) {
		
		LocalDate dataAtual = LocalDate.now();
		 
		   if (cotacao.getDataCotacao().isAfter(dataAtual)) {
			    	result.rejectValue("dataCotacao", "error.cotacao", "A data da cotação deve menor ou igual a data atual. Data atual: " + dataAtual + ", Data informada: " + cotacao.getDataCotacao());
		   } 	
		 
	        if (cotacao.getDataCotacao() != null && cotacao.getPropriedade() != null &&
	                cotacaoService.existeCotacaoComMesmaDataECategoria(cotacao)) {
	            result.rejectValue("dataCotacao", "error.cotacao", "Já existe uma cotação para a mesma propriedade na mesma data.");
	        }
		 
		if (result.hasErrors()) {
			return "cotacao/cotacao_cadastro";
		}
		
		cotacaoService.insert(cotacao);
		attr.addFlashAttribute("success", "Cotação cadastrada com sucesso");
		return "redirect:/cotacoes/cadastrar";
		}
	
	@GetMapping("/cotacoes/editar/{id}")
	public String preEditar(@PathVariable("id") Integer id, Model model) {
		
	
		model.addAttribute("cotacao", cotacaoService.findById(id));
		// model retirado do código por não ser mais necessário
		//model.addAttribute("cotados", propriedadeService.findPropriedadePorCotacao());
		return "cotacao/cotacao_editar";
	}
	
	@PostMapping("/cotacoes/editar") 
	public String editar (@Valid Cotacao cotacao, BindingResult result, RedirectAttributes attr) {
		
		
		
		LocalDate dataAtual = LocalDate.now();
		
		if (cotacao.getDataCotacao().isAfter(dataAtual)) {
		    	result.rejectValue("dataCotacao", "error.cotacao", "A data da cotação deve menor ou igual a data atual. Data atual: " + dataAtual + ", Data informada: " + cotacao.getDataCotacao());
	   } 	
	 
		
		if (result.hasErrors()) {
			return "cotacao/cotacao_editar";
		}
		
		cotacaoService.update(cotacao);
		attr.addFlashAttribute("success", "Cotação editada com sucesso");
		
		return "redirect:/cotacoes/por-data";
	}
	
	@GetMapping("/cotacoes/excluir/{id}")
	public String excluir(@PathVariable("id") Integer id, RedirectAttributes attr) {

		
		
			cotacaoService.delete(id);
			attr.addFlashAttribute("success","Cotação excluída com sucesso");		
			return "redirect:/cotacoes/por-data";
		} 
	
	
	@GetMapping("/cotacoes/listar")
	public String findAll(ModelMap model) {
		
		
		
		List<Cotacao> list = cotacaoService.findAll();
		model.addAttribute("cotacoes",list);
		
		return "cotacao/cotacao_listar";
	

	}
	
    @GetMapping("/cotacoes/por-data")
    public String pesquisarCotacoesPorData(@ModelAttribute("cotacao") Cotacao cotacao, Model model) {
        LocalDate selectedDate = cotacao.getDataCotacao();
        List<Cotacao> cotacaoResults = cotacaoService.getCotationsByDate(selectedDate);
       
        Collections.sort(cotacaoResults, Comparator.comparing(c -> c.getPropriedade().getProduto().getNome()));
        
        model.addAttribute("cotacaoResults", cotacaoResults);
        return "cotacao/cotacao_listagemdata";
    }

    @GetMapping("/cotacoes/gerar-pdf")
    public void gerarPdf(@RequestParam("dataCotacao") @DateTimeFormat(iso = ISO.DATE) LocalDate dataCotacao,
                         Cotacao cotacao, HttpServletResponse response) throws IOException {
    						response.setContentType("application/pdf");
    						response.setHeader("Content-Disposition", "attachment; filename=cotacao.pdf");

        // Certifique-se de carregar a lista de cotacoes usando o mesmo serviço utilizado na pesquisa
        LocalDate selectedDate = dataCotacao;
        List<Cotacao> cotacaoResults = cotacaoService.getCotationsByDate(selectedDate);
        Collections.sort(cotacaoResults, Comparator.comparing(c -> c.getPropriedade().getProduto().getNome()));
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
       
       // String logoPath = "/static/image/loginho.png";
       // PdfTableBuilder.addLogo(document, logoPath, 150, 50);
        
       

        String dataCabecalho = dataCotacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    
        
         

        // Adiciona os dados ao PDF
        document.add(new Paragraph("Formulário de Cotação CEASA CAMPINAS").setFontSize(7));
        document.add(new Paragraph("Cotação Realizada em: " + dataCabecalho).setFontSize(7));
  
      

        // Define estilos para a tabela
       
        Style cellStyle = new Style().setFontSize(5).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER);

        float[] columnWidths = {50f, 50f, 100f, 100f, 100f, 100f, 100f};
        Table table = new Table(columnWidths);
      
        String[] headers = {"Produto", "Variedade", "SubVariedade", "Classificação", "Valor Mínimo", "Valor Médio", "Valor Máximo"};
        for (String header : headers) {
            table.addHeaderCell(new Cell().add(new Paragraph(header).setFontSize(6).setBold()).setBorder(Border.NO_BORDER));
        }
        
        for (Cotacao cotacaoItem : cotacaoResults) {
            // Verifica se a data não é nula antes de formatar
            if (cotacaoItem.getDataCotacao() != null) {
                String produto = cotacaoItem.getPropriedade().getProduto().getNome();
                String variedade = cotacaoItem.getPropriedade().getVariedade();
                String subvariedade = cotacaoItem.getPropriedade().getSubvariedade();
                String classificacao = cotacaoItem.getPropriedade().getClassificacao();
                String valorMinimo = cotacaoItem.getPrecoMinimo().toString();
                String valorMedio = cotacaoItem.getPrecoMedio().toString();
                String valorMaximo = cotacaoItem.getPrecoMaximo().toString();

                // Adiciona células à tabela
                addCell(table, produto, cellStyle);
                addCell(table, variedade, cellStyle);
                addCell(table, subvariedade, cellStyle);
                addCell(table, classificacao, cellStyle);
                addCell(table, valorMinimo, cellStyle);
                addCell(table, valorMedio, cellStyle);
                addCell(table, valorMaximo, cellStyle);
            } else {
                document.add(new Paragraph("Data: [Data não disponível]"));
                document.add(new Paragraph("Cotação nula encontrada!"));
                
            }
        }

        // Adiciona a tabela ao documento fora do loop
        document.add(table);

       
        document.close();
    }

	private void addCell(Table table, String content, Style style) {
        table.addCell(new Cell().add(new Paragraph(content).addStyle(style)));
    }
}


