import { Injectable, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { catchError, of, switchMap, tap, throwError } from 'rxjs';

export interface Sessao { autenticado: boolean; usuario: string | null; perfis: string[]; csrfToken: string; demo: boolean; }
const vazia: Sessao = { autenticado: false, usuario: null, perfis: [], csrfToken: '', demo: false };

@Injectable({ providedIn: 'root' })
export class AuthService {
  readonly sessao = signal<Sessao>(vazia);
  readonly falhaConexao = signal(false);
  constructor(private http: HttpClient) {}

  get analista(): boolean { return this.sessao().perfis.includes('ANALISTA'); }
  limpar(): void { this.sessao.set({ ...vazia, demo: this.sessao().demo }); }

  restaurar() {
    return this.http.get<Sessao>('/api/session').pipe(tap(sessao => {
      this.sessao.set(sessao); this.falhaConexao.set(false);
    }), catchError(() => { this.limpar(); this.falhaConexao.set(true); return of(this.sessao()); }));
  }

  entrar(usuario: string, senha: string) {
    return this.restaurar().pipe(switchMap(sessao => {
      if (!sessao.csrfToken) return throwError(() => new Error('Não foi possível conectar à aplicação. Tente novamente.'));
      const body = new HttpParams().set('username', usuario.trim()).set('password', senha);
      return this.http.post('/api/login', body);
    }), switchMap(() => this.restaurar()));
  }

  sair() {
    return this.http.post('/api/logout', {}).pipe(tap(() => this.limpar()), switchMap(() => this.restaurar()));
  }
}
