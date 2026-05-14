import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MetasAhorro } from '../models/metas_ahorro';
import { API_BASE_URL } from '../config/api.config';

@Injectable({
  providedIn: 'root'
})
export class MetasAhorroService {

  private baseUrl = `${API_BASE_URL}/api/metasAhorro`;

  constructor(private http: HttpClient) { }

  getAll(): Observable<MetasAhorro[]> {
    return this.http.get<MetasAhorro[]>(`${this.baseUrl}/list`);
  }

  getById(id: number): Observable<MetasAhorro> {
    return this.http.get<MetasAhorro>(`${this.baseUrl}/list/${id}`);
  }

  create(meta: MetasAhorro): Observable<MetasAhorro> {
    return this.http.post<MetasAhorro>(`${this.baseUrl}/register`, meta);
  }

  update(id: number, meta: MetasAhorro): Observable<MetasAhorro> {
    return this.http.put<MetasAhorro>(`${this.baseUrl}/update/${id}`, meta);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${id}`);
  }

  abonar(id: number, monto: number): Observable<MetasAhorro> {
    return this.http.put<MetasAhorro>(`${this.baseUrl}/abonar/${id}?monto=${monto}`, {});
  }
}

