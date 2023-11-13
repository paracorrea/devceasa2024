package com.flc.springthymeleaf.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "subgrupo")
public class Subgrupo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;

	@Column(name = "nome", length = 50)
	@NotBlank(message ="Campo não pode ser em branco")
	@NotNull(message="Campo não pode ser nullo")
	private String nome;

	@NotNull(message = "Selecione grupo relativo ao subgrupo")
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
	@ManyToOne(cascade= CascadeType.ALL )
	@JoinColumn(name = "grupo_id")
	@JsonIgnore
	private Grupo grupo;

	
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
	@OneToMany(mappedBy = "subgrupo",
				fetch = FetchType.LAZY, 
				cascade =  CascadeType.ALL)
	
	@JsonIgnore
	private List<Produto> produtos = new ArrayList<>();;

	public Subgrupo() {
	}

	public Subgrupo(String nome) {
		this.nome = nome;
	}
	
	
	// Methods bestPratic in relationship between  Subgrupo and Product ---------
	public void addProduto(Produto theProduto) {
		
		this.produtos.add(theProduto);
		theProduto.setSubgrupo(this);
	
	}

	 public List<Produto> getProduto() {
	        List<Produto> listaSegura = Collections.unmodifiableList(this.produtos);  
	        return listaSegura;
	    }
	 // --------------------------------------------------------------------------
	 
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	

	
	
	
	
	
}