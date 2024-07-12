package com.flc.springthymeleaf.enums;

public enum TipoDeNota {
	N01("01","Entrada"),
	N02("02","Saída");
	
	private final String codigo;
	private final String descricao;
	
	private TipoDeNota(String codigo, String descricao) {
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