package com.flc.springthymeleaf.repository;



import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.flc.springthymeleaf.domain.ControlePortaria;
import com.flc.springthymeleaf.enums.StatusSessao;



public interface ControlePortariaRepository  
			extends JpaRepository<ControlePortaria, Integer>, 
			PagingAndSortingRepository<ControlePortaria, Integer> {

	
	ControlePortaria findByDataDaSessao(LocalDate hoje);

	Optional<ControlePortaria> findByStatusSessao(StatusSessao statusSessao);

	

	
}
