package com.flc.springthymeleaf.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.flc.springthymeleaf.domain.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

	// RETURN A LIST OF THE PRODUCTS IN A SUBGROUP
	@Query("SELECT a FROM Produto a INNER JOIN a.subgrupo s WHERE s.id = ?1")
	List<Produto> findSubgrupoEmProduto(Integer id, Pageable pageable);

	@Query("SELECT p FROM Produto p INNER JOIN p.subgrupo s INNER JOIN s.grupo g WHERE g.id = ?1")
	List<Produto> findProdutoPertencenteAoGrupo(Integer id);

	@Query("SELECT t FROM Produto t WHERE t.nome = ?1")
	List<Produto> findByNome(String nome);

	@Query("SELECT p FROM Produto p INNER JOIN p.subgrupo g WHERE g.id = ?1")
	List<Produto> findGrupoPorSubgrupo(Integer id);
	
	

}
