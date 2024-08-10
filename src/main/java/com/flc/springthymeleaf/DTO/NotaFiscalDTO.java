package com.flc.springthymeleaf.DTO;

import java.time.LocalDate;
import java.util.List;

import com.flc.springthymeleaf.enums.TipoDeNota;

import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;

public class NotaFiscalDTO {
	
	// dados do emissor
	
	// @NotNull
	@Size(min = 44, max = 44)
	private String chaveAcesso;

	// @NotNull
	private String numeroDaNota;

	// @NotNull
	private String serie;

	// @NotNull
	@Enumerated
	private TipoDeNota tipo; // se entrada ou saida
	
	//@NotNull
	private LocalDate dataEmissao; //

	// @NotNull
	private String cnpjEmissor;
	
	
	// @NotNull
	private MunicipioDTO municipio; // municipio origem

	// @NotNull
	private MercadoDTO mercado; // mercado interno



	// @NotNull
	private PermissionarioDTO permissionario; // tabela de permissionários

	
	//@NotNull
	private LocalDate dataEntrada;

		
	//@NotNull
	private List<ItemNotaFiscalDTO> itens;


	public String getChaveAcesso() {
		return chaveAcesso;
	}


	public String getNumeroDaNota() {
		return numeroDaNota;
	}


	public String getSerie() {
		return serie;
	}


	public TipoDeNota getTipo() {
		return tipo;
	}


	public LocalDate getDataEmissao() {
		return dataEmissao;
	}


	public String getCnpjEmissor() {
		return cnpjEmissor;
	}


	public MunicipioDTO getMunicipio() {
		return municipio;
	}


	public MercadoDTO getMercado() {
		return mercado;
	}


	public PermissionarioDTO getPermissionario() {
		return permissionario;
	}


	public LocalDate getDataEntrada() {
		return dataEntrada;
	}


	public List<ItemNotaFiscalDTO> getItens() {
		return itens;
	}


	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}


	public void setNumeroDaNota(String numeroDaNota) {
		this.numeroDaNota = numeroDaNota;
	}


	public void setSerie(String serie) {
		this.serie = serie;
	}


	public void setTipo(TipoDeNota tipo) {
		this.tipo = tipo;
	}


	public void setDataEmissao(LocalDate dataEmissao) {
		this.dataEmissao = dataEmissao;
	}


	public void setCnpjEmissor(String cnpjEmissor) {
		this.cnpjEmissor = cnpjEmissor;
	}


	public void setMunicipio(MunicipioDTO municipio) {
		this.municipio = municipio;
	}


	public void setMercado(MercadoDTO mercado) {
		this.mercado = mercado;
	}


	public void setPermissionario(PermissionarioDTO permissionario) {
		this.permissionario = permissionario;
	}


	public void setDataEntrada(LocalDate dataEntrada) {
		this.dataEntrada = dataEntrada;
	}


	public void setItens(List<ItemNotaFiscalDTO> itens) {
		this.itens = itens;
	}

	
	
	
}










