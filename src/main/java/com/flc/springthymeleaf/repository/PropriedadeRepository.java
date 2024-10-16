package com.flc.springthymeleaf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.flc.springthymeleaf.domain.Produto;
import com.flc.springthymeleaf.domain.Propriedade;

public interface PropriedadeRepository extends JpaRepository<Propriedade, Integer> {
	
	@Query("SELECT a FROM Propriedade a INNER JOIN a.produto s WHERE s.id = ?1")
	List<Propriedade> findByProduto(Produto produto);

	@Query("SELECT s FROM Propriedade s JOIN FETCH  s.produto g WHERE g.id = ?1")
	List<Propriedade> findPropriedadePorProduto(Integer id);

	@Query("SELECT s FROM Propriedade s WHERE s.status = true")
	List<Propriedade> findPropriedadePorCotacao();

	 boolean existsByCodigo(String codigo);

	 @Query("SELECT p FROM Propriedade p WHERE LOWER(p.codigo) = LOWER(:codigo)")
	    Propriedade findByCodigoIgnoreCase(@Param("codigo") String codigo);
			

	 @Query("SELECT p FROM Propriedade p WHERE p.codigo IS NOT NULL")
	    List<Propriedade> findAllWithNonNullCodigo();

	List<Propriedade> findByCodigoContainingOrVariedadeContainingOrProdutoNomeContaining(String query, String query2,
			String query3);

	@Query("SELECT p FROM Propriedade p WHERE LOWER(p.produto.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
	List<Propriedade> findByProdutoNomeContainingIgnoreCase(@Param("nome") String nome);

	 List<Propriedade> findByProdutoNome(String nome);
	 
	 @Query("SELECT p FROM Propriedade p JOIN p.embalagens e WHERE e.id = :id")
	 List<Propriedade> findPropriedadesByEmbalagemId(@Param("id") Integer id);
}
