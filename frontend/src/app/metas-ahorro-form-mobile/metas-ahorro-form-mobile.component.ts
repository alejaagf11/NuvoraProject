import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MetasAhorro } from '../models/metas_ahorro';
import { MetasAhorroService } from '../services/metas-ahorro.service';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-metas-ahorro-form-mobile',
  standalone: false,
  templateUrl: './metas-ahorro-form-mobile.component.html',
  styleUrls: ['./metas-ahorro-form-mobile.component.css'],
})
export class MetasAhorroFormMobileComponent implements OnInit {
  meta: MetasAhorro = {
    nombreMeta: '',
    montoObjetivo: 0,
    fechaLimite: ''
  };

  isEditMode = false;
  metaId: number | null = null;
  mensajeExito: string | null = null;
  errorMensaje: string | null = null;
  guardando = false;

  constructor(
    private metasService: MetasAhorroService,
    private router: Router,
    private route: ActivatedRoute,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.isEditMode = true;
      this.metaId = Number(id);
      this.cargarMeta(this.metaId);
    }
  }

  cargarMeta(id: number): void {
    this.metasService.getById(id).subscribe({
      next: (data) => {
        this.meta = {
          ...data,
          fechaLimite: data.fechaLimite
        };
      },
      error: (err) => {
        console.error('Error al cargar meta', err);
        this.router.navigate(['/mobile/metas-ahorro-list']);
      }
    });
  }

  saveMeta(): void {
    this.guardando = true;

    if (this.isEditMode && this.metaId) {
      this.metasService.update(this.metaId, this.meta).subscribe({
        next: () => {
          this.guardando = false;
          this.router.navigate(['/mobile/metas-ahorro-list']);
        },
        error: (err) => {
          console.error('Error al actualizar meta', err);
          this.errorMensaje = 'No se pudo actualizar la meta';
          this.guardando = false;
        }
      });
      return;
    }

    this.metasService.create(this.meta).subscribe({
      next: () => {
        this.guardando = false;
        this.router.navigate(['/mobile/metas-ahorro-list']);
      },
      error: (err) => {
        console.error('Error al guardar meta', err);
        this.errorMensaje = 'No se pudo guardar la meta';
        this.guardando = false;
      }
    });
  }

  eliminarMeta(id: number): void {
    if (!confirm('Deseas eliminar esta meta?')) {
      return;
    }

    this.metasService.delete(id).subscribe({
      next: () => {
        this.mensajeExito = 'Meta eliminada correctamente';
        this.errorMensaje = null;
        this.router.navigate(['/mobile/metas-ahorro-list']);
      },
      error: (err) => {
        console.error('Error al eliminar meta', err);
        this.errorMensaje = err.error?.message || 'No se pudo eliminar la meta';
      }
    });
  }

  logout(): void {
    this.usuarioService.logout();
    this.router.navigate(['/mobile/usuario-login']);
  }

}
