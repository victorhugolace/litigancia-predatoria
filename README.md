# Litigância Predatória — LYNO/TJPB

Aplicação baseada no documento da oficina em `doc/`, com **Angular 17 standalone,
TypeScript, SCSS e Chart.js** no frontend; **Spring Boot 3, Java 21, JPA, Flyway e
PostgreSQL 16** no backend. Playwright é usado apenas para testes de navegador.

## Demonstração local com Docker

Requer Docker e Docker Compose:

```sh
./scripts/build.ps1
docker compose up -d --no-build
```

Acesse [http://localhost:4200](http://localhost:4200). Contas da demonstração:

| Usuário | Senha | Acesso |
| --- | --- | --- |
| `comunicante` | `oficina-comunicante` | Registrar e acompanhar seus casos |
| `analista` | `oficina-analista` | Registrar, consultar a fila, analisar e consultar indicadores |

Esse Compose utiliza o perfil `demo`, com 55 casos fictícios. A primeira carga
distribui as datas nas últimas 12 semanas. Reinícios não duplicam o seed nem
modificam os registros existentes. As portas 4200, 8080 e 5432 ficam no localhost.

### Limites de recursos

| Serviço | CPU máxima | RAM máxima |
| --- | --- | --- |
| PostgreSQL | 0,50 CPU | 256 MiB |
| API | 0,75 CPU | 768 MiB |
| Nginx/frontend | 0,25 CPU | 64 MiB |

O limite de RAM dos três serviços soma **1.088 MiB**; o Docker Desktop/WSL,
cache e processos do host têm consumo adicional. O heap Java fica em até 60%
da memória do container. O swap dos serviços não pode exceder o teto de RAM.

**Limites de containers não limitam a compilação das imagens.**
`scripts/build.ps1` usa um builder próprio com 1 CPU e 2 GiB de RAM, sem swap
adicional, executa um build por vez e encerra o builder ao terminar. O Angular
usa um worker e heap Node de 768 MiB; o Maven usa heap de 512 MiB. Esse builder
mantém cache e não altera o builder padrão nem configurações globais do Docker.

Os `.dockerignore` permitem somente fontes e arquivos necessários. Os testes
de navegador possuem um contexto separado em `Dockerfile.e2e.dockerignore`.

Evite `docker compose up --build` em máquinas com pouca memória livre: ele usa
o builder padrão. Em Linux/macOS, o equivalente ao script é criar o builder com
as opções de CPU/memória e o TOML em `docker/buildkitd.toml`, depois executar
`docker buildx build --builder litigancia-limitado --load` para cada imagem.
Veja a [documentação do driver Docker Container](https://docs.docker.com/build/builders/drivers/docker-container/).

`docker compose down` encerra os serviços preservando o banco. **Não use `down -v`
se precisar manter os dados**. Trocar a senha do PostgreSQL no ambiente não altera
a senha de um usuário já existente em um volume inicializado.

## Fluxos disponíveis

- `/entrar`: login com sessão de 30 minutos, cookies HttpOnly/SameSite e CSRF.
- `/denunciar`: quatro passos para origem, OAB e CPF/CNPJ, tipologia e evidências.
  Exige 3 a 5 processos distintos na máscara CNJ e relato de até 500 caracteres.
  CPF/CNPJ têm dígitos verificadores validados; a máscara CNJ não verifica o
  dígito do processo. OAB é normalizada, sem consulta ao cadastro da Ordem.
  CNPJs numéricos e alfanuméricos são aceitos, com letras preservadas em maiúsculas.
  O cálculo segue o [manual da Receita Federal](https://www.gov.br/receitafederal/pt-br/centrais-de-conteudo/publicacoes/documentos-tecnicos/cnpj/manual-dv-cnpj.pdf).
- PDFs opcionais: até 5 arquivos de 10 MB cada, enviados de verdade, gravados
  com o caso na mesma transação e disponíveis para download autorizado. A
  validação verifica extensão, assinatura e final do PDF; não substitui análise
  de conteúdo ou antivírus. Nomes antigos sem arquivo são identificados como legado.
- `/acompanhar`: consulta por protocolo. Comunicantes acessam apenas seus registros;
  analistas consultam a fila completa. Casos legados sem autor ficam com analistas.
- `/painel`: indicadores por mês, ano ou histórico, Top 10 por OAB ou CPF/CNPJ,
  concentração por comarca, tipologias e 12 semanas de tendência com zeros.
  A fila usa paginação e filtros por etapa sobre todo o histórico, independentemente
  do período dos indicadores. Falhas de conexão aparecem como erro, com nova tentativa.
- Análise: modal com processos, PDFs, mudança de etapa, estimativas, nota técnica e
  histórico de autoria. A versão do caso impede sobrescrever alterações concorrentes;
  um conflito exige recarregar os detalhes antes de editar novamente.

## Banco e migrações

O Hibernate valida o schema; o Flyway aplica as migrações em
`backend/src/main/resources/db/migration`. A migração inicial adota as tabelas
da versão original sem apagar denúncias. Os novos campos são preenchidos conforme
as migrações; o histórico registra ações feitas após sua implantação.

Antes de atualizar um banco existente, faça backup e teste em uma cópia.
Exemplo de backup, sem remover o volume:

```sh
docker compose exec -T db pg_dump -U lyno -d litigancia_predatoria --no-owner --no-privileges > backup.sql
```

Adapte usuário e banco se tiver configurado outros valores. Guarde backups fora
do Git; eles podem conter dados sensíveis.

## Testes no Docker

Os projetos de validação possuem PostgreSQL temporário próprio, sem portas
publicadas. **Não apontar testes para o banco da aplicação.**
O teste de integração exige `DB_NAME=litigancia_test` antes de iniciar o contexto;
uma execução Maven fora da composição não deve reutilizar o banco da aplicação.

API, concorrência, permissões, períodos, paginação e uploads:

```sh
./scripts/build.ps1 -Target api-test
docker compose -p litigancia-validacao -f compose.test.yml up --no-build --abort-on-container-exit --exit-code-from backend-test
docker compose -p litigancia-validacao -f compose.test.yml down
```

Fluxos completos no Chromium, em desktop e celular, com Angular, API e Nginx reais:

```sh
./scripts/build.ps1 -Target e2e
docker compose -p litigancia-e2e -f compose.e2e.yml up --no-build --abort-on-container-exit --exit-code-from browser-test
docker compose -p litigancia-e2e -f compose.e2e.yml down
```

O primeiro build baixa as imagens e dependências. Capturas e traces ficam em
`tmp/e2e`, ignorado pelo Git. O PDF de referência é montado somente para leitura
e usado como arquivo de teste. A versão do pacote Playwright deve coincidir com
a tag da imagem em `frontend/Dockerfile.e2e`.
Os testes e sua configuração também são montados para leitura, permitindo repetir
a suíte após ajustes sem reinstalar as dependências do runner.

No PowerShell, `./scripts/validate.ps1` executa as duas suítes e encerra os
ambientes de validação mesmo se algum teste falhar.

## Desenvolvimento sem Docker

Backend: Java 21, Maven e PostgreSQL disponível. As variáveis `DB_HOST`, `DB_PORT`,
`DB_NAME`, `DB_USER` e `DB_PASSWORD` configuram a conexão.

```sh
cd backend
mvn spring-boot:run "-Dspring-boot.run.profiles=demo"
```

Frontend: Node 20 e npm. `/api` passa pelo proxy Angular para `localhost:8080`.

```sh
cd frontend
npm ci
npm start
```

## Ambiente sem dados fictícios

`compose.production.yml` desativa o perfil demo, exige senhas fornecidas pelo
ambiente e usa um volume de banco separado. Defina `DB_PASSWORD`,
`APP_REPORTER_PASSWORD`, `APP_ANALYST_PASSWORD` e `CORS_ORIGINS` (origem HTTPS).
As senhas das contas precisam ter ao menos 12 caracteres. Usuários podem ser
alterados com `APP_REPORTER_USER` e `APP_ANALYST_USER`.

```sh
./scripts/build.ps1
docker compose -p litigancia-production -f docker-compose.yml -f compose.production.yml up --no-build -d
```

O acesso exige um proxy HTTPS: nesse modo o cookie é `Secure`. Os nomes dos
containers e as portas são os mesmos da demonstração; encerre a demonstração
antes de usar a configuração no mesmo host. Não grave credenciais no repositório.

A autenticação local possui duas contas configuráveis e serve à execução e
validação do fluxo. Uso institucional ainda exige integrar SSO, provisionamento
de usuários, política de acesso, operação e revisão dos especialistas.

## Limites e referência

A auditoria `npm audit --package-lock-only --omit=dev` sinalizou 8 pacotes de
produção do Angular 17: quatro com gravidade alta e quatro moderada. A ferramenta
indica atualização de versão principal; não foi aplicada migração automática.
Os alertas e os resultados da validação estão em [`doc/VALIDACAO.md`](doc/VALIDACAO.md).
Essa pendência precisa ser resolvida antes de considerar uso institucional.

O mapeamento do documento está em [`doc/ESCOPO.md`](doc/ESCOPO.md).
SEI, PJe/Data Lake, e-mail, OCR/NLP, Receita Federal e ofícios dependem de APIs,
credenciais e regras do Tribunal. O enriquecimento disponível é manual. A
concentração por comarca utiliza barras, sem cartografia oficial integrada.
O aplicativo registra suspeitas; a análise não é uma conclusão automática de fraude.
