import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Categoria } from '../models/categoria';
import { Transaccion } from '../models/transaccion';
import { CategoriaService } from '../services/categoria.service';
import { TransaccionService } from '../services/transaccion.service';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-transacciones',
  standalone: false,
  templateUrl: './transacciones.component.html',
  styleUrls: ['./transacciones.component.css']
})
export class TransaccionesComponent implements OnInit {

  transacciones: Transaccion[] = [];
  categorias: Categoria[] = [];
  categoriasFiltradas: Categoria[] = [];
  errorMensaje: string | null = null;
  mensajeExito: string | null = null;
  filtroTipo: 'INGRESO' | 'GASTO' | '' = '';
  isEditMode = false;
  transaccionEditandoId: number | null = null;

  nuevaTransaccion: Transaccion = {
    montoTransaccion: 0,
    descTransaccion: '',
    fechaTransaccion: '',
    tipo: 'INGRESO',
    categoriaId: 0
  };

  constructor(
    private transaccionService: TransaccionService,
    private categoriaService: CategoriaService,
    private route: ActivatedRoute,
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const tipo = params['tipo'];
      if (tipo === 'INGRESO' || tipo === 'GASTO') {
        this.filtroTipo = tipo;
        this.nuevaTransaccion.tipo = tipo;
      }
      this.cargarCategorias();
      this.cargarTransacciones();
    });
  }

  cargarCategorias() {
    this.categoriaService.getAll().subscribe({
      next: (data) => {
        this.categorias = data;
        this.actualizarCategoriasFiltradas();
      },
      error: (err) => {
        console.error('Error al cargar categorías', err);
        this.errorMensaje = 'No se pudieron cargar las categorías';
      }
    });
  }

  cargarTransacciones() {
    const request = this.filtroTipo
      ? this.transaccionService.getByTipo(this.filtroTipo)
      : this.transaccionService.getAll();

    request.subscribe({
      next: (data) => {
        this.transacciones = data;
        this.errorMensaje = null;
      },
      error: (err) => {
        console.error('Error al cargar transacciones', err);
        this.errorMensaje = 'No se pudieron cargar las transacciones';
      }
    });
  }

  onTipoChange() {
    this.nuevaTransaccion.categoriaId = 0;
    this.actualizarCategoriasFiltradas();
    this.cargarTransacciones();
  }
actualizarCategoriasFiltradas() {
  this.categoriasFiltradas = this.categorias.filter(
    categoria => categoria.tipoCategoria === this.nuevaTransaccion.tipo
  );
}

guardarTransaccion() {

  this.nuevaTransaccion.montoTransaccion = Number(
    String(this.nuevaTransaccion.montoTransaccion).replace(/\./g, '')
  );

  if (this.categoriasFiltradas.length === 0) {
    this.errorMensaje = `Primero debes crear una categoría de tipo ${this.nuevaTransaccion.tipo}`;
    return;
  }

  if (
    !this.nuevaTransaccion.montoTransaccion ||
    !this.nuevaTransaccion.descTransaccion ||
    !this.nuevaTransaccion.categoriaId
  ) {
    this.errorMensaje = 'Completa todos los campos obligatorios';
    return;
  }

  if (this.isEditMode && this.transaccionEditandoId) {
    this.transaccionService.update(
      this.transaccionEditandoId,
      this.nuevaTransaccion
    ).subscribe({
      next: () => {
        this.mensajeExito = 'Transacción actualizada correctamente';
        this.errorMensaje = null;
        this.cancelarEdicion();
        this.cargarTransacciones();
      },
      error: (err) => {
        console.error('Error al actualizar transacción', err);
        this.errorMensaje =
          err.error?.message || 'No se pudo actualizar la transacción';
      }
    });
    return;
  }

  this.transaccionService.create(this.nuevaTransaccion).subscribe({
    next: () => {
      this.mensajeExito = 'Transacción registrada correctamente';
      this.errorMensaje = null;
      this.resetFormulario();
      this.cargarTransacciones();
    },
    error: (err) => {
      console.error('Error al guardar transacción', err);
      this.errorMensaje =
        err.error?.message || 'No se pudo guardar la transacción';
    }
  });
}
  editarTransaccion(transaccion: Transaccion) {
    this.isEditMode = true;
    this.transaccionEditandoId = transaccion.transaccionId || null;

    this.nuevaTransaccion = {
      transaccionId: transaccion.transaccionId,
      montoTransaccion: transaccion.montoTransaccion,
      descTransaccion: transaccion.descTransaccion,
      fechaTransaccion: transaccion.fechaTransaccion,
      tipo: transaccion.tipo,
      categoriaId: transaccion.categoriaId
    };

    this.actualizarCategoriasFiltradas();
    this.errorMensaje = null;
    this.mensajeExito = null;
  }

  cancelarEdicion() {
    this.isEditMode = false;
    this.transaccionEditandoId = null;
    this.resetFormulario();
  }

  eliminarTransaccion(id: number) {
    if (!confirm('Seguro que quieres eliminar esta transaccion?')) {
      return;
    }

    this.transaccionService.delete(id).subscribe({
      next: () => {
        this.mensajeExito = 'Transacción eliminada correctamente';
        this.errorMensaje = null;
        this.cargarTransacciones();
      },
      error: (err) => {
        console.error('Error al eliminar transacción', err);
        this.errorMensaje = 'No se pudo eliminar la transacción';
      }
    });
  }

  resetFormulario() {
    this.isEditMode = false;
    this.transaccionEditandoId = null;
    this.nuevaTransaccion = {
      montoTransaccion: 0,
      descTransaccion: '',
      fechaTransaccion: '',
      tipo: this.filtroTipo || 'INGRESO',
      categoriaId: 0
    };
    this.actualizarCategoriasFiltradas();
  }

  esGasto(tipo: string): boolean {
    return tipo === 'GASTO';
  }

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/usuario-login']);
  }
}
