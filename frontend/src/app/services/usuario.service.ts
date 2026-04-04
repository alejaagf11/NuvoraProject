import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Usuario } from '../models/usuarios';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private baseUrl = 'http://localhost:8080/api/usuario';
  private authUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  // 🔐 Método para headers con token
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('authToken');
    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }

  // 📄 LISTAR USUARIOS
  getAll(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.baseUrl}`, {
      headers: this.getAuthHeaders()
    });
  }

  // 🔍 OBTENER POR ID
  getById(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.baseUrl}/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  // ✏️ ACTUALIZAR
  update(id: number, usuario: Usuario): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.baseUrl}/update/${id}`, usuario, {
      headers: this.getAuthHeaders()
    });
  }

  // ❌ ELIMINAR
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  // 🆕 REGISTRO (NO lleva token)
  create(usuario: Usuario) {
    return this.http.post<Usuario>(`${this.authUrl}/register`, usuario);
  }

  // 🔑 LOGIN
  login(usuario: { correoUsuario: string, contrasenaUsuario: string }): Observable<any> {
    return this.http.post<any>(`${this.authUrl}/login`, usuario).pipe(
      tap((response: any) => {
        console.log('Respuesta login:', response);

        if (response.token) {
          localStorage.setItem('authToken', response.token);
          console.log('Token guardado correctamente');
        } else {
          console.warn('No llegó token en la respuesta');
        }
      })
    );
  }

  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  logout(): void {
    localStorage.removeItem('authToken');
  }
}