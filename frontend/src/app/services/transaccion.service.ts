import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transaccion } from '../models/transaccion';
import { API_BASE_URL } from '../config/api.config';

@Injectable({
  providedIn: 'root'
})
export class TransaccionService {

  private baseUrl = `${API_BASE_URL}/api/transacciones`;

  constructor(private http: HttpClient) { }

  getAll(): Observable<Transaccion[]> {
    return this.http.get<Transaccion[]>(`${this.baseUrl}/list`);
  }

  create(transaccion: Transaccion): Observable<Transaccion> {
    return this.http.post<Transaccion>(`${this.baseUrl}/register`, transaccion);
  }

  update(id: number, transaccion: Transaccion): Observable<Transaccion> {
    return this.http.put<Transaccion>(`${this.baseUrl}/update/${id}`, transaccion);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${id}`);
  }

  getByTipo(tipo: 'INGRESO' | 'GASTO'): Observable<Transaccion[]> {
    return this.http.get<Transaccion[]>(`${this.baseUrl}/tipo/${tipo}`);
  }

  getByCategoria(categoriaId: number): Observable<Transaccion[]> {
    return this.http.get<Transaccion[]>(`${this.baseUrl}/categoria/${categoriaId}`);
  }

  getSaldo(): Observable<number> {
    return this.http.put<number>(`${this.baseUrl}/saldo`, {});
  }
}
