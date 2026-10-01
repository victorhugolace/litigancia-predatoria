import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { DenunciaService } from '../../core/services/denuncia.service';
import { AcompanhamentoResponse } from '../../core/models/denuncia.model';

@Component({
  selector: 'lp-acompanhamento', standalone: true, imports: [CommonModule, ReactiveFormsModule],
  template: `
    <section class="lp-card acompanhamento">
      <h1>Acompanhar caso</h1>
      <p>Consulte o andamento pelo protocolo, sem login. Dados pessoais, documentos e notas da análise ficam restritos à equipe responsável.</p>
      <form [formGroup]="form" (ngSubmit)="consultar()" class="busca">
        <label for="protocolo">Número de protocolo</label>
        <input id="protocolo" formControlName="protocolo" maxlength="30" placeholder="LP-2026-000001">
        <button class="lp-btn lp-btn--primary" type="submit" [disabled]="carregando()">{{ carregando() ? 'Consultando…' : 'Consultar' }}</button>
      </form>
      <p *ngIf="form.invalid && form.touched" class="erro">Confira o formato do protocolo.</p>
      <p *ngIf="erro()" role="alert" class="erro">{{ erro() }}</p>
      <article *ngIf="denuncia() as d">
        <h2>{{ d.numeroProtocolo }}</h2><p><strong>Etapa atual:</strong> {{ d.statusDescricao }}</p>
        <p><strong>Registrado em:</strong> {{ d.dataCriacao | date:'dd/MM/yyyy HH:mm' }}</p>
        <p><strong>Última atualização:</strong> {{ d.dataAtualizacao | date:'dd/MM/yyyy HH:mm' }}</p>
      </article>
    </section>`,
  styles: [`.acompanhamento { max-width: 760px; margin: 24px auto; padding: 28px; } h1, h2, h3 { margin-bottom: 12px; } p { margin-bottom: 16px; line-height: 1.6; white-space: pre-wrap; overflow-wrap: anywhere; } .busca { display: flex; flex-wrap: wrap; gap: 12px; align-items: center; margin: 24px 0; } input { padding: 10px; border: 1px solid var(--lp-line); border-radius: 4px; font: inherit; } .erro { color: var(--lp-alert-700); } article { border-top: 1px solid var(--lp-line); padding-top: 24px; } li { margin-bottom: 8px; overflow-wrap: anywhere; } a { color: var(--lp-pje-blue-dark); text-decoration: underline; }`]
})
export class AcompanhamentoComponent implements OnInit, OnDestroy {
  readonly service = inject(DenunciaService);
  private route = inject(ActivatedRoute);
  readonly form = inject(FormBuilder).group({ protocolo: ['', [Validators.required, Validators.pattern(/^LP-(?:\d{4}|SEED)-\d{6,}$/)]] });
  readonly carregando = signal(false);
  readonly erro = signal<string | null>(null);
  readonly denuncia = signal<AcompanhamentoResponse | null>(null);
  private request?: Subscription;
  ngOnInit(): void {
    const protocolo = this.route.snapshot.queryParamMap.get('protocolo');
    if (protocolo) { this.form.setValue({ protocolo }); this.consultar(); }
  }
  ngOnDestroy(): void { this.request?.unsubscribe(); }
  consultar(): void {
    this.form.get('protocolo')!.setValue((this.form.value.protocolo ?? '').trim().toUpperCase());
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.request?.unsubscribe(); this.carregando.set(true); this.erro.set(null); this.denuncia.set(null);
    this.request = this.service.acompanhar(this.form.value.protocolo!).subscribe({
      next: d => { this.denuncia.set(d); this.carregando.set(false); },
      error: e => { this.carregando.set(false); this.erro.set(e.status === 404 ? 'Protocolo não encontrado. Confira o número informado.' : 'Não foi possível consultar o caso. Tente novamente.'); }
    });
  }
}
