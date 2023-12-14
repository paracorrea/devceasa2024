package com.flc.springthymeleaf.domain;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Embeddable;

@Embeddable
public class CotacaoId implements Serializable {

	private Integer id; // O mesmo nome do id na classe Cotacao

    private LocalDate dataDaCotacao;

    
    
    
    
    
    

}
