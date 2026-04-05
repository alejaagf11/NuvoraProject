import { Injectable } from '@angular/core';
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
  constructor(private usuarioService: UsuarioService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
  const isAuthRequest =
    request.url.includes('/api/auth/login') ||
    request.url.includes('/api/auth/register');

  console.log('Interceptando:', request.url);

  if (isAuthRequest) {
    console.log('Ruta publica, sin token');
    return next.handle(request);
  }

  const token = this.usuarioService.getToken();
  console.log('Token en interceptor:', token);

  if (token) {
    request = request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    console.log('Authorization agregado');
  }

  return next.handle(request);
}

}
