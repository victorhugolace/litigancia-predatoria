import { test, expect, Page } from '@playwright/test';
import { readdirSync, readFileSync } from 'node:fs';

async function entrar(page: Page) {
  await page.goto('/entrar');
  await page.getByLabel('Usuário', { exact: true }).fill('analista');
  await page.getByLabel('Senha', { exact: true }).fill('oficina-analista');
  await page.getByRole('button', { name: 'Entrar', exact: true }).click();
  await expect(page.getByRole('button', { name: 'Sair (analista)' })).toBeVisible();
}

async function semOverflow(page: Page) {
  expect(await page.evaluate(() => document.documentElement.scrollWidth <= window.innerWidth + 1)).toBe(true);
}

test('abre canal público, protege gestão e permite login após senha inválida', async ({ page }, info) => {
  await page.goto('/');
  await expect(page.getByRole('heading', { name: 'Reportar padrão suspeito' })).toBeVisible();
  await expect(page.getByRole('link', { name: 'Área gerencial' })).toBeVisible();
  await expect(page.getByRole('link', { name: 'Painel de Controle' })).toHaveCount(0);
  await page.getByRole('link', { name: 'Acompanhar caso', exact: true }).click();
  await expect(page.getByLabel('Número de protocolo')).toBeVisible();
  await page.goto('/painel');
  await expect(page).toHaveURL(/\/entrar/);
  await page.getByLabel('Usuário', { exact: true }).fill('analista');
  await page.getByLabel('Senha', { exact: true }).fill('senha-incorreta');
  await page.getByRole('button', { name: 'Entrar', exact: true }).click();
  await expect(page.getByRole('alert')).toContainText('Usuário ou senha inválidos');
  await expect(page.locator('.demo-access')).toContainText('oficina-analista');
  await page.getByLabel('Senha', { exact: true }).fill('oficina-analista');
  await page.getByRole('button', { name: 'Entrar', exact: true }).click();
  await expect(page).toHaveURL(/\/painel$/);
  await expect(page.locator('.kpi-card')).toHaveCount(3);
  await page.reload();
  await expect(page.getByRole('button', { name: 'Sair (analista)' })).toBeVisible();
  await page.getByRole('button', { name: 'Sair (analista)' }).click();
  await expect(page).toHaveURL(/\/denunciar$/);
  await page.goto('/painel');
  await expect(page).toHaveURL(/\/entrar/);
  await semOverflow(page);
  await page.screenshot({ path: info.outputPath('login-gerencial.png'), fullPage: true });
});

test('público registra PDF sem conta, acompanha andamento e gestão acessa o documento', async ({ page, context }, info) => {
  const falhas: string[] = [];
  page.on('pageerror', erro => falhas.push(erro.message));
  await page.goto('/denunciar');
  await expect(page.getByRole('link', { name: 'Painel de Controle' })).toHaveCount(0);
  await page.getByRole('button', { name: 'Continuar', exact: true }).click();
  await expect(page.getByText('Informe a comarca ou unidade judiciária.')).toBeVisible();
  await page.getByLabel('Comarca / Unidade Judiciária').fill(' João Pessoa ');
  await page.getByLabel('Nome do comunicante').fill('Teste da oficina');
  await page.getByRole('button', { name: 'Continuar', exact: true }).click();
  await page.getByLabel('Número da OAB').fill('inválida');
  await page.getByRole('button', { name: 'Continuar', exact: true }).click();
  await expect(page.getByText('Informe a UF e o número da OAB')).toBeVisible();
  await page.getByLabel('Número da OAB').fill('pb / 12345');
  await page.getByLabel('CPF / CNPJ').fill(info.project.name === 'mobile' ? '12.abc.345/01de-35' : '52998224725');
  await page.getByRole('button', { name: 'Continuar', exact: true }).click();
  await page.getByRole('radio', { name: 'Outros', exact: true }).check();
  await page.getByRole('button', { name: 'Continuar', exact: true }).click();
  await expect(page.getByText('Descreva a tipologia em até')).toBeVisible();
  await page.getByLabel('Descreva a tipologia observada').fill('Padrão descrito no teste de navegador');
  await page.getByRole('button', { name: 'Continuar', exact: true }).click();
  const processos = page.getByPlaceholder('NNNNNNN-DD.AAAA.J.TR.OOOO');
  await processos.nth(0).fill('00000010120258150001');
  await processos.nth(1).fill('00000010120258150001');
  await processos.nth(2).fill('00000030320258150001');
  await expect(page.getByText('Informe processos diferentes')).toBeVisible();
  await processos.nth(1).fill('00000020220258150001');
  await page.getByLabel('Breve relato do padrão observado').fill('Repetição de documentos observada em três processos.');
  const pasta = process.env['REFERENCE_DIR'] ?? '/references';
  const pdf = `${pasta}/${readdirSync(pasta).find(nome => nome.endsWith('.pdf'))!}`;
  await page.locator('input[type=file]').setInputFiles(pdf);
  await expect(page.locator('.file-item')).toHaveCount(1);
  await semOverflow(page);
  // Uma sessão expirada durante o preenchimento não deve impedir o envio público.
  await context.clearCookies();
  await page.getByRole('button', { name: 'Registrar denúncia', exact: true }).click();
  await expect(page.getByRole('heading', { name: 'Denúncia registrada' })).toBeVisible();
  const protocolo = (await page.locator('.confirmation__protocolo').innerText()).trim();
  expect(protocolo).toMatch(/^LP-\d{4}-\d{6,}$/);
  await page.getByRole('button', { name: 'Acompanhar este caso' }).click();
  await expect(page.getByRole('heading', { name: protocolo, exact: true })).toBeVisible();
  await expect(page.locator('article')).toContainText('Etapa atual: Novo');
  await expect(page.locator('article')).not.toContainText('Teste da oficina');
  await expect(page.locator('article a[download]')).toHaveCount(0);
  const publico = await page.request.get(`/api/denuncias/protocolo/${protocolo}`);
  expect(Object.keys(await publico.json()).sort()).toEqual(['dataAtualizacao', 'dataCriacao', 'numeroProtocolo', 'status', 'statusDescricao']);
  await page.reload();
  await expect(page.getByRole('heading', { name: protocolo, exact: true })).toBeVisible();
  await semOverflow(page);
  await page.screenshot({ path: info.outputPath('acompanhamento.png'), fullPage: true });
  await page.getByLabel('Número de protocolo').fill('LP-2026-999999999');
  await page.getByRole('button', { name: 'Consultar', exact: true }).click();
  await expect(page.getByRole('alert')).toContainText('Protocolo não encontrado');
  await entrar(page);
  await page.getByRole('button').filter({ hasText: protocolo }).click();
  await expect(page.getByRole('dialog')).toContainText('Teste da oficina');
  const baixado = page.waitForEvent('download');
  await page.getByRole('dialog').locator('a[download]').click();
  expect(readFileSync((await (await baixado).path())!)).toEqual(readFileSync(pdf));
  await page.keyboard.press('Escape');
  await page.getByRole('button', { name: 'Sair (analista)' }).click();
  await expect(page).toHaveURL(/\/denunciar/);
  await page.goto('/acompanhar');
  await expect(page).toHaveURL(/\/acompanhar$/);
  expect(falhas).toEqual([]);
});

test('analista atualiza análise, audita e impede sobrescrita concorrente', async ({ page, context }, info) => {
  await entrar(page);
  await expect(page.locator('.kpi-card__value').first()).toHaveText(/\d+/);
  await page.getByLabel('Período dos indicadores').selectOption('TODOS');
  await expect(page.locator('.kanban__card').first()).toBeVisible();
  await semOverflow(page);
  await page.screenshot({ path: info.outputPath('painel.png'), fullPage: true });
  const card = page.locator('.kanban__card').first();
  const protocolo = (await card.locator('.kanban__card-protocolo').innerText()).trim();
  await card.click();
  const dialogo = page.getByRole('dialog');
  await expect(dialogo.getByLabel('Etapa', { exact: true })).toBeVisible();
  await expect(dialogo.getByRole('button', { name: 'Fechar', exact: true })).toBeFocused();
  await page.keyboard.press('Shift+Tab');
  expect(await page.evaluate(() => document.querySelector('[role=dialog]')?.contains(document.activeElement))).toBe(true);
  const segunda = await context.newPage();
  await segunda.goto('/painel');
  await segunda.getByRole('button').filter({ hasText: protocolo }).click();
  await expect(segunda.getByRole('dialog').getByLabel('Etapa', { exact: true })).toBeVisible();
  await dialogo.getByLabel('Etapa', { exact: true }).selectOption('EM_ANALISE_NUMOPED');
  await dialogo.getByLabel('Volume processual estimado').fill('0');
  await dialogo.getByLabel('Número de comarcas atingidas').fill('2');
  await dialogo.getByLabel('Resumo da nota técnica').fill('Verificação E2E com registro de autoria.');
  await dialogo.getByRole('button', { name: 'Salvar análise' }).click();
  await expect(dialogo.getByRole('status')).toHaveText('Análise atualizada.');
  await expect(dialogo.locator('.historico')).toContainText('analista');
  await segunda.getByLabel('Resumo da nota técnica').fill('Alteração desatualizada');
  await segunda.getByRole('button', { name: 'Salvar análise' }).click();
  await expect(segunda.getByRole('dialog').getByRole('alert')).toContainText('Outro analista alterou este caso');
  await segunda.getByRole('button', { name: 'Recarregar caso' }).click();
  await expect(segunda.getByLabel('Resumo da nota técnica')).toHaveValue('Verificação E2E com registro de autoria.');
  await semOverflow(page);
  await page.screenshot({ path: info.outputPath('analise.png'), fullPage: true });
  await page.keyboard.press('Escape');
  await expect(dialogo).toHaveCount(0);
  await segunda.close();
});

test('falha da API não aparece como indicadores zerados e permite tentar novamente', async ({ page }) => {
  await entrar(page);
  await expect(page.locator('.kpi-card')).toHaveCount(3);
  await page.route('**/api/dashboard/kpis*', rota => rota.fulfill({ status: 503, contentType: 'application/json', body: '{}' }));
  await page.getByLabel('Período dos indicadores').selectOption('MES');
  await expect(page.getByRole('alert')).toContainText('Não foi possível carregar o painel');
  await expect(page.locator('.kpi-card')).toHaveCount(0);
  await page.unroute('**/api/dashboard/kpis*');
  await page.getByRole('button', { name: 'Tentar novamente' }).click();
  await expect(page.locator('.kpi-card')).toHaveCount(3);
});

test('recupera conexão inicial com API e permite entrar sem recarregar a página', async ({ page }) => {
  await page.route('**/api/session', rota => rota.fulfill({ status: 503, contentType: 'application/json', body: '{}' }));
  await page.goto('/entrar');
  await expect(page.getByRole('alert')).toContainText('Não foi possível conectar à API');
  await page.unroute('**/api/session');
  await page.getByRole('button', { name: 'Tentar conectar', exact: true }).click();
  await expect(page.locator('.demo-access')).toContainText('oficina-analista');
  await page.getByLabel('Usuário', { exact: true }).fill('analista');
  await page.getByLabel('Senha', { exact: true }).fill('oficina-analista');
  await page.getByRole('button', { name: 'Entrar', exact: true }).click();
  await expect(page).toHaveURL(/\/painel$/);
  await expect(page.locator('.kpi-card')).toHaveCount(3);
});

test('sessão gerencial expirada permite autenticar novamente e preserva canal público', async ({ page, context }) => {
  await entrar(page);
  await expect(page.locator('.kpi-card')).toHaveCount(3);
  await context.clearCookies();
  await page.getByLabel('Período dos indicadores').selectOption('MES');
  await expect(page).toHaveURL(/\/entrar/);
  await page.getByLabel('Usuário', { exact: true }).fill('analista');
  await page.getByLabel('Senha', { exact: true }).fill('oficina-analista');
  await page.getByRole('button', { name: 'Entrar', exact: true }).click();
  await expect(page).toHaveURL(/\/painel$/);
  await expect(page.locator('.kpi-card')).toHaveCount(3);
  await page.getByRole('button', { name: 'Sair (analista)' }).click();
  await expect(page.getByRole('heading', { name: 'Reportar padrão suspeito' })).toBeVisible();
});
