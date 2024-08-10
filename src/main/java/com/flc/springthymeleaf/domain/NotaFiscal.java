/*
 * package com.flc.springthymeleaf.domain;
 * 
 * import java.io.Serializable; import java.util.List; import java.util.Map;
 * import jakarta.persistence.Column; import jakarta.persistence.Entity; import
 * jakarta.persistence.GeneratedValue; import
 * jakarta.persistence.GenerationType; import jakarta.persistence.Id; import
 * jakarta.persistence.Table;
 * 
 * 
 * //@Entity //@Table(name = "notafiscal") public class NotaFiscal implements
 * Serializable {
 * 
 * private static final long serialVersionUID = 1L;
 * 
 * @Id
 * 
 * @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
 * 
 * 
 * @Column(columnDefinition = "jsonb") private Map<String, Object> notafiscal;
 * 
 * @SuppressWarnings("unchecked") public List<Map<String, Object>> getItens() {
 * return (List<Map<String, Object>>) notafiscal.get("itens"); }
 * 
 * // Getters and Setters public Integer getId() { return id; }
 * 
 * public void setId(Integer id) { this.id = id; }
 * 
 * public Map<String, Object> getNotafiscal() { return notafiscal; }
 * 
 * public void setNotafiscal(Map<String, Object> notafiscal) { this.notafiscal =
 * notafiscal; } }
 */