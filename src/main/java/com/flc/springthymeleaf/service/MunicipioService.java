package com.flc.springthymeleaf.service;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.flc.springthymeleaf.domain.Municipio;
import com.flc.springthymeleaf.repository.MunicipioRepository;

@Service
public class MunicipioService {

	private final MunicipioRepository municipioRepository;
	
	
	public MunicipioService(MunicipioRepository municipioRepository) {
		super();
		this.municipioRepository = municipioRepository;
	}


	
	    public List<Municipio> findAll() {
	        return municipioRepository.findAll();
	    }

	    
	    public Optional<Municipio> findById(Long id) {
	        return municipioRepository.findById(id);
	    }

	    
	    public Optional<Municipio> findByIbge(String ibge) {
	        return Optional.ofNullable(municipioRepository.findByIbge(ibge));
	    }
	
	
}
