import { Component, OnInit } from '@angular/core';
import { UsuarioService } from '../services/usuario.service';
import { Usuario } from '../models/usuarios';

@Component({
  selector: 'app-profile-icon-mobile',
  standalone: false,
  templateUrl: './profile-icon-mobile.component.html',
  styleUrls: ['./profile-icon-mobile.component.css']
})
export class ProfileIconMobileComponent implements OnInit {

  fotoPerfil: string | null = null;
  inicial: string = 'U';

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.cargarUsuario();

    window.addEventListener('storage', () => {
      this.cargarUsuario();
    });

    window.addEventListener('nuvora-user-updated', () => {
      this.cargarUsuario();
    });
  }
  private guardarUsuarioLocal(usuario: Usuario) {
  localStorage.setItem('usuario', JSON.stringify(usuario));
}
  private cargarUsuario() {
    const usuarioLocal: Usuario | null = this.usuarioService.getUsuario();

    if (usuarioLocal) {
      this.fotoPerfil = usuarioLocal.fotoPerfil || null;
      this.inicial = usuarioLocal.nombreUsuario?.charAt(0).toUpperCase() || 'U';
    } else {
      this.usuarioService.getMiUsuario().subscribe({
        next: (usuario: Usuario) => {
          this.fotoPerfil = usuario.fotoPerfil || null;
          this.inicial = usuario.nombreUsuario?.charAt(0).toUpperCase() || 'U';
        },
        error: (err) => {
          console.error('Error al cargar usuario móvil', err);
          this.fotoPerfil = null;
          this.inicial = 'U';
        }
      });
    }
  }

  
  eliminarFoto(): void {
  const usuario = this.usuarioService.getUsuario();

  if (!usuario) return;

  const usuarioActualizado: Usuario = {
    ...usuario,
    fotoPerfil: null
  };

  // 1. Guardar en backend
  this.usuarioService.updateMiUsuario(usuarioActualizado).subscribe({
    next: (data) => {

      // 2. Actualizar UI
      this.fotoPerfil = null;
      this.inicial = data.nombreUsuario?.charAt(0).toUpperCase() || 'U';

      // 3. Actualizar localStorage
      localStorage.setItem('usuario', JSON.stringify(data));

      // 4. (opcional) recargar por si otro dato cambia
      this.usuarioService.getMiUsuario().subscribe({
        next: (fresh) => {
          this.fotoPerfil = fresh.fotoPerfil || null;
          this.inicial = fresh.nombreUsuario?.charAt(0).toUpperCase() || 'U';

          localStorage.setItem('usuario', JSON.stringify(fresh));
        }
      });

    },
    error: (err) => {
      console.error('Error eliminando foto', err);
    }
  });
}
}
