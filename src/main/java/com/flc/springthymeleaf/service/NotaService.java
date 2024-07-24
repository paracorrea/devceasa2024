package com.flc.springthymeleaf.service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.flc.springthymeleaf.domain.Municipio;
import com.flc.springthymeleaf.domain.Nota;
import com.flc.springthymeleaf.repository.NotaRepository;



@Service
public class NotaService {

	private final NotaRepository notaRepository;
	
	 public NotaService(NotaRepository notaRepository) {
	        this.notaRepository = notaRepository;
    }
	
	
	public Page<Nota> findAll(Pageable pageable) {
	        return notaRepository.findAll(pageable);
    }

	public Page<Nota> findByDataNotaBetween(LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
		// TODO Auto-generated method stub
		return notaRepository.findByDataNotaBetween(dataInicio, dataFim, pageable);
	}

	public void save(Nota nota) {
		notaRepository.save(nota);		
	}

	public Optional<Nota> findById(Integer id) {
		// TODO Auto-generated method stub
		return notaRepository.findById(id);
	}

	public void deleteById(Integer id) {
		notaRepository.deleteById(id);
	}
	
}
