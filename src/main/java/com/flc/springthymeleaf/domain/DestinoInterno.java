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
	    private String codigo;
	    private String descricao;
	    private String segmento;
		
	    
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
		public String getCodigo() {
			return codigo;
		}
		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}
		public String getDescricao() {
			return descricao;
		}
		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
		public String getSegmento() {
			return segmento;
		}
		public void setSegmento(String segmento) {
			this.segmento = segmento;
		}
	    
}
