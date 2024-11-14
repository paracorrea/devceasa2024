package com.flc.springthymeleaf.DTO;
import java.util.List;

public class ProdutoCotadoDTO {
    private String nomeProduto;
    private List<Double> mediasSemanais;
    private Double mediaGeral;

    public ProdutoCotadoDTO(String nomeProduto, List<Double> mediasSemanais, Double mediaGeral) {
        this.nomeProduto = nomeProduto;
        this.mediasSemanais = mediasSemanais;
        this.mediaGeral = mediaGeral;
    }

    // Getters e Setters
    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public List<Double> getMediasSemanais() {
        return mediasSemanais;
    }

    public void setMediasSemanais(List<Double> mediasSemanais) {
        this.mediasSemanais = mediasSemanais;
    }

    public Double getMediaGeral() {
        return mediaGeral;
    }

    public void setMediaGeral(Double mediaGeral) {
        this.mediaGeral = mediaGeral;
    }
}

