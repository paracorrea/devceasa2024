package com.flc.springthymeleaf.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Municipio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


	private String ibge;
    private String codigo;
    private String uf;
    private String nome;

    @OneToMany(mappedBy = "municipio", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Nota> notas;
    // Getters and setters
   

    public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}
    
    public String getIbge() {
        return ibge;
    }

    

	public void setIbge(String ibge) {
        this.ibge = ibge;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

	/*
	 * public List<Nota> getNotas() { return notas; }
	 * 
	 * public void setNotas(List<Nota> notas) { this.notas = notas; }
	 */

    
}
