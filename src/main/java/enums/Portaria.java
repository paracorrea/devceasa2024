package enums;

public enum Portaria {
    PORTARIA_PRINCIPAL("Portaria Principal"),
    PORTARIA_MERCADO_FLORES("Portaria do Mercado de Flores");

    private final String descricao;

    Portaria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
