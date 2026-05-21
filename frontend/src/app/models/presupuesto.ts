export interface GastoItem {
  nombre: string;
  monto: number;
  diaPago: number;
}

export interface PresupuestoRequest {
  ingreso: number;
  deudas?: number;
  ahorroDeseado?: number;
  estiloVida?: string;
  tipoIngreso?: string;
  gastosFijos: GastoItem[];
  gastosVariables: GastoItem[];
}

export interface PresupuestoResponse {
  esenciales: number;
  variables: number;
  deudas: number;
  ahorro: number;
  estiloVida: number;
  imprevistos: number;
  disponibleSemanal: number;
  disponibleQuincena: { [key: string]: number };
}

export interface PresupuestoAiResponse {
  budget: PresupuestoResponse;
  resumen: string;
  recomendaciones: string[];
  alertas: string[];
  source: string;
}
