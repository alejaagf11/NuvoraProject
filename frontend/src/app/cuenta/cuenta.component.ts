import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Usuario } from '../models/usuarios';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-cuenta',
  standalone: false,
  templateUrl: './cuenta.component.html',
  styleUrls: ['./cuenta.component.css']
})
export class CuentaComponent implements OnInit {

  usuario: Usuario = {
    nombreUsuario: '',
    correoUsuario: '',
    montoMensual: 0
  };

  errorMensaje: string | null = null;
  mensajeExito: string | null = null;
  isEditMode = false;

  constructor(
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarUsuario();
  }

  cargarUsuario() {
    this.usuarioService.getMiUsuario().subscribe({
      next: (data) => {
        this.usuario = data;
        this.errorMensaje = null;
      },
      error: (err) => {
        console.error('Error al cargar usuario', err);
        this.errorMensaje = 'No se pudo cargar la información de la cuenta';
      }
    });
  }

  guardarCambios() {
    this.usuarioService.updateMiUsuario(this.usuario).subscribe({
      next: (data) => {
        this.usuario = data;
        this.mensajeExito = 'Datos actualizados correctamente';
        this.errorMensaje = null;
        this.isEditMode = false;
      },
      error: (err) => {
        console.error('Error al actualizar usuario', err);
        this.errorMensaje = err.error?.message || 'No se pudo actualizar la cuenta';
      }
    });
  }

  eliminarCuenta() {
    this.usuarioService.deleteMiUsuario().subscribe({
      next: () => {
        this.usuarioService.logout();
        this.router.navigate(['/usuario-login']);
      },
      error: (err) => {
        console.error('Error al eliminar cuenta', err);
        this.errorMensaje = 'No se pudo eliminar la cuenta';
      }
    });
  }

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/usuario-login']);
  }
}
