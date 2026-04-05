import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PresupuestoRequest, PresupuestoResponse } from '../models/presupuesto';

@Injectable({
  providedIn: 'root'
})
export class PresupuestoService {

  private baseUrl = 'http://localhost:8080/api/presupuesto';

  constructor(private http: HttpClient) { }

  generar(data: PresupuestoRequest): Observable<PresupuestoResponse> {
    return this.http.post<PresupuestoResponse>(`${this.baseUrl}/generate`, data);
  }
}
