import { APP_INITIALIZER, ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { AuthService } from './core/services/auth.service';
import { sessaoInterceptor } from './core/services/session.interceptor';
import { provideAnimations } from '@angular/platform-browser/animations';
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([sessaoInterceptor])),
    { provide: APP_INITIALIZER, multi: true, deps: [AuthService], useFactory: (auth: AuthService) => () => firstValueFrom(auth.restaurar()) },
    provideAnimations()
  ]
};
