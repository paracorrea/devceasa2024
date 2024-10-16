package com.flc.springthymeleaf.repository;


import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.flc.springthymeleaf.domain.Embalagem;


public interface EmbalagemRepository  extends JpaRepository<Embalagem, Integer>, PagingAndSortingRepository<Embalagem, Integer> {

	
	    List<Embalagem> findAll();

		Optional<Embalagem> findByCodigo(String codigo);

		@Query("SELECT e FROM Embalagem e JOIN e.propriedades p WHERE p.id = :propriedadeId")
		List<Embalagem> findEmbalagemByPropriedadeId(@Param("propriedadeId") Integer propriedadeId);
		
	
}
