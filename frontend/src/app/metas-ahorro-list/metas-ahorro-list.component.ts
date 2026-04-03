import { Component, OnInit } from '@angular/core';
import { MetasAhorroService } from '../services/metas-ahorro.service';
import { MetasAhorro } from '../models/metas_ahorro';


@Component({
  selector: 'app-metas-ahorro-list',
  standalone: false,
  templateUrl: './metas-ahorro-list.component.html',
  styleUrl: './metas-ahorro-list.component.css'
})
export class MetasAhorroListComponent implements OnInit {

  metas: MetasAhorro[] = [];
  abonoInput: { [key: number]: number } = {};

  constructor(private metasService: MetasAhorroService) { }

  ngOnInit(): void {
    this.loadMetas();
  }

  loadMetas() {
    this.metasService.getAll().subscribe({
      next: (data) => {
        this.metas = data;
        console.log('Metas cargadas:', data);
      },
      error: (err) => {
        console.error('Error al cargar metas:', err);
        alert('Error al cargar metas: ' + (err.error?.message || err.message || err.status));
      }
    });
  }

  deleteMeta(id: number) {
    if(confirm("¿Seguro que quieres eliminar esta meta?")) {
      this.metasService.delete(id).subscribe({
        next: () => {
          console.log('Meta eliminada');
          this.loadMetas();
        },
        error: (err) => {
          console.error('Error al eliminar meta:', err);
          alert('Error al eliminar: ' + (err.error?.message || err.message || err.status));
        }
      });
    }
  }

  abonar(id: number, monto: number) {
    if (!monto || monto <= 0) {
      alert('Ingresa un monto válido');
      return;
    }

    this.metasService.abonar(id, monto).subscribe({
      next: (metaActualizada) => {
        console.log('Abono realizado:', metaActualizada);
        this.abonoInput[id] = 0; // Limpiar input
        this.loadMetas(); // Recargar lista
        alert('¡Abono realizado exitosamente!');
      },
      error: (err) => {
        console.error('Error al abonar:', err);
        alert('Error al abonar: ' + (err.error?.message || err.message || err.status));
      }
    });
  }
}