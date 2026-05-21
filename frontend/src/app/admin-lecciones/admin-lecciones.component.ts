import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin, Observable, of } from 'rxjs';
import { switchMap } from 'rxjs/operators';
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
  videoUrl = '';

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
  ) { }

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

        this.lecciones = data.sort(
          (a, b) => a.ordenLeccion - b.ordenLeccion
        );

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

    this.formulario.ordenLeccion = this.normalizarOrden(
      this.formulario.ordenLeccion
    );

    if (
      !this.formulario.tituloLeccion.trim() ||
      !this.formulario.contenidoLeccion.trim() ||
      !this.formulario.moduloId
    ) {
      this.errorMensaje = 'Completa todos los campos obligatorios';
      return;
    }

    const leccionParaGuardar: Leccion = {

      ...this.formulario,

      contenidoLeccion: this.videoUrl.trim()
        ? `${this.formulario.contenidoLeccion.trim()}\nVIDEO:${this.videoUrl.trim()}`
        : this.formulario.contenidoLeccion.trim()
    };

    if (this.editandoId) {

      const leccionesAMover = this.obtenerLeccionesParaEditar(
        this.editandoId,
        leccionParaGuardar.ordenLeccion
      );

      this.actualizarOrdenes(leccionesAMover).pipe(

        switchMap(() => {

          leccionParaGuardar.ordenLeccion =
            this.formulario.ordenLeccion;

          return this.leccionService.updateLeccion(
            this.editandoId!,
            leccionParaGuardar
          );
        })

      ).subscribe({

        next: () => {

          this.mensajeExito =
            'Leccion actualizada correctamente';

          this.resetFormulario(false);

          if (this.moduloSeleccionadoId) {
            this.cargarLecciones(this.moduloSeleccionadoId);
          }
        },

        error: (err) => {
          console.error('Error al actualizar leccion', err);
          this.errorMensaje =
            'No se pudo actualizar la leccion';
        }

      });

      return;
    }

    // =========================
    // CREAR LECCION
    // =========================

    const leccionesAMover = this.obtenerLeccionesParaCrear(
      leccionParaGuardar.ordenLeccion
    );

    this.actualizarOrdenes(leccionesAMover).pipe(

      switchMap(() =>
        this.leccionService.createLeccion(leccionParaGuardar)
      )

    ).subscribe({

      next: () => {

        this.mensajeExito =
          'Leccion creada correctamente';

        this.resetFormulario(false);

        if (this.moduloSeleccionadoId) {
          this.cargarLecciones(this.moduloSeleccionadoId);
        }
      },

      error: (err) => {
        console.error('Error al crear leccion', err);
        this.errorMensaje =
          'No se pudo crear la leccion';
      }

    });
  }

  editarLeccion(leccion: Leccion) {

    this.editandoId = leccion.leccionId || null;

    const partes = leccion.contenidoLeccion.split('\nVIDEO:');

    this.formulario = {
      ...leccion,
      contenidoLeccion: partes[0].trim()
    };

    this.videoUrl = partes[1]?.trim() || '';

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

        this.mensajeExito =
          'Leccion eliminada correctamente';

        this.lecciones = this.lecciones.filter(
          leccion => leccion.leccionId !== leccionId
        );

        if (this.editandoId === leccionId) {
          this.resetFormulario(false);
        }
      },

      error: (err) => {
        console.error('Error al eliminar leccion', err);
        this.errorMensaje =
          'No se pudo eliminar la leccion';
      }

    });
  }

  resetFormulario(resetModulo: boolean = true) {

    this.editandoId = null;

    this.videoUrl = '';

    this.formulario = {
      tituloLeccion: '',
      contenidoLeccion: '',
      ordenLeccion: 1,
      activo: true,
      moduloId: resetModulo
        ? 0
        : (this.moduloSeleccionadoId || 0)
    };
  }

  private normalizarOrden(orden: number): number {

    const ordenNumerico = Number(orden);

    return Number.isFinite(ordenNumerico)
      && ordenNumerico > 0
      ? Math.floor(ordenNumerico)
      : 1;
  }
  obtenerVideoUrl(contenido: string): string {

  const partes = contenido.split('\nVIDEO:');

  return partes[1]?.trim() || '';
}

obtenerThumbnailYoutube(url: string): string {

  const match = url.match(
    /(?:youtube\.com\/watch\?v=|youtu\.be\/)([^&]+)/ 
  );

  const videoId = match?.[1];

  return videoId
    ? `https://img.youtube.com/vi/${videoId}/hqdefault.jpg`
    : '';
}

  // =========================
  // CREAR
  // =========================

  private obtenerLeccionesParaCrear(
    nuevoOrden: number
  ): Leccion[] {

    return this.lecciones

      .filter(leccion =>
        !!leccion.leccionId &&
        leccion.moduloId === this.formulario.moduloId &&
        leccion.ordenLeccion >= nuevoOrden
      )

      .map(leccion => ({
        ...leccion,
        ordenLeccion: leccion.ordenLeccion + 1
      }));
  }

  // =========================
  // EDITAR
  // =========================

  private obtenerLeccionesParaEditar(
    leccionId: number,
    nuevoOrden: number
  ): Leccion[] {

    const leccionesModulo = [...this.lecciones]

      .filter(
        l => l.moduloId === this.formulario.moduloId
      )

      .sort(
        (a, b) => a.ordenLeccion - b.ordenLeccion
      );

    const leccionEditando = leccionesModulo.find(
      l => l.leccionId === leccionId
    );

    if (!leccionEditando) {
      return [];
    }

    const restantes = leccionesModulo.filter(
      l => l.leccionId !== leccionId
    );

    const posicion = Math.max(
      0,
      Math.min(nuevoOrden - 1, restantes.length)
    );

    restantes.splice(posicion, 0, {
      ...leccionEditando,
      ordenLeccion: nuevoOrden
    });

    return restantes

      .map((leccion, index) => ({
        ...leccion,
        ordenLeccion: index + 1
      }))

      .filter(
        l => l.leccionId !== leccionId
      );
  }

  private actualizarOrdenes(
    lecciones: Leccion[]
  ): Observable<Leccion[]> {

    if (lecciones.length === 0) {
      return of([]);
    }

    return forkJoin(

      lecciones.map(leccion =>

        this.leccionService.updateLeccion(
          leccion.leccionId!,
          leccion
        )

      )

    );
  }

  logout() {

    this.usuarioService.logout();

    this.router.navigate(['/usuario-login']);
  }

}