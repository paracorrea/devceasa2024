package com.flc.springthymeleaf.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "veiculo")
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String placa;

    // Relacionamento com Transportador
    @ManyToOne
    @JoinColumn(name = "transportador_id")
    private Transportador transportador;

    // Relacionamento com Fornecedor (muitos para muitos)
    @ManyToMany
    @JoinTable(
        name = "fornecedor_veiculo",
        joinColumns = @JoinColumn(name = "veiculo_id"),
        inverseJoinColumns = @JoinColumn(name = "fornecedor_id")
    )
    private List<Emissor> fornecedores;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public Transportador getTransportador() {
		return transportador;
	}

	public void setTransportador(Transportador transportador) {
		this.transportador = transportador;
	}

	public List<Emissor> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<Emissor> fornecedores) {
		this.fornecedores = fornecedores;
	}

    // Getters and Setters
    
    
    
}
