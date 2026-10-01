# Validação da entrega local

Validação concluída em 1º de outubro de 2026, com imagens Docker locais,
PostgreSQL 16 e Chromium. O frontend continua em Angular 17, TypeScript,
SCSS e Chart.js. Não houve push nem implantação institucional.

## Resultados

| Verificação | Resultado |
| --- | --- |
| API com PostgreSQL real | 12 testes, zero falhas, erros ou testes ignorados |
| Navegador em desktop | 4 fluxos aprovados |
| Navegador em celular, emulação Pixel 7 | 4 fluxos aprovados |
| Compilação Java e Angular | Imagens geradas no builder limitado, sequencialmente |
| Proteção da suíte da API | Sem `DB_NAME=litigancia_test`, falha antes de iniciar a conexão |
| Falha de build | Script retorna código 1 e encerra o builder |
| Banco existente | 55 registros originais preservados após as migrações V1–V7 |
| Aplicação atualizada no localhost | Login/logout dos dois perfis; painel retorna 401 anônimo, 403 comunicante e 200 analista |
| Compose institucional | Sintaxe validada com valores temporários; ambiente não iniciado |

A suíte da API cobre normalização e validação, CPF/CNPJ numérico e alfanumérico,
processos repetidos, geração concorrente de protocolos, períodos e semanas vazias,
paginação, histórico, conflito de versão, PDFs reais e rejeição sem cadastro parcial,
login, CSRF, autoria, acesso por perfil e encerramento da sessão.

Os quatro fluxos de navegador são repetidos em desktop e celular: acesso anônimo
e login inválido; cadastro completo, PDF, acompanhamento e logout; análise com
histórico e conflito entre duas abas; indisponibilidade e nova tentativa do painel.
O download é comparado byte a byte com o arquivo enviado. As verificações incluem
foco do modal, fechamento com Escape e ausência de overflow horizontal.
Capturas do painel, análise e acompanhamento foram inspecionadas visualmente.

Os testes usam dois bancos temporários independentes, sem portas publicadas.
A aplicação de demonstração usa seu volume persistente original. A comparação
com o backup conferiu IDs, protocolos, comarca, nome e matrícula do comunicante,
tipologia, relato e data de criação dos 55 casos anteriores. Normalizações e novos
campos seguem as migrações; não se afirma que todas as colunas ficaram idênticas.
Backups, logs, capturas e traces locais ficam em `tmp/`, fora do Git.

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
