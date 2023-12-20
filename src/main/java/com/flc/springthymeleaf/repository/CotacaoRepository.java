package com.flc.springthymeleaf.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.flc.springthymeleaf.domain.Cotacao;

public interface CotacaoRepository extends JpaRepository<Cotacao, Integer> {
	
	// passar todas as propriedades uma lista que esteja marcado como cotado
	// mostrar os itens desta lista para o form

	
	 @Query("SELECT COUNT(c) > 0 FROM Cotacao c " +
	           "WHERE c.propriedade.id = :propriedadeId " +
	           "AND c.dataCotacao = :dataCotacao")
	    boolean existsByPropriedadeAndDataCotacao(@Param("propriedadeId") Integer propriedadeId,
	                                             @Param("dataCotacao") LocalDate dataCotacao);

	List<Cotacao> findByDataCotacao(LocalDate selectedDate);

		

	

}
