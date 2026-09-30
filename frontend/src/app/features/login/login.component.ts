import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'lp-login', standalone: true, imports: [CommonModule, ReactiveFormsModule],
  template: `
    <section class="lp-card login">
      <h1>Entrar na aplicação</h1>
      <p>Use sua conta de comunicante ou analista para acessar os casos permitidos ao seu perfil.</p>
      <p *ngIf="auth.sessao().demo">Ambiente de demonstração com dados fictícios.</p>
      <form [formGroup]="form" (ngSubmit)="entrar()">
        <div class="field"><label for="usuario">Usuário</label><input id="usuario" autocomplete="username" formControlName="usuario" maxlength="120"></div>
        <div class="field"><label for="senha">Senha</label><input id="senha" type="password" autocomplete="current-password" formControlName="senha"></div>
        <p *ngIf="erro()" role="alert" class="field__error">{{ erro() }}</p>
        <p *ngIf="form.invalid && form.touched" class="field__error">Informe usuário e senha.</p>
        <button type="submit" class="lp-btn lp-btn--primary" [disabled]="enviando()">{{ enviando() ? 'Entrando…' : 'Entrar' }}</button>
      </form>
    </section>`,
  styles: [`.login { max-width: 480px; margin: 40px auto; padding: 32px; } .login h1 { margin-bottom: 12px; } .login p { margin-bottom: 16px; } .field { display: flex; flex-direction: column; gap: 6px; margin-bottom: 16px; } input { padding: 10px; border: 1px solid var(--lp-line); border-radius: 4px; font: inherit; } .field__error { color: var(--lp-alert-700); }`]
})
export class LoginComponent {
  readonly auth = inject(AuthService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  readonly form = inject(FormBuilder).group({ usuario: ['', Validators.required], senha: ['', Validators.required] });
  readonly enviando = signal(false);
  readonly erro = signal<string | null>(null);
  entrar(): void {
    if (this.form.invalid || this.enviando()) { this.form.markAllAsTouched(); return; }
    this.enviando.set(true); this.erro.set(null);
    const { usuario, senha } = this.form.getRawValue();
    this.auth.entrar(usuario!, senha!).subscribe({
      next: sessao => {
        this.enviando.set(false); this.form.get('senha')!.reset();
        if (!sessao.autenticado) { this.erro.set('Não foi possível confirmar a sessão. Tente novamente.'); return; }
        const retorno = this.route.snapshot.queryParamMap.get('retorno');
        this.router.navigateByUrl(retorno?.startsWith('/') && !retorno.startsWith('//') ? retorno : this.auth.analista ? '/painel' : '/denunciar');
      },
      error: e => { this.enviando.set(false); this.erro.set(e.error?.mensagem ?? e.message ?? 'Não foi possível entrar.'); }
    });
  }
}
