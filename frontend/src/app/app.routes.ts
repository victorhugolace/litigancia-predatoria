import { Routes } from '@angular/router';
import { autenticado, somenteAnalista } from './core/services/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'denunciar', pathMatch: 'full' },
  { path: 'entrar', loadComponent: () => import('./features/login/login.component').then(m => m.LoginComponent) },
  { path: 'acompanhar', loadComponent: () => import('./features/acompanhamento/acompanhamento.component').then(m => m.AcompanhamentoComponent) },
  {
    path: 'denunciar',
    loadComponent: () =>
      import('./features/denuncia-form/denuncia-form.component').then(m => m.DenunciaFormComponent)
  },
  {
    path: 'painel',
    canActivate: [autenticado, somenteAnalista],
    loadComponent: () =>
      import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  { path: '**', redirectTo: 'denunciar' }
];
