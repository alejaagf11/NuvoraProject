import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminLeccionesComponent } from '../admin-lecciones/admin-lecciones.component';
import { LeccionService } from '../services/leccion.service';
import { ModuloAprendizajeService } from '../services/modulo-aprendizaje.service';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-admin-lecciones-mobile',
  standalone: false,
  templateUrl: './admin-lecciones-mobile.component.html',
  styleUrls: ['./admin-lecciones-mobile.component.css']
})
export class AdminLeccionesMobileComponent extends AdminLeccionesComponent {
  constructor(
    route: ActivatedRoute,
    private mobileRouter: Router,
    moduloService: ModuloAprendizajeService,
    leccionService: LeccionService,
    private mobileUsuarioService: UsuarioService
  ) {
    super(route, mobileRouter, moduloService, leccionService, mobileUsuarioService);
  }

  override logout() {
    this.mobileUsuarioService.logout();
    this.mobileRouter.navigate(['/mobile/usuario-login']);
  }
}
