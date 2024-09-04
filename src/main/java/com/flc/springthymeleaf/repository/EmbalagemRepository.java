package com.flc.springthymeleaf.repository;


import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.flc.springthymeleaf.domain.Embalagem;


public interface EmbalagemRepository  extends JpaRepository<Embalagem, Integer>, PagingAndSortingRepository<Embalagem, Integer> {

	
	    List<Embalagem> findAll();

		Optional<Embalagem> findByCodigo(String codigo);

}
