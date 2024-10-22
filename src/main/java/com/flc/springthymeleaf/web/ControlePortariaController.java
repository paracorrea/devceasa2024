package com.flc.springthymeleaf.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flc.springthymeleaf.domain.ControlePortaria;
import com.flc.springthymeleaf.domain.ItemDeNota;
import com.flc.springthymeleaf.domain.Nota;
import com.flc.springthymeleaf.enums.StatusSessao;
import com.flc.springthymeleaf.service.ControlePortariaService;
import com.flc.springthymeleaf.service.NotaService;



@Controller

public class ControlePortariaController {
    private final ControlePortariaService controlePortariaService;
    private final NotaService notaService;

    public ControlePortariaController(ControlePortariaService controlePortariaService, NotaService notaService) {
        this.controlePortariaService = controlePortariaService;
		this.notaService = notaService;
		
		
    }

    @GetMapping("/portarias/listar")
    public String listarSessoes(Model model) {
        controlePortariaService.abrirSessaoDiaria(); // Abre nova sessão se necessário
       
        List<ControlePortaria> portarias = controlePortariaService.findAll();
        
        model.addAttribute("portarias", portarias);
        
        return "portaria/portaria_listagem";
    }
    
    @GetMapping("/portarias/notas")
    public String listarNotas(@RequestParam(value = "page", defaultValue = "0")  int page,
    						@RequestParam(value = "data", required = false) LocalDate dataSessao, Model model) {
        if (dataSessao == null) {
            // Se a data não for fornecida, redireciona ou utiliza a data atual
            dataSessao = LocalDate.now(); // Ou qualquer outro comportamento desejado
        }

        System.out.println("Data da sessão: " + dataSessao);

        // Buscar a portaria ativa pela data
        Optional<ControlePortaria> portariaOptional = controlePortariaService.findByDataDaSessao(dataSessao);

        if (portariaOptional.isPresent()) {
            ControlePortaria portaria = portariaOptional.get();
            // Listar todas as notas associadas à data da sessão
            Page<Nota> notas = notaService.findNotasByData(portaria.getDataDaSessao(), PageRequest.of(page, 100));

            model.addAttribute("portaria", portaria);
            model.addAttribute("notas", notas);
            return "portaria/portaria_listagem_nota";
        }

        // Se não houver sessão ativa ou portaria correspondente, redirecionar
        return "redirect:/portarias/listar";
    }
    
    @PostMapping("/portarias/iniciar-digitalizacao/{id}")
    public String iniciarDigitacao(@PathVariable("id") Integer id, RedirectAttributes attr) {
        Optional<ControlePortaria> portariaOptional = controlePortariaService.findById(id);
        
        
        
        
        if (portariaOptional.isPresent()) {
            ControlePortaria portaria = portariaOptional.get();
            if (portaria.getStatusSessao() == StatusSessao.ABERTA) {
                portaria.setStatusSessao(StatusSessao.EM_DIGITACAO);
                controlePortariaService.save(portaria);
                attr.addFlashAttribute("success", "Sessão de digitação iniciada com sucesso.");
            } else {
                attr.addFlashAttribute("fail", "A sessão não pode ser iniciada.");
            }
        }

        return "redirect:/portarias/listar";
    }
    
    @PostMapping("/portarias/iniciar-analise/{id}")
    public String iniciarAnalise(@PathVariable("id") Integer id,  RedirectAttributes attr) {
        Optional<ControlePortaria> portariaOptional = controlePortariaService.findById(id);

        if (portariaOptional.isPresent()) {
            ControlePortaria portariaP = portariaOptional.get();
          
            if (portariaP.getStatusSessao() == StatusSessao.EM_DIGITACAO) {
               
            	List<Nota> notasSemPortaria = notaService.findNotasByDataAndNoControlePortaria(portariaP.getDataDaSessao());

            	// Associa as notas sem portaria à portaria atual
            	notasSemPortaria.forEach(nota -> {
            	    nota.setControlePortaria(portariaP);
            	    portariaP.getNotas().add(nota);
            	});
            	
            	double totalPeso = 0.0;
                int totalNotas = portariaP.getNotas().size();
            	
                for (Nota nota : portariaP.getNotas()) {
                      for (ItemDeNota item : nota.getItens()) {
                    	  
                    	  BigDecimal quantidade = new BigDecimal(item.getQuantidade().toString());
                    	  BigDecimal peso = new BigDecimal(item.getEmbalagem().getPeso().toString());

                    	  // Multiplica os valores como BigDecimal
                    	  BigDecimal resultado = quantidade.multiply(peso);

                    	  // Converte para double e adiciona ao totalPeso
                    	  totalPeso += resultado.doubleValue();
                      }
                    
                  }
                
                System.out.println("Total Peso: " + totalPeso);
                System.out.println("Total Notas: " + totalNotas);
                portariaP.setTotalPeso(totalPeso);
                portariaP.setTotalNotas(totalNotas);
                portariaP.setStatusSessao(StatusSessao.EM_ANALISE);
                controlePortariaService.save(portariaP);
                
                attr.addFlashAttribute("success", "Sessão marcada como 'Em Análise' com sucesso.");
            } else {
                attr.addFlashAttribute("error", "A sessão não pode ser marcada como 'Em Análise'.");
            }
        }

        return "redirect:/portarias/listar";
    }
    
    @PostMapping("/portarias/salvar")
    public String salvarPortaria(@ModelAttribute ControlePortaria portaria, RedirectAttributes attr) {
        // Verifica se já existe uma portaria com a mesma data
        Optional<ControlePortaria> sessaoExistente = controlePortariaService.findByDataDaSessao(portaria.getDataDaSessao());

        if (sessaoExistente.isPresent()) {
            // Caso já exista uma portaria com essa data, retorna uma mensagem de erro
            attr.addFlashAttribute("error", "Já existe uma sessão para a data selecionada.");
            return "redirect:/portarias/controle-portaria/portarias/listar";
        }

        // Define o total inicial de notas e peso da nova sessão
        portaria.setTotalNotas(0);
        portaria.setTotalPeso(0.0);

        // Salva a nova portaria
        controlePortariaService.save(portaria);

        // Mensagem de sucesso
        attr.addFlashAttribute("success", "Nova sessão de portaria criada com sucesso!");

        return "redirect:/portarias/listar";
    }
    
    @GetMapping("/portarias/excluir/{id}")
    public String excluirPortaria(@PathVariable("id") Integer id, RedirectAttributes attr) {
        Optional<ControlePortaria> portariaOptional = controlePortariaService.findById(id);
        if (portariaOptional.isPresent()) {
            ControlePortaria portaria = portariaOptional.get();

            // Desvincula as notas da portaria
            for (Nota nota : portaria.getNotas()) {
                nota.setControlePortaria(null); // Remove a referência à portaria
            }

            controlePortariaService.save(portaria); // Salva as notas desvinculadas
            controlePortariaService.delete(portaria); // Exclui a portaria

            attr.addFlashAttribute("success", "Portaria excluída com sucesso.");
        } else {
            attr.addFlashAttribute("error", "Portaria não encontrada.");
        }

        return "redirect:/portarias/listar";
    }
}
