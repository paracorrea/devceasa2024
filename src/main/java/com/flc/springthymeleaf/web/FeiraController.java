package com.flc.springthymeleaf.web;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flc.springthymeleaf.domain.Feira;

import com.flc.springthymeleaf.service.FeiraService;
import com.flc.springthymeleaf.service.exceptions.FeiraNaoEncontradaException;
import com.flc.springthymeleaf.service.exceptions.FeiraNaoPodeSerEncerradaException;
import com.flc.springthymeleaf.web.validator.FeiraValidator;

import enums.StatusFeira;
import jakarta.validation.Valid;

@Controller
public class FeiraController {


	
	private static final Logger logger = LoggerFactory.getLogger(Feira.class);
	
	
	
	@Autowired
	private FeiraService feiraService;
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(new FeiraValidator(feiraService));
	}


	@GetMapping("/feiras/cadastrar")
	public String cadastrar(Feira feira, ModelMap model ) {
		
		model.addAttribute("feiras", feiraService.findAll());
		return "feira/feira_cadastro";
	}
	
	
	@GetMapping("/feiras/listar")
	public String listarFeiras(Model model) {
		List<Feira> feiras = feiraService.findAll();
		feiras.sort(Comparator.comparing(Feira::getDataFeira).reversed());

		model.addAttribute("feiras", feiras);
		return "feira/lista_feiras"; // Nova página de listagem de feiras
	}

	
	
	
	@GetMapping("/nova")
	public String prepararNovaFeira(
			@RequestParam(name = "data", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate data, Feira feira,
			Model model, RedirectAttributes attr) {

		if (data == null) {
			data = LocalDate.now(); // Se nenhuma data for fornecida, assume a data atual
		}

		// Verificar se a data é passada
		if (data.isAfter(LocalDate.now())) {
			attr.addFlashAttribute("error", "A data da feira não pode ser posterior a data atual");
			return "redirect:/feiras"; // Redireciona para a lista de feiras com a mensagem de erro
		}

		// Verificar se já existe uma feira com a mesma data (aberta, fechada ou
		// publicada)
		if (feiraService.verificarSeJaExiste(data)) {
			attr.addFlashAttribute("error", "Já existe uma feira cadastrada para esta data.");
			return "redirect:/feiras"; // Redireciona para a lista de feiras com a mensagem de erro
		}

		Long ultimaFeira = feiraService.ObterNumero();
		String status = "ABERTA";

		// verificar se já existe uma feira com a data

		model.addAttribute("dataFeira", data);
		model.addAttribute("status", status);
		model.addAttribute("numero", ultimaFeira + 1);

		Feira novaFeira = new Feira();
		novaFeira.setDataFeira(data);
		novaFeira.setStatus(StatusFeira.ABERTA); // Define o status como ABERTA
		novaFeira.setNumero(ultimaFeira + 1);
		//feiraService.salvarFeira(novaFeira);

		return "feira/menu_feira";

	}

	@PostMapping("/salvar")
	public String criarFeira(@Valid @ModelAttribute("feira") Feira feira, BindingResult result, boolean resultado,
			RedirectAttributes attr) {
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
		return "redirect:/feiras/" + feira.getId() + "/cotacoes"; // Redireciona para a página de visualização de
																	// cotações da feira
	}

	 @PutMapping("/{id}/encerrar") // Certifique-se de que o @PutMapping está correto
	    public String encerrarFeira(@PathVariable("id") Long id, RedirectAttributes attr) {
		
		   logger.info("Recebida requisição PUT para encerrar feira com ID: {}", id); 
		 
		 try {
			feiraService.encerrarFeira(id);
			attr.addFlashAttribute("success", "Feira encerrada com sucesso.");
		} catch (FeiraNaoEncontradaException e) {
			attr.addFlashAttribute("error", "Feira não encontrada.");
		} catch (FeiraNaoPodeSerEncerradaException e) {
			attr.addFlashAttribute("error", e.getMessage()); // Mensagem específica da exceção
		}
		return "redirect:/feiras";
	}
}
