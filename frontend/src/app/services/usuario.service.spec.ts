import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Usuario } from '../models/usuarios';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private baseUrl = 'http://localhost:8080/api/usuario';
  private authUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

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
    return this.http.post<any>(`${this.authUrl}/login`, usuario).pipe(
      tap((response: any) => {
        if (
          response.token &&
          typeof window !== 'undefined' &&
          typeof localStorage !== 'undefined'
        ) {
          localStorage.setItem('authToken', response.token);
        }
      })
    );
  }

  getToken(): string | null {
    if (typeof window === 'undefined' || typeof localStorage === 'undefined') {
      return null;
    }
    return localStorage.getItem('authToken');
  }

  logout(): void {
    if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
      localStorage.removeItem('authToken');
    }
  }
}
