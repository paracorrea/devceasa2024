package com.flc.springthymeleaf.domain;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="transportador")			
public class Transportador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String razaoSocialTransportador;
    private String cnpjTransportador;
    private String inscricaoTransportador;
 
    @OneToMany(mappedBy = "transportador")
    private List<Veiculo> veiculos;

    @ManyToOne
    @JoinColumn(name = "emissor_id")
    private Emissor emissor;
    
    
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRazaoSocialTransportador() {
		return razaoSocialTransportador;
	}

	public void setRazaoSocialTransportador(String razaoSocialTransportador) {
		this.razaoSocialTransportador = razaoSocialTransportador;
	}

	public String getCnpjTransportador() {
		return cnpjTransportador;
	}

	public void setCnpjTransportador(String cnpjTransportador) {
		this.cnpjTransportador = cnpjTransportador;
	}

	public String getInscricaoTransportador() {
		return inscricaoTransportador;
	}

	public void setInscricaoTransportador(String inscricaoTransportador) {
		this.inscricaoTransportador = inscricaoTransportador;
	}

	

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}
    
    
    
    
}
