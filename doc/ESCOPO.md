# Escopo e referência da oficina

Referência: **Esqueleto das entregas da Oficina de Litigância Predatória.pdf**, incluída nesta pasta.

| Parte do documento | Implementação local |
| --- | --- |
| Identificação da origem | Comarca, nome e matrícula; identidade institucional depende de SSO |
| Alvo da suspeita | OAB obrigatória; CPF/CNPJ opcional; normalização de identificadores |
| Tipologia | Seleção única das seis categorias; descrição obrigatória em Outros |
| Materialidade | De 3 a 5 processos distintos com máscara CNJ, relato de 500 caracteres, PDFs |
| Visão executiva | Indicadores do período selecionado e ranking por OAB ou CPF/CNPJ |
| Visão geográfica | Concentração por comarca em barras; mapa cartográfico depende de geometrias oficiais |
| Tendência | Doze semanas consecutivas, incluindo semanas sem denúncias |
| Fila de trabalho | Consulta, mudança de etapa, enriquecimento, histórico e acompanhamento por protocolo |

## Integrações externas

SSO da intranet, notificações por e-mail, criação de processos no SEI, consultas ao PJe/Data Lake,
relatórios de robôs, OCR/NLP, Receita Federal, etiquetas do PJe e ofícios institucionais
exigem APIs, credenciais e regras fornecidas pelo Tribunal. A aplicação não deve anunciar que
essas ações ocorreram enquanto não houver integração real. O processamento e o enriquecimento
disponíveis aqui são manuais.

As categorias e os critérios de atuação continuam sujeitos à validação dos especialistas
indicada no documento. A aplicação registra suspeitas e não decide se há fraude.

## Dados e implantação

O perfil `demo` carrega registros fictícios para a oficina. Fora desse perfil, não há seed.
As datas do seed são calculadas em relação à semana de sua primeira carga; reiniciar a aplicação
não modifica registros existentes. Nunca utilizar o banco demonstrativo como base institucional.

As migrações Flyway adotam o esquema da versão inicial sem apagar denúncias. Toda migração deve
ser validada num banco de teste e, para implantação institucional, sobre uma cópia de segurança.

## Validação reproduzível

```sh
docker compose -p litigancia-validacao -f compose.test.yml up --build --abort-on-container-exit --exit-code-from backend-test
docker compose -p litigancia-validacao -f compose.test.yml down
```

Esse projeto Compose usa um PostgreSQL temporário independente, sem portas publicadas e sem
volume persistente. Não execute testes destrutivos no banco da aplicação.
