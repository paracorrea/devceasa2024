package com.flc.springthymeleaf.DTO;

public class ProdutoPesoDTO {
    private String produto;
    private Double totalPeso;

    public ProdutoPesoDTO(String produto, Double totalPeso) {
        this.produto = produto;
        this.totalPeso = totalPeso;
    }

	public String getProduto() {
		return produto;
	}

	public Double getTotalPeso() {
		return totalPeso;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public void setTotalPeso(Double totalPeso) {
		this.totalPeso = totalPeso;
	}

   
}
