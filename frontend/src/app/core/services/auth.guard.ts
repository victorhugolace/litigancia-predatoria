import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const autenticado: CanActivateFn = (_, state) => inject(AuthService).sessao().autenticado
  || inject(Router).createUrlTree(['/entrar'], { queryParams: { retorno: state.url } });
export const somenteAnalista: CanActivateFn = () => inject(AuthService).analista
  || inject(Router).createUrlTree(['/acompanhar']);
