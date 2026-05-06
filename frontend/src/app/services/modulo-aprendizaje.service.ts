import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ModuloAprendizaje } from '../models/aprendizaje';
import { ModuloProgresoResumen } from '../models/progreso-resumen';

@Injectable({
  providedIn: 'root'
})
export class ModuloAprendizajeService {
  private baseUrl = 'http://localhost:8080/api/modulos';

  constructor(private http: HttpClient) {}

  getModulos(): Observable<ModuloAprendizaje[]> {
    return this.http.get<ModuloAprendizaje[]>(`${this.baseUrl}/list`);
  }

  getModuloById(moduloId: number): Observable<ModuloAprendizaje> {
    return this.http.get<ModuloAprendizaje>(`${this.baseUrl}/${moduloId}`);
  }

  getProgresoModulos(): Observable<ModuloProgresoResumen[]> {
    return this.http.get<ModuloProgresoResumen[]>(`${this.baseUrl}/progreso`);
  }
}
