import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
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
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('tipologiaCanvas') tipologiaCanvas?: ElementRef<HTMLCanvasElement>;
  @ViewChild('tendenciaCanvas') tendenciaCanvas?: ElementRef<HTMLCanvasElement>;

  readonly statusOrder = STATUS_ORDER;
  readonly statusLabels = STATUS_LABELS;

  carregando = signal(true);
  kpis = signal<KpisResponse | null>(null);
  ranking = signal<RankingItem[]>([]);
  tipologias = signal<TipologiaItem[]>([]);
  comarcas = signal<ComarcaItem[]>([]);
  tendencia = signal<TendenciaItem[]>([]);
  denuncias = signal<DenunciaResponse[]>([]);
  denunciaSelecionada = signal<DenunciaResponse | null>(null);

  private tipologiaChart?: Chart;
  private tendenciaChart?: Chart;

  constructor(private dashboardService: DashboardService, private denunciaService: DenunciaService) {}

  ngOnInit(): void {
    forkJoin({
      kpis: this.dashboardService.kpis(),
      ranking: this.dashboardService.ranking(),
      tipologia: this.dashboardService.tipologia(),
      comarcas: this.dashboardService.comarcas(),
      tendencia: this.dashboardService.tendencia(),
      denuncias: this.denunciaService.listar()
    }).subscribe({
      next: dados => {
        this.kpis.set(dados.kpis);
        this.ranking.set(dados.ranking);
        this.tipologias.set(dados.tipologia);
        this.comarcas.set(dados.comarcas);
        this.tendencia.set(dados.tendencia);
        this.denuncias.set(dados.denuncias);
        this.carregando.set(false);
        setTimeout(() => this.renderCharts(), 0);
      },
      error: () => this.carregando.set(false)
    });
  }

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {
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
    const d = new Date(iso);
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
    this.denunciaSelecionada.set(denuncia);
  }

  fecharDrillDown(): void {
    this.denunciaSelecionada.set(null);
  }

  formatarData(iso: string): string {
    return new Date(iso).toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric' });
  }
}
