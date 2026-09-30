# Litigância Predatória — LYNO/TJPB

Implementação executável do "Esqueleto das Entregas da Oficina de Litigância Predatória":
formulário de denúncia (4 blocos do protótipo) + dashboard de Centro de Inteligência
(visão executiva, geográfica/comportamental e operacional/kanban), em **Angular 17** +
**Spring Boot 3** + **PostgreSQL**.

## Rodando com Docker (recomendado)

Pré-requisito: Docker e Docker Compose instalados.

```bash
docker compose up --build
```

- Formulário de denúncia: http://localhost:4200/denunciar
- Painel de controle: http://localhost:4200/painel
- API: http://localhost:8080/api

O Compose usa o perfil `demo`, com **55 denúncias de demonstração** (`backend/src/main/resources/demo/data.sql`),
espalhadas por 20 comarcas da Paraíba, com OABs reincidentes propositais para o ranking e
datas relativas à semana da primeira carga para a tendência — assim o painel já nasce populado para a oficina,
sem precisar cadastrar nada manualmente.

Para começar do zero, remova o volume: `docker compose down -v`.

## Rodando em desenvolvimento (sem Docker)

**Backend** (Java 21 + Maven, requer um PostgreSQL local — veja variáveis em `application.yml`):
```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

**Frontend** (Node 20+):
```bash
cd frontend
npm install
npm start
```
Acesse http://localhost:4200 — o `environment.ts` já aponta para `http://localhost:8080/api`.

## O que foi implementado

**Formulário (`/denunciar`)** — wizard de 4 passos espelhando o protótipo do documento:
1. Identificação da origem (comarca, comunicante, matrícula)
2. Alvo da suspeita (OAB obrigatória, CPF/CNPJ opcional)
3. Tipologia da ação (seleção única, as 6 opções sugeridas)
4. Materialidade (3 a 5 processos paradigmas com máscara CNJ validada, relato de até 500
   caracteres, placeholder de upload de anexos)

Ao enviar, o backend gera um número de protocolo (`LP-AAAA-NNNNNN`) e devolve a confirmação.

**Painel (`/painel`)**:
- **Visão executiva**: total de denúncias, volume processual afetado, comarcas afetadas,
  ranking de reincidência (Top 10 por OAB)
- **Visão geográfica/comportamental**: mapa de calor por comarca (barras de intensidade —
  substitui o heatmap geográfico até haver shapefile da Paraíba integrado), gráfico de barras
  de tipologia, gráfico de linha de tendência semanal (últimas 12 semanas)
- **Visão operacional**: kanban com as 5 etapas do fluxo (Novo → Em Análise pelo NUMOPED →
  Aguardando Nota Técnica → Em Providência na Corregedoria → Arquivado) e drill-down ao clicar
  em qualquer card, mostrando o relato e os processos paradigmas informados pelo juiz

## O que ficou fora do escopo desta entrega (pontos de integração futura)

- Upload real de anexos (PDFs) — o campo existe na API (`anexos: string[]`) mas o storage de
  arquivo em si não foi implementado, conforme combinado
- Integração real com PJe/SEI (criação automática de processo administrativo, etiquetas
  inteligentes no painel do magistrado, índice de similaridade de petições via NLP) — descritas
  no documento como "Sugestões de Automações e APIs do PJe", ficam para uma fase posterior
- Autenticação/SSO com a intranet do Tribunal (hoje qualquer usuário pode preencher qualquer
  campo do Bloco 1 manualmente)

## Estrutura

```
litigancia-predatoria/
├── backend/    # Spring Boot 3 (Java 21), REST API + JPA + PostgreSQL
├── frontend/   # Angular 17 standalone, Chart.js
└── docker-compose.yml
```

## Testes e referência

O documento original e o mapeamento do escopo estão em [`doc/`](doc/ESCOPO.md).
O banco é versionado com Flyway; fora do perfil `demo`, não há carga de dados fictícios.

Testes de integração contra PostgreSQL real, em ambiente Docker separado:

```bash
docker compose -p litigancia-validacao -f compose.test.yml up --build --abort-on-container-exit --exit-code-from backend-test
docker compose -p litigancia-validacao -f compose.test.yml down
```
