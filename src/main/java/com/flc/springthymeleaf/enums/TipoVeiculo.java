package com.flc.springthymeleaf.enums;

public enum TipoVeiculo {
    UP("UP", "ULTRA-PESADOS"),
    CT("CT", "CARRETA"),
    LV("LV", "LEVE");
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
