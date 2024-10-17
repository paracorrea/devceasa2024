package com.flc.springthymeleaf.enums;


public enum TipoVeiculo {
    CG("CG", "CARGA 100T", 60001, 100000),
    CT("CT", "CARRETAS", 18001, 60000),
    LV("LV", "LEVES", 1, 400),
    MD("MD", "MEDIOS", 401, 1500),
    MP("MP", "MEDIO-PESADOS", 1501, 4000),
    ON("ON", "ONIBUS", 0, 0),
    PD("PD", "PESADOS", 4001, 9000),
    UP("UP", "ULTRA-PESADOS", 9001, 18000);

    private final String codigo;
    private final String descricao;
    private final int cargaMinima;
    private final int cargaMaxima;

    TipoVeiculo(String codigo, String descricao, int cargaMinima, int cargaMaxima) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.cargaMinima = cargaMinima;
        this.cargaMaxima = cargaMaxima;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getCargaMinima() {
        return cargaMinima;
    }

    public int getCargaMaxima() {
        return cargaMaxima;
    }
}