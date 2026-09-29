package br.jus.tjpb.lyno.litigancia.model;

public enum Tipologia {
    FALSIFICACAO_DOCUMENTO("Falsificação ou adulteração de documentos"),
    FATIAMENTO_PEDIDOS("Ajuizamento de múltiplas ações idênticas fragmentadas"),
    CAPTACAO_ILICITA_CLIENTELA("Captação ilícita de clientela"),
    ACAO_SEM_CONSENTIMENTO("Ações propostas sem consentimento/conhecimento real da parte"),
    TESE_ARTIFICIAL_MASSA("Uso de teses artificiais ou fabricadas em massa"),
    OUTROS("Outros");

    private final String descricao;

    Tipologia(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
