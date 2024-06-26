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

@Entity(name = "Feira")
public class Feira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Long numero;
    
    @Enumerated(EnumType.STRING)
    public StatusFeira statusFeira;
   
    @DateTimeFormat(iso = ISO.DATE)
    @Column(name = "datafeira", columnDefinition = "DATE")
    private LocalDate dataFeira;

    // getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDataFeira() {
        return dataFeira;
    }

    public void setDataFeira(LocalDate dataFeira) {
        this.dataFeira = dataFeira;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public StatusFeira getStatusFeira() {
        return statusFeira;
    }

    public void setStatusFeira(StatusFeira statusFeira) {
        this.statusFeira = statusFeira;
    }
}



	
	

