import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
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
    if (!this.formulario.tituloModulo.trim() || !this.formulario.descripcionModulo.trim()) {
      this.errorMensaje = 'Completa el titulo y la descripcion del modulo';
      return;
    }

    if (this.editandoId) {
      this.moduloService.updateModulo(this.editandoId, this.formulario).subscribe({
        next: () => {
          this.mensajeExito = 'Modulo actualizado correctamente';
          this.resetFormulario();
          this.cargarModulos();
        },
        error: (err) => {
          console.error('Error al actualizar modulo', err);
          this.errorMensaje = 'No se pudo actualizar el modulo';
        }
      });
      return;
    }

    this.moduloService.createModulo(this.formulario).subscribe({
      next: () => {
        this.mensajeExito = 'Modulo creado correctamente';
        this.resetFormulario();
        this.cargarModulos();
      },
      error: (err) => {
        console.error('Error al crear modulo', err);
        this.errorMensaje = 'No se pudo crear el modulo';
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

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/usuario-login']);
  }
}
