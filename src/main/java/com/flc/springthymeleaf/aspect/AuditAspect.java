package com.flc.springthymeleaf.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flc.springthymeleaf.domain.AuditLog;
import com.flc.springthymeleaf.domain.Nota;
import com.flc.springthymeleaf.domain.NotaFiscal;
import com.flc.springthymeleaf.repository.AuditLogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.persistence.Id;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@Aspect
@Component
public class AuditAspect {

    @Autowired
    private AuditLogRepository auditLogRepository;

    private ObjectMapper objectMapper;

    public AuditAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private static final Logger logger = Logger.getLogger(AuditAspect.class.getName());

    @Pointcut("execution(* com.flc.springthymeleaf.service.*.save*(..))")
    public void savePointcut() {}

    @Pointcut("execution(* com.flc.springthymeleaf.service.*.delete*(..))")
    public void deletePointcut() {}

    @Before("savePointcut()")
    public void beforeSave(JoinPoint joinPoint) {
        Object entity = joinPoint.getArgs()[0];
        try {
            AuditLog auditLog = createAuditLog(entity, "SAVE");
            auditLog.setNewData(objectMapper.writeValueAsString(entity));
            auditLogRepository.save(auditLog);
            logger.info("LOGG: Auditoria SAVE registrada para entidade: " + entity.getClass().getSimpleName());
        } catch (JsonProcessingException e) {
            logger.severe("Erro ao serializar dados para auditoria: " + e.getMessage());
        }
    }

    @Before("deletePointcut()")
    public void beforeDelete(JoinPoint joinPoint) {
        Object entity = joinPoint.getArgs()[0];
        try {
            AuditLog auditLog = createAuditLog(entity, "DELETE");
            auditLog.setOldData(objectMapper.writeValueAsString(entity));
            auditLogRepository.save(auditLog);
            logger.info("LOGG: Auditoria DELETE registrada para entidade: " + entity.getClass().getSimpleName());
        } catch (JsonProcessingException e) {
            logger.severe("Erro ao serializar dados para auditoria: " + e.getMessage());
        }
    }

    private AuditLog createAuditLog(Object entity, String operation) {
        AuditLog auditLog = new AuditLog();
        auditLog.setOperation(operation);
        auditLog.setEntity(entity.getClass().getSimpleName());
        auditLog.setEntityId(getEntityId(entity));
        auditLog.setUsername(getCurrentUsername());
        auditLog.setOperationTime(LocalDateTime.now());
        return auditLog;
    }

    private Object getEntityId(Object entity) {
        try {
            Field idField = findIdField(entity.getClass());
            if (idField != null) {
                idField.setAccessible(true);
                return idField.get(entity);
            } else {
                throw new RuntimeException("Campo ID não encontrado na entidade " + entity.getClass().getSimpleName());
            }
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível obter o ID da entidade", e);
        }
    }

    private Field findIdField(Class<?> clazz) {
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    return field;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
