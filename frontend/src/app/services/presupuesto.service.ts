import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { API_BASE_URL } from '../config/api.config';
import { PresupuestoAiResponse, PresupuestoRequest, PresupuestoResponse } from '../models/presupuesto';

interface PresupuestoChatResponse {
  message: string;
  source: string;
}

@Injectable({
  providedIn: 'root'
})
export class PresupuestoService {

  private baseUrl = `${API_BASE_URL}/api/presupuesto`;

  constructor(private http: HttpClient) { }

  generar(data: PresupuestoRequest): Observable<PresupuestoResponse> {
    return this.http.post<PresupuestoResponse>(`${this.baseUrl}/generate`, data);
  }

  generarConIA(data: PresupuestoRequest): Observable<PresupuestoAiResponse> {
    return this.http.post<PresupuestoAiResponse>(`${this.baseUrl}/generate-ai`, data);
  }

  chat(mensaje: string): Observable<string> {
    return this.http
      .post<PresupuestoChatResponse | string>(`${this.baseUrl}/chat`, { message: mensaje })
      .pipe(
        map((respuesta) => this.obtenerTextoChat(respuesta)),
        catchError(() =>
          this.http.post(`${this.baseUrl}/chat`, mensaje, { responseType: 'text' })
        )
      );
  }

  private obtenerTextoChat(respuesta: PresupuestoChatResponse | string): string {
    if (typeof respuesta === 'string') {
      return respuesta;
    }

    return respuesta.message;
  }

  
}
