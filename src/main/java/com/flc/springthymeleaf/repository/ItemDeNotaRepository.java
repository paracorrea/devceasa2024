package com.flc.springthymeleaf.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


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
              "ORDER BY total_peso DESC ", nativeQuery = true)
	
	 List<Object[]> findTop5ProdutosByPesoBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	 
	
	 
	 
	 @Query(value = "SELECT sub.nome AS subgrupo, SUM(i.quantidade * e.peso) AS total_peso " +
             "FROM item_de_nota i " +
             "JOIN propriedade prop ON i.propriedade_id = prop.id " +
             "JOIN produto prod ON prop.produto_id = prod.id " +
             "JOIN subgrupo sub ON prod.subgrupo_id = sub.id " +
             "JOIN embalagem e ON i.embalagem_id = e.id " +
             "JOIN nota n ON i.nota_id = n.id " +
             "WHERE n.data BETWEEN :startDate AND :endDate " +
             "GROUP BY sub.nome " +
             "ORDER BY total_peso DESC " +
             "LIMIT 6", nativeQuery = true)
	 List<Object[]> findTop5SubgruposByPesoBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	 @Query(value = "SELECT SUM(i.quantidade * e.peso) AS volumeTotal " +
             "FROM item_de_nota i " +
             "JOIN nota n ON i.nota_id = n.id " +
             "JOIN embalagem e ON i.embalagem_id = e.id " +
             "WHERE n.data BETWEEN :startDate AND :endDate", nativeQuery = true)
	 Double findVolumeTotalEntreDatas(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	 
	 @Query(value = """
			    SELECT 
			        prop.codigo AS codigoPropriedade,
			        m.codigo AS codigoMunicipio,
			        subquery.peso_total AS total_peso,
			        AVG(c.valor_comum) AS valorComum
			    FROM 
			        (
			            SELECT 
			                i.propriedade_id,
			                n.municipio_id,
			                SUM(i.quantidade * e.peso) AS peso_total
			            FROM 
			                item_de_nota i
			            JOIN 
			                embalagem e ON i.embalagem_id = e.id
			            JOIN 
			                nota n ON i.nota_id = n.id
			            WHERE 
			                EXTRACT(YEAR FROM n.data) = :ano 
			                AND EXTRACT(MONTH FROM n.data) = :mes
			            GROUP BY 
			                i.propriedade_id, n.municipio_id
			        ) AS subquery
			    JOIN 
			        propriedade prop ON subquery.propriedade_id = prop.id
			    JOIN 
			        municipio m ON subquery.municipio_id = m.id
			    JOIN 
			        cotacao c ON prop.id = c.propriedade_id
			    GROUP BY 
			        prop.codigo, m.codigo, subquery.peso_total
			""", nativeQuery = true)
			List<Object[]> findDadosParaProhort(@Param("ano") int ano, @Param("mes") int mes);


	 
	 @Query(value = """
			    SELECT 
			        prop.codigo AS codigoPropriedade,
			        m.codigo AS codigoMunicipio,
			        subquery.peso_total AS total_peso,
			        0 AS valorComum
			    FROM 
			        (
			            SELECT 
			                i.propriedade_id,
			                n.municipio_id,
			                SUM(i.quantidade * e.peso) AS peso_total
			            FROM 
			                item_de_nota i
			            JOIN 
			                embalagem e ON i.embalagem_id = e.id
			            JOIN 
			                nota n ON i.nota_id = n.id
			            WHERE 
			                EXTRACT(YEAR FROM n.data) = :ano 
			                AND EXTRACT(MONTH FROM n.data) = :mes
			            GROUP BY 
			                i.propriedade_id, n.municipio_id
			        ) AS subquery
			    JOIN 
			        propriedade prop ON subquery.propriedade_id = prop.id
			    JOIN 
			        municipio m ON subquery.municipio_id = m.id
			    LEFT JOIN 
			        cotacao c ON prop.id = c.propriedade_id
			    WHERE 
			        c.id IS NULL
			    GROUP BY 
			        prop.codigo, m.codigo, subquery.peso_total
			""", nativeQuery = true)
			List<Object[]> findProdutosSemCotacao(@Param("ano") int ano, @Param("mes") int mes);

	 
	
			
			@Query(value = """
				    SELECT 
				        AVG(c.valor_comum) AS mediaValorComum
					FROM 
						cotacao c
					JOIN 
						propriedade prop ON c.propriedade_id = prop.id

				    WHERE 
				        prop.codigo LIKE CONCAT(:codigoBase, '%') 
				        AND prop.referencial_valor = true
				        AND EXTRACT(YEAR FROM c.data_cotacao) = :ano
				        AND EXTRACT(MONTH FROM c.data_cotacao) = :mes
				""", nativeQuery = true)
				Object[] findCotaçõesRelacionadasReferenciais(@Param("codigoBase") String codigoBase, @Param("ano") int ano, @Param("mes") int mes);




			@Query(value = """
				    SELECT 
				        prop.codigo AS codigoPropriedade,
				        m.codigo AS codigoMunicipio,
				        SUM(i.quantidade * e.peso) AS total_peso,
				        AVG(c.valor_comum) AS valorComum
				    FROM 
				        item_de_nota i
				    JOIN 
				        propriedade prop ON i.propriedade_id = prop.id
				    JOIN 
				        nota n ON i.nota_id = n.id
				    JOIN 
				        municipio m ON n.municipio_id = m.id
				    JOIN 
				        cotacao c ON prop.id = c.propriedade_id
				    JOIN 
				        embalagem e ON i.embalagem_id = e.id
				    WHERE 
				        EXTRACT(YEAR FROM n.data) = :ano 
				        AND EXTRACT(MONTH FROM n.data) = :mes
				        AND EXTRACT(DAY FROM n.data) = :dia
				    GROUP BY 
				        prop.codigo, m.codigo
				""", nativeQuery = true)
				List<Object[]> findDadosParaProhortPorDia(@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);

	



}