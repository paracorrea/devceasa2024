package com.flc.springthymeleaf.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="emissor")
public class Emissor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String razaoEmissor;
    private String cnpjEmissor;
    private String ieEmissor;
    
  
	
    @OneToMany(mappedBy = "emissor")
    private List<Transportador> transportadores = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRazaoEmissor() {
		return razaoEmissor;
	}

	public void setRazaoEmissor(String razaoEmissor) {
		this.razaoEmissor = razaoEmissor;
	}

	public String getCnpjEmissor() {
		return cnpjEmissor;
	}

	public void setCnpjEmissor(String cnpjEmissor) {
		this.cnpjEmissor = cnpjEmissor;
	}

	public String getIeEmissor() {
		return ieEmissor;
	}

	public void setIeEmissor(String ieEmissor) {
		this.ieEmissor = ieEmissor;
	}

	

	public List<Transportador> getTransportadores() {
		return transportadores;
	}

	public void setTransportadores(List<Transportador> transportadores) {
		this.transportadores = transportadores;
	}
    
  
    
    
    
    
	
}
