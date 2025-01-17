package com.flc.springthymeleaf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flc.springthymeleaf.domain.User;

public interface UserRepository extends JpaRepository<User, String>{

	User findByUsername(String username);

	
	
}
