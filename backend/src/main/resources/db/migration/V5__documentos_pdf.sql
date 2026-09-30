CREATE TABLE denuncia_documentos (
    id UUID PRIMARY KEY,
    denuncia_id BIGINT NOT NULL REFERENCES denuncias(id) ON DELETE CASCADE,
    nome VARCHAR(255) NOT NULL,
    tamanho BIGINT NOT NULL CHECK (tamanho > 0 AND tamanho <= 10485760),
    conteudo BYTEA NOT NULL
);
CREATE INDEX idx_documentos_denuncia ON denuncia_documentos(denuncia_id);
