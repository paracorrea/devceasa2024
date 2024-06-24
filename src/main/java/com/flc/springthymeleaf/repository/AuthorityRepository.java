package com.flc.springthymeleaf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flc.springthymeleaf.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long>{

}
