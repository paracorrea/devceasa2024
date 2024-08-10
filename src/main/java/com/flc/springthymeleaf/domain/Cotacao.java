package com.flc.springthymeleaf.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "cotacao", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "propriedade_id", "data_cotacao" })
})
public class Cotacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @DateTimeFormat(iso = ISO.DATE)
    @Column(name = "data_cotacao", columnDefinition = "DATE")
    @NotNull(message = "Campo não pode ser nulo")
    private LocalDate dataCotacao = LocalDate.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "propriedade_id")
    @NotNull(message = "Selecione uma produto/propriedade")
    @JsonIgnore
    private Propriedade propriedade;

    private String users;

    @Column(name = "valor1")
    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    @NotNull(message = "Campo não pode ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser maior ou igual a 0")
    private BigDecimal valor1;

    @Column(name = "valor2")
    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    @NotNull(message = "Campo não pode ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser maior ou igual a 0")
    private BigDecimal valor2;
    
    @Column(name = "valor3")
    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    @NotNull(message = "Campo não pode ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser maior ou igual a 0")
    private BigDecimal valor3;

    @Column(name = "valor4")
    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valor4;

    @Column(name = "valor5")
    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valor5;

    @Column(name = "valor6")
    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valor6;

    @Column(name = "valor7")
    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valor7;

    @Column(name = "valor8")
    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valor8;

    @Column(name = "valor9")
    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valor9;

    @Column(name = "valor10")
    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valor10;

    @Column(name = "preco_minimo")
    @NotNull(message = "Aplicar cálculo antes de salvar")
    private BigDecimal precoMinimo;

    @Column(name = "preco_maximo")
    @NotNull(message = "Aplicar cálculo antes de salvar")
    private BigDecimal precoMaximo;

    @Column(name = "preco_medio")
    @NotNull(message = "Aplicar cálculo antes de salvar")
    private BigDecimal precoMedio;

   @Column(name = "valor_comum")
    private BigDecimal valorComum;

    @Column(name = "mercado")
    private String mercado;

    @Column(name = "peso_variavel")
    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    @Digits(integer = 6, fraction = 3, message = "O valor deve ter no máximo 10 dígitos inteiros e 3 decimais")
    private BigDecimal pesoVariavel;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDataCotacao() {
        return dataCotacao;
    }

    public void setDataCotacao(LocalDate dataCotacao) {
        this.dataCotacao = dataCotacao;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
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

    public Propriedade getPropriedade() {
        return propriedade;
    }

    public void setPropriedade(Propriedade propriedade) {
        this.propriedade = propriedade;
    }

    public BigDecimal getValorComum() {
        return valorComum;
    }

    public void setValorComum(BigDecimal valorComum) {
        this.valorComum = valorComum;
    }

    public String getMercado() {
        return mercado;
    }

    public void setMercado(String mercado) {
        this.mercado = mercado;
    }

    public BigDecimal getPesoVariavel() {
        return pesoVariavel;
    }

    public void setPesoVariavel(BigDecimal pesoVariavel) {
        this.pesoVariavel = pesoVariavel;
    }
}
