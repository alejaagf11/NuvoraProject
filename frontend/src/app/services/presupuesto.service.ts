import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { PresupuestoRequest, PresupuestoResponse } from '../models/presupuesto';

@Injectable({
  providedIn: 'root'
})
export class PresupuestoService {

  private baseUrl = `${API_BASE_URL}/api/presupuesto`;

  constructor(private http: HttpClient) { }

  generar(data: PresupuestoRequest): Observable<PresupuestoResponse> {
    return this.http.post<PresupuestoResponse>(`${this.baseUrl}/generate`, data);
  }

  chat(mensaje:string): Observable<string> {
    return this.http.post(`${this.baseUrl}/chat`, mensaje,{
      responseType: 'text'
    } );
  }

  
}
