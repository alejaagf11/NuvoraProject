import { Component, OnInit } from '@angular/core';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-profile-icon',
  standalone: false,
  templateUrl: './profile-icon.component.html',
  styleUrls: ['./profile-icon.component.css']
})
export class ProfileIconComponent implements OnInit {
  fotoPerfil: string | null = null;

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.usuarioService.getMiUsuario().subscribe({
      next: (usuario) => {
        this.fotoPerfil = usuario.fotoPerfil || null;
      },
      error: (err) => {
        console.error('Error al cargar foto de perfil', err);
      }
    });
  }
}
