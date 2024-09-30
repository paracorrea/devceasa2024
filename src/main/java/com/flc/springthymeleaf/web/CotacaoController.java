package com.flc.springthymeleaf.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
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
import com.flc.springthymeleaf.domain.Feira;
import com.flc.springthymeleaf.domain.Propriedade;
import com.flc.springthymeleaf.service.CotacaoService;
import com.flc.springthymeleaf.service.FeiraService;
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
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;


@Controller
public class CotacaoController {

	private static final Logger LOGGER = Logger.getLogger(CotacaoController.class.getName());
	
	private BigDecimal valorComumAnterior = null; 
	private BigDecimal valorComumAtual = new BigDecimal(0); 
	 
	@Autowired
	private CotacaoService cotacaoService;
	@Autowired
	private FeiraService feiraService;
	@Autowired
	private PropriedadeService propriedadeService;
	@InitBinder
	 
	public void initBinder(WebDataBinder binder) {
			binder.addValidators(new CotacaoValidator(cotacaoService));
	}
	
	@ModelAttribute("cotados")
	public List<Propriedade> listarCotados() {
		
		List<Propriedade> listaCotados = propriedadeService.findPropriedadePorCotacao();
		Collections.sort(listaCotados, (p1, p2) -> p1.getProduto().getNome().compareToIgnoreCase(p2.getProduto().getNome()));
		return listaCotados;
	}
	
	@GetMapping("/cotacoes/pesquisar")
    public String pesquisar(Model model) {
          
		return "cotacao/cotacao_pesquisar";
        // call the folowings endpoints  
		// "/cotacoes/searchPropertyByCode" - for search properties by codigo
		// "/cotacoes/searchPropertyByProductName" for search properties by name of product
		// "/cotacoes/cadastrar" - in form 
    }
	
	@GetMapping("/cotacoes/buscar-cotacao-anterior")
	@ResponseBody
	public ResponseEntity<Cotacao> buscarCotacaoAnterior(
	         
	        @RequestParam Integer propriedadeId,
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
	 public String cadastrar(@RequestParam("propriedadeId") Integer propriedadeId, Model model) {

		 	LOGGER.info("Recebido propriedadeId em cotacoes/cadastrar: " + propriedadeId);

		 	  if (propriedadeId == null) {
		 	        LOGGER.info("propriedadeId está nulo!");
		 	  }
		 	
		 	  Optional<Propriedade> propriedadeOpt = propriedadeService.findById(propriedadeId);
	 	 
		 	  if (propriedadeOpt.isPresent()) {
	           
	        	
	        	Propriedade propriedade = propriedadeOpt.get();
	            LocalDate dataCotacao = obterDataUltimaFeiraAberta();
	            Cotacao ultimaCotacao = cotacaoService.buscarCotacaoAnterior1(propriedadeId, dataCotacao);

	            Cotacao cotacao = new Cotacao();
	           
	            if (ultimaCotacao != null) {
	            		            	
	            	 valorComumAnterior=ultimaCotacao.getValorComum();
	            	 
	            	 cotacao.setValor1(ultimaCotacao.getValor1());
	                 cotacao.setValor2(ultimaCotacao.getValor2());
	                 cotacao.setValor3(ultimaCotacao.getValor3());
	                 cotacao.setValor4(ultimaCotacao.getValor4());
	                 cotacao.setValor5(ultimaCotacao.getValor5());
	                 cotacao.setValor6(ultimaCotacao.getValor6());
	                 cotacao.setValor7(ultimaCotacao.getValor7());
	                 cotacao.setValor8(ultimaCotacao.getValor8());
	                 cotacao.setValor9(ultimaCotacao.getValor9());
	                 cotacao.setValor10(ultimaCotacao.getValor10());
	                 cotacao.setPesoVariavel(ultimaCotacao.getPesoVariavel());
	                 cotacao.setPrecoMinimo(ultimaCotacao.getPrecoMinimo());
	                 cotacao.setPrecoMedio(ultimaCotacao.getPrecoMedio());
	                 cotacao.setPrecoMaximo(ultimaCotacao.getPrecoMaximo());
	                 cotacao.setValorComum(ultimaCotacao.getValorComum());
	                
	                LOGGER.info("Último Valor Comum: " + valorComumAnterior);
	                LOGGER.info("Última cotação: " + ultimaCotacao.toString());
	                logCotacaoValues("Última cotação", ultimaCotacao);
	            } else {
	            	valorComumAnterior=null;
	            }

	            	cotacao.setPropriedade(propriedade);
	            	cotacao.setDataCotacao(dataCotacao);

	            	model.addAttribute("dataCotacao", dataCotacao);
	            	model.addAttribute("cotacao", cotacao);
	            	model.addAttribute("ultimaCotacao", ultimaCotacao);
	            
	            	LOGGER.info("Propriedade selecionada: " + propriedade.getProduto().getNome() + " " + propriedade.getCodigo() + " " + propriedade.getVariedade());
	            	//logCotacaoValues("Cotação atual", cotacao);
	           
	       
	        }
		 	  	return "cotacao/cotacao_cadastro";
	    }
	
	 private void logCotacaoValues(String prefix, Cotacao cotacao) {
	        LOGGER.info(prefix + " - PrecoMinimo: " + cotacao.getPrecoMinimo());
	        LOGGER.info(prefix + " - PrecoMedio: " + cotacao.getPrecoMedio());
	        LOGGER.info(prefix + " - PrecoMaximo: " + cotacao.getPrecoMaximo());
	        LOGGER.info(prefix + " - ValorComum: " + cotacao.getValorComum());
	        LOGGER.info(prefix + " - Valor1: " + cotacao.getValor1());
	        LOGGER.info(prefix + " - Valor2: " + cotacao.getValor2());
	        LOGGER.info(prefix + " - Valor3: " + cotacao.getValor3());
	        LOGGER.info(prefix + " - Valor4: " + cotacao.getValor4());
	        LOGGER.info(prefix + " - Valor5: " + cotacao.getValor5());
	        LOGGER.info(prefix + " - Valor6: " + cotacao.getValor6());
	        LOGGER.info(prefix + " - Valor7: " + cotacao.getValor7());
	        LOGGER.info(prefix + " - Valor8: " + cotacao.getValor8());
	        LOGGER.info(prefix + " - Valor9: " + cotacao.getValor9());
	        LOGGER.info(prefix + " - Valor10: " + cotacao.getValor10());
	    }
	
	 
	   @PostMapping("/cotacoes/salvar")
	   public String salvar(@Valid @ModelAttribute Cotacao cotacao, BindingResult result, Model model, RedirectAttributes attr) {
	      
		   LocalDate dataAtual = LocalDate.now();
	       valorComumAtual = cotacao.getValorComum();
	       
	       LOGGER.info("Valor Comum anterior: " + valorComumAnterior);
	       LOGGER.info("Valor Comum atual: " + valorComumAtual);
	       
	       if (cotacao.getDataCotacao().isAfter(dataAtual)) {
	           result.rejectValue("dataCotacao", "error.cotacao", "A data da cotação deve ser menor ou igual à data atual. Data atual: " + dataAtual + ", Data informada: " + cotacao.getDataCotacao());
	       }

	       if (cotacao.getDataCotacao() != null && cotacao.getPropriedade() != null &&
	               	cotacaoService.existeCotacaoComMesmaDataECategoria(cotacao)) {
	    	   		attr.addFlashAttribute("fail", "A cotação não foi salva pois já existe uma cotação deste produto para este dia, selecione outro produto para cotar");
	    	   		return "redirect:/cotacoes/pesquisar";
	       }

	       if (result.hasErrors()) {
	           LOGGER.info("Erros de validação ao salvar cotação: " + result.toString());
	           attr.addFlashAttribute("Erros de validação ao salvar cotação: " + result.toString());
	           return "cotacao/cotacao_pesquisar";
	       }
	      
	       if (valorComumAnterior != null) {
	           if (valorComumAtual.compareTo(valorComumAnterior) > 0) {
	               cotacao.setMercado("MFI"); // Mercado Forte
	           } else if (valorComumAtual.compareTo(valorComumAnterior) < 0) {
	               cotacao.setMercado("MFR"); // Mercado Ruim
	           } else {
	               cotacao.setMercado("ME"); // Mercado Estável
	           }
	       } else {
	           cotacao.setMercado("MV"); // Mercado Vazio (sem cotação anterior)
	       }
	       
	      
	       cotacaoService.insert(cotacao);
	       attr.addFlashAttribute("success", "Cotação cadastrada com sucesso");
	       return "redirect:/cotacoes/pesquisar";
	   }
	
	@GetMapping("/cotacoes/editar/{id}")
	public String preEditar(@PathVariable("id") Integer id,  Model model) {
		
		Cotacao cotacao = cotacaoService.findById(id);
		BigDecimal peso = cotacao.getPropriedade().getPeso();
		
		valorComumAnterior = cotacao.getValorComum();
		
		model.addAttribute("peso",peso );
		model.addAttribute("cotacao",cotacao);
				
		return "cotacao/cotacao_editar";
	}
	
	@PostMapping("/cotacoes/editar") 
	public String editar (@Valid Cotacao cotacao, BindingResult result, RedirectAttributes attr) {
		
		valorComumAtual = cotacao.getValorComum();
		LocalDate dataAtual = LocalDate.now();
		
		if (cotacao.getDataCotacao().isAfter(dataAtual)) {
		    	result.rejectValue("dataCotacao", "error.cotacao", "A data da cotação deve menor ou igual a data atual. Data atual: " + dataAtual + ", Data informada: " + cotacao.getDataCotacao());
	   } 	
	 
	
		if (result.hasErrors()) {
			return "cotacao/cotacao_editar";
		}
		
		LOGGER.info("Valor Comum Anterior - em cotacao Editar: "+valorComumAnterior);
		LOGGER.info("Valor Comum Atual - em cotacao Editar: " + valorComumAtual);
		
		if (valorComumAnterior != null) {
	           if (valorComumAtual.compareTo(valorComumAnterior) > 0) {
	               cotacao.setMercado("MFI"); // Mercado Forte
	           } else if (valorComumAtual.compareTo(valorComumAnterior) < 0) {
	               cotacao.setMercado("MFR"); // Mercado Ruim
	           } else {
	               cotacao.setMercado("ME"); // Mercado Estável
	           }
	       } else {
	           cotacao.setMercado("MV"); // Mercado Vazio (sem cotação anterior)
	       }
		
		 LOGGER.info("Alterada uma cotacao: " + cotacao.getPropriedade().getProduto().getNome()+ " "+ cotacao.getPropriedade().getCodigo());
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
	

    @GetMapping("/cotacoes/por-data")
    public String pesquisarCotacoesPorData(@ModelAttribute("cotacao") Cotacao cotacao, Model model) {

    	LocalDate selectedDate = cotacao.getDataCotacao();
        List<Cotacao> cotacaoResults = cotacaoService.getCotationsByDate(selectedDate);
        Collections.sort(cotacaoResults, Comparator
                .comparing((Cotacao c) -> c.getPropriedade().getProduto().getSubgrupo().getNome())
                .thenComparing(c -> c.getPropriedade().getProduto().getNome())
                .thenComparing(c -> c.getPropriedade().getVariedade())
                .thenComparing(c -> c.getPropriedade().getSubvariedade())
                .thenComparing(c -> c.getPropriedade().getClassificacao()));
        model.addAttribute("cotacaoResults", cotacaoResults);
        return "cotacao/cotacao_listagemdata";
    }

    @GetMapping("/cotacoes/por-feira/{dataFeira}")
    public String pesquisarCotacoesPorFeira(@PathVariable("dataFeira") @DateTimeFormat(iso = ISO.DATE) LocalDate dataFeira, Model model) {
       
    	List<Cotacao> cotacaoResults = cotacaoService.getCotationsByDate(dataFeira);
        
    	// Listar propriedades Não cotadas 
    	List<Propriedade> requiredProperties = propriedadeService.findPropriedadePorCotacao(); // Adjust this method to fetch properties with status=true
		// Extract the properties from cotacaoResults
		List<Propriedade> quotedProperties = cotacaoResults.stream()
                                                        .map(Cotacao::getPropriedade)
                                                        .collect(Collectors.toList());

		// Find the missing properties
		List<Propriedade> missingProperties = requiredProperties.stream()
                                                             .filter(prop -> !quotedProperties.contains(prop))
                                                             .collect(Collectors.toList());
		
                Collections.sort(cotacaoResults, Comparator
                .comparing((Cotacao c) -> c.getPropriedade().getProduto().getSubgrupo().getNome())
                .thenComparing(c -> c.getPropriedade().getProduto().getNome())
                .thenComparing(c -> c.getPropriedade().getVariedade())
                .thenComparing(c -> c.getPropriedade().getSubvariedade())
                .thenComparing(c -> c.getPropriedade().getClassificacao()));
        
        // Adiciona a data formatada para exibição no template (opcional, se necessário)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dataFeiraFormatada = dataFeira.format(formatter);
        model.addAttribute("dataFeiraFormatada", dataFeiraFormatada);
        model.addAttribute("cotacaoResults", cotacaoResults);
        

		if (!missingProperties.isEmpty()) {
			// Show a message or handle the missing properties
			LOGGER.info("Warning: The following properties were not quoted:");
        
			StringBuilder missingPropsHtml = new StringBuilder();
        
         for (Propriedade prop : missingProperties) {
        	 
        	 String formattedPeso = String.format("%.2f", prop.getPeso());
         	LOGGER.info("Cotação faltante: "+prop.getProduto().getNome() + " - " + prop.getVariedade()+ " - " + prop.getSubvariedade());
         	   missingPropsHtml.append("<li>")
         	   	.append(prop.getId()).append(" - ")
               	.append(prop.getCodigo()).append(" - ") 
               	.append(prop.getProduto().getNome()).append(" ")
                .append(prop.getVariedade()).append(" ")
                .append(prop.getSubvariedade()).append(" ")
                .append(prop.getClassificacao()).append(" ")
                .append(prop.getEmbalagens()).append(" ")
                .append(formattedPeso).append(" Kilos ")
                .append("</li>");
         }
         model.addAttribute("missing", missingPropsHtml.toString());
     }
        
        return "cotacao/cotacao_por_feira";
    }
  
    
	@GetMapping("/cotacoes/gerar-pdf")
    public void gerarPdf(@RequestParam("dataCotacao") @DateTimeFormat(iso = ISO.DATE) LocalDate dataCotacao,
                         @RequestParam("numeroFeira") Long numeroFeira,
                         Cotacao cotacao, HttpServletResponse response, Model model) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=cotacao.pdf");

        // Certifique-se de carregar a lista de cotacoes usando o mesmo serviço utilizado na pesquisa
        LocalDate selectedDate = dataCotacao;
        List<Cotacao> cotacaoResults = cotacaoService.getCotationsByDate(selectedDate);
       
        // Ordena a lista de cotacoes primeiro por subgrupo, depois por produto dentro de cada subgrupo e por variedade
        Collections.sort(cotacaoResults, Comparator
                .comparing((Cotacao c) -> c.getPropriedade().getProduto().getSubgrupo().getNome())
                .thenComparing(c -> c.getPropriedade().getProduto().getNome())
                .thenComparing(c -> c.getPropriedade().getVariedade())
                .thenComparing(c -> c.getPropriedade().getSubvariedade())
                .thenComparing(c -> c.getPropriedade().getClassificacao()));

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        String dataCabecalho = dataCotacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Adiciona os dados ao PDF
        document.add(new Paragraph("CENTRAIS DE ABASTECIMENTO DE CAMPINAS - SA").setFontSize(12).setBold());
        document.add(new Paragraph("Formulário de Cotação - CEASA CAMPINAS - Boletim número: " + numeroFeira).setFontSize(11));
        document.add(new Paragraph("Cotação Realizada em: " + dataCabecalho).setFontSize(10).setHorizontalAlignment(HorizontalAlignment.CENTER));
        
        // Define estilos para a tabela
        Style cellStyle = new Style().setFontSize(8).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER);

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

        String[] headers = {"Produto", "Variedade", "SubVariedade", "Classificação", "Valor Mínimo", "Valor +Comum",  "Valor Máximo","Mercado"};
        for (String header : headers) {
            // cabeçalho da tabela com fonte 8
            table.addHeaderCell(new Cell().add(new Paragraph(header).setFontSize(8)).setBorder(Border.NO_BORDER));
        }

        String lastSubgrupo = null;
        for (Cotacao cotacaoItem : cotacaoResults) {
            String subgrupoAtual = cotacaoItem.getPropriedade().getProduto().getSubgrupo().getNome();

            if (!Objects.equals(lastSubgrupo, subgrupoAtual)) {
                // texto do subgrupo com fonte 8
                table.addCell(new Cell(1, headers.length).add(new Paragraph(subgrupoAtual).setFontSize(8)).setBorder(Border.NO_BORDER));
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
                
                addCell(table, valorMaisComum, cellStyle);
                addCell(table, valorMaximo, cellStyle);
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

    @GetMapping("/cotacoes/search")
    @ResponseBody
    public List<Propriedade> searchPropriedades(@RequestParam("query") String query) {
        return propriedadeService.searchByQuery(query);
    }
    
    @GetMapping("/cotacoes/searchPropertyByProductName")
    public ResponseEntity<List<Propriedade>> searchPropertyByProductName(@RequestParam String productName) {
        List<Propriedade> propriedades = propriedadeService.findByProdutoNome(productName);
        
        return ResponseEntity.ok(propriedades);
    }
    
    @GetMapping("/cotacoes/searchPropertyByCode")
    public ResponseEntity<?> searchPropertyByCode(@RequestParam String code) {
       
    	String codigoTrimmed = code.trim();
    	Propriedade propriedade = propriedadeService.findByCodigo(codigoTrimmed);
        if (propriedade != null) {
            return ResponseEntity.ok(propriedade);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Propriedade não encontrada.");
        }
    }

    private LocalDate obterDataUltimaFeiraAberta() {
        Optional<Feira> ultimaFeiraAberta = feiraService.obterUltimaFeiraAberta();
        return ultimaFeiraAberta.map(Feira::getDataFeira).orElse(LocalDate.now());
    }
   
}
