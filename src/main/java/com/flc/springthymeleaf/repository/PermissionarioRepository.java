

	package com.flc.springthymeleaf.repository;

import com.flc.springthymeleaf.domain.Permissionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionarioRepository extends JpaRepository<Permissionario, String> {
}

