package com.flc.springthymeleaf.repository;

import com.flc.springthymeleaf.domain.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {

	Municipio findByIbge(String ibge);
	
	

}
