import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UsuariosListComponent } from './usuarios-list/usuarios-list.component';
import { UsuariosFormComponent } from './usuarios-form/usuarios-form.component';
import { MetasAhorroFormComponent } from './metas-ahorro-form/metas-ahorro-form.component';
import { MetasAhorroListComponent } from './metas-ahorro-list/metas-ahorro-list.component';
import { UsuarioLoginComponent } from './usuario-login/usuario-login.component';
import { UsuarioLoginMobileComponent } from './usuario-login-mobile/usuario-login-mobile.component';
import { UsuariosFormMobileComponent } from './usuarios-form-mobile/usuarios-form-mobile.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { DashboardMobileComponent } from './dashboard-mobile/dashboard-mobile.component';
import { TransaccionesComponent } from './transacciones/transacciones.component';
import { TransaccionesMobileComponent } from './transacciones-mobile/transacciones-mobile.component';
import { CategoriasComponent } from './categorias/categorias.component';
import { CategoriasMobileComponent } from './categorias-mobile/categorias-mobile.component';
import { PresupuestoComponent } from './presupuesto/presupuesto.component';
import { PresupuestoMobileComponent } from './presupuesto-mobile/presupuesto-mobile.component';
import { CuentaComponent } from './cuenta/cuenta.component';
import { CuentaMobileComponent } from './cuenta-mobile/cuenta-mobile.component';
import { AdminUsuariosComponent } from './admin-usuarios/admin-usuarios.component';
import { AprendizajeComponent } from './aprendizaje/aprendizaje.component';
import { AprendizajeMobileComponent } from './aprendizaje-mobile/aprendizaje-mobile.component';
import { ModuloDetalleComponent } from './modulo-detalle/modulo-detalle.component';
import { ModuloDetalleMobileComponent } from './modulo-detalle-mobile/modulo-detalle-mobile.component';
import { LeccionComponent } from './leccion/leccion.component';
import { LeccionMobileComponent } from './leccion-mobile/leccion-mobile.component';
import { AdminModulosComponent } from './admin-modulos/admin-modulos.component';
import { AdminLeccionesComponent } from './admin-lecciones/admin-lecciones.component';
import { MetasAhorroListMobileComponent } from './metas-ahorro-list-mobile/metas-ahorro-list-mobile.component';
import { MetasAhorroFormMobileComponent } from './metas-ahorro-form-mobile/metas-ahorro-form-mobile.component';


const routes: Routes = [
  { path: 'usuario-login', component: UsuarioLoginComponent },
  { path: 'mobile/usuario-login', component: UsuarioLoginMobileComponent },
  { path: 'mobile/usuario-form', component: UsuariosFormMobileComponent },
  { path: 'usuario-form', component: UsuariosFormComponent },

  { path: 'dashboard', component: DashboardComponent },
  { path: 'mobile/dashboard', component: DashboardMobileComponent },
  { path: 'mobile/transacciones', component: TransaccionesMobileComponent },
  { path: 'mobile/metas-ahorro-list', component: MetasAhorroListMobileComponent },
  { path: 'mobile/metas-ahorro-form', component: MetasAhorroFormMobileComponent },
  { path: 'mobile/metas-ahorro-form/:id', component: MetasAhorroFormMobileComponent },
  { path: 'mobile/categorias', component: CategoriasMobileComponent },
  { path: 'mobile/presupuesto', component: PresupuestoMobileComponent },
  { path: 'mobile/cuenta', component: CuentaMobileComponent },
  { path: 'mobile/aprendizaje', component: AprendizajeMobileComponent },
  { path: 'mobile/aprendizaje/modulo/:moduloId', component: ModuloDetalleMobileComponent },
  { path: 'mobile/aprendizaje/leccion/:leccionId', component: LeccionMobileComponent },
  { path: 'metas-ahorro-list', component: MetasAhorroListComponent },
  { path: 'metas-ahorro-form', component: MetasAhorroFormComponent },
  { path: 'metas-ahorro-form/:id', component: MetasAhorroFormComponent },
  { path: 'transacciones', component: TransaccionesComponent },
  { path: 'categorias', component: CategoriasComponent },
  { path: 'presupuesto', component: PresupuestoComponent },
  { path: 'cuenta', component: CuentaComponent },
  { path: 'admin/usuarios', component: AdminUsuariosComponent },
  { path: 'aprendizaje', component: AprendizajeComponent },
  { path: 'aprendizaje/modulo/:moduloId', component: ModuloDetalleComponent },
  { path: 'aprendizaje/leccion/:leccionId', component: LeccionComponent },
  { path: 'admin/modulos', component: AdminModulosComponent },
  { path: 'admin/lecciones', component: AdminLeccionesComponent },
  { path: 'usuario-list', component: UsuariosListComponent },
  

  { path: '', redirectTo: '/usuario-login', pathMatch: 'full' },
  { path: '**', redirectTo: '/usuario-login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
