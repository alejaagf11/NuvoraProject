import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UsuariosListComponent } from './usuarios-list/usuarios-list.component';
import { UsuariosFormComponent } from './usuarios-form/usuarios-form.component';
import { MetasAhorroFormComponent } from './metas-ahorro-form/metas-ahorro-form.component';
import { MetasAhorroListComponent } from './metas-ahorro-list/metas-ahorro-list.component';
import { UsuarioLoginComponent } from './usuario-login/usuario-login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { TransaccionesComponent } from './transacciones/transacciones.component';
import { CategoriasComponent } from './categorias/categorias.component';
import { PresupuestoComponent } from './presupuesto/presupuesto.component';
import { CuentaComponent } from './cuenta/cuenta.component';


const routes: Routes = [
  { path: 'usuario-login', component: UsuarioLoginComponent },
  { path: 'usuario-form', component: UsuariosFormComponent },

  { path: 'dashboard', component: DashboardComponent },
  { path: 'metas-ahorro-list', component: MetasAhorroListComponent },
  { path: 'metas-ahorro-form', component: MetasAhorroFormComponent },
  { path: 'metas-ahorro-form/:id', component: MetasAhorroFormComponent },
  { path: 'transacciones', component: TransaccionesComponent },
  { path: 'categorias', component: CategoriasComponent },
  { path: 'presupuesto', component: PresupuestoComponent },
  { path: 'cuenta', component: CuentaComponent },

  { path: 'usuario-list', component: UsuariosListComponent },

  { path: '', redirectTo: '/usuario-login', pathMatch: 'full' },
  { path: '**', redirectTo: '/usuario-login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
