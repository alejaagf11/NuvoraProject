export interface Transaccion {
  transaccionId?: number;
  montoTransaccion: number;
  descTransaccion: string;
  fechaTransaccion?: string;
  tipo: 'INGRESO' | 'GASTO';
  categoriaId: number;
  usuarioId?: number;
}
