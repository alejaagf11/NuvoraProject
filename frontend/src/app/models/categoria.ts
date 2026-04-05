export interface Categoria {
  categoriaId?: number;
  nombreCategoria: string;
  tipoCategoria: 'INGRESO' | 'GASTO';
  usuarioId?: number;
}
