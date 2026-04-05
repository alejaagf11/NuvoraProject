import { Component } from '@angular/core';
import { PresupuestoRequest, PresupuestoResponse } from '../models/presupuesto';
import { PresupuestoService } from '../services/presupuesto.service';

@Component({
  selector: 'app-presupuesto',
  standalone: false,
  templateUrl: './presupuesto.component.html',
  styleUrls: ['./presupuesto.component.css']
})
export class PresupuestoComponent {

  presupuestoForm: PresupuestoRequest = {
    ingreso: 0,
    gastosFijos: 0,
    deudas: 0,
    ahorroDeseado: 0,
    estiloVida: ''
  };

  resultado: PresupuestoResponse | null = null;
  errorMensaje: string | null = null;

  constructor(private presupuestoService: PresupuestoService) {}

  generarPresupuesto() {
    if (!this.presupuestoForm.ingreso || this.presupuestoForm.ingreso <= 0) {
      this.errorMensaje = 'Debes ingresar un monto válido';
      return;
    }

    this.presupuestoService.generar(this.presupuestoForm).subscribe({
      next: (data) => {
        this.resultado = data;
        this.errorMensaje = null;
      },
      error: (err) => {
        console.error('Error al generar presupuesto', err);
        this.errorMensaje = err.error?.message || 'No se pudo generar el presupuesto';
      }
    });
  }
}
