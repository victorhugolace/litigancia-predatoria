CREATE SEQUENCE denuncia_protocolo_seq;
SELECT setval('denuncia_protocolo_seq', COALESCE(
    (SELECT MAX(split_part(numero_protocolo, '-', 3)::BIGINT)
     FROM denuncias WHERE numero_protocolo ~ '^LP-[0-9]{4}-[0-9]+$'), 0) + 1, false);
