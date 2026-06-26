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
  fotoPerfil: string | null = null;
inicial: string = 'U';
  modulos: ModuloProgresoResumen[] = [];
  cargando = true;
  errorMensaje: string | null = null;

  constructor(
    private moduloAprendizajeService: ModuloAprendizajeService,
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarUsuario();
    this.cargarModulos();
  }

  private cargarUsuario(): void {
  this.usuarioService.getMiUsuario().subscribe({
    next: (usuario) => {
      this.fotoPerfil = usuario.fotoPerfil || null;
      this.inicial = usuario.nombreUsuario?.charAt(0).toUpperCase() || 'U';
    },
    error: (err) => {
      console.error('Error al cargar usuario móvil', err);
      this.fotoPerfil = null;
      this.inicial = 'U';
    }
  });

  window.addEventListener('storage', () => this.cargarUsuario());
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
