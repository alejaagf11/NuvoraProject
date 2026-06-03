import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AdminModulosComponent } from '../admin-modulos/admin-modulos.component';
import { ModuloAprendizajeService } from '../services/modulo-aprendizaje.service';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-admin-modulos-mobile',
  standalone: false,
  templateUrl: './admin-modulos-mobile.component.html',
  styleUrls: ['./admin-modulos-mobile.component.css']
})
export class AdminModulosMobileComponent extends AdminModulosComponent {
  constructor(
    moduloService: ModuloAprendizajeService,
    private mobileUsuarioService: UsuarioService,
    private mobileRouter: Router
  ) {
    super(moduloService, mobileUsuarioService, mobileRouter);
  }

  override irALecciones(moduloId?: number) {
    if (!moduloId) return;
    this.mobileRouter.navigate(['/mobile/admin/lecciones'], { queryParams: { moduloId } });
  }

  override logout() {
    this.mobileUsuarioService.logout();
    this.mobileRouter.navigate(['/mobile/usuario-login']);
  }
}
