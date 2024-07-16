package com.flc.springthymeleaf.service;


import java.util.List;

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

	public void save(Municipio municipio) {
		
		municipioRepository.save(municipio);
	}

	public List<Municipio> findAll() {
		// TODO Auto-generated method stub
		return municipioRepository.findAll();
	}

	public Municipio findByIbge(String ibge) {
		// TODO Auto-generated method stub
		return municipioRepository.findByIbge(ibge);
	}

	
	
}
