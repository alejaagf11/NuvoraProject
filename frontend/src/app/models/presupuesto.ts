export interface PresupuestoRequest {
  ingreso: number;
  gastosFijos?: number;
  deudas?: number;
  ahorroDeseado?: number;
  estiloVida?: string;
}

export interface PresupuestoResponse {
  esenciales: number;
  deudas: number;
  ahorro: number;
  estiloVida: number;
  imprevistos: number;
  disponibleSemanal: number;
}
