package com.flc.springthymeleaf.DTO;

import java.math.BigDecimal;

public class ItemNotaFiscalDTO {
	private BigDecimal quantidade;
	private BigDecimal volume;
	private BigDecimal valorUnitario;
	private BigDecimal valorTotal;
	private PropriedadeDTO propriedade;
	
	
	public BigDecimal getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}
	public BigDecimal getVolume() {
		return volume;
	}
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	public PropriedadeDTO getPropriedade() {
		return propriedade;
	}
	public void setPropriedade(PropriedadeDTO propriedade) {
		this.propriedade = propriedade;
	}

	// Getters and setters
	
	
	
}