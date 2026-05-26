import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ModuloProgresoResumen } from '../models/progreso-resumen';
import { ModuloAprendizajeService } from '../services/modulo-aprendizaje.service';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-aprendizaje-mobile',
  standalone: false,
  templateUrl: './aprendizaje-mobile.component.html',
  styleUrls: ['./aprendizaje-mobile.component.css'],
})
export class AprendizajeMobileComponent implements OnInit {
  modulos: ModuloProgresoResumen[] = [];
  cargando = true;
  errorMensaje: string | null = null;

  constructor(
    private moduloAprendizajeService: ModuloAprendizajeService,
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarModulos();
  }

  cargarModulos() {
    this.cargando = true;
    this.moduloAprendizajeService.getProgresoModulos().subscribe({
      next: (data) => {
        this.modulos = data;
        this.cargando = false;
        this.errorMensaje = null;
      },
      error: (err) => {
        console.error('Error al cargar modulos', err);
        this.errorMensaje = 'No se pudieron cargar los modulos de aprendizaje';
        this.cargando = false;
      }
    });
  }

  verModulo(moduloId: number, desbloqueado: boolean) {
    if (!desbloqueado) return;
    this.router.navigate(['/mobile/aprendizaje/modulo', moduloId]);
  }

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/mobile/usuario-login']);
  }

}
