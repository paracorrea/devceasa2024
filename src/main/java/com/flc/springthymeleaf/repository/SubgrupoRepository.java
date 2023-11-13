package com.flc.springthymeleaf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.flc.springthymeleaf.domain.Subgrupo;

public interface SubgrupoRepository extends JpaRepository<Subgrupo, Integer> {

	@Query("SELECT t FROM Subgrupo t WHERE t.nome = ?1")
	List<Subgrupo> findSubgrupoByNome(String nome);
	
	@Query("SELECT a FROM Subgrupo a INNER JOIN a.produtos s WHERE s.id = ?1")
	List<Subgrupo> findProdutoEmSubgrupo(Integer id);
	
	@Query("SELECT s FROM Subgrupo s JOIN FETCH  s.grupo g WHERE g.id = ?1")
	List<Subgrupo> findGrupoPorSubgrupo(Integer id);

	
}