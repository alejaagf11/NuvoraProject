import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Transaccion } from '../models/transaccion';
import { TransaccionService } from '../services/transaccion.service';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  transacciones: Transaccion[] = [];
  saldoActual = 0;
  errorMensaje: string | null = null;

  constructor(
    private transaccionService: TransaccionService,
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarSaldo();
    this.cargarTransacciones();
  }

  cargarSaldo() {
    this.transaccionService.getSaldo().subscribe({
      next: (data) => {
        this.saldoActual = data;
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

  irATransacciones(tipo?: 'INGRESO' | 'GASTO') {
    if (tipo) {
      this.router.navigate(['/transacciones'], { queryParams: { tipo } });
      return;
    }

    this.router.navigate(['/transacciones']);
  }

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/usuario-login']);
  }

  esGasto(tipo: string): boolean {
    return tipo === 'GASTO';
  }
}
