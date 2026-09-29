-- Seed de dados de demonstracao para a oficina de litigancia predatoria (LYNO/TJPB)
-- Gerado automaticamente; usa protocolos com prefixo LP-SEED- para nao colidir com numeracao real.

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000001', 'Areia', 'Juíza Helena Nóbrega', 'MAT2110', 'PB98265', NULL, 'FALSIFICACAO_DOCUMENTO', NULL, 'Fracionamento de pedidos para burlar o rito dos juizados especiais.', 'EM_PROVIDENCIA_CORREGEDORIA', 1496, 10, TIMESTAMP '2026-05-24 05:00:00', TIMESTAMP '2026-05-24 05:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['5661907-29.2024.8.11.1675', '2556017-58.2024.8.12.5636', '5437923-15.2026.8.15.8786', '3094235-58.2024.8.18.4804']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000002', 'Santa Rita', 'Juíza Renata Sales', 'MAT1987', 'PB23456', NULL, 'OUTROS', 'Reciclagem de laudos periciais adulterados', 'Mesmo advogado ajuizou dezenas de ações idênticas de consignado em poucos dias.', 'ARQUIVADO', 286, 4, TIMESTAMP '2026-06-22 16:00:00', TIMESTAMP '2026-06-22 16:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['5663623-68.2026.8.12.2665', '7210606-55.2024.8.09.1170', '3871230-78.2026.8.08.2678', '8755439-58.2025.8.18.3599', '6440561-17.2024.8.02.5169']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000003', 'Piancó', 'Juíza Camila Duarte', 'MAT2299', 'PB45678', '99827297893', 'CAPTACAO_ILICITA_CLIENTELA', NULL, 'Comprovantes de residência reciclados identificados em múltiplos processos.', 'EM_ANALISE_NUMOPED', 2281, 14, TIMESTAMP '2026-06-06 16:00:00', TIMESTAMP '2026-06-06 16:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['8187926-84.2025.8.12.3594', '3320821-75.2025.8.03.0772', '2839607-29.2026.8.06.6917', '2065818-59.2025.8.20.7669']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000004', 'João Pessoa', 'Juíza Renata Sales', 'MAT1987', 'PB51952', NULL, 'ACAO_SEM_CONSENTIMENTO', NULL, 'Petições iniciais com texto quase idêntico, mudando apenas nome da parte.', 'EM_ANALISE_NUMOPED', 2338, 2, TIMESTAMP '2026-08-05 17:00:00', TIMESTAMP '2026-08-05 17:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['3653446-68.2024.8.09.8202', '3997281-74.2024.8.10.8318', '4337174-29.2025.8.06.8838', '9897858-10.2026.8.11.8006', '1326765-24.2025.8.10.3924']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000005', 'Patos', 'Juíza Patrícia Lima', 'MAT1962', 'PB86781', NULL, 'FALSIFICACAO_DOCUMENTO', NULL, 'Procurações com assinaturas visivelmente distintas das partes autoras.', 'EM_PROVIDENCIA_CORREGEDORIA', 917, 1, TIMESTAMP '2026-07-14 06:00:00', TIMESTAMP '2026-07-14 06:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['9852897-87.2025.8.07.8836', '4374754-49.2025.8.12.7178', '9683481-67.2024.8.08.3682', '2074176-53.2024.8.19.9076']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000006', 'Patos', 'Juiz Marcos Andrade', 'MAT2201', 'PB12345', NULL, 'FALSIFICACAO_DOCUMENTO', NULL, 'Advogado de outro estado concentrando dezenas de ações na mesma vara.', 'EM_ANALISE_NUMOPED', 418, 1, TIMESTAMP '2026-08-06 03:00:00', TIMESTAMP '2026-08-06 03:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['3219824-83.2026.8.16.3982', '8935169-62.2024.8.04.1589', '8231838-55.2025.8.14.7652']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000007', 'Esperança', 'Juíza Camila Duarte', 'MAT2299', 'PB98598', '83831385071', 'FATIAMENTO_PEDIDOS', NULL, 'Petições iniciais com texto quase idêntico, mudando apenas nome da parte.', 'AGUARDANDO_NOTA_TECNICA', 2291, 11, TIMESTAMP '2026-06-19 02:00:00', TIMESTAMP '2026-06-19 02:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['5191056-19.2025.8.18.1605', '1848731-93.2026.8.01.1529', '4965789-31.2025.8.16.7887', '4586084-61.2024.8.06.6210', '1036161-59.2025.8.15.4674']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000008', 'Mamanguape', 'Juiz Fábio Cavalcanti', 'MAT2054', 'PB23456', NULL, 'CAPTACAO_ILICITA_CLIENTELA', NULL, 'Parte autora afirma desconhecer a ação distribuída em seu nome.', 'AGUARDANDO_NOTA_TECNICA', 1023, 10, TIMESTAMP '2026-07-28 06:00:00', TIMESTAMP '2026-07-28 06:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['1841248-84.2025.8.17.8702', '3641282-17.2026.8.03.3045', '2149597-86.2024.8.08.6616']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000009', 'Cuité', 'Juiz Marcos Andrade', 'MAT2201', 'PB55697', NULL, 'CAPTACAO_ILICITA_CLIENTELA', NULL, 'Petições iniciais com texto quase idêntico, mudando apenas nome da parte.', 'EM_ANALISE_NUMOPED', 2087, 5, TIMESTAMP '2026-06-05 18:00:00', TIMESTAMP '2026-06-05 18:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['6271130-40.2025.8.13.2145', '6033115-68.2025.8.03.0153', '8688755-89.2026.8.04.1201']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000010', 'Cajazeiras', 'Juíza Camila Duarte', 'MAT2299', 'PB95962', '32698848018', 'FATIAMENTO_PEDIDOS', NULL, 'Petições iniciais com texto quase idêntico, mudando apenas nome da parte.', 'AGUARDANDO_NOTA_TECNICA', 1096, 9, TIMESTAMP '2026-07-17 03:00:00', TIMESTAMP '2026-07-17 03:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['6022741-94.2024.8.05.4334', '2936571-23.2026.8.18.2547', '5569244-46.2026.8.07.5618']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000011', 'Mamanguape', 'Juiz Ricardo Farias', 'MAT1875', 'PB92590', '68558805795', 'TESE_ARTIFICIAL_MASSA', NULL, 'Parte autora afirma desconhecer a ação distribuída em seu nome.', 'NOVO', NULL, NULL, TIMESTAMP '2026-06-03 20:00:00', TIMESTAMP '2026-06-03 20:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['5394880-30.2026.8.15.9039', '8175395-81.2024.8.04.1233', '3500715-79.2024.8.12.9544']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000012', 'Areia', 'Juiz Fábio Cavalcanti', 'MAT2054', 'PB34567', NULL, 'TESE_ARTIFICIAL_MASSA', NULL, 'Parte autora afirma desconhecer a ação distribuída em seu nome.', 'ARQUIVADO', 1031, 5, TIMESTAMP '2026-07-01 11:00:00', TIMESTAMP '2026-07-01 11:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['5186414-95.2024.8.12.9174', '7817881-89.2026.8.05.3879', '3726327-32.2025.8.01.2939']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000013', 'Guarabira', 'Juíza Renata Sales', 'MAT1987', 'PB12345', NULL, 'FALSIFICACAO_DOCUMENTO', NULL, 'Advogado de outro estado concentrando dezenas de ações na mesma vara.', 'EM_PROVIDENCIA_CORREGEDORIA', 171, 2, TIMESTAMP '2026-05-30 16:00:00', TIMESTAMP '2026-05-30 16:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['6120253-39.2024.8.01.3165', '7685149-52.2025.8.03.4574', '6891254-92.2026.8.13.8786', '6555772-13.2024.8.09.2926']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000014', 'Cuité', 'Juiz André Barbosa', 'MAT2033', 'PB34567', '82946737519', 'FALSIFICACAO_DOCUMENTO', NULL, 'Comprovantes de residência reciclados identificados em múltiplos processos.', 'ARQUIVADO', 536, 4, TIMESTAMP '2026-07-06 07:00:00', TIMESTAMP '2026-07-06 07:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['1744682-65.2024.8.17.8822', '4305700-56.2025.8.03.5410', '6266630-94.2024.8.10.8309', '6188935-95.2025.8.11.6593']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000015', 'Areia', 'Juiz André Barbosa', 'MAT2033', 'PB95962', '51098994137', 'FATIAMENTO_PEDIDOS', NULL, 'Petições iniciais com texto quase idêntico, mudando apenas nome da parte.', 'EM_PROVIDENCIA_CORREGEDORIA', 1286, 4, TIMESTAMP '2026-08-01 17:00:00', TIMESTAMP '2026-08-01 17:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['4526280-65.2026.8.20.5280', '8801207-66.2025.8.07.8376', '8938746-31.2026.8.03.4650', '9647849-94.2026.8.20.5492']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000016', 'Santa Rita', 'Juiz Fábio Cavalcanti', 'MAT2054', 'PB23456', NULL, 'CAPTACAO_ILICITA_CLIENTELA', NULL, 'Advogado de outro estado concentrando dezenas de ações na mesma vara.', 'EM_PROVIDENCIA_CORREGEDORIA', 1827, 10, TIMESTAMP '2026-05-28 14:00:00', TIMESTAMP '2026-05-28 14:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['4262081-99.2025.8.16.6548', '5093379-28.2026.8.01.1747', '8132769-38.2024.8.17.7612', '1842524-81.2024.8.04.7479', '3237058-69.2026.8.17.9158']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000017', 'Solânea', 'Juiz André Barbosa', 'MAT2033', 'PB51952', '78124601904', 'CAPTACAO_ILICITA_CLIENTELA', NULL, 'Petições iniciais com texto quase idêntico, mudando apenas nome da parte.', 'EM_PROVIDENCIA_CORREGEDORIA', 1610, 15, TIMESTAMP '2026-07-17 11:00:00', TIMESTAMP '2026-07-17 11:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['5013879-45.2025.8.03.4682', '4934149-44.2025.8.11.8850', '2351868-27.2024.8.08.6276', '3563646-37.2024.8.14.6679', '6551268-79.2025.8.14.1021']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000018', 'Piancó', 'Juiz Marcos Andrade', 'MAT2201', 'PB86781', NULL, 'CAPTACAO_ILICITA_CLIENTELA', NULL, 'Petições iniciais com texto quase idêntico, mudando apenas nome da parte.', 'ARQUIVADO', 1770, 3, TIMESTAMP '2026-08-02 04:00:00', TIMESTAMP '2026-08-02 04:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['8030010-78.2026.8.18.9884', '4700025-72.2024.8.09.7141', '9147720-13.2025.8.11.6625', '3768991-69.2024.8.20.8752', '1452421-60.2026.8.19.0445']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000019', 'Itaporanga', 'Juiz Fábio Cavalcanti', 'MAT2054', 'PB45678', '54902591705', 'FATIAMENTO_PEDIDOS', NULL, 'Petições iniciais com texto quase idêntico, mudando apenas nome da parte.', 'ARQUIVADO', 484, 13, TIMESTAMP '2026-07-22 05:00:00', TIMESTAMP '2026-07-22 05:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['5232413-20.2025.8.01.8838', '1873851-54.2024.8.03.0660', '1520596-41.2024.8.01.2497', '5002120-26.2025.8.04.9241', '4656838-69.2026.8.09.6044']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000020', 'Guarabira', 'Juiz Ricardo Farias', 'MAT1875', 'PB12345', NULL, 'CAPTACAO_ILICITA_CLIENTELA', NULL, 'Fracionamento de pedidos para burlar o rito dos juizados especiais.', 'ARQUIVADO', 641, 7, TIMESTAMP '2026-05-23 10:00:00', TIMESTAMP '2026-05-23 10:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['4327421-19.2026.8.08.1670', '6059717-97.2026.8.04.9273', '1689025-54.2026.8.14.6072', '2156902-74.2026.8.11.0208', '8047416-72.2024.8.14.5935']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000021', 'Guarabira', 'Juiz Ricardo Farias', 'MAT1875', 'PB85713', NULL, 'CAPTACAO_ILICITA_CLIENTELA', NULL, 'Petições iniciais com texto quase idêntico, mudando apenas nome da parte.', 'AGUARDANDO_NOTA_TECNICA', 1146, 9, TIMESTAMP '2026-05-28 02:00:00', TIMESTAMP '2026-05-28 02:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['5118725-21.2025.8.15.3996', '8796514-82.2026.8.13.5512', '1481506-73.2025.8.06.7989', '4558780-55.2025.8.11.4582']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000022', 'João Pessoa', 'Juíza Helena Nóbrega', 'MAT2110', 'PB45678', '43321063465', 'ACAO_SEM_CONSENTIMENTO', NULL, 'Advogado de outro estado concentrando dezenas de ações na mesma vara.', 'EM_PROVIDENCIA_CORREGEDORIA', 2209, 13, TIMESTAMP '2026-07-03 21:00:00', TIMESTAMP '2026-07-03 21:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['1289293-21.2025.8.08.6626', '5081983-49.2026.8.19.6047', '8939993-80.2026.8.12.6972', '6549757-55.2026.8.15.4439', '6144280-42.2024.8.04.3156']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000023', 'Guarabira', 'Juíza Helena Nóbrega', 'MAT2110', 'PB45678', '81984544780', 'CAPTACAO_ILICITA_CLIENTELA', NULL, 'Comprovantes de residência reciclados identificados em múltiplos processos.', 'ARQUIVADO', 770, 1, TIMESTAMP '2026-06-17 14:00:00', TIMESTAMP '2026-06-17 14:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['4816534-56.2024.8.10.0232', '9961299-26.2025.8.02.0894', '5901239-99.2024.8.16.1681', '1205779-83.2025.8.16.7844']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000024', 'Itabaiana', 'Juíza Patrícia Lima', 'MAT1962', 'PB12345', '87627591831', 'CAPTACAO_ILICITA_CLIENTELA', NULL, 'Procurações com assinaturas visivelmente distintas das partes autoras.', 'NOVO', NULL, NULL, TIMESTAMP '2026-05-24 18:00:00', TIMESTAMP '2026-05-24 18:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['2429098-41.2024.8.18.6819', '4786401-76.2025.8.15.7254', '5988757-85.2025.8.10.9317', '2010496-88.2026.8.04.3405']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000025', 'Itabaiana', 'Juíza Renata Sales', 'MAT1987', 'PB23456', NULL, 'FALSIFICACAO_DOCUMENTO', NULL, 'Advogado de outro estado concentrando dezenas de ações na mesma vara.', 'AGUARDANDO_NOTA_TECNICA', 2346, 15, TIMESTAMP '2026-07-22 15:00:00', TIMESTAMP '2026-07-22 15:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['1547605-39.2025.8.10.7439', '2194198-97.2024.8.09.9664', '4318855-64.2024.8.18.3684', '3499627-44.2024.8.03.0978']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000026', 'Monteiro', 'Juíza Patrícia Lima', 'MAT1962', 'PB34567', NULL, 'TESE_ARTIFICIAL_MASSA', NULL, 'Advogado de outro estado concentrando dezenas de ações na mesma vara.', 'AGUARDANDO_NOTA_TECNICA', 1687, 6, TIMESTAMP '2026-06-13 13:00:00', TIMESTAMP '2026-06-13 13:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['2349590-86.2024.8.14.5281', '5200476-13.2024.8.08.9425', '1348050-96.2025.8.19.0660', '3939212-70.2026.8.15.4558', '4044882-84.2025.8.16.1495']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000027', 'Catolé do Rocha', 'Juíza Renata Sales', 'MAT1987', 'PB94348', '50782728919', 'CAPTACAO_ILICITA_CLIENTELA', NULL, 'Todos os comprovantes de energia têm a mesma data de vencimento e tipografia alterada.', 'AGUARDANDO_NOTA_TECNICA', 133, 11, TIMESTAMP '2026-06-11 21:00:00', TIMESTAMP '2026-06-11 21:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['2477398-50.2025.8.11.1900', '7780932-75.2024.8.18.7570', '7933143-16.2024.8.17.5928', '9363326-90.2025.8.02.3336', '5480460-80.2024.8.10.7179']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000028', 'Cuité', 'Juíza Helena Nóbrega', 'MAT2110', 'PB56717', NULL, 'CAPTACAO_ILICITA_CLIENTELA', NULL, 'Mesmo advogado ajuizou dezenas de ações idênticas de consignado em poucos dias.', 'NOVO', NULL, NULL, TIMESTAMP '2026-08-12 15:00:00', TIMESTAMP '2026-08-12 15:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['2970754-92.2024.8.16.4782', '9537455-44.2025.8.16.7737', '5089126-68.2026.8.05.6285', '4197911-86.2026.8.05.1144', '5634662-63.2025.8.17.4378']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000029', 'Monteiro', 'Juíza Patrícia Lima', 'MAT1962', 'PB88698', NULL, 'FATIAMENTO_PEDIDOS', NULL, 'Fracionamento de pedidos para burlar o rito dos juizados especiais.', 'AGUARDANDO_NOTA_TECNICA', 605, 7, TIMESTAMP '2026-08-11 01:00:00', TIMESTAMP '2026-08-11 01:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['6398528-34.2026.8.08.9369', '7425184-39.2025.8.02.5214', '8935224-58.2025.8.05.8116', '1621086-26.2026.8.19.5440', '2683838-66.2024.8.17.7487']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000030', 'Cajazeiras', 'Juíza Renata Sales', 'MAT1987', 'PB98265', '64514751164', 'CAPTACAO_ILICITA_CLIENTELA', NULL, 'Fracionamento de pedidos para burlar o rito dos juizados especiais.', 'ARQUIVADO', 1216, 6, TIMESTAMP '2026-07-02 23:00:00', TIMESTAMP '2026-07-02 23:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['9186400-79.2024.8.20.1122', '4938791-90.2026.8.10.3728', '2516313-65.2024.8.04.7270', '3791137-98.2025.8.01.0754']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000031', 'Pombal', 'Juiz André Barbosa', 'MAT2033', 'PB45678', NULL, 'TESE_ARTIFICIAL_MASSA', NULL, 'Mesmo advogado ajuizou dezenas de ações idênticas de consignado em poucos dias.', 'EM_ANALISE_NUMOPED', 1994, 2, TIMESTAMP '2026-07-18 12:00:00', TIMESTAMP '2026-07-18 12:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['5040453-73.2026.8.05.3805', '8736811-91.2025.8.15.4184', '1157662-69.2025.8.18.2589', '2239197-66.2025.8.19.4902', '8118722-98.2025.8.15.4950']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000032', 'Bayeux', 'Juiz André Barbosa', 'MAT2033', 'PB56140', '51658086456', 'FALSIFICACAO_DOCUMENTO', NULL, 'Fracionamento de pedidos para burlar o rito dos juizados especiais.', 'EM_PROVIDENCIA_CORREGEDORIA', 2388, 6, TIMESTAMP '2026-06-17 23:00:00', TIMESTAMP '2026-06-17 23:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['1135861-82.2026.8.02.9936', '9333700-46.2024.8.20.5773', '4674286-91.2024.8.20.4107', '3294774-90.2024.8.02.5062']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000033', 'Cajazeiras', 'Juíza Renata Sales', 'MAT1987', 'PB94348', NULL, 'FALSIFICACAO_DOCUMENTO', NULL, 'Parte autora afirma desconhecer a ação distribuída em seu nome.', 'NOVO', NULL, NULL, TIMESTAMP '2026-06-26 03:00:00', TIMESTAMP '2026-06-26 03:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['3760597-42.2025.8.10.5550', '2932227-69.2024.8.05.3697', '7667458-81.2025.8.03.6465', '1233516-43.2026.8.04.7452']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000034', 'Itabaiana', 'Juiz André Barbosa', 'MAT2033', 'PB98540', '75428766878', 'FALSIFICACAO_DOCUMENTO', NULL, 'Parte autora afirma desconhecer a ação distribuída em seu nome.', 'ARQUIVADO', 1873, 6, TIMESTAMP '2026-07-26 12:00:00', TIMESTAMP '2026-07-26 12:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['2060605-91.2025.8.10.6690', '2957497-27.2024.8.02.4987', '9264869-24.2024.8.08.8808']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000035', 'Princesa Isabel', 'Juiz André Barbosa', 'MAT2033', 'PB51739', '97681261401', 'FALSIFICACAO_DOCUMENTO', NULL, 'Fracionamento de pedidos para burlar o rito dos juizados especiais.', 'AGUARDANDO_NOTA_TECNICA', 1381, 4, TIMESTAMP '2026-08-10 23:00:00', TIMESTAMP '2026-08-10 23:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['1548888-98.2025.8.07.7265', '8460758-40.2025.8.04.6019', '7017548-17.2025.8.09.3110', '3049499-68.2024.8.07.9784']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000036', 'Cajazeiras', 'Juíza Helena Nóbrega', 'MAT2110', 'PB23456', NULL, 'TESE_ARTIFICIAL_MASSA', NULL, 'Procurações com assinaturas visivelmente distintas das partes autoras.', 'EM_ANALISE_NUMOPED', 1101, 1, TIMESTAMP '2026-08-10 19:00:00', TIMESTAMP '2026-08-10 19:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['5651899-28.2024.8.18.4107', '3927575-24.2026.8.01.2160', '1249314-55.2024.8.19.5305']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000037', 'Cajazeiras', 'Juiz André Barbosa', 'MAT2033', 'PB51739', '80274422288', 'CAPTACAO_ILICITA_CLIENTELA', NULL, 'Comprovantes de residência reciclados identificados em múltiplos processos.', 'AGUARDANDO_NOTA_TECNICA', 531, 5, TIMESTAMP '2026-07-25 22:00:00', TIMESTAMP '2026-07-25 22:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['9748499-48.2025.8.01.0997', '9035982-61.2025.8.04.8033', '8441741-19.2024.8.11.9967']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000038', 'Cuité', 'Juíza Camila Duarte', 'MAT2299', 'PB34567', '68434863125', 'FALSIFICACAO_DOCUMENTO', NULL, 'Comprovantes de residência reciclados identificados em múltiplos processos.', 'EM_PROVIDENCIA_CORREGEDORIA', 260, 10, TIMESTAMP '2026-07-27 07:00:00', TIMESTAMP '2026-07-27 07:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['8575834-39.2025.8.11.7431', '7689582-63.2026.8.04.5121', '8159911-50.2026.8.09.6133', '3560856-97.2025.8.03.1495', '2432176-21.2025.8.04.6106']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000039', 'Princesa Isabel', 'Juíza Camila Duarte', 'MAT2299', 'PB55697', '69057021183', 'TESE_ARTIFICIAL_MASSA', NULL, 'Petições iniciais com texto quase idêntico, mudando apenas nome da parte.', 'EM_PROVIDENCIA_CORREGEDORIA', 1110, 1, TIMESTAMP '2026-08-09 05:00:00', TIMESTAMP '2026-08-09 05:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['6899644-23.2026.8.17.3486', '3595941-94.2025.8.08.1774', '6873938-81.2025.8.04.4565', '4794319-64.2026.8.20.9122']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000040', 'Guarabira', 'Juiz Ricardo Farias', 'MAT1875', 'PB56717', NULL, 'FATIAMENTO_PEDIDOS', NULL, 'Procurações com assinaturas visivelmente distintas das partes autoras.', 'EM_ANALISE_NUMOPED', 1711, 5, TIMESTAMP '2026-05-27 10:00:00', TIMESTAMP '2026-05-27 10:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['2167449-28.2026.8.01.1504', '9900022-37.2025.8.14.7433', '6719285-30.2025.8.10.5315', '2425342-16.2024.8.06.0816', '2368710-44.2025.8.14.7958']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000041', 'Santa Rita', 'Juíza Renata Sales', 'MAT1987', 'PB12345', '90223487600', 'CAPTACAO_ILICITA_CLIENTELA', NULL, 'Todos os comprovantes de energia têm a mesma data de vencimento e tipografia alterada.', 'EM_PROVIDENCIA_CORREGEDORIA', 1572, 9, TIMESTAMP '2026-07-21 20:00:00', TIMESTAMP '2026-07-21 20:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['7631419-86.2024.8.01.3350', '6056977-37.2024.8.09.4743', '6504979-25.2024.8.16.7056']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000042', 'Bayeux', 'Juíza Camila Duarte', 'MAT2299', 'PB12345', '88653996486', 'FATIAMENTO_PEDIDOS', NULL, 'Fracionamento de pedidos para burlar o rito dos juizados especiais.', 'AGUARDANDO_NOTA_TECNICA', 703, 2, TIMESTAMP '2026-07-15 14:00:00', TIMESTAMP '2026-07-15 14:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['2932288-61.2024.8.11.2816', '8719145-98.2025.8.03.7156', '2776088-41.2025.8.19.6561', '9790632-20.2025.8.10.5563']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000043', 'Solânea', 'Juíza Renata Sales', 'MAT1987', 'PB64628', NULL, 'FATIAMENTO_PEDIDOS', NULL, 'Procurações com assinaturas visivelmente distintas das partes autoras.', 'EM_PROVIDENCIA_CORREGEDORIA', 1338, 14, TIMESTAMP '2026-05-22 04:00:00', TIMESTAMP '2026-05-22 04:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['2724773-28.2025.8.07.2843', '3564729-93.2024.8.06.8096', '8783840-82.2026.8.15.9249']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000044', 'Catolé do Rocha', 'Juiz Fábio Cavalcanti', 'MAT2054', 'PB77651', '88489125735', 'FALSIFICACAO_DOCUMENTO', NULL, 'Petições iniciais com texto quase idêntico, mudando apenas nome da parte.', 'AGUARDANDO_NOTA_TECNICA', 360, 9, TIMESTAMP '2026-07-30 14:00:00', TIMESTAMP '2026-07-30 14:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['8581856-14.2024.8.12.4704', '2287332-92.2024.8.20.9734', '9507186-59.2025.8.19.9082', '1688246-67.2026.8.07.5268', '8979730-74.2024.8.02.7382']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000045', 'Guarabira', 'Juiz Marcos Andrade', 'MAT2201', 'PB45678', NULL, 'CAPTACAO_ILICITA_CLIENTELA', NULL, 'Parte autora afirma desconhecer a ação distribuída em seu nome.', 'EM_PROVIDENCIA_CORREGEDORIA', 594, 15, TIMESTAMP '2026-08-02 06:00:00', TIMESTAMP '2026-08-02 06:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['5746166-59.2025.8.11.9796', '1878547-90.2026.8.11.1080', '6531293-22.2026.8.13.4656', '5227787-94.2026.8.05.5462']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000046', 'Pombal', 'Juiz Ricardo Farias', 'MAT1875', 'PB89453', NULL, 'FALSIFICACAO_DOCUMENTO', NULL, 'Mesmo advogado ajuizou dezenas de ações idênticas de consignado em poucos dias.', 'EM_PROVIDENCIA_CORREGEDORIA', 331, 5, TIMESTAMP '2026-07-26 20:00:00', TIMESTAMP '2026-07-26 20:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['7319630-92.2025.8.05.8625', '2567880-92.2026.8.14.8332', '7070468-12.2025.8.10.2954', '4593556-53.2025.8.07.3712']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000047', 'Sousa', 'Juiz Marcos Andrade', 'MAT2201', 'PB95962', NULL, 'FALSIFICACAO_DOCUMENTO', NULL, 'Procurações com assinaturas visivelmente distintas das partes autoras.', 'EM_ANALISE_NUMOPED', 2351, 8, TIMESTAMP '2026-07-19 13:00:00', TIMESTAMP '2026-07-19 13:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['3777286-66.2024.8.14.5969', '4985095-66.2026.8.10.7356', '4925917-78.2024.8.10.7685']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000048', 'Itaporanga', 'Juiz Ricardo Farias', 'MAT1875', 'PB52082', NULL, 'OUTROS', 'Reciclagem de laudos periciais adulterados', 'Procurações com assinaturas visivelmente distintas das partes autoras.', 'EM_PROVIDENCIA_CORREGEDORIA', 233, 7, TIMESTAMP '2026-08-09 11:00:00', TIMESTAMP '2026-08-09 11:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['1875431-92.2025.8.12.9086', '2721170-76.2024.8.10.1375', '3689594-44.2025.8.17.2415', '8339571-21.2024.8.15.5728']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000049', 'Solânea', 'Juíza Camila Duarte', 'MAT2299', 'PB12345', '53070737542', 'TESE_ARTIFICIAL_MASSA', NULL, 'Parte autora afirma desconhecer a ação distribuída em seu nome.', 'EM_PROVIDENCIA_CORREGEDORIA', 1748, 2, TIMESTAMP '2026-05-27 08:00:00', TIMESTAMP '2026-05-27 08:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['3308538-14.2025.8.16.2274', '8870361-67.2026.8.01.1299', '1318832-42.2024.8.05.8992']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000050', 'Monteiro', 'Juíza Helena Nóbrega', 'MAT2110', 'PB12345', '72804493160', 'FALSIFICACAO_DOCUMENTO', NULL, 'Advogado de outro estado concentrando dezenas de ações na mesma vara.', 'EM_PROVIDENCIA_CORREGEDORIA', 2169, 9, TIMESTAMP '2026-06-27 22:00:00', TIMESTAMP '2026-06-27 22:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['9646569-83.2024.8.05.4773', '8201433-10.2026.8.12.3942', '7988844-33.2026.8.03.8577']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000051', 'Solânea', 'Juiz Marcos Andrade', 'MAT2201', 'PB45678', '45963059926', 'ACAO_SEM_CONSENTIMENTO', NULL, 'Mesmo advogado ajuizou dezenas de ações idênticas de consignado em poucos dias.', 'EM_ANALISE_NUMOPED', 194, 13, TIMESTAMP '2026-07-06 18:00:00', TIMESTAMP '2026-07-06 18:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['5045421-94.2026.8.04.9534', '6578736-27.2024.8.12.8946', '6679880-92.2024.8.15.7841', '4058117-27.2024.8.15.0606']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000052', 'Santa Rita', 'Juiz Marcos Andrade', 'MAT2201', 'PB34567', NULL, 'OUTROS', 'Reciclagem de laudos periciais adulterados', 'Petições iniciais com texto quase idêntico, mudando apenas nome da parte.', 'AGUARDANDO_NOTA_TECNICA', 2141, 5, TIMESTAMP '2026-06-10 13:00:00', TIMESTAMP '2026-06-10 13:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['4206183-46.2025.8.02.5439', '5583814-25.2025.8.14.6554', '8377556-59.2025.8.06.8130']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000053', 'Patos', 'Juiz André Barbosa', 'MAT2033', 'PB23456', NULL, 'FATIAMENTO_PEDIDOS', NULL, 'Petições iniciais com texto quase idêntico, mudando apenas nome da parte.', 'NOVO', NULL, NULL, TIMESTAMP '2026-07-25 05:00:00', TIMESTAMP '2026-07-25 05:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['8481225-87.2026.8.14.2731', '8447546-54.2025.8.02.5777', '8297090-45.2026.8.02.1230', '7815076-56.2026.8.06.0511']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000054', 'Itaporanga', 'Juiz Marcos Andrade', 'MAT2201', 'PB23456', NULL, 'FATIAMENTO_PEDIDOS', NULL, 'Todos os comprovantes de energia têm a mesma data de vencimento e tipografia alterada.', 'EM_ANALISE_NUMOPED', 1600, 15, TIMESTAMP '2026-06-10 08:00:00', TIMESTAMP '2026-06-10 08:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['8548448-57.2025.8.15.1260', '3310140-77.2025.8.13.5151', '5675638-41.2024.8.01.3049']) AS p;

WITH ins AS (
  INSERT INTO denuncias (numero_protocolo, comarca, nome_comunicante, matricula_comunicante, oab_advogado, cpf_cnpj_parte, tipologia, tipologia_outros, relato, status, volume_processual_estimado, comarcas_atingidas, data_criacao, data_atualizacao)
  VALUES ('LP-SEED-000055', 'Princesa Isabel', 'Juíza Renata Sales', 'MAT1987', 'PB34567', NULL, 'FALSIFICACAO_DOCUMENTO', NULL, 'Advogado de outro estado concentrando dezenas de ações na mesma vara.', 'EM_PROVIDENCIA_CORREGEDORIA', 728, 13, TIMESTAMP '2026-07-23 02:00:00', TIMESTAMP '2026-07-23 02:00:00')
  ON CONFLICT (numero_protocolo) DO NOTHING
  RETURNING id
)
INSERT INTO denuncia_processos_paradigmas (denuncia_id, numero_processo)
SELECT id, p FROM ins, unnest(ARRAY['3057514-27.2024.8.15.2829', '8467726-21.2026.8.11.5693', '2089496-80.2026.8.10.4915']) AS p;
