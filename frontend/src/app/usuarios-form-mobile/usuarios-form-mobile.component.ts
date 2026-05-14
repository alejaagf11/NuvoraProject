import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-usuarios-form-mobile',
  standalone: false,
  templateUrl: './usuarios-form-mobile.component.html',
  styleUrls: ['./usuarios-form-mobile.component.css']
})
export class UsuariosFormMobileComponent {
  form: FormGroup;
  errorMensaje: string | null = null;
  cargando = false;

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private router: Router
  ) {
    this.form = this.fb.group({
      nombreUsuario: ['', [Validators.required, Validators.minLength(3)]],
      correoUsuario: ['', [Validators.required, Validators.email]],
      contrasenaUsuario: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  saveUsuario() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.cargando = true;
    this.errorMensaje = null;

    const usuario = {
      nombreUsuario: this.form.value.nombreUsuario,
      correoUsuario: this.form.value.correoUsuario,
      contrasenaUsuario: this.form.value.contrasenaUsuario
    };

    this.usuarioService.create(usuario).subscribe({
      next: () => {
        this.usuarioService.login({
          correoUsuario: usuario.correoUsuario,
          contrasenaUsuario: usuario.contrasenaUsuario
        }).subscribe({
          next: () => {
            this.cargando = false;
            this.router.navigate(['/mobile/dashboard']);
          },
          error: (err) => {
            console.error('Usuario creado, pero fallo el login automatico', err);
            this.cargando = false;
            this.router.navigate(['/mobile/usuario-login']);
          }
        });
      },
      error: (err) => {
        console.error('Error al crear usuario', err);
        this.cargando = false;
        this.errorMensaje = err.error?.message || err.message || 'No se pudo crear la cuenta';
      }
    });
  }
}
