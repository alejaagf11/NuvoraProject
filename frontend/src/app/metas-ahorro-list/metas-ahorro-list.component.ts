import { Component, OnInit } from '@angular/core';
import { MetasAhorroService } from '../services/metas-ahorro.service';
import { MetasAhorro } from '../models/metas_ahorro';
import { Router } from '@angular/router';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-metas-ahorro-list',
  standalone: false,
  templateUrl: './metas-ahorro-list.component.html',
  styleUrls: ['./metas-ahorro-list.component.css']
})
export class MetasAhorroListComponent implements OnInit {

  metas: MetasAhorro[] = [];
  abonoInput: { [key: number]: number } = {};
  errorMensaje: string | null = null;
  mensajeExito: string | null = null;

  constructor(
    private metasService: MetasAhorroService,
    private usuarioService: UsuarioService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadMetas();
  }

  loadMetas() {
    this.metasService.getAll().subscribe({
      next: (data) => {
        this.metas = data;
        this.errorMensaje = null;
        console.log('Metas cargadas:', data);
      },
      error: (err) => {
        console.error('Error al cargar metas:', err);
        this.errorMensaje = 'Error al cargar metas: ' + (err.error?.message || err.message || err.status);
      }
    });
  }

  deleteMeta(id: number) {
    this.metasService.delete(id).subscribe({
      next: () => {
        console.log('Meta eliminada');
        this.mensajeExito = 'Meta eliminada correctamente';
        this.loadMetas();
      },
      error: (err) => {
        console.error('Error al eliminar meta:', err);
        this.errorMensaje = 'Error al eliminar: ' + (err.error?.message || err.message || err.status);
      }
    });
  }

  abonar(id: number, monto: number) {
    if (!monto || monto <= 0) {
      this.errorMensaje = 'Ingresa un monto valido';
      return;
    }

    this.metasService.abonar(id, monto).subscribe({
      next: (metaActualizada) => {
        console.log('Abono realizado:', metaActualizada);
        this.abonoInput[id] = 0;
        this.mensajeExito = 'Abono realizado exitosamente';
        this.errorMensaje = null;
        this.loadMetas();
      },
      error: (err) => {
        console.error('Error al abonar:', err);
        this.errorMensaje = 'Error al abonar: ' + (err.error?.message || err.message || err.status);
      }
    });
  }

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/usuario-login']);
  }
}
