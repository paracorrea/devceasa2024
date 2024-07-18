package com.flc.springthymeleaf.repository;

import com.flc.springthymeleaf.domain.Municipio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {

    List<Municipio> findAll();
    Optional<Municipio> findById(Long id);
    Municipio findByIbge(String ibge);

}
