package com.flc.springthymeleaf.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.flc.springthymeleaf.enums.FaixaHorario;
import com.flc.springthymeleaf.enums.LocalDestino;
import com.flc.springthymeleaf.enums.Portaria;
import com.flc.springthymeleaf.enums.TipoVeiculo;

import jakarta.persistence.*;

@Entity
public class Nota implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;

    @Enumerated(EnumType.STRING)
    private Portaria portaria;

    @Enumerated(EnumType.STRING)
    private FaixaHorario faixaHorario;

    @Enumerated(EnumType.STRING)
    private TipoVeiculo tipoVeiculo;

    @Enumerated(EnumType.STRING)
    private LocalDestino localDestino;

    @OneToMany(mappedBy = "nota", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemDeNota> itens;

    // Getters and setters



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
