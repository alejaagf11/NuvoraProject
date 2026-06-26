import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MetasAhorro } from '../models/metas_ahorro';
import { MetasAhorroService } from '../services/metas-ahorro.service';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-metas-ahorro-list-mobile',
  standalone: false,
  templateUrl: './metas-ahorro-list-mobile.component.html',
  styleUrls: ['./metas-ahorro-list-mobile.component.css'],
})
export class MetasAhorroListMobileComponent implements OnInit {
  fotoPerfil: string | null = null;
  inicial: string = 'U';

  metas: MetasAhorro[] = [];
  abonoInput: { [key: number]: string } = {};
  errorMensaje: string | null = null;
  mensajeExito: string | null = null;
  cargando = false;

  constructor(
    private metasService: MetasAhorroService,
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadMetas();
    this.cargarUsuario();
  }

  private cargarUsuario(): void {
    const usuarioLocal = this.usuarioService.getUsuario();

    if (usuarioLocal) {
      this.fotoPerfil = usuarioLocal.fotoPerfil || null;
      this.inicial = usuarioLocal.nombreUsuario?.charAt(0).toUpperCase() || 'U';
    } else {
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
    }

    // Escucha cambios en localStorage
    window.addEventListener('storage', () => this.cargarUsuario());
  }

  loadMetas(): void {
    this.cargando = true;

    this.metasService.getAll().subscribe({
      next: (data) => {
        this.metas = data;
        this.errorMensaje = null;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al cargar metas:', err);
        this.errorMensaje = 'Error al cargar metas: ' + (err.error?.message || err.message || err.status);
        this.cargando = false;
      }
    });
  }

  deleteMeta(id: number): void {
    if (!confirm('Seguro que quieres eliminar esta meta?')) {
      return;
    }

    this.metasService.delete(id).subscribe({
      next: () => {
        this.mensajeExito = 'Meta eliminada correctamente';
        this.errorMensaje = null;
        this.loadMetas();
      },
      error: (err) => {
        console.error('Error al eliminar meta:', err);
        this.errorMensaje = 'Error al eliminar: ' + (err.error?.message || err.message || err.status);
      }
    });
  }

  formatAbono(event: any, metaId: number): void {
  let input = event.target.value;

  // Solo números
  input = input.replace(/\D/g, '');

  // Formato con puntos
  const formatted = input.replace(/\B(?=(\d{3})+(?!\d))/g, '.');

  // Actualizar input
  event.target.value = formatted;

  // Actualizar ngModel
  this.abonoInput[metaId] = formatted;
}

  abonar(id: number, monto: string | undefined): void {

  const montoNumerico = Number(
    String(monto || '').replace(/\./g, '')
  );

  if (!montoNumerico || montoNumerico <= 0) {
    this.errorMensaje = 'Ingresa un monto válido';
    this.mensajeExito = null;
    return;
  }

  this.metasService.abonar(id, montoNumerico).subscribe({
    next: () => {
      this.abonoInput[id] = '';
      this.mensajeExito = 'Abono realizado exitosamente';
      this.errorMensaje = null;
      this.loadMetas();
    },
    error: (err) => {
      console.error('Error al abonar:', err);
      this.errorMensaje =
        'Error al abonar: ' +
        (err.error?.message || err.message || err.status);
    }
  });
}
  progreso(meta: MetasAhorro): number {
    if (!meta.montoObjetivo) {
      return 0;
    }

    return Math.min(((meta.montoAhorrado || 0) / meta.montoObjetivo) * 100, 100);
  }

  logout(): void {
    this.usuarioService.logout();
    this.router.navigate(['/mobile/usuario-login']);
  }

}
