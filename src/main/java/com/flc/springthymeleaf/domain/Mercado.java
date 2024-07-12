package com.flc.springthymeleaf.domain;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// banco de dados representa o mercado interno da ceasa campinas

@Entity
@Table(name = "mercado")
public class Mercado implements Serializable {

    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String codigoSimab;
	private String sigla;
	private String nome;
	private String segmento;
	private String setor;
	private String atividade;
	private String mercadoSup;
	private String uf;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
		
	public String getCodigoSimab() {
		return codigoSimab;
	}
	public void setCodigoSimab(String codigoSimab) {
		this.codigoSimab = codigoSimab;
	}
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSegmento() {
		return segmento;
	}
	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}
	public String getSetor() {
		return setor;
	}
	public void setSetor(String setor) {
		this.setor = setor;
	}
	public String getAtividade() {
		return atividade;
	}
	public void setAtividade(String atividade) {
		this.atividade = atividade;
	}
	public String getMercadoSup() {
		return mercadoSup;
	}
	public void setMercadoSup(String mercadoSup) {
		this.mercadoSup = mercadoSup;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	
	
}
