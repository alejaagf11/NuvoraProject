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

  usuarioEditandoId: number | null = null;
  rolEditando = '';
  montoMensualEditando: number | null = null;

  constructor(
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarUsuarios();
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
    this.rolEditando = usuario.rolUsuario || 'USER';
    this.montoMensualEditando = usuario.montoMensual || 0;
  }

  cancelarEdicion() {
    this.usuarioEditandoId = null;
    this.rolEditando = '';
    this.montoMensualEditando = null;
  }

  guardarEdicion(usuario: Usuario) {
    if (!usuario.usuarioId) return;

    const payload: Usuario = {
      ...usuario,
      rolUsuario: this.rolEditando,
      montoMensual: this.montoMensualEditando || 0
    };

    this.usuarioService.updateUsuarioAdmin(usuario.usuarioId, payload).subscribe({
      next: () => {
        this.mensajeExito = 'Usuario actualizado correctamente';
        this.cancelarEdicion();
        this.cargarUsuarios();
      },
      error: (err) => {
        console.error('Error al actualizar usuario', err);
        this.errorMensaje = 'No se pudo actualizar el usuario';
      }
    });
  }

  eliminarUsuario(id?: number) {
    if (!id) return;

    if (!confirm('¿Seguro que quieres eliminar este usuario?')) {
      return;
    }

    this.usuarioService.deleteUsuarioAdmin(id).subscribe({
      next: () => {
        this.mensajeExito = 'Usuario eliminado correctamente';
        this.cargarUsuarios();
      },
      error: (err) => {
        console.error('Error al eliminar usuario', err);
        this.errorMensaje = 'No se pudo eliminar el usuario';
      }
    });
  }

  contarAdmins(): number {
    return this.usuarios.filter(u => u.rolUsuario === 'ADMIN').length;
  }

  contarUsers(): number {
    return this.usuarios.filter(u => (u.rolUsuario || 'USER') !== 'ADMIN').length;
  }

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/usuario-login']);
  }
}
