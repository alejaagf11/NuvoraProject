import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Leccion } from '../models/leccion';

@Injectable({
  providedIn: 'root'
})
export class LeccionService {
  private baseUrl = 'http://localhost:8080/api/lecciones';

  constructor(private http: HttpClient) {}

  getLeccionesByModulo(moduloId: number): Observable<Leccion[]> {
    return this.http.get<Leccion[]>(`${this.baseUrl}/modulo/${moduloId}`);
  }

  getLeccionById(leccionId: number): Observable<Leccion> {
    return this.http.get<Leccion>(`${this.baseUrl}/${leccionId}`);
  }

  createLeccion(leccion: Leccion): Observable<Leccion> {
    return this.http.post<Leccion>(`${this.baseUrl}/register`, leccion);
  }

  updateLeccion(leccionId: number, leccion: Leccion): Observable<Leccion> {
    return this.http.put<Leccion>(`${this.baseUrl}/update/${leccionId}`, leccion);
  }

  deleteLeccion(leccionId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${leccionId}`);
  }
}
