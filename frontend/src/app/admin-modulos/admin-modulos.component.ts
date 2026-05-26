import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { from, Observable, of } from 'rxjs';
import { concatMap, switchMap, toArray } from 'rxjs/operators';
import { ModuloAprendizaje } from '../models/aprendizaje';
import { ModuloAprendizajeService } from '../services/modulo-aprendizaje.service';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-admin-modulos',
  standalone: false,
  templateUrl: './admin-modulos.component.html',
  styleUrls: ['./admin-modulos.component.css']
})
export class AdminModulosComponent implements OnInit {
  modulos: ModuloAprendizaje[] = [];
  cargando = true;
  errorMensaje: string | null = null;
  mensajeExito: string | null = null;

  formulario: ModuloAprendizaje = {
    tituloModulo: '',
    descripcionModulo: '',
    ordenModulo: 1,
    activo: true
  };

  editandoId: number | null = null;

  constructor(
    private moduloService: ModuloAprendizajeService,
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarModulos();
  }

  cargarModulos() {
    this.cargando = true;
    this.moduloService.getModulos().subscribe({
      next: (data) => {
        this.modulos = data;
        this.cargando = false;
        this.errorMensaje = null;
      },
      error: (err) => {
        console.error('Error al cargar modulos', err);
        this.errorMensaje = 'No se pudieron cargar los modulos';
        this.cargando = false;
      }
    });
  }

  guardarModulo() {
    this.formulario.ordenModulo = this.normalizarOrden(this.formulario.ordenModulo);

    if (!this.formulario.tituloModulo.trim() || !this.formulario.descripcionModulo.trim()) {
      this.errorMensaje = 'Completa el titulo y la descripcion del modulo';
      return;
    }

    if (this.editandoId) {
      const modulosAMover = this.obtenerModulosParaEditar(
        this.editandoId,
        this.formulario.ordenModulo
      );

      this.moverOrdenesTemporalmente(modulosAMover).pipe(
        switchMap(() => this.moduloService.updateModulo(this.editandoId!, this.formulario)),
        switchMap(() => this.actualizarOrdenesFinales(modulosAMover))
      ).subscribe({
        next: () => {
          this.mensajeExito = 'Modulo actualizado correctamente';
          this.resetFormulario();
          this.cargarModulos();
        },
        error: (err) => {
          console.error('Error al actualizar modulo', err);
          this.errorMensaje = this.obtenerMensajeError(err, 'No se pudo actualizar el modulo');
        }
      });
      return;
    }

    const modulosAMover = this.obtenerModulosParaCrear(this.formulario.ordenModulo);

    this.moverOrdenesTemporalmente(modulosAMover).pipe(
      switchMap(() => this.moduloService.createModulo(this.formulario)),
      switchMap(() => this.actualizarOrdenesFinales(modulosAMover))
    ).subscribe({
      next: () => {
        this.mensajeExito = 'Modulo creado correctamente';
        this.resetFormulario();
        this.cargarModulos();
      },
      error: (err) => {
        console.error('Error al crear modulo', err);
        this.errorMensaje = this.obtenerMensajeError(err, 'No se pudo crear el modulo');
      }
    });
  }

  editarModulo(modulo: ModuloAprendizaje) {
    this.editandoId = modulo.moduloId || null;
    this.formulario = {
      moduloId: modulo.moduloId,
      tituloModulo: modulo.tituloModulo,
      descripcionModulo: modulo.descripcionModulo,
      ordenModulo: modulo.ordenModulo,
      activo: modulo.activo
    };
    this.errorMensaje = null;
    this.mensajeExito = null;
  }

  eliminarModulo(moduloId?: number) {
    if (!moduloId) return;

    if (!confirm('¿Seguro que quieres eliminar este modulo?')) {
      return;
    }

    this.moduloService.deleteModulo(moduloId).subscribe({
      next: () => {
        this.mensajeExito = 'Modulo eliminado correctamente';
        this.cargarModulos();
      },
      error: (err) => {
        console.error('Error al eliminar modulo', err);
        this.errorMensaje = 'No se pudo eliminar el modulo';
      }
    });
  }

  resetFormulario() {
    this.editandoId = null;
    this.formulario = {
      tituloModulo: '',
      descripcionModulo: '',
      ordenModulo: 1,
      activo: true
    };
  }

  irALecciones(moduloId?: number) {
    if (!moduloId) return;
    this.router.navigate(['/admin/lecciones'], { queryParams: { moduloId } });
  }

  private normalizarOrden(orden: number): number {
    const ordenNumerico = Number(orden);

    return Number.isFinite(ordenNumerico) && ordenNumerico > 0
      ? Math.floor(ordenNumerico)
      : 1;
  }

  private obtenerModulosParaCrear(nuevoOrden: number): ModuloAprendizaje[] {
    return this.modulos
      .filter(modulo =>
        !!modulo.moduloId &&
        modulo.ordenModulo >= nuevoOrden
      )
      .map(modulo => ({
        ...modulo,
        ordenModulo: modulo.ordenModulo + 1
      }));
  }

  private obtenerModulosParaEditar(moduloId: number, nuevoOrden: number): ModuloAprendizaje[] {
    const modulosOrdenados = [...this.modulos].sort(
      (a, b) => a.ordenModulo - b.ordenModulo
    );

    const moduloEditando = modulosOrdenados.find(
      modulo => modulo.moduloId === moduloId
    );

    if (!moduloEditando) {
      return [];
    }

    const restantes = modulosOrdenados.filter(
      modulo => modulo.moduloId !== moduloId
    );

    const posicion = Math.max(
      0,
      Math.min(nuevoOrden - 1, restantes.length)
    );

    restantes.splice(posicion, 0, {
      ...moduloEditando,
      ordenModulo: nuevoOrden
    });

    return restantes
      .map((modulo, index) => ({
        ...modulo,
        ordenModulo: index + 1
      }))
      .filter(modulo => modulo.moduloId !== moduloId);
  }

  private moverOrdenesTemporalmente(modulos: ModuloAprendizaje[]): Observable<ModuloAprendizaje[]> {
    if (modulos.length === 0) {
      return of([]);
    }

    const ordenTemporalBase =
      Math.max(
        ...this.modulos.map(modulo => modulo.ordenModulo),
        ...modulos.map(modulo => modulo.ordenModulo),
        0
      ) + 1000;

    return from(modulos).pipe(
      concatMap((modulo, index) =>
        this.moduloService.updateModulo(
          modulo.moduloId!,
          {
            ...modulo,
            ordenModulo: ordenTemporalBase + index
          }
        )
      ),
      toArray()
    );
  }

  private actualizarOrdenesFinales(modulos: ModuloAprendizaje[]): Observable<ModuloAprendizaje[]> {
    if (modulos.length === 0) {
      return of([]);
    }

    return from(modulos).pipe(
      concatMap(modulo =>
        this.moduloService.updateModulo(
          modulo.moduloId!,
          modulo
        )
      ),
      toArray()
    );
  }

  private obtenerMensajeError(err: any, mensajePorDefecto: string): string {
    if (typeof err?.error === 'string' && err.error.trim()) {
      return err.error;
    }

    return mensajePorDefecto;
  }

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/usuario-login']);
  }
}
