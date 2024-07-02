package com.flc.springthymeleaf.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

@Entity
public class NotaFiscal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "chave_acesso", unique = true, nullable = false)
    @NotNull(message = "Chave de acesso não pode ser nula")
    private String chaveAcesso;

    @Column(name = "numero", nullable = false)
    @NotNull(message = "Número não pode ser nulo")
    private String numero;

    @Column(name = "serie", nullable = false)
    @NotNull(message = "Série não pode ser nula")
    private String serie;

    @Column(name = "tipo", nullable = false)
    @NotNull(message = "Tipo não pode ser nulo")
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "municipio_id")
    @NotNull(message = "Município não pode ser nulo")
    private Municipio municipio;

    @ManyToOne
    @JoinColumn(name = "mercado_id")
    @NotNull(message = "Mercado não pode ser nulo")
    private Mercado mercado;

    @ManyToOne
    @JoinColumn(name = "permissionario_cnpj")
    @NotNull(message = "Permissionário não pode ser nulo")
    private Permissionario permissionario;

    @Column(name = "data_emissao", nullable = false)
    @NotNull(message = "Data de emissão não pode ser nula")
    private LocalDate dataEmissao;

    @Column(name = "data_entrada", nullable = false)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
    @NotNull(message = "Data de entrada não pode ser nula")
    private LocalDateTime dataEntrada;

    @OneToMany(mappedBy = "notaFiscal", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ItemNotaFiscal> itens = new ArrayList<>();

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChaveAcesso() {
        return chaveAcesso;
    }

    public void setChaveAcesso(String chaveAcesso) {
        this.chaveAcesso = chaveAcesso;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Mercado getMercado() {
        return mercado;
    }

    public void setMercado(Mercado mercado) {
        this.mercado = mercado;
    }

    public Permissionario getPermissionario() {
        return permissionario;
    }

    public void setPermissionario(Permissionario permissionario) {
        this.permissionario = permissionario;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public List<ItemNotaFiscal> getItens() {
        return itens;
    }

    public void setItens(List<ItemNotaFiscal> itens) {
        this.itens = itens;
    }
}
