package com.flc.springthymeleaf.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class ItemNotaFiscal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "nota_fiscal_id")
    @NotNull(message = "Nota fiscal não pode ser nula")
    @JsonBackReference
    private NotaFiscal notaFiscal;

    @ManyToOne
    @JoinColumn(name = "propriedade_id")
    @NotNull(message = "Propriedade não pode ser nula")
    private Propriedade propriedade;

    @Column(name = "quantidade", nullable = false)
    @NotNull(message = "Quantidade não pode ser nula")
    private BigDecimal quantidade;

    @Column(name = "volume", nullable = false)
    @NotNull(message = "Volume não pode ser nula")
    private BigDecimal volume;

    @Column(name = "valor_unitario", nullable = false)
    @NotNull(message = "Valor unitário não pode ser nulo")
    private BigDecimal valorUnitario;

    @Column(name = "valor_total", nullable = false)
    @NotNull(message = "Valor total não pode ser nulo")
    private BigDecimal valorTotal;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public NotaFiscal getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(NotaFiscal notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    public Propriedade getPropriedade() {
        return propriedade;
    }

    public void setPropriedade(Propriedade propriedade) {
        this.propriedade = propriedade;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
}
