package com.flc.springthymeleaf.domain;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DestinoInterno implements Serializable {

	
	    private static final long serialVersionUID = 1L;

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
	    
	    
	    private String codigoSimab;
	    private String sigla;
	    private String descricao;
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
		public String getDescricao() {
			return descricao;
		}
		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
	    
	    
	    
	    
	    
}
