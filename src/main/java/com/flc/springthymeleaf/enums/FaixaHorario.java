package com.flc.springthymeleaf.enums;

public enum FaixaHorario {
	HORA_00_02("00:01 às 02:00"),
    HORA_02_04("02:01 às 04:00"),
    HORA_04_06("04:01 às 06:00"),
    HORA_06_08("06:01 às 08:00"),
    HORA_08_10("08:01 às 10:00"),
    HORA_10_12("10:01 às 12:00"),
    HORA_12_14("12:01 às 14:00"),
    HORA_14_16("14:01 às 16:00"),
    HORA_16_18("16:01 às 18:00"),
    HORA_18_24("18:01 às 24:00");
    // Continue com os outros intervalos de 2 horas...

    private String descricao;

    FaixaHorario(String descricao) {
        this.descricao = descricao;
    }

    public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
        return descricao;
    }
}
