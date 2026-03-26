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
  cargando: boolean = true;
  error: string = '';

  constructor(private usuarioService: UsuarioService) { }

  ngOnInit(): void {
    this.loadUsuarios();
  }

  loadUsuarios() {
    this.cargando = true;
    this.error = '';
    this.usuarioService.getAll().subscribe({
      next: (data) => {
        console.log('Usuarios cargados:', data);
        this.usuarios = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al cargar usuarios:', err);
        this.error = 'Error al cargar los usuarios del servidor';
        this.cargando = false;
      }
    });
  }

  deleteUsuario(id?: number) {
    if (!id) return;
    if (confirm("¿Seguro que quieres eliminar este usuario?")) {
      this.usuarioService.delete(id).subscribe({
        next: () => {
          console.log('Usuario eliminado');
          this.loadUsuarios();
        },
        error: (err) => {
          console.error('Error al eliminar usuario:', err);
          alert('Error al eliminar el usuario');
        }
      });
    }
  }
}