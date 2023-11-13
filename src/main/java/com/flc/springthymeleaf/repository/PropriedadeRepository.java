package com.flc.springthymeleaf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.flc.springthymeleaf.domain.Produto;
import com.flc.springthymeleaf.domain.Propriedade;

public interface PropriedadeRepository extends JpaRepository<Propriedade, Integer> {
	
	@Query("SELECT a FROM Propriedade a INNER JOIN a.produto s WHERE s.id = ?1")
	List<Propriedade> findByProduto(Produto produto);

	@Query("SELECT s FROM Propriedade s JOIN FETCH  s.produto g WHERE g.id = ?1")
	List<Propriedade> findPropriedadePorProduto(Integer id);


}
