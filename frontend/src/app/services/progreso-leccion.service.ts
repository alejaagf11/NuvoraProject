import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProgresoLeccionUsuario } from '../models/progreso-leccion';
import { ModuloProgresoResumen } from '../models/progreso-resumen';

@Injectable({
  providedIn: 'root'
})
export class ProgresoLeccionService {
  private baseUrl = 'http://localhost:8080/api/progreso';

  constructor(private http: HttpClient) {}

  completarLeccion(leccionId: number): Observable<ProgresoLeccionUsuario> {
    return this.http.post<ProgresoLeccionUsuario>(`${this.baseUrl}/completar/${leccionId}`, {});
  }

  getMisLecciones(): Observable<ProgresoLeccionUsuario[]> {
    return this.http.get<ProgresoLeccionUsuario[]>(`${this.baseUrl}/mis-lecciones`);
  }

  getProgresoModulos(): Observable<ModuloProgresoResumen[]> {
    return this.http.get<ModuloProgresoResumen[]>(`${this.baseUrl}/modulos`);
  }
}
