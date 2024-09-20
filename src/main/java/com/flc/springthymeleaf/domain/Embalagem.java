package com.flc.springthymeleaf.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.flc.springthymeleaf.enums.TipoEmbalagem;
import com.flc.springthymeleaf.enums.UnidadeMedida;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Digits;

@Entity
public class Embalagem implements Serializable {

    private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
   
    @Column(name = "codigo", length = 10, unique = true)
    private String codigo;
   
    @Enumerated(EnumType.STRING)
    private TipoEmbalagem tipoEmbalagem;
    
    @Column(name = "peso", precision = 10, scale = 1)
    @Digits(integer = 10, fraction = 1, message = "O valor deve ter no máximo 10 dígitos inteiros e 1 decimal")
    private BigDecimal peso;

    @Enumerated(EnumType.STRING)
    private UnidadeMedida unidadeMedida;
    
    @ManyToMany(mappedBy = "embalagens")
    private List<Propriedade> propriedades;
    
	public Integer getId() {
		return id;
	}

	public String getCodigo() {
		return codigo;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo != null ? codigo.toUpperCase() : null;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}
   
	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public TipoEmbalagem getTipoEmbalagem() {
		return tipoEmbalagem;
	}

	public void setTipoEmbalagem(TipoEmbalagem tipoEmbalagem) {
		this.tipoEmbalagem = tipoEmbalagem;
	}

	public List<Propriedade> getPropriedades() {
		return propriedades;
	}

	public void setPropriedades(List<Propriedade> propriedades) {
		this.propriedades = propriedades;
	}
    
    
}
