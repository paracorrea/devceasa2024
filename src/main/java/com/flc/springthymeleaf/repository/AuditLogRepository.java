package com.flc.springthymeleaf.repository;

import com.flc.springthymeleaf.domain.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

	AuditLog findByUsername(String name);
}
