import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UsuariosListComponent } from './usuarios-list/usuarios-list.component';
import { UsuariosFormComponent } from './usuarios-form/usuarios-form.component';
import { MetasAhorroListComponent } from './metas-ahorro-list/metas-ahorro-list.component';
import { MetasAhorroFormComponent } from './metas-ahorro-form/metas-ahorro-form.component';
import { CategoriasComponent } from './categorias/categorias.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { UsuarioLoginComponent } from './usuario-login/usuario-login.component';
import { AuthInterceptor } from './services/auth.interceptor';
import { PresupuestoComponent } from './presupuesto/presupuesto.component';
import { TransaccionesComponent } from './transacciones/transacciones.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { CuentaComponent } from './cuenta/cuenta.component';
import { AdminUsuariosComponent } from './admin-usuarios/admin-usuarios.component';




@NgModule({
  declarations: [
    AppComponent,
    UsuariosListComponent,
    UsuariosFormComponent,
    MetasAhorroListComponent,
    MetasAhorroFormComponent,
    CategoriasComponent,
    UsuarioLoginComponent,
    PresupuestoComponent,
    TransaccionesComponent,
    DashboardComponent,
    CuentaComponent,
    AdminUsuariosComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule, 
    HttpClientModule,
    ReactiveFormsModule
    
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
