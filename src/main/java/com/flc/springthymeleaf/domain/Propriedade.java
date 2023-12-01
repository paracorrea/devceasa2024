package com.flc.springthymeleaf.domain;

import java.io.Serializable;


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
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Propriedade  implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="variedade", length = 50)
	private String variedade;
	
	@Column(name="subvariedade", length = 50)
	private String subvariedade;
	
	@Column(name="classificacao", length = 50)
	private String classificacao;
	
	@Column(name="unidade", length = 25)
	@Size(min =0, max = 2, message = "Campo com dois caracteres no máximo")
	private String unidade;
	
	@Column(name="peso")
	@Min(value = 0, message = "valor mínimo é 0")
	@Max(value = 1000, message = "valor máximo é 1000")
	private Integer peso;
	
	@Column(name="embalagem", length = 25)
	private String embalagem;
	
	// 1 para cotado 0 para não cotar
	
	@Column(columnDefinition = "bit default 0")
	private Boolean status=false;
	
	
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
	
	@ManyToOne(cascade= {CascadeType.PERSIST,CascadeType.MERGE,	CascadeType.DETACH,
							CascadeType.REFRESH})
	@JoinColumn(name = "produto_id")
	@JsonIgnore
	@NotNull(message="Campo não pode ser nullo")
	private Produto produto;
	
	@OneToOne(mappedBy = "propriedade")
	private Cotacao cotacao;
	
	
	public Propriedade() {
		
	}


	public Propriedade(Integer id, String variedade, String subvariedade, String classificacao, String unidade,
			Integer peso, String embalagem, Boolean status, Produto produto) {
		super();
		this.id = id;
		this.variedade = variedade;
		this.subvariedade = subvariedade;
		this.classificacao = classificacao;
		this.unidade = unidade;
		this.peso = peso;
		this.embalagem = embalagem;
		this.status = status;
		this.produto = produto;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getVariedade() {
		return variedade;
	}


	public void setVariedade(String variedade) {
		this.variedade = variedade.toUpperCase();
	}


	public String getSubvariedade() {
		return subvariedade;
	}


	public void setSubvariedade(String subvariedade) {
		this.subvariedade = subvariedade.toUpperCase();
	}


	public String getUnidade() {
		return unidade;
	}


	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}


	public Integer getPeso() {
		return peso;
	}


	public void setPeso(Integer peso) {
		this.peso = peso;
	}


	public String getEmbalagem() {
		return embalagem;
	}


	public void setEmbalagem(String embalagem) {
		this.embalagem = embalagem;
	}

	

	public String getClassificacao() {
		return classificacao;
	}




	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao.toUpperCase();
	}



	public Produto getProduto() {
		return produto;
	}


	public void setProduto(Produto produto) {
		this.produto = produto;
	}


	public Boolean getStatus() {
		return status;
	}


	public void setStatus(Boolean status) {
		this.status = status;
	}


	public Cotacao getCotacao() {
		return cotacao;
	}


	public void setCotacao(Cotacao cotacao) {
		this.cotacao = cotacao;
	}


	
}
