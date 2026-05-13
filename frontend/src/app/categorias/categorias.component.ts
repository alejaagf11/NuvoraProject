import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Categoria } from '../models/categoria';
import { CategoriaService } from '../services/categoria.service';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-categorias',
  standalone: false,
  templateUrl: './categorias.component.html',
  styleUrls: ['./categorias.component.css']
})
export class CategoriasComponent implements OnInit {

  categorias: Categoria[] = [];
  errorMensaje: string | null = null;
  mensajeExito: string | null = null;
  isEditMode = false;
  categoriaEditandoId: number | null = null;

  nuevaCategoria: Categoria = {
    nombreCategoria: '',
    tipoCategoria: 'GASTO'
  };

  constructor(
    private categoriaService: CategoriaService,
    private usuarioService: UsuarioService,
    private router: Router
  ) {}
 ngOnInit(): void {
    this.cargarCategorias();
  }

  cargarCategorias() {
    this.categoriaService.getAll().subscribe({
      next: (data) => {
        this.categorias = data;
        this.errorMensaje = null;
      },
      error: (err) => {
        console.error('Error al cargar categorías', err);
        this.errorMensaje = 'No se pudieron cargar las categorías';
      }
    });
  }

  guardarCategoria() {
    if (!this.nuevaCategoria.nombreCategoria.trim()) {
      this.errorMensaje = 'El nombre de la categoría es obligatorio';
      return;
    }

    if (this.isEditMode && this.categoriaEditandoId) {
      this.categoriaService.update(this.categoriaEditandoId, this.nuevaCategoria).subscribe({
        next: () => {
          this.mensajeExito = 'Categoría actualizada correctamente';
          this.errorMensaje = null;
          this.resetFormulario();
          this.cargarCategorias();
        },
        error: (err) => {
          console.error('Error al actualizar categoría', err);
          this.errorMensaje = err.error?.message || 'No se pudo actualizar la categoría';
        }
      });
      return;
    }

    this.categoriaService.create(this.nuevaCategoria).subscribe({
      next: () => {
        this.mensajeExito = 'Categoría creada correctamente';
        this.errorMensaje = null;
        this.resetFormulario();
        this.cargarCategorias();
      },
      error: (err) => {
        console.error('Error al crear categoría', err);
        this.errorMensaje = err.error?.message || 'No se pudo crear la categoría';
      }
    });
  }

  eliminarCategoria(id: number) {
    if (!confirm('Seguro que quieres eliminar esta categoria?')) {
      return;
    }

    this.categoriaService.delete(id).subscribe({
      next: () => {
        console.log('Categoría eliminada');
        this.mensajeExito = 'Categoría eliminada correctamente';
        this.cargarCategorias();
      },
      error: (err) => {
        console.error('Error al eliminar categoría:', err);
        this.errorMensaje = 'Error al eliminar: ' + (err.error?.message || err.message || err.status);
      }
    });
  }

  editarCategoria(categoria: Categoria) {
    this.isEditMode = true;
    this.categoriaEditandoId = categoria.categoriaId || null;
    this.nuevaCategoria = {
      nombreCategoria: categoria.nombreCategoria,
      tipoCategoria: categoria.tipoCategoria
    };
    this.errorMensaje = null;
    this.mensajeExito = null;
  }

  cancelarEdicion() {
    this.resetFormulario();
  }

  
  

  resetFormulario() {
    this.isEditMode = false;
    this.categoriaEditandoId = null;
    this.nuevaCategoria = {
      nombreCategoria: '',
      tipoCategoria: 'GASTO'
    };
  }

  esIngreso(tipo: string): boolean {
    return tipo === 'INGRESO';
  }

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/usuario-login']);
  }
}
  



 
