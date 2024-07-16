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

	
	
	
}










