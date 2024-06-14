package com.flc.springthymeleaf.service.exceptions;

@SuppressWarnings("serial")
public class FeiraNaoPodeSerEncerradaException extends RuntimeException {
    public FeiraNaoPodeSerEncerradaException(String message) {
        super(message);
    }
}