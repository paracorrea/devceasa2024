package com.flc.springthymeleaf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flc.springthymeleaf.domain.ItemDeNota;

@Repository
public interface ItemDeNotaRepository extends JpaRepository<ItemDeNota, Integer> {
}