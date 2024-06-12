package com.flc.springthymeleaf.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import enums.StatusFeira;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Feira {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

   // @Column(unique = true, nullable = false)
    private Long numero;

    
    @Enumerated(EnumType.STRING)
    public StatusFeira statusFeira;
   
    
	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "data_feira", columnDefinition = "DATE")
	//@NotNull(message = "Campo não pode ser nullo")
	private LocalDate dataFeira;
    
    
    
    
    
	public LocalDate getDataFeira() {
		return dataFeira;
	}

	public void setDataFeira(LocalDate dataFeira) {
		this.dataFeira = dataFeira;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numeroFeira) {
		this.numero = numeroFeira;
	}

	public StatusFeira getStatus() {
		return statusFeira;
	}

	public void setStatus(StatusFeira status) {
		this.statusFeira = status;
	}

	

	

    
}



	
	

