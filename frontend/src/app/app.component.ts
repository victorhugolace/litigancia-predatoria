import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from './core/services/auth.service';
import { Router } from '@angular/router';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'lp-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="shell">
      <header class="shell__topbar">
        <div class="shell__brand">
          <img src="assets/brasao-tjpb.png" alt="Brasão TJPB" class="shell__coat-of-arms">
          <div class="shell__brand-text">
            <strong>TRIBUNAL DE JUSTIÇA DO ESTADO DA PARAÍBA</strong>
            <span>Centro de Inteligência &middot; Litigância Predatória (LYNO/TJPB)</span>
          </div>
        </div>
        <nav class="shell__nav" *ngIf="auth.sessao().autenticado">
          <a routerLink="/denunciar" routerLinkActive="is-active">Reportar Caso</a>
          <a routerLink="/painel" routerLinkActive="is-active" *ngIf="auth.analista">Painel de Controle</a>
          <a routerLink="/acompanhar" routerLinkActive="is-active">Acompanhar caso</a>
          <button class="lp-btn lp-btn--ghost" (click)="sair()" [disabled]="saindo()">Sair ({{ auth.sessao().usuario }})</button>
        </nav>
      </header>
      <p class="demo-banner" *ngIf="auth.sessao().demo">Demonstração da oficina · dados fictícios</p>
      <p class="demo-banner" role="alert" *ngIf="erroSaida()">{{ erroSaida() }}</p>
      <main class="shell__main">
        <router-outlet></router-outlet>
      </main>
    </div>
  `,
  styleUrl: './app.component.scss'
})
export class AppComponent {
  readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  readonly saindo = signal(false);
  readonly erroSaida = signal<string | null>(null);
  sair(): void {
    if (this.saindo()) return;
    this.saindo.set(true); this.erroSaida.set(null);
    this.auth.sair().subscribe({
      next: () => { this.saindo.set(false); this.router.navigate(['/entrar']); },
      error: () => { this.saindo.set(false); this.erroSaida.set('Não foi possível encerrar a sessão. Tente novamente.'); }
    });
  }
}

