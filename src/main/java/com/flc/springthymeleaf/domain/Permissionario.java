package com.flc.springthymeleaf.domain;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Permissionario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 14, nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false)
    private String nome;

    
    // Getters and Setters
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

   
}
