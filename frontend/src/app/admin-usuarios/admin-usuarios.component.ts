import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Usuario } from '../models/usuarios';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-admin-usuarios',
  standalone: false,
  templateUrl: './admin-usuarios.component.html',
  styleUrls: ['./admin-usuarios.component.css']
})
export class AdminUsuariosComponent implements OnInit {
  usuarios: Usuario[] = [];
  cargando = true;
  errorMensaje: string | null = null;
  mensajeExito: string | null = null;
  usuarioActualId: number | null = null;

  usuarioEditandoId: number | null = null;
  nombreEditando = '';
  correoEditando = '';
  rolEditando = '';
  montoMensualEditando: number | null = null;

  constructor(
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarUsuarioActual();
    this.cargarUsuarios();
  }

  cargarUsuarioActual() {
    this.usuarioService.getMiUsuario().subscribe({
      next: (usuario) => {
        this.usuarioActualId = usuario.usuarioId ?? null;
      },
      error: (err) => {
        console.error('Error al cargar usuario actual', err);
      }
    });
  }

  cargarUsuarios() {
    this.cargando = true;
    this.usuarioService.getAdminUsuarios().subscribe({
      next: (data) => {
        this.usuarios = data;
        this.cargando = false;
        this.errorMensaje = null;
      },
      error: (err) => {
        console.error('Error al cargar usuarios', err);
        this.errorMensaje = 'No se pudieron cargar los usuarios';
        this.cargando = false;
      }
    });
  }

  iniciarEdicion(usuario: Usuario) {
    this.usuarioEditandoId = usuario.usuarioId || null;
    this.nombreEditando = usuario.nombreUsuario;
    this.correoEditando = usuario.correoUsuario;
    this.rolEditando = usuario.rolUsuario || 'USER';
    this.montoMensualEditando = usuario.montoMensual || 0;
    this.errorMensaje = null;
    this.mensajeExito = null;
  }

  cancelarEdicion() {
    this.usuarioEditandoId = null;
    this.nombreEditando = '';
    this.correoEditando = '';
    this.rolEditando = '';
    this.montoMensualEditando = null;
  }

  guardarEdicion(usuario: Usuario) {
    if (!usuario.usuarioId) return;

    if (this.esUsuarioActual(usuario)) {
      this.errorMensaje = 'Tu propia cuenta se gestiona desde Mi cuenta, no desde el panel admin.';
      return;
    }

    if (!this.nombreEditando.trim() || !this.correoEditando.trim()) {
      this.errorMensaje = 'Completa el nombre y el correo del usuario';
      return;
    }

    const payload: Usuario = {
      nombreUsuario: this.nombreEditando.trim(),
      correoUsuario: this.correoEditando.trim(),
      rolUsuario: this.rolEditando,
      montoMensual: this.montoMensualEditando || 0
    };

    this.usuarioService.updateUsuarioAdmin(usuario.usuarioId, payload).subscribe({
      next: (usuarioActualizado) => {
        this.usuarios = this.usuarios.map(item =>
          item.usuarioId === usuarioActualizado.usuarioId ? usuarioActualizado : item
        );
        this.mensajeExito = 'Usuario actualizado correctamente';
        this.errorMensaje = null;
        this.cancelarEdicion();
        this.cargarUsuarios();
      },
      error: (err) => {
        console.error('Error al actualizar usuario', err);
        this.errorMensaje = this.obtenerMensajeError(err, 'No se pudo actualizar el usuario');
      }
    });
  }

  eliminarUsuario(id?: number) {
    if (!id) return;

    if (id === this.usuarioActualId) {
      this.errorMensaje = 'No puedes eliminar la cuenta con la que tienes la sesion abierta.';
      return;
    }

    if (!confirm('Seguro que quieres eliminar este usuario?')) {
      return;
    }

    this.usuarioService.deleteUsuarioAdmin(id).subscribe({
      next: () => {
        this.mensajeExito = 'Usuario eliminado correctamente';
        this.errorMensaje = null;
        this.usuarios = this.usuarios.filter(usuario => usuario.usuarioId !== id);
        this.cancelarEdicion();
      },
      error: (err) => {
        console.error('Error al eliminar usuario', err);
        this.errorMensaje = this.obtenerMensajeError(
          err,
          'No se pudo eliminar el usuario. Es posible que tenga transacciones, metas u otros datos relacionados.'
        );
        this.cargarUsuarios();
      }
    });
  }

  contarAdmins(): number {
    return this.usuarios.filter(u => u.rolUsuario === 'ADMIN').length;
  }

  contarUsers(): number {
    return this.usuarios.filter(u => (u.rolUsuario || 'USER') !== 'ADMIN').length;
  }

  esUsuarioActual(usuario: Usuario): boolean {
    return (usuario.usuarioId ?? null) === this.usuarioActualId;
  }

  trackByUsuarioId(index: number, usuario: Usuario): number {
    return usuario.usuarioId ?? index;
  }

  private obtenerMensajeError(err: any, mensajePorDefecto: string): string {
    if (typeof err?.error === 'string' && err.error.trim()) {
      return err.error;
    }

    return err?.error?.message || mensajePorDefecto;
  }

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/usuario-login']);
  }
}
