import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ModuloAprendizaje } from '../models/aprendizaje';
import { Leccion } from '../models/leccion';
import { ProgresoLeccionUsuario } from '../models/progreso-leccion';
import { LeccionService } from '../services/leccion.service';
import { ModuloAprendizajeService } from '../services/modulo-aprendizaje.service';
import { ProgresoLeccionService } from '../services/progreso-leccion.service';

@Component({
  selector: 'app-modulo-detalle-mobile',
  standalone: false,
  templateUrl: './modulo-detalle-mobile.component.html',
  styleUrls: ['./modulo-detalle-mobile.component.css']
})
export class ModuloDetalleMobileComponent implements OnInit {
  moduloId!: number;
  modulo: ModuloAprendizaje | null = null;
  lecciones: Leccion[] = [];
  progresoUsuario: ProgresoLeccionUsuario[] = [];
  cargando = true;
  errorMensaje: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private moduloAprendizajeService: ModuloAprendizajeService,
    private leccionService: LeccionService,
    private progresoLeccionService: ProgresoLeccionService
  ) {}

  ngOnInit(): void {
    this.moduloId = Number(this.route.snapshot.paramMap.get('moduloId'));

    if (!this.moduloId) {
      this.errorMensaje = 'Modulo invalido';
      this.cargando = false;
      return;
    }

    this.cargarDetalle();
  }

  cargarDetalle() {
    this.cargando = true;

    this.moduloAprendizajeService.getModuloById(this.moduloId).subscribe({
      next: (modulo) => {
        this.modulo = modulo;
        this.leccionService.getLeccionesByModulo(this.moduloId).subscribe({
          next: (lecciones) => {
            this.lecciones = lecciones;
            this.progresoLeccionService.getMisLecciones().subscribe({
              next: (progreso) => {
                this.progresoUsuario = progreso;
                this.cargando = false;
                this.errorMensaje = null;
              },
              error: (err) => {
                console.error('Error al cargar progreso', err);
                this.cargando = false;
                this.errorMensaje = 'No se pudo cargar el progreso del usuario';
              }
            });
          },
          error: (err) => {
            console.error('Error al cargar lecciones', err);
            this.cargando = false;
            this.errorMensaje = 'No se pudieron cargar las lecciones';
          }
        });
      },
      error: (err) => {
        console.error('Error al cargar modulo', err);
        this.cargando = false;
        this.errorMensaje = 'No se pudo cargar el modulo';
      }
    });
  }

  estaCompletada(leccionId?: number): boolean {
    if (!leccionId) return false;
    return this.progresoUsuario.some(
      (progreso) => progreso.leccionId === leccionId && progreso.completada
    );
  }

  abrirLeccion(leccionId?: number) {
    if (!leccionId) return;
    this.router.navigate(['/mobile/aprendizaje/leccion', leccionId]);
  }

  obtenerVideoUrl(contenido: string): string {
    const partes = contenido.split('\nVIDEO:');
    return partes[1]?.trim() || '';
  }

  obtenerThumbnailYoutube(url: string): string {
    const match = url.match(/(?:youtube\.com\/watch\?v=|youtu\.be\/)([^&]+)/);
    const videoId = match?.[1];
    return videoId ? `https://img.youtube.com/vi/${videoId}/hqdefault.jpg` : '';
  }
}
