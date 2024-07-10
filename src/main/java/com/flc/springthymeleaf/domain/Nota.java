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

@Entity(name = "Nota")
@Table(name = "nota")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "numero_da_nota")
    //@NotNull(message = "Campo não pode ser nulo")
    private String numeroDaNotaFiscal;

    @DateTimeFormat(iso = ISO.DATE)
    @Column(name = "data_captura", columnDefinition = "DATE")
    //@NotNull(message = "Campo não pode ser nulo")
    private LocalDate dataCaptura;

    @DateTimeFormat(iso = ISO.DATE)
    @Column(name = "data_emissao", columnDefinition = "DATE")
    private LocalDate dataEmissao;

    @Column(name = "cnpj_emissor")
    private String cnpjEmissor;

    @Enumerated(EnumType.STRING)
    @Column(name = "portaria")
    //@NotNull(message = "Campo não pode ser nulo")
    private Portaria portaria;

    @ManyToOne
    @JoinColumn(name = "permissionario_cnpj")
   // @NotNull(message = "Permissionário não pode ser nulo")
    private Permissionario permissionario;

    @Enumerated(EnumType.STRING)
    @Column(name = "faixa_horario")
    //@NotNull(message = "Campo não pode ser nulo")
    private FaixaHorario faixaHorario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_veiculo")
    //@NotNull(message = "Campo não pode ser nulo")
    private TipoVeiculo tipoVeiculo;

    @Enumerated(EnumType.STRING)
    @Column(name = "local_destino")
    //@NotNull(message = "Campo não pode ser nulo")
    private LocalDestino localDestino;

    @ManyToOne
    @JoinColumn(name = "municipio_codigo")
    //@NotNull(message = "Município não pode ser nulo")
    private Municipio municipio;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroDaNotaFiscal() {
        return numeroDaNotaFiscal;
    }

    public void setNumeroDaNotaFiscal(String numeroDaNotaFiscal) {
        this.numeroDaNotaFiscal = numeroDaNotaFiscal;
    }

    public LocalDate getDataCaptura() {
        return dataCaptura;
    }

    public void setDataCaptura(LocalDate dataCaptura) {
        this.dataCaptura = dataCaptura;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getCnpjEmissor() {
        return cnpjEmissor;
    }

    public void setCnpjEmissor(String cnpjEmissor) {
        this.cnpjEmissor = cnpjEmissor;
    }

  
    public Portaria getPortaria() {
        return portaria;
    }

    public void setPortaria(Portaria portaria) {
        this.portaria = portaria;
    }

    public Permissionario getPermissionario() {
        return permissionario;
    }

    public void setPermissionario(Permissionario permissionario) {
        this.permissionario = permissionario;
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

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }
}
