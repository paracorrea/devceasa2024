package com.flc.springthymeleaf.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.flc.springthymeleaf.domain.NotaFiscal;

public interface NotaFiscalRepository extends JpaRepository<NotaFiscal, Integer>{

	List<NotaFiscal> findByDataEntrada(LocalDateTime dataEntrada);


	

}
