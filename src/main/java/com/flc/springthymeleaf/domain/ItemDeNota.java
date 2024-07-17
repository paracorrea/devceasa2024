package com.flc.springthymeleaf.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "item_de_nota")
public class ItemDeNota implements Serializable {

    private static final long serialVersionUID = 1L;
  
  
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    

    @ManyToOne
    @JoinColumn(name = "nota_id")
    //@NotNull(message = "Nota fiscal não pode ser nula")
    @JsonIgnore
    private Nota nota;

    
    @ManyToOne
    @JoinColumn(name = "propriedade_id")
    private Propriedade propriedade;
    
    @Column(name = "quantidade")
    //@NotNull(message = "Quantidade não pode ser nula")
    private BigDecimal quantidade;

    @Column(name = "volume")
    //@NotNull(message = "Volume não pode ser nula")
    private BigDecimal volume;

    @Column(name = "valor_unitario")
    //@NotNull(message = "Valor unitário não pode ser nulo")
    private BigDecimal valorUnitario;

    @Column(name = "valor_total")
    //@NotNull(message = "Valor total não pode ser nulo")
    private BigDecimal valorTotal;

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

	public Propriedade getPropriedades() {
		return propriedade;
	}

	public void setPropriedades(Propriedade propriedade) {
		this.propriedade = propriedade;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	  public Propriedade getPropriedade() {
			return propriedade;
		}

		public void setPropriedade(Propriedade propriedade) {
			this.propriedade = propriedade;
		}
}
