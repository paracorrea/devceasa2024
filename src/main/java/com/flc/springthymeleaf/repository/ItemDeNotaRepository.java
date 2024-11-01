package com.flc.springthymeleaf.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.flc.springthymeleaf.DTO.RegistroProhortDTO;
import com.flc.springthymeleaf.domain.ItemDeNota;


@Repository
public interface ItemDeNotaRepository extends JpaRepository<ItemDeNota, Integer> {

	  @Query(value = "SELECT prod.nome AS produto, SUM(i.quantidade * e.peso) AS total_peso " +
              "FROM item_de_nota i " +
              "JOIN propriedade prop ON i.propriedade_id = prop.id " +
              "JOIN produto prod ON prop.produto_id = prod.id " +
              "JOIN embalagem e ON i.embalagem_id = e.id " +
              "JOIN nota n ON i.nota_id = n.id " +
              "WHERE n.data BETWEEN :startDate AND :endDate " +
              "GROUP BY prod.nome " +
              "ORDER BY total_peso DESC " +
              "LIMIT 5", nativeQuery = true)
	
	 List<Object[]> findTop5ProdutosByPesoBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	 
	 @Query(value = "SELECT prop.codigo AS codigoPropriedade, m.codigo AS codigoMunicipio, SUM(i.quantidade * e.peso) AS total_peso, AVG(c.preco_medio) AS precoMedio " +
             "FROM item_de_nota i " +
             "JOIN propriedade prop ON i.propriedade_id = prop.id " +
             "JOIN nota n ON i.nota_id = n.id " +
             "JOIN municipio m ON n.municipio_id = m.id " +
             "JOIN cotacao c ON prop.id = c.propriedade_id " +
             "JOIN embalagem e ON i.embalagem_id = e.id " +
             "WHERE EXTRACT(YEAR FROM n.data) = :ano AND EXTRACT(MONTH FROM n.data) = :mes " +
             "GROUP BY prop.codigo, m.codigo",
             nativeQuery = true)
	 List<Object[]> findDadosParaProhort(@Param("ano") int ano, @Param("mes") int mes);
}