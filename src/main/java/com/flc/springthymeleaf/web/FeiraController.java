package com.flc.springthymeleaf.web;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flc.springthymeleaf.domain.Feira;

import com.flc.springthymeleaf.service.FeiraService;

import enums.StatusFeira;
import jakarta.validation.Valid;

@Controller
public class FeiraController {

	@Autowired
	private FeiraService feiraService;
	
	@GetMapping("/feiras")
    public String listarFeiras(Model model) {
        List<Feira> feiras = feiraService.findAll();
        feiras.sort(Comparator.comparing(Feira::getDataFeira).reversed());
      
        
        
        model.addAttribute("feiras", feiras);
        return "feira/lista_feiras"; // Nova página de listagem de feiras
    }
	
	
	@GetMapping("/feiras/nova")
	public String novaFeira(@RequestParam(name = "data", required = false) 
    @DateTimeFormat(iso = ISO.DATE) LocalDate data,
    Feira feira, Model model, RedirectAttributes attr) {

		  if (data == null) {
		        data = LocalDate.now(); // Se nenhuma data for fornecida, assume a data atual
		    }
		  
		  // Verificar se a data é passada
		    if (data.isAfter(LocalDate.now())) {
		        attr.addFlashAttribute("error", "A data da feira não pode ser posterior a data atual");
		        return "redirect:/feiras"; // Redireciona para a lista de feiras com a mensagem de erro
		    }
		    
		 // Verificar se já existe uma feira com a mesma data (aberta, fechada ou publicada)
		    if (feiraService.verificarSeJaExiste(data)) {
		        attr.addFlashAttribute("error", "Já existe uma feira cadastrada para esta data.");
		        return "redirect:/feiras"; // Redireciona para a lista de feiras com a mensagem de erro
		    }
		
		    
		    
	
		Long ultimaFeira = feiraService.ObterNumero();
		String status = "ABERTA";
		
		// verificar se já existe uma feira com a data 
		
		model.addAttribute("dataFeira", data);
		model.addAttribute("status",status);
		model.addAttribute("numero", ultimaFeira+1);
		
		Feira novaFeira = new Feira();
        novaFeira.setDataFeira(data);
        novaFeira.setStatus(StatusFeira.ABERTA); // Define o status como ABERTA
        novaFeira.setNumero(ultimaFeira+1);
        feiraService.salvarFeira(novaFeira);
		
		return "feira/menu_feira";
		
	}
	
	@PostMapping("/feiras/salvar")
	public String salvarFeira(@Valid @ModelAttribute("feira") Feira feira, BindingResult result, boolean resultado, RedirectAttributes attr) {
	    if (result.hasErrors()) {
	        return "feira/menu_feira"; // Retorna para a página de abertura de feira em caso de erros
	    }

	    // Verificar se já existe uma feira publicada na mesma data
	    LocalDate dataFeira = feira.getDataFeira();
	    if (feiraService.verificarSeJaExiste(dataFeira)) {
	        attr.addFlashAttribute("error", "Já existe uma feira publicada na data " + dataFeira);
	        return "redirect:/feiras"; // Redireciona para a página de abertura de feira com mensagem de erro
	    }

	    feiraService.salvarFeira(feira); // Salva a nova feira no banco de dados

	    attr.addFlashAttribute("success", "Feira aberta com sucesso!");
	    return "redirect:/feiras/" + feira.getId() + "/cotacoes"; // Redireciona para a página de visualização de cotações da feira
	}
}
