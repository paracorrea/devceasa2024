package com.flc.springthymeleaf.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.ResponseBody;
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

	//private static final Logger logger = LoggerFactory.getLogger(Cotacao.class);
	
	@Autowired
	private CotacaoService cotacaoService;
	
	@Autowired
	private PropriedadeService propriedadeService;
	
		@InitBinder
	public void initBinder(WebDataBinder binder) {
			
		binder.addValidators(new CotacaoValidator(cotacaoService));
	}
	
	@ModelAttribute("fatores")
	public FatorSazonal[] getFatores() {
		return FatorSazonal.values();
	}
	
	@ModelAttribute("cotados")
	public List<Propriedade> listarCotados() {
		
		List<Propriedade> listaCotados = propriedadeService.findPropriedadePorCotacao();
		Collections.sort(listaCotados, (p1, p2) -> p1.getProduto().getNome().compareToIgnoreCase(p2.getProduto().getNome()));
		return listaCotados;
	}
		
	@GetMapping("/cotacoes/buscar-cotacao-anterior")
	@ResponseBody
	public ResponseEntity<Cotacao> buscarCotacaoAnterior(
	         
	        @RequestParam Long propriedadeId,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataCotacao) {

		
	    Cotacao cotacaoAnterior = cotacaoService.buscarCotacaoAnterior1(propriedadeId, dataCotacao);

	    if (cotacaoAnterior != null) {
	       
	    	//BigDecimal valorMinimoAnterior = cotacaoAnterior.getPrecoMinimo();
	    	return ResponseEntity.ok(cotacaoAnterior);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
		 
	@GetMapping("/cotacoes/cadastrar")
	public String cadastrar(Cotacao cotacao, Model model) {
		
		List<Propriedade> listaCotados = listarCotados();
		LocalDate dataCotacao = LocalDate.now();
		model.addAttribute("dataCotacao", dataCotacao);
		model.addAttribute("cotacoes", listaCotados);
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
	public String preEditar(@PathVariable("id") Integer id,  Model model) {
		
		Cotacao cotacao = cotacaoService.findById(id);
		BigDecimal peso = cotacao.getPropriedade().getPeso();
		System.out.println("Propriedade Peso: " + peso);
		model.addAttribute("peso",peso );
		model.addAttribute("cotacao",cotacao);
				
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
       
        Collections.sort(cotacaoResults, Comparator.comparing(c -> c.getPropriedade().getProduto().getSubgrupo().getNome()));
        
        model.addAttribute("cotacaoResults", cotacaoResults);
        return "cotacao/cotacao_listagemdata";
    }

    @GetMapping("/cotacoes/por-feira/{dataFeira}")
    public String pesquisarCotacoesPorFeira(@PathVariable("dataFeira") @DateTimeFormat(iso = ISO.DATE) LocalDate dataFeira, Model model) {
        List<Cotacao> cotacaoResults = cotacaoService.getCotationsByDate(dataFeira);
        Collections.sort(cotacaoResults, Comparator.comparing(c -> c.getPropriedade().getProduto().getSubgrupo().getNome()));
        
        // Adiciona a data formatada para exibição no template (opcional, se necessário)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dataFeiraFormatada = dataFeira.format(formatter);
        model.addAttribute("dataFeiraFormatada", dataFeiraFormatada);

        model.addAttribute("cotacaoResults", cotacaoResults);
        return "cotacao/cotacao_por_feira";
    }
    
    @GetMapping("/cotacoes/gerar-pdf")
    public void gerarPdf(@RequestParam("dataCotacao") @DateTimeFormat(iso = ISO.DATE) LocalDate dataCotacao,
                         @RequestParam("numeroFeira") Long numeroFeira,
                         Cotacao cotacao, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=cotacao.pdf");

        // Certifique-se de carregar a lista de cotacoes usando o mesmo serviço utilizado na pesquisa
        LocalDate selectedDate = dataCotacao;
        List<Cotacao> cotacaoResults = cotacaoService.getCotationsByDate(selectedDate);

        // Ordena a lista de cotacoes primeiro por subgrupo e depois por produto dentro de cada subgrupo
        Collections.sort(cotacaoResults, Comparator
                .comparing((Cotacao c) -> c.getPropriedade().getProduto().getSubgrupo().getNome())
                .thenComparing(c -> c.getPropriedade().getProduto().getNome()));

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        String dataCabecalho = dataCotacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Adiciona os dados ao PDF
        document.add(new Paragraph("Formulário de Cotação CEASA CAMPINAS").setFontSize(7));
        document.add(new Paragraph("Cotação Realizada em: " + dataCabecalho).setFontSize(7));
        document.add(new Paragraph("Número da Feira: " + numeroFeira).setFontSize(7));

        // Define estilos para a tabela
        Style cellStyle = new Style().setFontSize(5).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER);

        float[] columnWidths = {95f, 75f, 55f, 55f, 55f, 55f, 55f, 40f};

        float scaleFactor = 0.85f; // Fator de escala para reduzir em 15%
        float[] scaledColumnWidths = new float[columnWidths.length];
        for (int i = 0; i < columnWidths.length; i++) {
            if (i >= 2 && i <= 7) {
                scaledColumnWidths[i] = columnWidths[i] * scaleFactor;
            } else {
                scaledColumnWidths[i] = columnWidths[i];
            }
        }

        Table table = new Table(scaledColumnWidths);

        String[] headers = {"Produto", "Variedade", "SubVariedade", "Classificação", "Valor Mínimo", "Valor Máximo", "Valor +Comum", "Mercado"};
        for (String header : headers) {
            table.addHeaderCell(new Cell().add(new Paragraph(header).setFontSize(6).setBold()).setBorder(Border.NO_BORDER));
        }

        String lastSubgrupo = null;
        for (Cotacao cotacaoItem : cotacaoResults) {
            String subgrupoAtual = cotacaoItem.getPropriedade().getProduto().getSubgrupo().getNome();

            if (!Objects.equals(lastSubgrupo, subgrupoAtual)) {
                table.addCell(new Cell(1, headers.length).add(new Paragraph("Subgrupo: " + subgrupoAtual).setFontSize(8).setBold()).setBorder(Border.NO_BORDER));
                lastSubgrupo = subgrupoAtual;
            }

            if (cotacaoItem.getDataCotacao() != null) {
                String unidade = cotacaoItem.getPropriedade().getUnidade();
                String mercado = cotacaoItem.getMercado();

                if (mercado == null) {
                    mercado = "MV";
                }

                if (unidade == null) {
                    unidade = "KG";
                }

                String produto = cotacaoItem.getPropriedade().getProduto().getNome() + " - " + unidade;
                String variedade = cotacaoItem.getPropriedade().getVariedade();
                String subvariedade = cotacaoItem.getPropriedade().getSubvariedade();
                String classificacao = cotacaoItem.getPropriedade().getClassificacao();
                String valorMinimo = cotacaoItem.getPrecoMinimo().toString();
                //String valorMedio = cotacaoItem.getPrecoMedio().toString();
                String valorMaximo = cotacaoItem.getPrecoMaximo().toString();
                String valorMaisComum = cotacaoItem.getValorComum().toString();

                addCell(table, produto, cellStyle);
                addCell(table, variedade, cellStyle);
                addCell(table, subvariedade, cellStyle);
                addCell(table, classificacao, cellStyle);
                addCell(table, valorMinimo, cellStyle);
               // addCell(table, valorMedio, cellStyle);
                addCell(table, valorMaximo, cellStyle);
                addCell(table, valorMaisComum, cellStyle);
                addCell(table, mercado, cellStyle);
            } else {
                document.add(new Paragraph("Data: [Data não disponível]"));
                document.add(new Paragraph("Cotação nula encontrada!"));
            }
        }

        document.add(table);

        String notas = "Este boletim informa o comportamento do preço na CEASA.\n" +
                "Não é tabela.\n" +
                "Abreviaturas\n" +
                "============\n" +
                "Min = Preço mínimo pesquisado\n" +
                "MC = Preço mais comum ou preço modal.\n" +
                "Aquele que apresentou maior número de observações durante a pesquisa.\n" +
                "Preço de maior expressão no mercado.\n" +
                "Max = Preço máximo pesquisado.\n" +
                "MFR = Mercado fraco. Quando o preço mais comum é inferior ao da pesquisa anterior.\n" +
                "ME = Mercado estável. Quando o preço mais comum é igual ao da pesquisa anterior.\n" +
                "MFI = Mercado firme. Quando o preço mais comum é superior ao da pesquisa anterior.\n" +
                "MV = Mercado vazio. Quando não existe pesquisa anterior do produto.\n" +
                "T. = Tipo ou número de frutas por embalagem\n" +
                "CX/MAD = Caixa de madeira\n" +
                "CX/PAP = Caixa de papelão\n" +
                "S/CLIM = Sem climatizar\n" +
                "CLIM = Climatizada\n" +
                "KG = Quilograma\n" +
                "S/LAV = Sem lavar\n" +
                "ENG = Engradado\n" +
                "IMP = Importado\n" +
                "ESP = Especial\n" +
                "PRI = Primeira\n" +
                "SEG = Segunda\n" +
                "CORT = Cortado\n" +
                "REST = Réstia\n" +
                "EXT = Extra\n" +
                "CX = Caixa\n" +
                "DZ = Dúzia\n" +
                "MO = Molho\n" +
                "SC = Saca";

        document.add(new Paragraph(notas).setFontSize(7).setTextAlignment(TextAlignment.LEFT));

        document.close();
    }

    private void addCell(Table table, String content, Style style) {
        table.addCell(new Cell().add(new Paragraph(content).addStyle(style)));
    }

	
	
}
