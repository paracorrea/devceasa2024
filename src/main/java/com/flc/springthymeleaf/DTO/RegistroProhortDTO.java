package com.flc.springthymeleaf.DTO;

import java.math.BigDecimal;

public class RegistroProhortDTO {
    
    private String codigoProduto;
    private String codigoMunicipio;
    private BigDecimal totalPeso; // Use BigDecimal para maior precisão
    private Double precoMedio;
    
    // Construtor necessário para a query `new`
    public RegistroProhortDTO(String codigoProduto, String codigoMunicipio, BigDecimal totalPeso, Double precoMedio) {
        this.codigoProduto = codigoProduto;
        this.codigoMunicipio = codigoMunicipio;
        this.totalPeso = totalPeso;
        this.precoMedio = precoMedio;
    }

  

	// Getters and Setters
    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(String codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public BigDecimal getTotalPeso() {
        return totalPeso;
    }

    public void setPesoTotal(BigDecimal totalPeso) {
        this.totalPeso = totalPeso;
    }

    public Double getPrecoMedio() {
        return precoMedio;
    }

    public void setPrecoMedio(Double precoMedio) {
        this.precoMedio = precoMedio;
    }
}