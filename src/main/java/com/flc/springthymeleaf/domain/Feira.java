package com.flc.springthymeleaf.domain;

import java.time.LocalDate;

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
	@Column(name = "datafeira", columnDefinition = "DATE")
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

	
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public StatusFeira getStatusFeira() {
		return statusFeira;
	}

	public void setStatusFeira(StatusFeira statusFeira) {
		this.statusFeira = statusFeira;
	}

	
	
	

	

	

    
}



	
	

