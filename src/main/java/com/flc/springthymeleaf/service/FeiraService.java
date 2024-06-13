package com.flc.springthymeleaf.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.flc.springthymeleaf.domain.Feira;
import com.flc.springthymeleaf.repository.FeiraRepository;

import enums.StatusFeira;
import jakarta.transaction.Transactional;

@Service
public class FeiraService {

	
	    @Autowired
	    private FeiraRepository feiraRepository;

	   

	    // ... outros métodos ...

	    @Transactional
	    public void publicarFeira(Long feiraId) {
	        Feira feira = feiraRepository.findById(feiraId)
	        	.orElseThrow(() -> new RuntimeException("Feira não encontrada"));

	        if (feira.getStatus() != StatusFeira.FECHADA) {
	            throw new RuntimeException("Feira não encontrada");
	        }

	       
	        feira.setStatus(StatusFeira.PUBLICADA);
	        feiraRepository.save(feira);

	        

	        // ... (gerar PDF da feira) ...
	    }
	    
	    
	    public void verificarSePodeEditar(Long id) {
	        Feira feira = feiraRepository.findById(id).orElseThrow(() -> new RuntimeException("Feira não encontrada"));
	        if (feira.getStatus() == StatusFeira.PUBLICADA) {
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
	        novaFeira.setStatus(StatusFeira.ABERTA); // Define o status como ABERTA
	        novaFeira.setNumero(ultimoNumero);
	        return feiraRepository.save(novaFeira);
	    }
	    
	    public Feira salvarFeira(Feira feira) {
	    	return feiraRepository.save(feira);
	    }
	    
		public List<Feira> findAll() {
			// TODO Auto-generated method stub
			return feiraRepository.findAll();
		}
		
		
		public Long ObterNumero() {
			
	    	 Long ultimoNumero = feiraRepository.findMaxNumero();
		        if (ultimoNumero == null) {
		            ultimoNumero = (long) +1;
		        }
			return ultimoNumero;
		}
	  
	}

	
	

