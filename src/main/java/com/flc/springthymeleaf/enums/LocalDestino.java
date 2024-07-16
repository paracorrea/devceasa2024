package com.flc.springthymeleaf.enums;

public enum LocalDestino {
	GP1("GP1", "GP1"),
	GP3("GP3", "GP3"),
	MC1("ML1", "ML1"),
	MC2("ML2","ML2"),
	MC3("ML3","ML3"),
	GP4("GP4", "GP4"),
    GP2("GP2", "GP2"),
    MLC("MLC", "MLC");
    // Continue com os outros locais de destino...

    private final String codigo;
    private final String descricao;

    LocalDestino(String codigo, String descricao) {
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