export interface Leccion {
  leccionId?: number;
  tituloLeccion: string;
  contenidoLeccion: string;
  ordenLeccion: number;
  activo?: boolean;
  moduloId: number;
}
