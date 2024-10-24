package com.flc.springthymeleaf.web.error;

import java.util.Map;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class MyErrorView implements ErrorViewResolver {

	@Override
	public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> map) {
		
		ModelAndView model = new ModelAndView("/error");
		model.addObject("status", status.value());
		
		switch (status.value()) {
		
				case 404:
					model.addObject("error", "Página não encontrada.");
					model.addObject("message", "A url para a página '" + map.get("path")+ "'não existe.");
					break;
				case 500:
					
				
				    model.addObject("error", "Ocorreu um erro interno no servidor");

				    // Captura a exceção gerada
				    Throwable throwable = (Throwable) request.getAttribute("jakarta.servlet.error.exception");
				    Throwable rootCause = throwable.getCause();
				    if (rootCause != null && rootCause instanceof IllegalStateException) {
				        model.addObject("message", rootCause.getMessage());
				    } else {
				        model.addObject("message", "Erro inesperado: " + throwable.getClass().getName());
				    }
				  
				    break;
					
					
					
				
				default:
					model.addObject("error", map.get("error"));
					model.addObject("message", map.get("message"));
					break;
					
		}
		
			
		return model;
	}

	

}
