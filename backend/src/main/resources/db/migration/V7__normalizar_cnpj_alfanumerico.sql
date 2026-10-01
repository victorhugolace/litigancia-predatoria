-- Mantem os documentos numericos e normaliza letras para o ranking.
UPDATE denuncias SET cpf_cnpj_parte = upper(cpf_cnpj_parte)
WHERE cpf_cnpj_parte IS NOT NULL AND cpf_cnpj_parte <> upper(cpf_cnpj_parte);
