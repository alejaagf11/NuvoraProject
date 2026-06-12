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
  inicial: string = 'U';

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
  this.loadUser();

  // 👇 esto es clave en apps Angular
  window.addEventListener('storage', () => {
    this.loadUser();
  });
}

private loadUser() {
  const usuario = this.usuarioService.getUsuario();

  if (usuario) {
    this.fotoPerfil = usuario.fotoPerfil || null;
    this.inicial = usuario.nombreUsuario?.charAt(0).toUpperCase() || 'U';
  }
}
}