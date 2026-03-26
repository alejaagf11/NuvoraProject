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
    fechaLimite: '', 
    ahorroMensual: 0 
  };

  constructor(private metasService: MetasAhorroService, private router: Router) { }

  saveMeta() {
    this.metasService.create(this.meta).subscribe(() => {
      this.router.navigate(['/metas-ahorro-list']);
    });
  }
}

