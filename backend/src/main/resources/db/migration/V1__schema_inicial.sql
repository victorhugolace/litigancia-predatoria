-- IF NOT EXISTS permite adotar bancos criados pela versão inicial com Hibernate.
CREATE TABLE IF NOT EXISTS denuncias (
    id BIGSERIAL PRIMARY KEY,
    numero_protocolo VARCHAR(30) NOT NULL UNIQUE,
    comarca VARCHAR(120) NOT NULL,
    nome_comunicante VARCHAR(150) NOT NULL,
    matricula_comunicante VARCHAR(30) NOT NULL,
    oab_advogado VARCHAR(30) NOT NULL,
    cpf_cnpj_parte VARCHAR(30),
    tipologia VARCHAR(40) NOT NULL,
    tipologia_outros VARCHAR(120),
    relato VARCHAR(500),
    status VARCHAR(40) NOT NULL,
    volume_processual_estimado INTEGER,
    comarcas_atingidas INTEGER,
    nota_tecnica_resumo VARCHAR(800),
    data_criacao TIMESTAMP NOT NULL,
    data_atualizacao TIMESTAMP NOT NULL
);
CREATE TABLE IF NOT EXISTS denuncia_processos_paradigmas (
    denuncia_id BIGINT NOT NULL REFERENCES denuncias(id),
    numero_processo VARCHAR(25)
);
CREATE TABLE IF NOT EXISTS denuncia_anexos (
    denuncia_id BIGINT NOT NULL REFERENCES denuncias(id),
    nome_arquivo VARCHAR(255)
);
CREATE INDEX IF NOT EXISTS idx_denuncias_status_data ON denuncias(status, data_criacao DESC);
CREATE INDEX IF NOT EXISTS idx_denuncias_data ON denuncias(data_criacao);
CREATE INDEX IF NOT EXISTS idx_denuncias_oab ON denuncias(oab_advogado);
CREATE INDEX IF NOT EXISTS idx_processos_denuncia ON denuncia_processos_paradigmas(denuncia_id);
CREATE INDEX IF NOT EXISTS idx_anexos_denuncia ON denuncia_anexos(denuncia_id);
