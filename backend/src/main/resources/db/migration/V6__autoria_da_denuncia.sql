ALTER TABLE denuncias ADD COLUMN criado_por VARCHAR(120);
CREATE INDEX idx_denuncias_autor ON denuncias(criado_por);
-- Registros anteriores continuam acessíveis apenas aos analistas: autoria não pode ser inferida.
