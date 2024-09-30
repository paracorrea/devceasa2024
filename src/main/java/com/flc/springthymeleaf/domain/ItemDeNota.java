package com.flc.springthymeleaf.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import java.io.Serializable;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


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
    
    @ManyToOne
    @JoinColumn(name = "embalagem_id", nullable = false)  // Relacionamento com Embalagem
    private Embalagem embalagem;
    
    @Column(name = "pesoItem", precision = 10, scale = 1)
    @Digits(integer = 10, fraction = 1, message = "O valor deve ter no máximo 10 dígitos inteiros e 1 decimal")
    private BigDecimal pesoItem;

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

	public Embalagem getEmbalagem() {
		return embalagem;
	}

	
	public void setEmbalagem(Embalagem embalagem) {
		this.embalagem = embalagem;
	}

	public BigDecimal getPesoItem() {
		return pesoItem;
	}

	public void setPesoItem(BigDecimal pesoItem) {
		this.pesoItem = pesoItem;
	}



	



    // Getters and setters
    
    
}
