package com.flc.springthymeleaf.enums;

public enum FaixaHorario {
    HORARIO_01("00:01 às 02:00"),
    HORARIO_02("02:01 às 04:00"),
    HORARIO_03("04:01 às 06:00");
    // Continue com os outros intervalos de 2 horas...

    private String descricao;

    FaixaHorario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
