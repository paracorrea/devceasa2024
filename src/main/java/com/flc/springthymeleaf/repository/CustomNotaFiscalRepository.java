/*
 * package com.flc.springthymeleaf.repository;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.jdbc.core.JdbcTemplate; import
 * org.springframework.stereotype.Repository; import
 * com.fasterxml.jackson.databind.ObjectMapper; import
 * com.flc.springthymeleaf.domain.NotaFiscal;
 * 
 * 
 * 
 * @Repository public class CustomNotaFiscalRepository {
 * 
 * @Autowired private JdbcTemplate jdbcTemplate;
 * 
 * private final ObjectMapper objectMapper = new ObjectMapper();
 * 
 * public void saveJsonNotaFiscal(NotaFiscal notaFiscal) { try { String
 * jsonNotaFiscal = objectMapper.writeValueAsString(notaFiscal.getNotafiscal());
 * String sql = "INSERT INTO notafiscal (notafiscal) VALUES (CAST(? AS JSONB))";
 * jdbcTemplate.update(sql, jsonNotaFiscal); } catch (Exception e) { throw new
 * RuntimeException("Erro ao salvar Nota Fiscal como JSONB", e); } } }
 */