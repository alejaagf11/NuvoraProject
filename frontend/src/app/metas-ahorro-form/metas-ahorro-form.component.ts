import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MetasAhorroService } from '../services/metas-ahorro.service';
import { MetasAhorro } from '../models/metas_ahorro';
import { UsuarioService } from '../services/usuario.service';
import id from '@angular/common/locales/extra/id';

@Component({
  selector: 'app-metas-ahorro-form',
  standalone: false,
  templateUrl: './metas-ahorro-form.component.html',
  styleUrls: ['./metas-ahorro-form.component.css']
})
export class MetasAhorroFormComponent implements OnInit {
  meta: MetasAhorro = {
    nombreMeta: '',
    montoObjetivo: 0,
    fechaLimite: ''
    
  };

  mensajeExito: string | null = null;
errorMensaje: string | null = null;

  isEditMode = false;
  metaId: number | null = null;


  constructor(
    private metasService: MetasAhorroService,
    private router: Router,
    private route: ActivatedRoute,
    private usuarioService: UsuarioService
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.isEditMode = true;
      this.metaId = Number(id);
      this.cargarMeta(this.metaId);
    }
  }

  cargarMeta(id: number) {
    this.metasService.getById(id).subscribe({
      next: (data) => {
        this.meta = {
          ...data,
          fechaLimite: data.fechaLimite
        };
      },
      error: (err) => {
        console.error('Error al cargar meta', err);
        this.router.navigate(['/metas-ahorro-list']);
      }
    });
  }

  saveMeta() {

  this.meta.montoObjetivo = Number(
    String(this.meta.montoObjetivo).replace(/\./g, '')
  );

  // Validar campos obligatorios
  if (
    !this.meta.nombreMeta ||
    !this.meta.montoObjetivo ||
    !this.meta.fechaLimite
  ) {
    this.errorMensaje = 'Completa todos los campos obligatorios';
    this.mensajeExito = null;
    return;
  }

  // Validar fecha
  const fechaSeleccionada = new Date(this.meta.fechaLimite);
  const hoy = new Date();

  // Quitar horas para comparar solo fechas
  hoy.setHours(0, 0, 0, 0);
  fechaSeleccionada.setHours(0, 0, 0, 0);

  if (fechaSeleccionada < hoy) {
    this.errorMensaje =
      'La fecha límite no puede ser una fecha anterior a la actual';
    this.mensajeExito = null;
    return;
  }

  if (this.isEditMode && this.metaId) {
    this.metasService.update(this.metaId, this.meta).subscribe({
      next: () => {
        console.log('Meta actualizada');
        this.router.navigate(['/metas-ahorro-list']);
      },
      error: (err) => {
        console.error('Error al actualizar meta', err);
      }
    });
    return;
  }

  this.metasService.create(this.meta).subscribe({
    next: () => {
      console.log('Meta guardada');
      this.router.navigate(['/metas-ahorro-list']);
    },
    error: (err) => {
      console.error('Error al guardar meta', err);
    }
  });
}

eliminarMeta(id: number) {

  const confirmar = confirm(
    '¿Deseas eliminar esta meta?'
  );

  if (!confirmar) {
    return;
  }

  this.metasService.delete(id).subscribe({

    next: () => {

      this.mensajeExito = 'Meta eliminada correctamente';
      this.errorMensaje = null;

      this.router.navigate(['/metas-ahorro-list']);
    },

    error: (err) => {

      console.error('Error al eliminar meta', err);

      this.errorMensaje =
        err.error?.message ||
        'No se pudo eliminar la meta';
    }

  });
}
  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/usuario-login']);
  }
}
