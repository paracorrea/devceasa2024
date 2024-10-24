package com.flc.springthymeleaf.service.exceptions;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
	 @ExceptionHandler(IllegalStateException.class)
	    public ModelAndView handleIllegalStateException(IllegalStateException ex) {
	        ModelAndView modelAndView = new ModelAndView("errorPage");
	        modelAndView.addObject("errorMessage", "Página de Controle de erros: Não foi possível prosseguir.");
	        modelAndView.addObject("detailedMessage", ex.getMessage());
	        return modelAndView;
	    }
}

