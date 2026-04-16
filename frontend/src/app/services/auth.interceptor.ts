import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { UsuarioService } from './usuario.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(
    private usuarioService: UsuarioService,
    @Inject(PLATFORM_ID) private platformId: object
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const isAuthRequest =
      request.url.includes('/api/auth/login') ||
      request.url.includes('/api/auth/register');

    if (isAuthRequest) {
      return next.handle(request);
    }

    if (!isPlatformBrowser(this.platformId)) {
      return next.handle(request);
    }

    const token = this.usuarioService.getToken();

    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(request);
  }
}
