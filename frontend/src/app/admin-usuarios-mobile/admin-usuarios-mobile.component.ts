import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AdminUsuariosComponent } from '../admin-usuarios/admin-usuarios.component';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-admin-usuarios-mobile',
  standalone: false,
  templateUrl: './admin-usuarios-mobile.component.html',
  styleUrls: ['./admin-usuarios-mobile.component.css']
})
export class AdminUsuariosMobileComponent extends AdminUsuariosComponent {
  constructor(
    private mobileUsuarioService: UsuarioService,
    private mobileRouter: Router
  ) {
    super(mobileUsuarioService, mobileRouter);
  }

  override logout() {
    this.mobileUsuarioService.logout();
    this.mobileRouter.navigate(['/mobile/usuario-login']);
  }
}
