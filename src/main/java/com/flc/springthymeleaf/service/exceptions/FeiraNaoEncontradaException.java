package com.flc.springthymeleaf.service.exceptions;

import java.time.LocalDate;

@SuppressWarnings("serial")
public class FeiraNaoEncontradaException extends RuntimeException {
    public FeiraNaoEncontradaException(Long id) {
        super("Feira não encontrada com o ID: " + id);
    }

    public FeiraNaoEncontradaException(LocalDate data) {
        super("Feira não encontrada para a data: " + data);
    }
}
