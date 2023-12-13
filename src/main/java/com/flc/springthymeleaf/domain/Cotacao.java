package com.flc.springthymeleaf.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;



import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import enums.FatorSazonal;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "cotacao")
public class Cotacao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	//@PastOrPresent(message = "{PastOrPresent.funcionario.dataEntrada}")
	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "data_cotacao", nullable = false, columnDefinition = "DATE")
	private LocalDate dataDaCotacao;
	
	@NotNull
	private String users; 
	
	@Column(name="valores")
	private String[] valores;
	
	@Column(name="valor1")
	@PositiveOrZero
	@NumberFormat(style = Style.CURRENCY, pattern ="#,##0.00")
	private BigDecimal valor1;
	
	@Column(name="valor2")
	@PositiveOrZero
	@NumberFormat(style = Style.CURRENCY, pattern ="#,##0.00")
	private BigDecimal valor2;
	
	
	@Column(name="valor3")
	@PositiveOrZero
	@NumberFormat(style = Style.CURRENCY, pattern ="#,##0.00")
	private BigDecimal valor3;
	
	@Column(name="valor4")
	@PositiveOrZero
	@NumberFormat(style = Style.CURRENCY, pattern ="#,##0.00")
	private BigDecimal valor4;
	
	@Column(name="valor5")
	@PositiveOrZero
	@NumberFormat(style = Style.CURRENCY, pattern ="#,##0.00")
	private BigDecimal valor5;
	
	@Column(name="valor6")
	@PositiveOrZero
	@NumberFormat(style = Style.CURRENCY, pattern ="#,##0.00")
	private BigDecimal valor6;
	
	@Column(name="valor7")
	@PositiveOrZero
	@NumberFormat(style = Style.CURRENCY, pattern ="#,##0.00")
	private BigDecimal valor7;
	
	@Column(name="valor8")
	@PositiveOrZero
	@NumberFormat(style = Style.CURRENCY, pattern ="#,##0.00")
	private BigDecimal valor8;
	
	@Column(name="valor9")
	@PositiveOrZero
	@NumberFormat(style = Style.CURRENCY, pattern ="#,##0.00")
	private BigDecimal valor9;
	
	@Column(name="valor10")
	@PositiveOrZero
	@NumberFormat(style = Style.CURRENCY, pattern ="#,##0.00")
	private BigDecimal valor10;
	
	
	
	//private Integer quantCotado;
	
	@Column(name="preco_minimo")
	private BigDecimal precoMinimo;
	
	@Column(name="preco_maximo")
	private BigDecimal precoMaximo;
	
	
	@Column(name="preco_medio")
	private BigDecimal precoMedio;
	
	@Column(name="fator_sazonal")
	@Enumerated(EnumType.STRING)
	private FatorSazonal fatorSazonal;

	
	@OneToOne(cascade= {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH,
				CascadeType.REFRESH})
	@JoinColumn(name = "propriedade_id")
	@NotNull(message="Campo não pode ser nullo")
	private Propriedade propriedade;


	
	
	
	
	public Cotacao() {
		super();
	}


	
	
	public Integer getId() {
		return id;
	}




	public void setId(Integer id) {
		this.id = id;
	}




	public LocalDate getDataDaCotacao() {
		return dataDaCotacao;
	}


	public void setDataDaCotacao(LocalDate dataDaCotacao) {
		this.dataDaCotacao = dataDaCotacao;
	}


	public String getUsers() {
		return users;
	}


	public void setUsers(String users) {
		this.users = users;
	}


	public String[] getValores() {
		return valores;
	}


	public void setValores(String[] valores) {
		this.valores = valores;
	}


	public BigDecimal getValor1() {
		return valor1;
	}


	public void setValor1(BigDecimal valor1) {
		this.valor1 = valor1;
	}


	public BigDecimal getValor2() {
		return valor2;
	}


	public void setValor2(BigDecimal valor2) {
		this.valor2 = valor2;
	}


	public BigDecimal getValor3() {
		return valor3;
	}


	public void setValor3(BigDecimal valor3) {
		this.valor3 = valor3;
	}


	public BigDecimal getValor4() {
		return valor4;
	}


	public void setValor4(BigDecimal valor4) {
		this.valor4 = valor4;
	}


	public BigDecimal getValor5() {
		return valor5;
	}


	public void setValor5(BigDecimal valor5) {
		this.valor5 = valor5;
	}


	public BigDecimal getValor6() {
		return valor6;
	}


	public void setValor6(BigDecimal valor6) {
		this.valor6 = valor6;
	}


	public BigDecimal getValor7() {
		return valor7;
	}


	public void setValor7(BigDecimal valor7) {
		this.valor7 = valor7;
	}


	public BigDecimal getValor8() {
		return valor8;
	}


	public void setValor8(BigDecimal valor8) {
		this.valor8 = valor8;
	}


	public BigDecimal getValor9() {
		return valor9;
	}


	public void setValor9(BigDecimal valor9) {
		this.valor9 = valor9;
	}


	public BigDecimal getValor10() {
		return valor10;
	}


	public void setValor10(BigDecimal valor10) {
		this.valor10 = valor10;
	}


	public BigDecimal getPrecoMinimo() {
		return precoMinimo;
	}


	public void setPrecoMinimo(BigDecimal precoMinimo) {
		this.precoMinimo = precoMinimo;
	}


	public BigDecimal getPrecoMaximo() {
		return precoMaximo;
	}


	public void setPrecoMaximo(BigDecimal precoMaximo) {
		this.precoMaximo = precoMaximo;
	}


	public BigDecimal getPrecoMedio() {
		return precoMedio;
	}


	public void setPrecoMedio(BigDecimal precoMedio) {
		this.precoMedio = precoMedio;
	}


	public FatorSazonal getFatorSazonal() {
		return fatorSazonal;
	}


	public void  setFatorSazonal(FatorSazonal fatorSazonal) {
		this.fatorSazonal = fatorSazonal;
	}


	public Propriedade getPropriedade() {
		return propriedade;
	}


	public void setPropriedade(Propriedade propriedade) {
		this.propriedade = propriedade;
	}
	
	
	
	
	
	
	


}
