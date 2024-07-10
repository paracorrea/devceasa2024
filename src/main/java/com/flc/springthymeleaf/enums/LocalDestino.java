package com.flc.springthymeleaf.enums;

public enum LocalDestino {
    GP4("GP4", "GP4"),
    GP2("GP2", "GP2"),
    MLC("MLC", "390");
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