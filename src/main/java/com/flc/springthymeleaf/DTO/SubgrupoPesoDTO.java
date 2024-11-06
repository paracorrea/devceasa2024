package com.flc.springthymeleaf.DTO;

public class SubgrupoPesoDTO {

	private String subgrupo;
	private Double totalPeso;
	
	public SubgrupoPesoDTO(String subgrupo, Double totalPeso) {
		super();
		this.subgrupo = subgrupo;
		this.totalPeso = totalPeso;
	}

	public String getSubgrupo() {
		return subgrupo;
	}

	public Double getTotalPeso() {
		return totalPeso;
	}

	public void setSubgrupo(String subgrupo) {
		this.subgrupo = subgrupo;
	}

	public void setTotalPeso(Double totalPeso) {
		this.totalPeso = totalPeso;
	}
	
	
	
	
}
