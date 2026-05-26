import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Usuario } from '../models/usuarios';
import { MetasAhorroService } from '../services/metas-ahorro.service';
import { ProfilePhotoService } from '../services/profile-photo.service';
import { TransaccionService } from '../services/transaccion.service';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-cuenta-mobile',
  standalone: false,
  templateUrl: './cuenta-mobile.component.html',
  styleUrls: ['./cuenta-mobile.component.css'],
})
export class CuentaMobileComponent implements OnInit {
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
    private profilePhotoService: ProfilePhotoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.fotoPerfil = this.profilePhotoService.getPhoto();
    this.cargarUsuario();
    this.cargarSaldo();
    this.cargarResumen();
  }

  cargarUsuario() {
    this.usuarioService.getMiUsuario().subscribe({
      next: (data) => {
        this.usuario = data;
        this.errorMensaje = null;
      },
      error: (err) => {
        console.error('Error al cargar usuario', err);
        this.errorMensaje = 'No se pudo cargar la informacion de la cuenta';
      }
    });
  }

  cargarSaldo() {
    this.transaccionService.getSaldo().subscribe({
      next: (data) => this.saldoActual = data,
      error: (err) => console.error('Error al cargar saldo', err)
    });
  }

  cargarResumen() {
    this.transaccionService.getAll().subscribe({
      next: (data) => this.totalTransacciones = data.length,
      error: (err) => console.error('Error al cargar transacciones', err)
    });

    this.metasAhorroService.getAll().subscribe({
      next: (data) => this.totalMetas = data.length,
      error: (err) => console.error('Error al cargar metas', err)
    });
  }

  guardarCambios() {
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

    if (!archivo) return;

    if (!archivo.type.startsWith('image/')) {
      this.errorMensaje = 'Selecciona una imagen valida';
      return;
    }

    if (archivo.size > 1_500_000) {
      this.errorMensaje = 'La imagen debe pesar menos de 1.5 MB';
      return;
    }

    const lector = new FileReader();
    lector.onload = () => {
      this.fotoPerfil = lector.result as string;
      this.profilePhotoService.savePhoto(this.fotoPerfil);
      this.mensajeExito = 'Foto de perfil actualizada';
      this.errorMensaje = null;
    };
    lector.readAsDataURL(archivo);
  }

  quitarFoto() {
    this.fotoPerfil = null;
    this.profilePhotoService.removePhoto();
    this.mensajeExito = 'Foto de perfil eliminada';
    this.errorMensaje = null;
  }

  eliminarCuenta() {
    this.usuarioService.deleteMiUsuario().subscribe({
      next: () => {
        this.usuarioService.logout();
        this.router.navigate(['/mobile/usuario-login']);
      },
      error: (err) => {
        console.error('Error al eliminar cuenta', err);
        this.errorMensaje = 'No se pudo eliminar la cuenta';
      }
    });
  }

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/mobile/usuario-login']);
  }

}
