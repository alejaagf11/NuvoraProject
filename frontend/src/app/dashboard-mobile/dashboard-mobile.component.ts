import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Transaccion } from '../models/transaccion';
import { Usuario } from '../models/usuarios';
import { TransaccionService } from '../services/transaccion.service';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-dashboard-mobile',
  standalone: false,
  templateUrl: './dashboard-mobile.component.html',
  styleUrls: ['./dashboard-mobile.component.css']
})
export class DashboardMobileComponent implements OnInit {

  transacciones: Transaccion[] = [];
  saldoActual = 0;
  errorMensaje: string | null = null;

  usuario: Usuario = {
    nombreUsuario: '',
    correoUsuario: '',
    montoMensual: 0
  };

  nivelBotella = 8;

  constructor(
    private transaccionService: TransaccionService,
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarUsuario();
    this.cargarSaldo();
    this.cargarTransacciones();
  }

  cargarUsuario() {
    this.usuarioService.getMiUsuario().subscribe({
      next: (data) => {
        this.usuario = data;
        this.actualizarNivelBotella();
      },
      error: (err) => {
        console.error('Error al cargar usuario', err);
      }
    });
  }

  cargarSaldo() {
    this.transaccionService.getSaldo().subscribe({
      next: (data) => {
        this.saldoActual = data;
        this.actualizarNivelBotella();
      },
      error: (err) => {
        console.error('Error al cargar saldo', err);
        this.errorMensaje = 'No se pudo cargar el saldo actual';
      }
    });
  }

  cargarTransacciones() {
    this.transaccionService.getAll().subscribe({
      next: (data) => {
        this.transacciones = data.slice(0, 5);
      },
      error: (err) => {
        console.error('Error al cargar transacciones', err);
        this.errorMensaje = 'No se pudieron cargar las transacciones';
      }
    });
  }

  actualizarNivelBotella() {
    const saldo = this.saldoActual || 0;
    const montoMensual = this.usuario.montoMensual || 0;

    if (saldo <= 0) {
      this.nivelBotella = 8;
      return;
    }

    const referencia = montoMensual > 0 ? montoMensual : 500000;
    const porcentaje = (saldo / referencia) * 100;

    this.nivelBotella = Math.max(8, Math.min(porcentaje, 100));
  }

  irATransacciones(tipo?: 'INGRESO' | 'GASTO') {
    if (tipo) {
      this.router.navigate(['/transacciones'], { queryParams: { tipo } });
      return;
    }

    this.router.navigate(['/transacciones']);
  }

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/mobile/usuario-login']);
  }

  esGasto(tipo: string): boolean {
    return tipo === 'GASTO';
  }

  esAdmin(): boolean {
    return (this.usuario.rolUsuario || '').toUpperCase() === 'ADMIN';
  }
}
