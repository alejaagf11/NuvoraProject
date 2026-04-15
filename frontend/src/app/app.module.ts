import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration, withEventReplay } from '@angular/platform-browser';
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
    CuentaComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule, 
    HttpClientModule,
    ReactiveFormsModule
    
  ],
  providers: [
    provideClientHydration(withEventReplay()),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
