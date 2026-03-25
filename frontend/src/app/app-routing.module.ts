import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UsuariosListComponent } from './usuarios-list/usuarios-list.component';
import { UsuariosFormComponent } from './usuarios-form/usuarios-form.component';
import { MetasAhorroFormComponent } from './metas-ahorro-form/metas-ahorro-form.component';
import { MetasAhorroListComponent } from './metas-ahorro-list/metas-ahorro-list.component';
import { UsuarioLoginComponent } from './usuario-login/usuario-login.component';

const routes: Routes = [
  { path: 'usuario-list', component: UsuariosListComponent },
  { path: 'usuario-form', component: UsuariosFormComponent },
   {path: 'usuario-login', component: UsuarioLoginComponent},
  { path: 'metas-ahorro-list', component: MetasAhorroListComponent },
  { path: 'metas-ahorro-form', component: MetasAhorroFormComponent },
  { path: '', redirectTo: '/usuario-list', pathMatch: 'full' } // redirige al abrir /
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }