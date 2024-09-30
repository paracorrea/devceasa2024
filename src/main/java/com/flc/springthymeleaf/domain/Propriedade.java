package com.flc.springthymeleaf.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Propriedade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codigo", length = 10, unique = true)
    private String codigo;

    @Column(name = "variedade", length = 50)
    private String variedade;

    @Column(name = "subvariedade", length = 50)
    private String subvariedade;

    @Column(name = "classificacao", length = 50)
    private String classificacao;

    @Column(name = "unidade", length = 25)
    @Size(min = 0, max = 2, message = "Campo com dois caracteres no máximo")
    private String unidade;

    @Column(name = "peso")
    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    @Digits(integer = 6, fraction = 3, message = "O valor deve ter no máximo 10 dígitos inteiros e 3 decimais")
    private BigDecimal peso;

    @ManyToMany
    @JoinTable(
        name = "propriedade_embalagem",
        joinColumns = @JoinColumn(name = "propriedade_id"),
        inverseJoinColumns = @JoinColumn(name = "embalagem_id")
    )
    private List<Embalagem> embalagens;

//    @OneToMany(mappedBy = "propriedade", cascade = CascadeType.ALL)
//    @JsonIgnore
//    private List<ItemNotaFiscal> itensNotaFiscal = new ArrayList<>();

    @Column(columnDefinition = "bit default 0")
    private Boolean status = false;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "produto_id")
    @JsonManagedReference
    @NotNull(message = "Campo não pode ser nullo")
    private Produto produto;

    @OneToMany(mappedBy = "propriedade", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Cotacao> cotacoes = new ArrayList<>();

    @OneToMany(mappedBy = "propriedade", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ItemDeNota> itens;
    
       
    public Propriedade() { 
    }

    public Propriedade(Integer id, String codigo, String variedade, String subvariedade, String classificacao, String unidade,
                       BigDecimal peso, Boolean status, Produto produto) {
        this.id = id;
        this.codigo = codigo;
        this.variedade = variedade;
        this.subvariedade = subvariedade;
        this.classificacao = classificacao;
        this.unidade = unidade;
        this.peso = peso;
        
        this.status = status;
        this.produto = produto;
    }
    
    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo.toUpperCase();
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

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao.toUpperCase();
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public List<Embalagem> getEmbalagens() {
        return embalagens;
    }

    public void setEmbalagens(List<Embalagem> embalagens) {
        this.embalagens = embalagens;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public List<Cotacao> getCotacoes() {
        return Collections.unmodifiableList(cotacoes);
    }

    public void setCotacoes(List<Cotacao> cotacoes) {
        this.cotacoes = cotacoes;
    }

	public List<ItemDeNota> getItensDeNota() {
		return itens;
	}

	public void setItensDeNota(List<ItemDeNota> itensDeNota) {
		this.itens = itensDeNota;
	}

	
}