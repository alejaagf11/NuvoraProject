import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MetasAhorroService } from '../services/metas-ahorro.service';
import { MetasAhorro } from '../models/metas_ahorro';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-metas-ahorro-form',
  standalone: false,
  templateUrl: './metas-ahorro-form.component.html',
  styleUrls: ['./metas-ahorro-form.component.css']
})
export class MetasAhorroFormComponent implements OnInit {
  meta: MetasAhorro = {
    nombreMeta: '',
    montoObjetivo: 0,
    fechaLimite: ''
  };

  isEditMode = false;
  metaId: number | null = null;


  constructor(
    private metasService: MetasAhorroService,
    private router: Router,
    private route: ActivatedRoute,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.isEditMode = true;
      this.metaId = Number(id);
      this.cargarMeta(this.metaId);
    }
  }

  cargarMeta(id: number) {
    this.metasService.getById(id).subscribe({
      next: (data) => {
        this.meta = {
          ...data,
          fechaLimite: data.fechaLimite
        };
      },
      error: (err) => {
        console.error('Error al cargar meta', err);
        this.router.navigate(['/metas-ahorro-list']);
      }
    });
  }

  saveMeta() {
    if (this.isEditMode && this.metaId) {
      this.metasService.update(this.metaId, this.meta).subscribe({
        next: () => {
          console.log('Meta actualizada');
          this.router.navigate(['/metas-ahorro-list']);
        },
        error: (err) => {
          console.error('Error al actualizar meta', err);
        }
      });
      return;
    }

    this.metasService.create(this.meta).subscribe({
      next: () => {
        console.log('Meta guardada');
        this.router.navigate(['/metas-ahorro-list']);
      },
      error: (err) => {
        console.error('Error al guardar meta', err);
      }
    });
  }

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/usuario-login']);
  }
}
