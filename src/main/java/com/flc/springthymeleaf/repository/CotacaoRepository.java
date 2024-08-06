package com.flc.springthymeleaf.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.flc.springthymeleaf.domain.Cotacao;

import jakarta.persistence.QueryHint;

public interface CotacaoRepository extends JpaRepository<Cotacao, Integer> {
	
	// passar todas as propriedades uma lista que esteja marcado como cotado
	// mostrar os itens desta lista para o form

	
	 @Query("SELECT COUNT(c) > 0 FROM Cotacao c " +
	           "WHERE c.propriedade.id = :propriedadeId " +
	           "AND c.dataCotacao = :dataCotacao")
	    boolean existsByPropriedadeAndDataCotacao(@Param("propriedadeId") Integer propriedadeId,
	                                             @Param("dataCotacao") LocalDate dataCotacao);

	List<Cotacao> findByDataCotacao(LocalDate selectedDate);

	
	 @Query(value = "SELECT * FROM cotacao c WHERE c.propriedade_id = :propriedadeId AND c.data_cotacao <= :dataCotacao ORDER BY c.data_cotacao DESC LIMIT 1", nativeQuery = true)
	    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "false") })
	    Optional<Cotacao> findTopByPropriedadeIdAndDataCotacaoLessThanEqualOrderByDataCotacaoDesc(
	        @Param("propriedadeId") Integer propriedadeId,
	        @Param("dataCotacao") LocalDate dataCotacao);
	
	    @Query("SELECT c FROM Cotacao c WHERE c.propriedade.id = :propriedadeId AND c.dataCotacao BETWEEN :dataInicio AND :dataFim")
	    List<Cotacao> findByPropriedadeIdAndDataCotacaoBetween(@Param("propriedadeId") Integer propriedadeId, 
	                                                            @Param("dataInicio") LocalDate dataInicio, 
	                                                            @Param("dataFim") LocalDate dataFim);
	
}
