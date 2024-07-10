package com.flc.springthymeleaf.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.flc.springthymeleaf.domain.Nota;


public interface NotaRepository  extends JpaRepository<Nota, Integer>{

	

	  @Query("SELECT f FROM Nota f WHERE f.dataCaptura BETWEEN :dataInicio AND :dataFim ORDER BY f.dataCaptura DESC")
	    Page<Nota> findByDataNotaBetween(LocalDate dataInicio, LocalDate dataFim, Pageable pageable);

	    @Query("SELECT f FROM Nota f ORDER BY f.dataCaptura DESC")
	    Page<Nota> findAll(Pageable pageable);
	
}
