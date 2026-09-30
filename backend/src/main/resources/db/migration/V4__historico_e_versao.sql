ALTER TABLE denuncias ADD COLUMN versao BIGINT NOT NULL DEFAULT 0;
CREATE TABLE denuncia_historico (
    id BIGSERIAL PRIMARY KEY,
    denuncia_id BIGINT NOT NULL REFERENCES denuncias(id) ON DELETE CASCADE,
    status_anterior VARCHAR(40),
    status_novo VARCHAR(40) NOT NULL,
    autor VARCHAR(120) NOT NULL,
    data TIMESTAMP NOT NULL,
    volume INTEGER,
    comarcas INTEGER,
    nota VARCHAR(800)
);
CREATE INDEX idx_historico_denuncia ON denuncia_historico(denuncia_id, id DESC);
