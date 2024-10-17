package com.flc.springthymeleaf.enums;

public enum LocalDestino {
	 GP1("GP1", "Galpão Permanente 1"),
	    GP2("GP2", "Galpão Permanente 2"),
	    GP3("GP3", "Galpão Permanente 3"),
	    GP4("GP4", "Galpão Permanente 4"),
	    MLC("MLC", "Mercado Livre Central"),
	    ML1("ML1", "Mercado Livre 1"),
	    ML2("ML2", "Mercado Livre 2"),
	    ML4("ML4", "Mercado Livre 4"),
	    OTR("OTR", "Outro");
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