package com.flc.springthymeleaf.domain;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.flc.springthymeleaf.enums.FaixaHorario;
import com.flc.springthymeleaf.enums.LocalDestino;
import com.flc.springthymeleaf.enums.TipoVeiculo;

import enums.Portaria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "nota")
public class Nota {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "data", columnDefinition = "DATE")
	// @NotNull(message = "Campo não pode ser nulo")
	private LocalDate data = LocalDate.now();

	@ManyToOne
	@JoinColumn(name = "municipio_id")
	private Municipio municipio;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "portaria")
	// @NotNull(message = "Campo não pode ser nulo")
	private Portaria portaria;

	@Enumerated(EnumType.STRING)
	@Column(name = "faixa_horario")
	// @NotNull(message = "Campo não pode ser nulo")
	private FaixaHorario faixaHorario;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_veiculo")
	// @NotNull(message = "Campo não pode ser nulo")
	private TipoVeiculo tipoVeiculo;

	
	@ManyToOne
	@JoinColumn(name = "destino_id")
	private DestinoInterno localDestino;



	// Getters and Setters
	

}
