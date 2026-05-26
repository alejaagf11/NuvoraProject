import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Leccion } from '../models/leccion';
import { ProgresoLeccionUsuario } from '../models/progreso-leccion';
import { LeccionService } from '../services/leccion.service';
import { ProgresoLeccionService } from '../services/progreso-leccion.service';

@Component({
  selector: 'app-leccion-mobile',
  standalone: false,
  templateUrl: './leccion-mobile.component.html',
  styleUrls: ['./leccion-mobile.component.css'],
})
export class LeccionMobileComponent implements OnInit {
  leccionId!: number;
  leccion: Leccion | null = null;
  progresoUsuario: ProgresoLeccionUsuario[] = [];
  cargando = true;
  guardando = false;
  completada = false;
  errorMensaje: string | null = null;
  mensajeExito: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private sanitizer: DomSanitizer,
    private leccionService: LeccionService,
    private progresoLeccionService: ProgresoLeccionService
  ) {}

  ngOnInit(): void {
    this.leccionId = Number(this.route.snapshot.paramMap.get('leccionId'));

    if (!this.leccionId) {
      this.errorMensaje = 'Leccion invalida';
      this.cargando = false;
      return;
    }

    this.cargarLeccion();
  }

  cargarLeccion() {
    this.cargando = true;
    this.leccionService.getLeccionById(this.leccionId).subscribe({
      next: (leccion) => {
        this.leccion = leccion;
        this.progresoLeccionService.getMisLecciones().subscribe({
          next: (progreso) => {
            this.progresoUsuario = progreso;
            this.completada = progreso.some(
              (item) => item.leccionId === this.leccionId && item.completada
            );
            this.cargando = false;
            this.errorMensaje = null;
          },
          error: (err) => {
            console.error('Error al cargar progreso', err);
            this.cargando = false;
            this.errorMensaje = 'No se pudo cargar el progreso de la leccion';
          }
        });
      },
      error: (err) => {
        console.error('Error al cargar leccion', err);
        this.cargando = false;
        this.errorMensaje = 'No se pudo cargar la leccion';
      }
    });
  }

  completarLeccion() {
    if (this.completada || this.guardando || !this.leccionId) return;

    this.guardando = true;
    this.progresoLeccionService.completarLeccion(this.leccionId).subscribe({
      next: () => {
        this.completada = true;
        this.guardando = false;
        this.mensajeExito = 'Leccion completada correctamente';
      },
      error: (err) => {
        console.error('Error al completar leccion', err);
        this.guardando = false;
        this.errorMensaje = 'No se pudo completar la leccion';
      }
    });
  }

  volverModulo() {
    if (!this.leccion?.moduloId) {
      this.router.navigate(['/mobile/aprendizaje']);
      return;
    }

    this.router.navigate(['/mobile/aprendizaje/modulo', this.leccion.moduloId]);
  }

  obtenerTextoLeccion(contenido: string): string {
    return contenido.split('\nVIDEO:')[0]?.trim() || contenido;
  }

  obtenerVideoUrl(contenido: string): string {
    const partes = contenido.split('\nVIDEO:');
    return partes[1]?.trim() || '';
  }

  obtenerYoutubeEmbedUrl(url: string): SafeResourceUrl {
    const match = url.match(/(?:youtube\.com\/watch\?v=|youtu\.be\/)([^&]+)/);
    const videoId = match?.[1];

    return this.sanitizer.bypassSecurityTrustResourceUrl(
      `https://www.youtube.com/embed/${videoId}`
    );
  }

}
