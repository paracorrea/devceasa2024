package com.flc.springthymeleaf.repository;

import com.flc.springthymeleaf.domain.Mercado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MercadoRepository extends JpaRepository<Mercado, Integer> {
}
