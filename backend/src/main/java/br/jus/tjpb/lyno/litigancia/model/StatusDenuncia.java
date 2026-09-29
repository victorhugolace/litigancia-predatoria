package br.jus.tjpb.lyno.litigancia.model;

public enum StatusDenuncia {
    NOVO("Novo"),
    EM_ANALISE_NUMOPED("Em Análise pelo NUMOPED"),
    AGUARDANDO_NOTA_TECNICA("Aguardando Nota Técnica"),
    EM_PROVIDENCIA_CORREGEDORIA("Em Providência na Corregedoria"),
    ARQUIVADO("Arquivado");

    private final String descricao;

    StatusDenuncia(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
