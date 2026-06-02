import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Usuario } from '../models/usuarios';
import { API_BASE_URL } from '../config/api.config';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private baseUrl = `${API_BASE_URL}/api/usuario`;
  private authUrl = `${API_BASE_URL}/api/auth`;

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: object
  ) {}

  private isBrowser(): boolean {
    return isPlatformBrowser(this.platformId);
  }

  getAll(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.baseUrl}/list`);
  }

  getById(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.baseUrl}/${id}`);
  }

  update(id: number, usuario: Usuario): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.baseUrl}/update/${id}`, usuario);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${id}`);
  }

  getMiUsuario(): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.baseUrl}/me`);
  }

  getAdminUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.baseUrl}/admin/list`);
  }

  getUsuarioAdminById(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.baseUrl}/admin/${id}`);
  }

  updateUsuarioAdmin(id: number, usuario: Usuario): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.baseUrl}/admin/update/${id}`, usuario);
  }

  deleteUsuarioAdmin(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/admin/delete/${id}`);
  }

  updateMiUsuario(usuario: Usuario): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.baseUrl}/update`, usuario);
  }

  deleteMiUsuario(): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete`);
  }

  create(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.authUrl}/register`, usuario);
  }

  login(usuario: { correoUsuario: string; contrasenaUsuario: string }): Observable<any> {
    const tokenActual = this.getToken();
    const options = tokenActual
      ? {
          headers: new HttpHeaders({
            Authorization: `Bearer ${tokenActual}`
          })
        }
      : {};

    return this.http.post<any>(`${this.authUrl}/login`, usuario, options).pipe(
      tap((response: any) => {
        if (response.token && this.isBrowser()) {
          localStorage.setItem('authToken', response.token);
        }
      })
    );
  }

  getToken(): string | null {
    if (!this.isBrowser()) {
      return null;
    }
    return localStorage.getItem('authToken');
  }

  logout(): void {
    if (this.isBrowser()) {
      localStorage.removeItem('authToken');
    }
  }
}
