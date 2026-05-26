import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Categoria } from '../models/categoria';
import { CategoriaService } from '../services/categoria.service';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-categorias-mobile',
  standalone: false,
  templateUrl: './categorias-mobile.component.html',
  styleUrls: ['./categorias-mobile.component.css'],
})
export class CategoriasMobileComponent implements OnInit {
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

  cargarCategorias(): void {
    this.categoriaService.getAll().subscribe({
      next: (data) => {
        this.categorias = data;
        this.errorMensaje = null;
      },
      error: (err) => {
        console.error('Error al cargar categorias', err);
        this.errorMensaje = 'No se pudieron cargar las categorias';
      }
    });
  }

  guardarCategoria(): void {
    if (!this.nuevaCategoria.nombreCategoria.trim()) {
      this.errorMensaje = 'El nombre de la categoria es obligatorio';
      return;
    }

    const request = this.isEditMode && this.categoriaEditandoId
      ? this.categoriaService.update(this.categoriaEditandoId, this.nuevaCategoria)
      : this.categoriaService.create(this.nuevaCategoria);

    request.subscribe({
      next: () => {
        this.mensajeExito = this.isEditMode
          ? 'Categoria actualizada correctamente'
          : 'Categoria creada correctamente';
        this.errorMensaje = null;
        this.resetFormulario();
        this.cargarCategorias();
      },
      error: (err) => {
        console.error('Error al guardar categoria', err);
        this.errorMensaje = err.error?.message || 'No se pudo guardar la categoria';
      }
    });
  }

  editarCategoria(categoria: Categoria): void {
    this.isEditMode = true;
    this.categoriaEditandoId = categoria.categoriaId || null;
    this.nuevaCategoria = {
      nombreCategoria: categoria.nombreCategoria,
      tipoCategoria: categoria.tipoCategoria
    };
    this.errorMensaje = null;
    this.mensajeExito = null;
  }

  eliminarCategoria(id?: number): void {
    if (!id || !confirm('Seguro que quieres eliminar esta categoria?')) {
      return;
    }

    this.categoriaService.delete(id).subscribe({
      next: () => {
        this.mensajeExito = 'Categoria eliminada correctamente';
        this.cargarCategorias();
      },
      error: (err) => {
        console.error('Error al eliminar categoria', err);
        this.errorMensaje = 'No se pudo eliminar la categoria';
      }
    });
  }

  resetFormulario(): void {
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

  logout(): void {
    this.usuarioService.logout();
    this.router.navigate(['/mobile/usuario-login']);
  }

}
