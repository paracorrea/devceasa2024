package com.flc.springthymeleaf.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flc.springthymeleaf.domain.NotaFiscal;

@Repository
public interface NotaFiscalRepository extends JpaRepository<NotaFiscal, Integer>{

	boolean existsByChaveAcesso(String chaveAcesso);

	//List<NotaFiscal> findByDataEntrada(LocalDate dataEntrada);


	

}
