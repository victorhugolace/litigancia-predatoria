import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'lp-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
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
        <nav class="shell__nav">
          <a routerLink="/denunciar" routerLinkActive="is-active">Reportar Caso</a>
          <a routerLink="/painel" routerLinkActive="is-active">Painel de Controle</a>
        </nav>
      </header>
      <main class="shell__main">
        <router-outlet></router-outlet>
      </main>
    </div>
  `,
  styleUrl: './app.component.scss'
})
export class AppComponent {}

