import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from './auth.service';

export const sessaoInterceptor: HttpInterceptorFn = (request, next) => {
  if (!request.url.startsWith('/api/')) return next(request);
  const auth = inject(AuthService), router = inject(Router);
  const token = auth.sessao().csrfToken;
  let headers = request.headers;
  if (token && !['GET', 'HEAD', 'OPTIONS'].includes(request.method)) headers = headers.set('X-CSRF-TOKEN', token);
  return next(request.clone({ headers, withCredentials: true })).pipe(catchError(error => {
    if (error.status === 401 && request.url !== '/api/login') {
      auth.limpar();
      if (router.url.startsWith('/painel')) router.navigate(['/entrar'], { queryParams: { retorno: router.url } });
    }
    return throwError(() => error);
  }));
};
