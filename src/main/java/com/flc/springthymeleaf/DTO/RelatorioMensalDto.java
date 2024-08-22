package com.flc.springthymeleaf.DTO;

import java.math.BigDecimal;
import java.util.Map;

import com.flc.springthymeleaf.domain.Propriedade;

public class RelatorioMensalDto {
    private Propriedade propriedade;
    private Map<String, BigDecimal> mediasPorMes;
	
    
    public Propriedade getPropriedade() {
		return propriedade;
	}
	public Map<String, BigDecimal> getMediasPorMes() {
		return mediasPorMes;
	}
	public void setPropriedade(Propriedade propriedade) {
		this.propriedade = propriedade;
	}
	public void setMediasPorMes(Map<String, BigDecimal> mediasPorMes) {
		this.mediasPorMes = mediasPorMes;
	}

    // Getters e setters
    
    
    
}