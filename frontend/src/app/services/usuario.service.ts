import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario } from '../models/usuarios';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {


  private baseUrl = 'http://localhost:8080/api/usuario';

  constructor(private http: HttpClient) { }

  getAll(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.baseUrl}/list`);
  }

  getById(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.baseUrl}/list/${id}`);
  }

  update(id: number, usuario: Usuario): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.baseUrl}/update/${id}`, usuario);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${id}`);
  }

  create(usuario: Usuario) {
  return this.http.post<Usuario>(`${this.baseUrl}/register`, usuario);
}

login(usuario: { correoUsuario: string, contrasenaUsuario: string }): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.baseUrl}/login`, usuario);
  }

}