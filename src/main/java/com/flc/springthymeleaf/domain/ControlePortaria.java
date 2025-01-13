package com.flc.springthymeleaf.domain;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import com.flc.springthymeleaf.enums.StatusSessao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class ControlePortaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    @DateTimeFormat(iso = ISO.DATE)
    @Column(name = "data_sessao", columnDefinition = "DATE")
    private LocalDate dataDaSessao;

    private Integer totalNotas = 0; // Inicializa com 0

    private Double totalPeso = 0.0; // Inicializa com 0.0

    @Enumerated(EnumType.STRING)
    private StatusSessao statusSessao;

    @OneToMany(mappedBy = "controlePortaria", fetch = FetchType.EAGER)
    private List<Nota> notas;
    
	public Long getId() {
		return id;
	}

	public LocalDate getDataDaSessao() {
		return dataDaSessao;
	}

	public Integer getTotalNotas() {
		return totalNotas;
	}

	public Double getTotalPeso() {
		return totalPeso;
	}

	public StatusSessao getStatusSessao() {
		return statusSessao;
	}

	
	public void setId(Long id) {
		this.id = id;
	}

	public void setDataDaSessao(LocalDate dataDaSessao) {
		this.dataDaSessao = dataDaSessao;
	}

	public void setTotalNotas(Integer totalNotas) {
		this.totalNotas = totalNotas;
	}

	public void setTotalPeso(Double totalPeso) {
		this.totalPeso = totalPeso;
	}

	public void setStatusSessao(StatusSessao statusSessao) {
		this.statusSessao = statusSessao;
	}

	public List<Nota> getNotas() {
		return notas;
	}

	public void setNotas(List<Nota> notas) {
		this.notas = notas;
	}

	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}



   
    
}