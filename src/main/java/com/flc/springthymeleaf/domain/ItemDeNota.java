package com.flc.springthymeleaf.domain;

import jakarta.persistence.*;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flc.springthymeleaf.enums.UnidadeMedida;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ItemDeNota implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "nota_id")
    @JsonBackReference
    private Nota nota;

    @ManyToOne
    @JoinColumn(name = "propriedade_id")
    @JsonBackReference
    private Propriedade propriedade;

    private Double quantidade;

    @Enumerated(EnumType.STRING)
    private UnidadeMedida unidadeMedida;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Nota getNota() {
		return nota;
	}

	public void setNota(Nota nota) {
		this.nota = nota;
	}

	public Propriedade getPropriedade() {
		return propriedade;
	}

	public void setPropriedade(Propriedade propriedade) {
		this.propriedade = propriedade;
	}

	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

    // Getters and setters
    
    
}
