package com.flc.springthymeleaf.repository;



import org.springframework.data.jpa.repository.JpaRepository;


import com.flc.springthymeleaf.domain.NotaFiscal;

public interface NotaFiscalRepository extends JpaRepository<NotaFiscal, Integer>{

	//List<NotaFiscal> findByDataEntrada(LocalDate dataEntrada);


	

}
