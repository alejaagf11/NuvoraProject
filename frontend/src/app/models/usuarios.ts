export interface Usuario {
  usuarioId?: number;
  rolUsuario?: string;
  nombreUsuario: string;
  correoUsuario: string;
  contrasenaUsuario?: string;
  montoMensual?: number;
  fotoPerfil?: string;
}
