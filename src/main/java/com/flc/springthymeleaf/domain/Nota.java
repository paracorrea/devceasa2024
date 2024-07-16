package com.flc.springthymeleaf.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.flc.springthymeleaf.enums.FaixaHorario;
import com.flc.springthymeleaf.enums.LocalDestino;
import com.flc.springthymeleaf.enums.Portaria;
import com.flc.springthymeleaf.enums.TipoVeiculo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


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

	private BigDecimal valorTotalDaNota;
	private BigDecimal valorTotalDosProdutos;
	
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

	@Enumerated(EnumType.STRING)
	@Column(name = "local_destino")
	// @NotNull(message = "Campo não pode ser nulo")
	private LocalDestino localDestino;

    @OneToMany(mappedBy = "nota", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
    private List<ItemDeNota> itens = new ArrayList<>();


    public void addItem(ItemDeNota item) {
        itens.add(item);
        item.setNota(this);
    }
	// Getters and Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public BigDecimal getValorTotalDaNota() {
		return valorTotalDaNota;
	}

	public void setValorTotalDaNota(BigDecimal valorTotalDaNota) {
		this.valorTotalDaNota = valorTotalDaNota;
	}

	public BigDecimal getValorTotalDosProdutos() {
		return valorTotalDosProdutos;
	}

	public void setValorTotalDosProdutos(BigDecimal valorTotalDosProdutos) {
		this.valorTotalDosProdutos = valorTotalDosProdutos;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}



	public Portaria getPortaria() {
		return portaria;
	}

	public void setPortaria(Portaria portaria) {
		this.portaria = portaria;
	}

	public FaixaHorario getFaixaHorario() {
		return faixaHorario;
	}

	public void setFaixaHorario(FaixaHorario faixaHorario) {
		this.faixaHorario = faixaHorario;
	}

	public TipoVeiculo getTipoVeiculo() {
		return tipoVeiculo;
	}

	public void setTipoVeiculo(TipoVeiculo tipoVeiculo) {
		this.tipoVeiculo = tipoVeiculo;
	}

	

	public LocalDestino getLocalDestino() {
		return localDestino;
	}

	public void setLocalDestino(LocalDestino localDestino) {
		this.localDestino = localDestino;
	}

	public List<ItemDeNota> getItens() {
		return itens;
	}

	public void setItens(List<ItemDeNota> itens) {
		this.itens = itens;
	}
	

	
	

}
