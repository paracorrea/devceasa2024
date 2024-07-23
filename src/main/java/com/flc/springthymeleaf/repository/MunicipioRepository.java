package com.flc.springthymeleaf.repository;

import com.flc.springthymeleaf.domain.Municipio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {

    List<Municipio> findAll();
    Optional<Municipio> findById(Long id);
    Municipio findByIbge(String ibge);
    
    List<Municipio> findByNomeContaining(String nome);
    List<Municipio> findByNomeContainingAndUf(String nome, String uf);
    List<Municipio> findByUf(String uf);
	List<Municipio> findByNomeContainingIgnoreCase(String nome);
	List<Municipio> findByUfIgnoreCase(String uf);
    
	
	   List<Municipio> findByNomeContainingIgnoreCaseAndUfContainingIgnoreCaseAndCodigoContainingIgnoreCase(String nome, String uf, String codigo);
	}

