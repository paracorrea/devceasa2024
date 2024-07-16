package com.flc.springthymeleaf.enums;

public enum TipoVeiculo {
    UP("UP","Ultra-Pesados"),
    CT("CT","Carreta"),
    LV("LV","Leve"),
    CG("CG","Caminhão de Carga"),
    PD("PD","Pesado");
    // Continue com os outros tipos de veículo...

    private final String codigo;
    private final String descricao;

    TipoVeiculo(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
}
