import { Component, OnInit } from '@angular/core';
import { Usuario } from '../models/usuarios';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-usuarios-list',
  standalone: false,
  templateUrl: './usuarios-list.component.html',
  styleUrls: ['./usuarios-list.component.css']
})
export class UsuariosListComponent implements OnInit {

  usuarios: Usuario[] = [];

  constructor(private usuarioService: UsuarioService) { }

  ngOnInit(): void {
    this.loadUsuarios();
  }

  loadUsuarios() {
    this.usuarioService.getAll().subscribe(data => this.usuarios = data);
  }

  deleteUsuario(id?: number) {
    if (!id) return;
    if (confirm("¿Seguro que quieres eliminar este usuario?")) {
      this.usuarioService.delete(id).subscribe(() => this.loadUsuarios());
    }
  }
}