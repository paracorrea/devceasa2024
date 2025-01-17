package com.flc.springthymeleaf.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


import com.flc.springthymeleaf.domain.Nota;


public interface NotaRepository  extends JpaRepository<Nota, Integer>, PagingAndSortingRepository<Nota, Integer> {

	  @Query("SELECT f FROM Nota f WHERE f.data BETWEEN :dataInicio AND :dataFim ORDER BY f.data DESC")
	    Page<Nota> findByDataNotaBetween(LocalDate dataInicio, LocalDate dataFim, Pageable pageable);

	    @Query("SELECT f FROM Nota f ORDER BY f.data DESC")
	    Page<Nota> findAll(Pageable pageable);
	    
	    @Query("SELECT f FROM Nota f WHERE f.data = :dataSessao ORDER BY f.data DESC")
	    Page<Nota> findAllByData(@Param("dataSessao") LocalDate dataSessao, Pageable pageable);
	    	
	    @Query("SELECT n FROM Nota n WHERE n.controlePortaria IS NULL AND n.data = :dataSessao")
	    List<Nota> findNotasByDataAndNoControlePortaria(@Param("dataSessao") LocalDate dataSessao);

	    @Query(value = "SELECT n.id, n.controle_portaria_id, n.data, n.faixa_horario, n.local_destino, n.municipio_id, n.peso_total, n.portaria, n.tipo_veiculo " +
	               "FROM nota n " +
	               "JOIN item_de_nota i ON n.id = i.nota_id " +
	               "JOIN propriedade p ON i.propriedade_id = p.id " +
	               "WHERE (n.data >= COALESCE(:dataInicio, n.data)) " +
	               "AND (n.data <= COALESCE(:dataFim, n.data)) " +
	               "AND (p.codigo = COALESCE(:codigoPropriedade, p.codigo)) " +
	               "ORDER BY n.data", 
	       nativeQuery = true)
		Page<Nota> findByDataNotaBetweenAndCodigo(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim, @Param("codigoPropriedade") String codigoPropriedade, Pageable pageable);


		
	  
}
