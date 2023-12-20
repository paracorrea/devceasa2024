package com.flc.springthymeleaf.web;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.itextpdf.layout.element.Paragraph;




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
		return propriedadeService.findPropriedadePorCotacao();
	}
	
	@ModelAttribute("fatores")
	public FatorSazonal[] getFatores() {
		return FatorSazonal.values();

	}
	
	@GetMapping("/cotacoes/cadastrar")
	public String cadastrar(Cotacao cotacao, Model model) {
		
		LocalDate dataAtual = LocalDate.now();
		
		List<Cotacao> lista = cotacaoService.findAll();
		
		model.addAttribute("dataAtual", dataAtual);
		model.addAttribute("cotacoes", lista);		
		return "cotacao/cotacao_cadastro";
	}
	

	
	
	@PostMapping("/cotacoes/salvar")
	public String salvar(@Valid @ModelAttribute Cotacao cotacao, Model model,BindingResult result, RedirectAttributes attr ) {
		
		 LocalDate dataAtual = LocalDate.now();
		 
		 

	        // Exibe a data no terminal
	       
		 
	        if (!cotacao.getDataCotacao().equals(dataAtual)) {
			
	        	result.rejectValue("dataCotacao", "error.cotacao", "A data da cotação deve ser a data atual. Data atual: " + dataAtual + ", Data informada: " + cotacao.getDataCotacao());
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
	public String preEditar(@PathVariable("id") Integer id, ModelMap model) {
		model.addAttribute("cotacao", cotacaoService.findById(id));
		return "cotacao/cotacao_cadastro";
	}
	
	@PostMapping("/cotacoes/editar") 
	public String editar (@Valid Cotacao cotacao, BindingResult result, RedirectAttributes attr) {
		if (result.hasErrors()) {
			return "cotacao/cotacao_cadastro";
		}
		
		cotacaoService.update(cotacao);
		attr.addFlashAttribute("success", "Cotação editada com sucesso");
		return "redirect:/cotacoes/cadastrar";
	}
	
	@GetMapping("/cotacao/excluir/{id}")
	public String excluir(@PathVariable("id") Integer id, RedirectAttributes attr) {

		
		
			cotacaoService.delete(id);
			attr.addFlashAttribute("success","Cotação excluída com sucesso");		
			return "redirect:/cotacoes/cadastrar";
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
        model.addAttribute("cotacaoResults", cotacaoResults);
        return "cotacao/cotacao_listagemdata";
    }

    @GetMapping("/cotacoes/gerar-pdf")
    public void gerarPdf(@RequestParam("dataCotacao") @DateTimeFormat(iso = ISO.DATE) LocalDate dataCotacao, Cotacao cotacao, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=cotacao.pdf");

        System.out.println("Data recebida no controlador: " + dataCotacao);
        
        // Certifique-se de carregar a lista de cotacoes usando o mesmo serviço utilizado na pesquisa
        LocalDate selectedDate = dataCotacao;
        List<Cotacao> cotacaoResults = cotacaoService.getCotationsByDate(selectedDate);

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Adiciona os dados ao PDF
        document.add(new Paragraph("Formulário de Cotação"));
        System.out.println("ops1");

        for (Cotacao cotacaoItem : cotacaoResults) {
            // Verifica se a data não é nula antes de formatar
            if (cotacaoItem.getDataCotacao() != null) {
                String dataFormatada = cotacaoItem.getDataCotacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                document.add(new Paragraph("Data: " + dataFormatada));
                document.add(new Paragraph("Produto"+ cotacao.getId()));
                
            } else {
                document.add(new Paragraph("Data: [Data não disponível]"));
                document.add(new Paragraph("Cotacao nula encontrada!"));
                System.out.println("ops2");
            }
        }

        System.out.println("ops3");
        document.close();
    }
}
