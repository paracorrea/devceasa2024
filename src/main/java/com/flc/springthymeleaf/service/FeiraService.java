package com.flc.springthymeleaf.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flc.springthymeleaf.domain.Feira;
import com.flc.springthymeleaf.repository.FeiraRepository;
import com.flc.springthymeleaf.service.exceptions.FeiraNaoEncontradaException;
import com.flc.springthymeleaf.service.exceptions.FeiraNaoPodeSerEncerradaException;

import enums.StatusFeira;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@Transactional(readOnly = false)
public class FeiraService {

	
	    @Autowired
	    private FeiraRepository feiraRepository;

	    private static final Logger logger = LoggerFactory.getLogger(Feira.class);

	    // ... outros métodos ...

	 
	    
	    
	    @Transactional
	    public void publicarFeira(Long feiraId) {
	       
	    	Feira feira = feiraRepository.findById(feiraId)
	        	.orElseThrow(() -> new RuntimeException("Feira não encontrada"));

	        if (feira.getStatusFeira() != StatusFeira.FECHADA) {
	            throw new RuntimeException("Feira não encontrada");
	        }

	       
	        feira.setStatusFeira(StatusFeira.PUBLICADA);
	        feiraRepository.save(feira);

	        

	        // ... (gerar PDF da feira) ...
	    }
	    
	    
	    
	    // first metodo findAll
		public List<Feira> findAll() {
			List<Feira> listFeiras = feiraRepository.findAll();
			listFeiras.sort(Comparator.comparing(Feira::getDataFeira).reversed()); 
			return (listFeiras);
		}
		
		
		
		
		
		
	    public void verificarSePodeEditar(Long id) {
	        Feira feira = feiraRepository.findById(id).orElseThrow(() -> new RuntimeException("Feira não encontrada"));
	        if (feira.getStatusFeira() == StatusFeira.PUBLICADA) {
	            throw new RuntimeException("Não é possível editar uma feira publicada");
	        }
	    }

	    public boolean verificarSeJaExiste(LocalDate data) {
	        return feiraRepository.existsByDataFeira(data); // Usa o método existsByDataFeira
	    }

	  
	    public Feira criarNovaFeira(LocalDate data) {
	        
	    	 Long ultimoNumero = feiraRepository.findMaxNumero();
		        if (ultimoNumero == null) {
		            ultimoNumero = (long) +1 ;
		        }
	    	Feira novaFeira = new Feira();
	        novaFeira.setDataFeira(data);
	        novaFeira.setStatusFeira(StatusFeira.ABERTA); // Define o status como ABERTA
	        novaFeira.setNumero(ultimoNumero);
	        return feiraRepository.save(novaFeira);
	    }
	    
	    public Feira salvarFeira(Feira feira) {
	    	return feiraRepository.save(feira);
	    }
	    

		
		public Long ObterNumero() {
			
	    	 Long ultimoNumero = feiraRepository.findMaxNumero();
		        if (ultimoNumero == null) {
		            ultimoNumero = (long) +1;
		        }
			return ultimoNumero;
		}


		@Transactional
		public void encerrarFeira(Long id) {
			
			  logger.info("Encerrando feira com ID: {}", id); // Adicione este log
		    
		    Feira feira = feiraRepository.findById(id)
		            .orElseThrow(() -> new FeiraNaoEncontradaException(id));

		    if (feira.getStatusFeira() != StatusFeira.ABERTA) {
		        throw new FeiraNaoPodeSerEncerradaException("Apenas feiras abertas podem ser encerradas.");
		    }

		    feira.setStatusFeira(StatusFeira.FECHADA);
		    feiraRepository.save(feira);
		}



		public Feira findByDataFeira(LocalDate dataFeira) {
			// TODO Auto-generated method stub
			return feiraRepository.findByDataFeira(dataFeira);
		}
	  
	}

	
	

