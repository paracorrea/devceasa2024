package com.flc.springthymeleaf.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.flc.springthymeleaf.domain.Embalagem;


public interface EmbalagemRepository  extends JpaRepository<Embalagem, Integer>, PagingAndSortingRepository<Embalagem, Integer> {

	
	    Page<Embalagem> findAll(Pageable pageable);
}
