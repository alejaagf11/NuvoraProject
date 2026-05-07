import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ModuloAprendizaje } from '../models/aprendizaje';
import { Leccion } from '../models/leccion';
import { LeccionService } from '../services/leccion.service';
import { ModuloAprendizajeService } from '../services/modulo-aprendizaje.service';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-admin-lecciones',
  standalone: false,
  templateUrl: './admin-lecciones.component.html',
  styleUrls: ['./admin-lecciones.component.css']
})
export class AdminLeccionesComponent implements OnInit {
  modulos: ModuloAprendizaje[] = [];
  lecciones: Leccion[] = [];
  cargando = true;
  errorMensaje: string | null = null;
  mensajeExito: string | null = null;

  moduloSeleccionadoId: number | null = null;
  editandoId: number | null = null;

  formulario: Leccion = {
    tituloLeccion: '',
    contenidoLeccion: '',
    ordenLeccion: 1,
    activo: true,
    moduloId: 0
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private moduloService: ModuloAprendizajeService,
    private leccionService: LeccionService,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const moduloId = Number(params['moduloId']);
      if (moduloId) {
        this.moduloSeleccionadoId = moduloId;
        this.formulario.moduloId = moduloId;
      }

      this.cargarModulos();
    });
  }

  cargarModulos() {
    this.moduloService.getModulos().subscribe({
      next: (data) => {
        this.modulos = data;

        if (!this.moduloSeleccionadoId && data.length > 0) {
          this.moduloSeleccionadoId = data[0].moduloId || null;
        }

        this.formulario.moduloId = this.moduloSeleccionadoId || 0;

        if (this.moduloSeleccionadoId) {
          this.cargarLecciones(this.moduloSeleccionadoId);
        } else {
          this.lecciones = [];
          this.cargando = false;
        }
      },
      error: (err) => {
        console.error('Error al cargar modulos', err);
        this.errorMensaje = 'No se pudieron cargar los modulos';
        this.cargando = false;
      }
    });
  }

  cargarLecciones(moduloId: number) {
    this.cargando = true;
    this.leccionService.getLeccionesByModulo(moduloId).subscribe({
      next: (data) => {
        this.lecciones = data;
        this.cargando = false;
        this.errorMensaje = null;
      },
      error: (err) => {
        console.error('Error al cargar lecciones', err);
        this.errorMensaje = 'No se pudieron cargar las lecciones';
        this.cargando = false;
      }
    });
  }

  onModuloChange() {
    if (!this.moduloSeleccionadoId) {
      this.lecciones = [];
      this.formulario.moduloId = 0;
      this.resetFormulario(false);
      return;
    }

    this.formulario.moduloId = this.moduloSeleccionadoId;
    this.resetFormulario(false);
    this.cargarLecciones(this.moduloSeleccionadoId);
  }

  guardarLeccion() {
    if (
      !this.formulario.tituloLeccion.trim() ||
      !this.formulario.contenidoLeccion.trim() ||
      !this.formulario.moduloId
    ) {
      this.errorMensaje = 'Completa todos los campos obligatorios';
      return;
    }

    if (this.editandoId) {
      this.leccionService.updateLeccion(this.editandoId, this.formulario).subscribe({
        next: () => {
          this.mensajeExito = 'Leccion actualizada correctamente';
          this.resetFormulario(false);
          if (this.moduloSeleccionadoId) {
            this.cargarLecciones(this.moduloSeleccionadoId);
          }
        },
        error: (err) => {
          console.error('Error al actualizar leccion', err);
          this.errorMensaje = 'No se pudo actualizar la leccion';
        }
      });
      return;
    }

    this.leccionService.createLeccion(this.formulario).subscribe({
      next: () => {
        this.mensajeExito = 'Leccion creada correctamente';
        this.resetFormulario(false);
        if (this.moduloSeleccionadoId) {
          this.cargarLecciones(this.moduloSeleccionadoId);
        }
      },
      error: (err) => {
        console.error('Error al crear leccion', err);
        this.errorMensaje = 'No se pudo crear la leccion';
      }
    });
  }

  editarLeccion(leccion: Leccion) {
    this.editandoId = leccion.leccionId || null;
    this.formulario = { ...leccion };
    this.moduloSeleccionadoId = leccion.moduloId;
    this.errorMensaje = null;
    this.mensajeExito = null;
  }

  eliminarLeccion(leccionId?: number) {
    if (!leccionId) return;

    if (!confirm('Seguro que quieres eliminar esta leccion?')) {
      return;
    }

    this.leccionService.deleteLeccion(leccionId).subscribe({
      next: () => {
        this.mensajeExito = 'Leccion eliminada correctamente';
        this.lecciones = this.lecciones.filter(leccion => leccion.leccionId !== leccionId);
        if (this.editandoId === leccionId) {
          this.resetFormulario(false);
        }
      },
      error: (err) => {
        console.error('Error al eliminar leccion', err);
        this.errorMensaje = 'No se pudo eliminar la leccion';
      }
    });
  }

  resetFormulario(resetModulo: boolean = true) {
    this.editandoId = null;
    this.formulario = {
      tituloLeccion: '',
      contenidoLeccion: '',
      ordenLeccion: 1,
      activo: true,
      moduloId: resetModulo ? 0 : (this.moduloSeleccionadoId || 0)
    };
  }

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/usuario-login']);
  }
}
