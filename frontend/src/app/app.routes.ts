import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'denunciar', pathMatch: 'full' },
  {
    path: 'denunciar',
    loadComponent: () =>
      import('./features/denuncia-form/denuncia-form.component').then(m => m.DenunciaFormComponent)
  },
  {
    path: 'painel',
    loadComponent: () =>
      import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  { path: '**', redirectTo: 'denunciar' }
];
