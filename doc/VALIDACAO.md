# Validação da entrega local

Validação concluída em 1º de outubro de 2026, com imagens Docker locais,
PostgreSQL 16 e Chromium. O frontend continua em Angular 17, TypeScript,
SCSS e Chart.js. A validação ocorreu no ambiente local, sem implantação institucional.

## Resultados

| Verificação | Resultado |
| --- | --- |
| API com PostgreSQL real | 15 testes, zero falhas, erros ou testes ignorados |
| Navegador em desktop | 6 fluxos aprovados |
| Navegador em celular, emulação Pixel 7 | 6 fluxos aprovados |
| Compilação Java e Angular | Imagens geradas no builder limitado, sequencialmente |
| Proteção da suíte da API | Sem `DB_NAME=litigancia_test`, falha antes de iniciar a conexão |
| Falha de build | Script retorna código 1 e encerra o builder |
| Banco existente | 55 registros preservados na migração V8; assinatura dos campos da tabela idêntica antes e depois |
| Controle de acesso | Registro e andamento públicos; painel, detalhes, análise e PDFs exigem analista |
| Aplicação atualizada em localhost e 127.0.0.1 | Login, senha inválida, sessão, painel, acompanhamento público e logout aprovados; login por 127.0.0.1 confirmado também na tela |
| Compose institucional | Sintaxe validada com valores temporários; ambiente não iniciado |

A suíte da API cobre normalização e validação, CPF/CNPJ numérico e alfanumérico,
processos repetidos, geração concorrente de protocolos, períodos e semanas vazias,
paginação, histórico, conflito de versão, PDFs reais e rejeição sem cadastro parcial,
login, tentativa após senha inválida, CSRF, autoria, acesso por perfil e encerramento
da sessão. Também cobre cadastro público sem matrícula, PDF sem conta, projeção
pública sem dados pessoais ou notas internas e CORS nas duas origens locais.

Os seis fluxos de navegador são repetidos em desktop e celular: navegação pública,
proteção gerencial e login após senha inválida; cadastro sem conta ou matrícula,
PDF, consulta pública e download pela gestão; análise com histórico e conflito
entre duas abas; indisponibilidade e nova tentativa do painel; recuperação da
conexão inicial da API; e novo login após expiração da sessão gerencial.
O download é comparado byte a byte com o arquivo enviado. As verificações incluem
foco do modal, fechamento com Escape e ausência de overflow horizontal.
Capturas do login e acompanhamento público foram inspecionadas visualmente.
Capturas do painel e análise também foram geradas nesta execução.

Os testes usam dois bancos temporários independentes, sem portas publicadas.
A aplicação de demonstração usa seu volume persistente original. A comparação
com o backup conferiu IDs, protocolos, comarca, nome e matrícula do comunicante,
tipologia, relato e data de criação dos 55 casos anteriores. Normalizações e novos
campos seguem as migrações; não se afirma que todas as colunas ficaram idênticas.
Backups, logs, capturas e traces locais ficam em `tmp/`, fora do Git. Os resultados
desta correção estão em `tmp/correcao-api-test.log` e `tmp/correcao-e2e-test.log`.
O teste da aplicação local consta em `tmp/correcao-smoke-local.log`; o backup
anterior à migração V8 está em `tmp/correcao-banco-antes.sql`. A assinatura
calculada sobre os campos de cada linha da tabela `denuncias`, ordenada pelo ID,
permaneceu idêntica após a atualização.

## Correção de acesso público e login

Foi reproduzida uma resposta 403 de CORS quando a origem era
`http://127.0.0.1:4200`. A configuração local permite `localhost` e `127.0.0.1`,
e o proxy preserva a porta do cabeçalho Host. Origens externas continuam
rejeitadas pela configuração CORS.

O login agora atende somente à equipe gerencial. No ambiente demonstrativo,
a tela mostra `analista` / `oficina-analista`. Falhas da conexão inicial permitem
tentar conectar novamente; chamadas de login/logout têm limite de espera.
O canal público renova o token CSRF antes do envio, inclusive quando a sessão
expira durante o preenchimento. A migração V8 torna a matrícula opcional.

O acompanhamento público retorna somente protocolo, etapa, descrição da etapa,
data de registro e última atualização. A suíte verifica os campos exatos da
resposta mesmo após uma análise com nota interna. A identidade do comunicante,
os processos, os documentos e a nota permanecem na API restrita à gestão.

Para reproduzir a validação no PowerShell:

```powershell
./scripts/validate.ps1
```

## Recursos Docker

Os limites efetivos foram conferidos com `docker inspect`: PostgreSQL com
0,50 CPU/256 MiB, API com 0,75 CPU/768 MiB e Nginx com 0,25 CPU/64 MiB.
O teto de RAM dos três serviços soma 1.088 MiB. O builder dedicado usa 1 CPU
e 2 GiB, um build por vez, sem swap adicional, e fica parado fora das compilações.
Docker Desktop/WSL e o sistema operacional têm consumo adicional.

Os contextos de compilação usam listas de permissão em `.dockerignore`.
Não foi alterado o builder padrão nem o limite global de memória do Docker/WSL.
O README descreve o comando de build com esses limites; `compose up --build`
não utiliza automaticamente o builder dedicado.

## Auditoria de dependências

`npm audit --package-lock-only --omit=dev` encontrou **8 pacotes de produção
afetados**, quatro classificados como alta gravidade e quatro como moderada:

| Pacote | Gravidade máxima reportada |
| --- | --- |
| `@angular/animations` | Moderada |
| `@angular/common` | Alta |
| `@angular/compiler` | Alta |
| `@angular/core` | Alta |
| `@angular/forms` | Moderada |
| `@angular/platform-browser` | Moderada |
| `@angular/platform-browser-dynamic` | Moderada |
| `@angular/router` | Alta |

O número conta pacotes, não vulnerabilidades únicas. A auditoria relaciona
18 avisos distintos, com condições específicas, incluindo vazamento de token
[XSRF](https://github.com/advisories/GHSA-58c5-g7wp-6w37), esgotamento de memória
em [formatação de datas](https://github.com/advisories/GHSA-48r7-hpm6-gfxm) e
problemas de [cache de requisições](https://github.com/advisories/GHSA-39pv-4j6c-2g6v).
Não foi realizada uma análise de exploração de cada aviso no aplicativo.

O npm aponta correções por atualização de versão principal. Angular 17 foi
preservado nesta entrega; não foi executado `npm audit fix --force`. A atualização
para uma versão suportada, com repetição destas suítes e revisão de compatibilidade,
permanece uma pendência antes do uso institucional. Aprovação nos testes funcionais
não elimina esses alertas de segurança.

## Limites de implantação e integração

A configuração sem seed possui volume separado, credenciais obrigatórias e cookie
Secure, exigindo acesso HTTPS. Foi validada apenas a configuração Compose, sem
provisionar TLS, SSO ou infraestrutura institucional.

SSO, usuários institucionais, PJe/Data Lake, SEI, e-mail, OCR/NLP, consultas à Receita,
cartografia oficial e ofícios continuam dependendo das interfaces e regras do
Tribunal. O PDF é validado por extensão, assinatura e final do arquivo; análise de
conteúdo e antivírus não foram integrados. Os critérios de atuação precisam da
revisão dos especialistas prevista no documento da oficina.
