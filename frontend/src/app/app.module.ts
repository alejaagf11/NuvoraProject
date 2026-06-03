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
import { DashboardMobileComponent } from './dashboard-mobile/dashboard-mobile.component';
import { CuentaComponent } from './cuenta/cuenta.component';
import { AdminUsuariosComponent } from './admin-usuarios/admin-usuarios.component';
import { AprendizajeComponent } from './aprendizaje/aprendizaje.component';
import { ModuloDetalleComponent } from './modulo-detalle/modulo-detalle.component';
import { LeccionComponent } from './leccion/leccion.component';
import { AdminModulosComponent } from './admin-modulos/admin-modulos.component';
import { AdminLeccionesComponent } from './admin-lecciones/admin-lecciones.component';
import { IonicModule } from '@ionic/angular';
import { UsuarioLoginMobileComponent } from './usuario-login-mobile/usuario-login-mobile.component';
import { UsuariosFormMobileComponent } from './usuarios-form-mobile/usuarios-form-mobile.component';
import { ProfileIconComponent } from './shared/profile-icon/profile-icon.component';
import { CuentaMobileComponent } from './cuenta-mobile/cuenta-mobile.component';
import { PresupuestoMobileComponent } from './presupuesto-mobile/presupuesto-mobile.component';
import { CategoriasMobileComponent } from './categorias-mobile/categorias-mobile.component';
import { AprendizajeMobileComponent } from './aprendizaje-mobile/aprendizaje-mobile.component';
import { LeccionMobileComponent } from './leccion-mobile/leccion-mobile.component';
import { TransaccionesMobileComponent } from './transacciones-mobile/transacciones-mobile.component';
import { ModuloDetalleMobileComponent } from './modulo-detalle-mobile/modulo-detalle-mobile.component';
import { MetasAhorroListMobileComponent } from './metas-ahorro-list-mobile/metas-ahorro-list-mobile.component';
import { MetasAhorroFormMobileComponent } from './metas-ahorro-form-mobile/metas-ahorro-form-mobile.component';
import { AdminUsuariosMobileComponent } from './admin-usuarios-mobile/admin-usuarios-mobile.component';
import { AdminModulosMobileComponent } from './admin-modulos-mobile/admin-modulos-mobile.component';
import { AdminLeccionesMobileComponent } from './admin-lecciones-mobile/admin-lecciones-mobile.component';



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
    DashboardMobileComponent,
    CuentaComponent,
    AdminUsuariosComponent,
    AprendizajeComponent,
    ModuloDetalleComponent,
    LeccionComponent,
    AdminModulosComponent,
    AdminLeccionesComponent,
    UsuarioLoginMobileComponent,
    UsuariosFormMobileComponent,
    ProfileIconComponent,
    CuentaMobileComponent,
    PresupuestoMobileComponent,
    CategoriasMobileComponent,
    AprendizajeMobileComponent,
    LeccionMobileComponent,
    TransaccionesMobileComponent,
    ModuloDetalleMobileComponent,
    MetasAhorroListMobileComponent,
    MetasAhorroFormMobileComponent,
    AdminUsuariosMobileComponent,
    AdminModulosMobileComponent,
    AdminLeccionesMobileComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule, 
    HttpClientModule,
    ReactiveFormsModule,
    IonicModule.forRoot({})
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
