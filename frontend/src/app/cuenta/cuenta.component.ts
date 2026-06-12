import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Usuario } from '../models/usuarios';
import { MetasAhorroService } from '../services/metas-ahorro.service';
import { TransaccionService } from '../services/transaccion.service';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-cuenta',
  standalone: false,
  templateUrl: './cuenta.component.html',
  styleUrls: ['./cuenta.component.css']
})
export class CuentaComponent implements OnInit {

  usuario: Usuario = {
    nombreUsuario: '',
    correoUsuario: '',
    montoMensual: 0
  };

  errorMensaje: string | null = null;
  mensajeExito: string | null = null;
  isEditMode = false;
  fotoPerfil: string | null = null;

  saldoActual = 0;
  totalMetas = 0;
  totalTransacciones = 0;

  constructor(
    private usuarioService: UsuarioService,
    private transaccionService: TransaccionService,
    private metasAhorroService: MetasAhorroService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarUsuario();
    this.cargarSaldo();
    this.cargarResumen();
  }

  cargarUsuario() {
    this.usuarioService.getMiUsuario().subscribe({
      next: (data) => {
        this.usuario = data;
        if (this.usuario.montoMensual) {
  this.usuario.montoMensual = Number(
    this.usuario.montoMensual
  ).toLocaleString('es-CO') as any;
}
        this.fotoPerfil = data.fotoPerfil || null;
        this.errorMensaje = null;
      },
      error: (err) => {
        console.error('Error al cargar usuario', err);
        this.errorMensaje = 'No se pudo cargar la información de la cuenta';
      }
    });
  }

  

  cargarSaldo() {
    this.transaccionService.getSaldo().subscribe({
      next: (data) => {
        this.saldoActual = data;
      },
      error: (err) => {
        console.error('Error al cargar saldo', err);
      }
    });
  }

  cargarResumen() {
    this.transaccionService.getAll().subscribe({
      next: (data) => {
        this.totalTransacciones = data.length;
      },
      error: (err) => {
        console.error('Error al cargar transacciones', err);
      }
    });

    this.metasAhorroService.getAll().subscribe({
      next: (data) => {
        this.totalMetas = data.length;
      },
      error: (err) => {
        console.error('Error al cargar metas', err);
      }
    });
  }

  formatMontoMensual(event: any) {
  let input = event.target.value;

  // Solo números
  input = input.replace(/\D/g, '');

  // Agregar puntos de miles
  const formatted = input.replace(/\B(?=(\d{3})+(?!\d))/g, '.');

  // Actualizar input
  event.target.value = formatted;

  // Actualizar modelo
  this.usuario.montoMensual = formatted as any;
}
  guardarCambios() {
    this.usuario.montoMensual = Number(
    String(this.usuario.montoMensual).replace(/\./g, '')
  );

  this.usuarioService.updateMiUsuario(this.usuario).subscribe({

      next: (data) => {
        this.usuario = data;
        this.mensajeExito = 'Datos actualizados correctamente';
        this.errorMensaje = null;
        this.isEditMode = false;
      },
      error: (err) => {
        console.error('Error al actualizar usuario', err);
        this.errorMensaje = err.error?.message || 'No se pudo actualizar la cuenta';
      }
    });
  }

  seleccionarFoto(event: Event) {
    const input = event.target as HTMLInputElement;
    const archivo = input.files?.[0];

    if (!archivo) {
      return;
    }

    if (!archivo.type.startsWith('image/')) {
      this.errorMensaje = 'Selecciona una imagen valida';
      return;
    }

    if (archivo.size > 1_500_000) {
      this.errorMensaje = 'La imagen debe pesar menos de 1.5 MB';
      return;
    }

    this.convertirImagenPerfil(archivo)
      .then((foto) => {
        this.fotoPerfil = foto;
        this.guardarFotoPerfil(foto);
      })
      .catch((error) => {
        console.error('Error al procesar foto de perfil', error);
        this.errorMensaje = 'No se pudo procesar la imagen';
      });
  }

  quitarFoto() {
    this.guardarFotoPerfil(null);
  }

  private guardarFotoPerfil(fotoPerfil: string | null) {
    const usuarioActualizado: Usuario = {
      ...this.usuario,
      fotoPerfil
    };

    this.usuarioService.updateMiUsuario(usuarioActualizado).subscribe({
      next: (data) => {
        this.usuario = data;
        this.fotoPerfil = data.fotoPerfil || null;
        this.mensajeExito = fotoPerfil
          ? 'Foto de perfil actualizada'
          : 'Foto de perfil eliminada';
        this.errorMensaje = null;
      },
      error: (err) => {
        console.error('Error al guardar foto de perfil', err);
        this.fotoPerfil = this.usuario.fotoPerfil || null;
        this.errorMensaje = this.obtenerMensajeError(err, 'No se pudo actualizar la foto de perfil');
      }
    });
  }

  private convertirImagenPerfil(archivo: File): Promise<string> {
    return new Promise((resolve, reject) => {
      const lector = new FileReader();

      lector.onload = () => {
        const imagen = new Image();

        imagen.onload = () => {
          const maxSize = 320;
          const escala = Math.min(maxSize / imagen.width, maxSize / imagen.height, 1);
          const canvas = document.createElement('canvas');
          canvas.width = Math.round(imagen.width * escala);
          canvas.height = Math.round(imagen.height * escala);

          const contexto = canvas.getContext('2d');
          if (!contexto) {
            reject(new Error('No se pudo preparar la imagen'));
            return;
          }

          contexto.drawImage(imagen, 0, 0, canvas.width, canvas.height);
          resolve(canvas.toDataURL('image/jpeg', 0.72));
        };

        imagen.onerror = () => reject(new Error('Imagen invalida'));
        imagen.src = lector.result as string;
      };

      lector.onerror = () => reject(new Error('No se pudo leer la imagen'));
      lector.readAsDataURL(archivo);
    });
  }

  private obtenerMensajeError(err: any, mensajePorDefecto: string): string {
    if (typeof err?.error === 'string' && err.error.trim()) {
      return err.error;
    }

    return err?.error?.message || mensajePorDefecto;
  }

  eliminarCuenta() {
    this.usuarioService.deleteMiUsuario().subscribe({
      next: () => {
        this.usuarioService.logout();
        this.router.navigate(['/usuario-login']);
      },
      error: (err) => {
        console.error('Error al eliminar cuenta', err);
        this.errorMensaje = 'No se pudo eliminar la cuenta';
      }
    });
  }

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/usuario-login']);
  }
}
