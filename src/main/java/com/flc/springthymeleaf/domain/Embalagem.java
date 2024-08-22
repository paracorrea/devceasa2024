package com.flc.springthymeleaf.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Digits;

@Entity
public class Embalagem implements Serializable {

    private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
   
    @Column(name = "codigo", length = 10, unique = true)
    private String codigo;
   
    @Column(name = "tipo", length = 30 )
    private String tipo;
    
    @Column(name = "peso", precision = 10, scale = 1)
    @Digits(integer = 10, fraction = 1, message = "O valor deve ter no máximo 10 dígitos inteiros e 1 decimal")
    private BigDecimal peso;

	public Integer getId() {
		return id;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getTipo() {
		return tipo;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}
   
    
    
    
    
    
    
	
}
