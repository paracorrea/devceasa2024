package com.flc.springthymeleaf.web;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.flc.springthymeleaf.DTO.RelatorioMensalDto;
import com.flc.springthymeleaf.domain.Cotacao;
import com.flc.springthymeleaf.domain.Feira;
import com.flc.springthymeleaf.domain.Propriedade;
import com.flc.springthymeleaf.service.CotacaoService;
import com.flc.springthymeleaf.service.FeiraService;
import com.flc.springthymeleaf.service.PropriedadeService;
import com.flc.springthymeleaf.web.validator.CotacaoValidator;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
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
  
    // Relatório de cotacao principal - publicado em cada dia de cotacao - 
 	//chamado em feira passando a data da cotacao e o número da feira /cotacoes/gerar-pdf
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
    
    // Relatório chamado do Menu (Reletório Bimestral de Cotações)
    // que mostra as cotações de todos os produtos que foram cotados nos últimos 60 dias agrupados por semana
    @SuppressWarnings("resource")
	@GetMapping("/cotacoes/relatorio-cotacoes")
    public ResponseEntity<InputStreamResource> gerarRelatorioCotacoes() {
    	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4.rotate());
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(60);
            // Adicionar logo

            try (InputStream logoStream = new ClassPathResource("static/image/logo1.png").getInputStream()) {
            		Image logo = new Image(ImageDataFactory.create(logoStream.readAllBytes())).scaleToFit(80, 40);
            		document.add(logo);
            } catch (IOException e) {
            		e.printStackTrace();
            			throw new RuntimeException("Erro ao carregar o logo", e);
            }

            // Cabeçalho do relatório
            Paragraph titulo = new Paragraph("Relatório de Cotações de Produtos")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(titulo);

            Paragraph periodo = new Paragraph("Período: "  + startDate.format(dateFormatter) + " a " + endDate.format(dateFormatter))
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(periodo);

            // Obter dados do serviço
            Map<String, Map<LocalDate, Double>> mediaSemanalPorProduto = cotacaoService.getMediaSemanalPorProduto(startDate, endDate);

            // Tabela de cotações
            List<LocalDate> semanas = new ArrayList<>(mediaSemanalPorProduto.values().iterator().next().keySet());
            semanas.sort(LocalDate::compareTo); // Organizar semanas em ordem cronológica

            float[] columnWidths = new float[semanas.size() + 1];
            columnWidths[0] = 200f; // Largura para a coluna de produtos
            Arrays.fill(columnWidths, 1, columnWidths.length, 80f); // Largura das colunas de semanas

            Table table = new Table(columnWidths);
            DeviceRgb headerColor = new DeviceRgb(35, 107, 54); // Cor verde para o cabeçalho
            DeviceRgb alternateRowColor = new DeviceRgb(173, 216, 230); // Cor alternada azul-claro para linhas

            // Cabeçalho da Tabela
            Cell headerCell = new Cell().add(new Paragraph("Produto").setFontSize(10).setBold().setFontColor(ColorConstants.WHITE));
            headerCell.setBackgroundColor(headerColor);
            table.addHeaderCell(headerCell);

            for (LocalDate semana : semanas) {
                Cell weekHeader = new Cell().add(new Paragraph(semana.format(dateFormatter)).setFontSize(10).setBold().setFontColor(ColorConstants.WHITE));
                weekHeader.setBackgroundColor(headerColor);
                table.addHeaderCell(weekHeader);
            }

            // Adicionar produtos e médias à tabela
            int rowIndex = 0;
            for (Map.Entry<String, Map<LocalDate, Double>> entry : mediaSemanalPorProduto.entrySet()) {
                String produto = entry.getKey();
                Map<LocalDate, Double> mediasSemanais = entry.getValue();

                DeviceRgb rowColor = (rowIndex++ % 2 == 0) ? (DeviceRgb) ColorConstants.WHITE : alternateRowColor;

                // Linha do produto
                Cell productCell = new Cell().add(new Paragraph(produto).setFontSize(9)).setBackgroundColor(rowColor);
                table.addCell(productCell);

                // Colunas de médias semanais
                for (LocalDate semana : semanas) {
                    Double media = mediasSemanais.getOrDefault(semana, 0.0);
                    Cell mediaCell = new Cell().add(new Paragraph(String.format("%.2f", media)).setFontSize(8)).setBackgroundColor(rowColor);
                    table.addCell(mediaCell);
                }
            }

            // Rodapé
            Paragraph fonte = new Paragraph("Fonte: CEASA/Campinas - Sistema DEVCEASA")
                    .setFontSize(6)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginTop(10);
            
            document.add(table);
            document.add(fonte);
            document.close();

            // Configurar resposta HTTP
            ByteArrayInputStream pdfStream = new ByteArrayInputStream(baos.toByteArray());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=relatorio-cotacoes.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdfStream));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Os  próximos três endpoints são do relatório que vai para o sindicato rural com determinados produtos que é passado
    // como parâmetro através de uma página html. É fornecido um csv com a lista das propriedades que vao no relatório
    
    @GetMapping("cotacoes/relatorio-mensal")
	public String relatorio() {
		return "cotacao/relatorio_form";
	}
	
	// gerar relatorio mensal para o Sindicato Rural
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
	    List<RelatorioMensalDto> relatorio = cotacaoService.gerarRelatorioMensal(propriedadeIds, dataInicial, quantidadeMeses);

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

        // Adicionar o título
        Paragraph titulo = new Paragraph("CEASA CAMPINAS")
            .setFontSize(26)       // Define o tamanho da fonte
            .setBold() 
            .setFontColor(new DeviceRgb(0, 100, 0))  // Verde escuro
            .setTextAlignment(com.itextpdf.layout.property.TextAlignment.LEFT) // Alinha o título ao centro
            .setMarginBottom(1);   // Define o espaço inferior (margem)
        document.add(titulo);

     
        PdfPage pdfPage = pdf.getFirstPage();  // Obtém a primeira página
        PdfCanvas canvas = new PdfCanvas(pdfPage);

        // Desenhar uma linha horizontal abaixo do título
        float xStart = 33 ;    // Início da linha no eixo X
        float xEnd = pageSize.getWidth() - 400 ;  // Fim da linha no eixo X (ajusta conforme necessário)
        float yPosition = pageSize.getHeight() - 80;  // Posição Y logo abaixo do título (ajustável)
        
        canvas.setLineWidth(2f)  // Define a espessura da linha
              .moveTo(xStart, yPosition)  // Ponto inicial da linha
              .lineTo(xEnd, yPosition)    // Ponto final da linha
              .setStrokeColor(new DeviceRgb(0, 100, 0))  // Cor verde escuro
              .stroke();  // Desenha a linha

        
        // Adicionar outras informações abaixo do título
        Paragraph subtitulo = new Paragraph("EVOLUÇÃO DOS PREÇOS DOS PRINCIPAIS PRODUTOS NA CEASA/CAMPINAS (R$/kg)")
            .setFontSize(9)        // Define o tamanho da fonte para o subtítulo
            .setTextAlignment(com.itextpdf.layout.property.TextAlignment.LEFT) // Alinha o subtítulo ao centro
            .setMarginBottom(3)   // Define o espaço inferior (margem)
        	.setMarginTop(12);
        document.add(subtitulo);

        // Obter e ordenar os meses (como no código anterior)
        List<String> mesesOrdenados = relatorio.get(0).getMediasPorMes().keySet().stream()
            .sorted((m1, m2) -> {
                String[] parts1 = m1.split("-");
                String[] parts2 = m2.split("-");
                int year1 = Integer.parseInt(parts1[0]);
                int month1 = Integer.parseInt(parts1[1]);
                int year2 = Integer.parseInt(parts2[0]);
                int month2 = Integer.parseInt(parts2[1]);
                if (year1 != year2) {
                    return Integer.compare(year1, year2); // ordena por ano, crescente
                } else {
                    return Integer.compare(month1, month2); // ordena por mês, crescente
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
        DeviceRgb customColorGreen = new DeviceRgb(35, 107, 54); // RGB para verde
        DeviceRgb alternateRowColorBlueLigth = new DeviceRgb(173, 216, 230); // RGB para azul claro

        // Cabeçalho - Primeira célula "Produtos" e os meses nas colunas seguintes
        Cell headerCell = new Cell();
        headerCell.add(new Paragraph("Produtos").setFontSize(10).setBold().setFontColor(ColorConstants.WHITE));
        headerCell.setBackgroundColor(customColorGreen); // Definindo a cor de fundo
        headerCell.setBorder(Border.NO_BORDER);
        table.addHeaderCell(headerCell);

        for (String mes : mesesOrdenados) {
            String[] parts = mes.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            String monthName = Month.of(month).getDisplayName(TextStyle.SHORT, Locale.of("pt", "BR"));
            Cell monthHeaderCell = new Cell();
            monthHeaderCell.add(new Paragraph(monthName + "/" + year).setFontSize(10).setBold().setFontColor(ColorConstants.WHITE));
            monthHeaderCell.setBackgroundColor(customColorGreen); // Definindo a cor de fundo
            monthHeaderCell.setBorder(Border.NO_BORDER);
            table.addHeaderCell(monthHeaderCell);
        }

        // Adicionar os produtos e suas médias
        for (int i = 0; i < relatorio.size(); i++) {
            RelatorioMensalDto dto = relatorio.get(i);

            // Determinar a cor de fundo da linha
            DeviceRgb rowColorBlue = (i % 2 == 0) ? (DeviceRgb) ColorConstants.WHITE : alternateRowColorBlueLigth;

            // Primeira célula da linha (nome do produto)
            Cell productCell = new Cell().add(new Paragraph(dto.getPropriedade().getProduto().getNome()
                + "  " + dto.getPropriedade().getVariedade()  + "  " 
                + (dto.getPropriedade().getSubvariedade())
                + " " + dto.getPropriedade().getClassificacao()).setFontSize(8));
            productCell.setBackgroundColor(rowColorBlue);
            productCell.setBorder(Border.NO_BORDER);
            table.addCell(productCell);

            // Colunas das médias
            for (String mes : mesesOrdenados) {
                BigDecimal media = dto.getMediasPorMes().get(mes);
                Cell mediaCell = new Cell().add(new Paragraph(media != null ? media.toString() : "N/A").setFontSize(10));
                mediaCell.setBackgroundColor(rowColorBlue);
                mediaCell.setBorder(Border.NO_BORDER);
                table.addCell(mediaCell);
            }
        }
        
        Paragraph fonte = new Paragraph("Fonte: CEASA/Campinas - Sistema DEVCEASA")
                .setFontSize(6)       // Define o tamanho da fonte
                .setFontColor(ColorConstants.BLACK)
                .setTextAlignment(com.itextpdf.layout.property.TextAlignment.LEFT) // Alinha o título ao centro
                .setMarginBottom(1);   // Define o espaço inferior (margem)
       
        document.add(table);
        document.add(fonte);
        document.close();
    }   // fecha o conjunto de métodos do relatório que vai para o Sindicato
    
    
    //Endpoind para gerar valores referência para o indice
   // http://localhost:5001/cotacoes/gerar-relatorio-indice-txt?data=2025-01-13
    
    
    @GetMapping("/cotacoes/gerar-relatorio-indice-txt")
    public ResponseEntity<String> gerarRelatorioTxt(
            @RequestParam String data) {

        LocalDate dataFormatada = LocalDate.parse(data);
        String caminhoArquivo = "C:\\txt\\relatorio_indice.txt";

        // Gera o relatório chamando o service
        cotacaoService.gerarRelatorioTxtPorData(dataFormatada, caminhoArquivo);

        return ResponseEntity.ok("Relatório gerado com sucesso: " + caminhoArquivo);
    }
    
    @GetMapping("/testar-leitura")
    public ResponseEntity<String> testarLeitura() {
        cotacaoService.testarLeituraArquivo();
        return ResponseEntity.ok("Teste de leitura executado. Verifique os logs.");
    }
}
    
    

