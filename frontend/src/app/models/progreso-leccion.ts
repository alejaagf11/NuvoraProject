export interface ProgresoLeccionUsuario {
  progresoId?: number;
  usuarioId: number;
  leccionId: number;
  completada: boolean;
  fechaCompletada?: string;
}
