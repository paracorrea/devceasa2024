package enums;

public enum FatorSazonal {
	
	MUITOBAIXO("MB", "MUITO BAIXO"), 
	BAIXO("BX", "BAIXO"), 
	NORMAL("NR", "NORMAL"), 
	ALTO("AL", "ALTO"), 
	MUITOALTO("MA", "MUITO ALTO");

	private String codigo;
	private String descricao;
	FatorSazonal(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	public String getCodigo() {
		return codigo;
	}
	public String getDescricao() {
		return descricao;
	}
}
