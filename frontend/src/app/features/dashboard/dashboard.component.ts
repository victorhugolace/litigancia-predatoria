import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild, signal, inject } from '@angular/core';
import { forkJoin, Subscription } from 'rxjs';
import { FormsModule, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Chart, registerables } from 'chart.js';
import { DashboardService } from '../../core/services/dashboard.service';
import { DenunciaService } from '../../core/services/denuncia.service';
import {
  ComarcaItem, DenunciaResponse, KpisResponse, RankingItem,
  STATUS_LABELS, STATUS_ORDER, StatusDenuncia, TendenciaItem, TipologiaItem
} from '../../core/models/denuncia.model';

Chart.register(...registerables);

@Component({
  selector: 'lp-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('tipologiaCanvas') tipologiaCanvas?: ElementRef<HTMLCanvasElement>;
  @ViewChild('tendenciaCanvas') tendenciaCanvas?: ElementRef<HTMLCanvasElement>;

  readonly statusOrder = STATUS_ORDER;
  readonly statusLabels = STATUS_LABELS;
  private readonly fb = inject(FormBuilder);

  carregando = signal(true);
  erro = signal<string | null>(null);
  periodo = 'ANO';
  porDocumento = false;
  filtroStatus: StatusDenuncia | '' = '';
  pagina = 0;
  totalPaginas = 0;
  totalFila = 0;
  totaisPorStatus: Partial<Record<StatusDenuncia, number>> = {};
  carregandoDetalhe = signal(false);
  salvando = signal(false);
  erroDetalhe = signal<string | null>(null);
  sucessoEdicao = signal(false);
  readonly edicao = this.fb.group({
    status: [StatusDenuncia.NOVO, Validators.required],
    volume: [null as number | null, [Validators.min(0), Validators.pattern(/^\d+$/)]],
    comarcas: [null as number | null, [Validators.min(1), Validators.pattern(/^\d+$/)]],
    nota: ['', Validators.maxLength(800)]
  });
  readonly periodos: Record<string, string> = { MES: 'Mês atual', ANO: 'Ano atual', TODOS: 'Todo o histórico' };
  kpis = signal<KpisResponse | null>(null);
  ranking = signal<RankingItem[]>([]);
  tipologias = signal<TipologiaItem[]>([]);
  comarcas = signal<ComarcaItem[]>([]);
  tendencia = signal<TendenciaItem[]>([]);
  denuncias = signal<DenunciaResponse[]>([]);
  denunciaSelecionada = signal<DenunciaResponse | null>(null);

  private tipologiaChart?: Chart;
  private tendenciaChart?: Chart;
  private requisicao?: Subscription;
  private chartTimer?: ReturnType<typeof setTimeout>;
  private detalheRequest?: Subscription;
  private saveRequest?: Subscription;
  private focoAnterior?: HTMLElement;

  constructor(private dashboardService: DashboardService, public denunciaService: DenunciaService) {}

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.requisicao?.unsubscribe();
    clearTimeout(this.chartTimer);
    this.carregando.set(true);
    this.erro.set(null);
    this.requisicao = forkJoin({
      kpis: this.dashboardService.kpis(this.periodo),
      ranking: this.dashboardService.ranking(this.periodo, this.porDocumento),
      tipologia: this.dashboardService.tipologia(this.periodo),
      comarcas: this.dashboardService.comarcas(this.periodo),
      tendencia: this.dashboardService.tendencia(this.periodo),
      denuncias: this.denunciaService.listar(this.filtroStatus || undefined, this.pagina)
    }).subscribe({
      next: dados => {
        this.kpis.set(dados.kpis);
        this.ranking.set(dados.ranking);
        this.tipologias.set(dados.tipologia);
        this.comarcas.set(dados.comarcas);
        this.tendencia.set(dados.tendencia);
        this.denuncias.set(dados.denuncias.itens);
        this.totalPaginas = dados.denuncias.totalPaginas;
        this.totalFila = dados.denuncias.total;
        this.totaisPorStatus = dados.denuncias.totaisPorStatus;
        this.carregando.set(false);
        this.chartTimer = setTimeout(() => this.renderCharts(), 0);
      },
      error: () => {
        this.carregando.set(false);
        this.erro.set('Não foi possível carregar o painel. Verifique a conexão e tente novamente.');
      }
    });
  }

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {
    this.requisicao?.unsubscribe();
    this.detalheRequest?.unsubscribe();
    this.saveRequest?.unsubscribe();
    clearTimeout(this.chartTimer);
    this.tipologiaChart?.destroy();
    this.tendenciaChart?.destroy();
  }

  private renderCharts(): void {
    this.renderTipologiaChart();
    this.renderTendenciaChart();
  }

  private renderTipologiaChart(): void {
    if (!this.tipologiaCanvas) return;
    const dados = this.tipologias();
    this.tipologiaChart?.destroy();
    this.tipologiaChart = new Chart(this.tipologiaCanvas.nativeElement, {
      type: 'bar',
      data: {
        labels: dados.map(d => d.descricao),
        datasets: [{
          data: dados.map(d => d.total),
          backgroundColor: '#1c3a63',
          borderRadius: 4,
          maxBarThickness: 28
        }]
      },
      options: {
        indexAxis: 'y',
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: false } },
        scales: {
          x: { grid: { color: '#ece8dc' }, ticks: { color: '#6b6759', font: { size: 11 } } },
          y: { grid: { display: false }, ticks: { color: '#423f36', font: { size: 11.5 } } }
        }
      }
    });
  }

  private renderTendenciaChart(): void {
    if (!this.tendenciaCanvas) return;
    const dados = this.tendencia();
    this.tendenciaChart?.destroy();
    this.tendenciaChart = new Chart(this.tendenciaCanvas.nativeElement, {
      type: 'line',
      data: {
        labels: dados.map(d => this.formatarSemana(d.semana)),
        datasets: [{
          data: dados.map(d => d.total),
          borderColor: '#c9922b',
          backgroundColor: 'rgba(201, 146, 43, 0.15)',
          fill: true,
          tension: 0.35,
          pointRadius: 3,
          pointBackgroundColor: '#c9922b'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: false } },
        scales: {
          x: { grid: { display: false }, ticks: { color: '#6b6759', font: { size: 11 } } },
          y: { grid: { color: '#ece8dc' }, ticks: { color: '#6b6759', font: { size: 11 }, precision: 0 } }
        }
      }
    });
  }

  private formatarSemana(iso: string): string {
    const d = new Date(`${iso}T12:00:00`);
    return d.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit' });
  }

  maxComarcaTotal(): number {
    const totais = this.comarcas().map(c => c.total);
    return totais.length ? Math.max(...totais) : 1;
  }

  intensidade(total: number): number {
    const max = this.maxComarcaTotal();
    return max === 0 ? 0 : Math.round((total / max) * 100);
  }

  denunciasPorStatus(status: StatusDenuncia): DenunciaResponse[] {
    return this.denuncias().filter(d => d.status === status);
  }

  abrirDrillDown(denuncia: DenunciaResponse): void {
    this.focoAnterior = document.activeElement as HTMLElement;
    this.denunciaSelecionada.set(denuncia);
    this.recarregarDetalhe();
    setTimeout(() => document.querySelector<HTMLButtonElement>('.drilldown__close')?.focus());
  }

  recarregarDetalhe(): void {
    const denuncia = this.denunciaSelecionada();
    if (!denuncia) return;
    this.detalheRequest?.unsubscribe();
    this.carregandoDetalhe.set(true);
    this.erroDetalhe.set(null);
    this.sucessoEdicao.set(false);
    this.detalheRequest = this.denunciaService.buscar(denuncia.id).subscribe({
      next: d => {
        this.denunciaSelecionada.set(d);
        this.edicao.reset({ status: d.status, volume: d.volumeProcessualEstimado ?? null,
          comarcas: d.comarcasAtingidas ?? null, nota: d.notaTecnicaResumo ?? '' });
        this.carregandoDetalhe.set(false);
      },
      error: () => { this.carregandoDetalhe.set(false); this.erroDetalhe.set('Não foi possível carregar os detalhes do caso.'); }
    });
  }

  salvarEdicao(): void {
    const d = this.denunciaSelecionada();
    if (!d || this.salvando() || this.carregandoDetalhe() || this.edicao.invalid) {
      this.edicao.markAllAsTouched(); return;
    }
    const valor = this.edicao.getRawValue();
    this.salvando.set(true);
    this.erroDetalhe.set(null);
    this.sucessoEdicao.set(false);
    this.saveRequest = this.denunciaService.atualizarStatus(d.id, {
      versao: d.versao, status: valor.status!, volumeProcessualEstimado: valor.volume ?? undefined,
      comarcasAtingidas: valor.comarcas ?? undefined, notaTecnicaResumo: valor.nota ?? '',
      limparVolume: valor.volume == null, limparComarcas: valor.comarcas == null
    }).subscribe({
      next: atualizado => {
        this.denunciaSelecionada.set(atualizado);
        this.salvando.set(false); this.sucessoEdicao.set(true);
        this.carregar();
      },
      error: e => {
        this.salvando.set(false);
        this.erroDetalhe.set(e.status === 409 ? 'Outro analista alterou este caso. Recarregue os dados antes de editar novamente.'
          : e.error?.mensagem ?? 'Não foi possível salvar a análise.');
      }
    });
  }

  mudarPagina(delta: number): void {
    this.pagina += delta; this.carregar();
  }

  filtrarFila(): void { this.pagina = 0; this.carregar(); }

  tecladoModal(event: KeyboardEvent): void {
    if (event.key === 'Escape') { this.fecharDrillDown(); return; }
    if (event.key !== 'Tab') return;
    const modal = event.currentTarget as HTMLElement;
    const elementos = Array.from(modal.querySelectorAll<HTMLElement>('button:not(:disabled), input:not(:disabled), select:not(:disabled), textarea:not(:disabled), a[href]'));
    const primeiro = elementos[0], ultimo = elementos[elementos.length - 1];
    if (event.shiftKey && document.activeElement === primeiro) { event.preventDefault(); ultimo?.focus(); }
    else if (!event.shiftKey && document.activeElement === ultimo) { event.preventDefault(); primeiro?.focus(); }
  }

  fecharDrillDown(): void {
    if (this.salvando()) return;
    this.detalheRequest?.unsubscribe();
    this.denunciaSelecionada.set(null);
    this.focoAnterior?.focus();
  }

  formatarData(iso: string): string {
    return new Date(iso).toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric' });
  }
}
