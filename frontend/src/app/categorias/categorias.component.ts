import { Component, OnInit } from '@angular/core';
import { Categoria } from '../models/categoria';
import { CategoriaService } from '../services/categoria.service';

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

  constructor(private categoriaService: CategoriaService) {}

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

  eliminarCategoria(id: number) {
    this.categoriaService.delete(id).subscribe({
      next: () => {
        this.mensajeExito = 'Categoría eliminada correctamente';
        this.errorMensaje = null;
        this.cargarCategorias();
      },
      error: (err) => {
        console.error('Error al eliminar categoría', err);
        this.errorMensaje =
          err.error?.message ||
          'No puedes eliminar esta categoría porque ya tiene transacciones asociadas';
      }
    });
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
}
