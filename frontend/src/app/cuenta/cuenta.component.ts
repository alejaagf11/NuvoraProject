import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Usuario } from '../models/usuarios';
import { MetasAhorroService } from '../services/metas-ahorro.service';
import { TransaccionService } from '../services/transaccion.service';
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

  saldoActual = 0;
  totalMetas = 0;
  totalTransacciones = 0;

  constructor(
    private usuarioService: UsuarioService,
    private transaccionService: TransaccionService,
    private metasAhorroService: MetasAhorroService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarUsuario();
    this.cargarSaldo();
    this.cargarResumen();
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

  cargarSaldo() {
    this.transaccionService.getSaldo().subscribe({
      next: (data) => {
        this.saldoActual = data;
      },
      error: (err) => {
        console.error('Error al cargar saldo', err);
      }
    });
  }

  cargarResumen() {
    this.transaccionService.getAll().subscribe({
      next: (data) => {
        this.totalTransacciones = data.length;
      },
      error: (err) => {
        console.error('Error al cargar transacciones', err);
      }
    });

    this.metasAhorroService.getAll().subscribe({
      next: (data) => {
        this.totalMetas = data.length;
      },
      error: (err) => {
        console.error('Error al cargar metas', err);
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
