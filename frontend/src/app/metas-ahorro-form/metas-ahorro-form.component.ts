import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MetasAhorroService } from '../services/metas-ahorro.service';
import { MetasAhorro } from '../models/metas_ahorro';

@Component({
  selector: 'app-metas-ahorro-form',
  standalone: false,
  templateUrl: './metas-ahorro-form.component.html',
  styleUrl: './metas-ahorro-form.component.css'
})
export class MetasAhorroFormComponent {
  meta: MetasAhorro = { 
    nombreMeta: '', 
    montoObjetivo: 0, 
    fechaLimite: ''
  };

  metas: MetasAhorro[] = [];

  constructor(private metasService: MetasAhorroService, private router: Router) { }

  saveMeta() {
    this.metasService.create(this.meta).subscribe({
      next: () => {
        console.log('Meta guardada');
        this.router.navigate(['/metas-ahorro-list']);
      },
      error: (err) => {
        console.error('Error al guardar meta', err);
        alert('Error al guardar la meta');
      }
    });
  }

  deleteMeta(id: number) {
    if (confirm('¿Estás seguro de que deseas eliminar esta meta?')) {
      this.metasService.delete(id).subscribe({
        next: () => {
          console.log('Meta eliminada');
          this.cargarMetas();
        },
        error: (err) => {
          console.error('Error al eliminar meta', err);
          alert('Error al eliminar la meta');
        }
      });
    }
  }

  cargarMetas() {
    this.metasService.getAll().subscribe({
      next: (data) => {
        this.metas = data;
      },
      error: (err) => {
        console.error('Error al cargar metas', err);
      }
    });
  }
}

