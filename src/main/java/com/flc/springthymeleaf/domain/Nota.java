package com.flc.springthymeleaf.domain;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;


@Entity(name = "Nota")
@Table(name = "nota")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "numero_da_nota")
    @NotNull(message = "Campo não pode ser nulo")
    private String numeroDaNotaFiscal;

    @DateTimeFormat(iso = ISO.DATE)
    @Column(name = "data_captura", columnDefinition = "DATE")
    @NotNull(message = "Campo não pode ser nulo")
    public LocalDate dataCaptura;
    
    @Column(name = "cnpj_emissor")
    private String cnpjEmissor;
    
    @Column(name = "cnpj_destinatario")
    private String cnpjDestinatario;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumeroDaNotaFiscal() {
		return numeroDaNotaFiscal;
	}

	public void setNumeroDaNotaFiscal(String numeroDaNotaFiscal) {
		this.numeroDaNotaFiscal = numeroDaNotaFiscal;
	}

	public LocalDate getDatacaptura() {
		return dataCaptura;
	}

	public void setDataCaptura(LocalDate dataCaptura) {
		this.dataCaptura = dataCaptura;
	}

	public String getCnpjEmissor() {
		return cnpjEmissor;
	}

	public void setCnpjEmissor(String cnpjEmissor) {
		this.cnpjEmissor = cnpjEmissor;
	}

	public String getCnpjDestinatario() {
		return cnpjDestinatario;
	}

	public void setCnpjDestinatario(String cnpjDestinatario) {
		this.cnpjDestinatario = cnpjDestinatario;
	}

	public LocalDate getDataCaptura() {
		return dataCaptura;
	}

	

	}
    
    
    



