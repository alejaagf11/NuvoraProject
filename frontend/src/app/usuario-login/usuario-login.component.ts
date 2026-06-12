import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-usuario-login',
  standalone: false,
  templateUrl: './usuario-login.component.html',
  styleUrls: ['./usuario-login.component.css']
})
export class UsuarioLoginComponent {
  loginForm: FormGroup;
  loginError: string | null = null;

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      correoUsuario: ['', [Validators.required, Validators.email]],
      contrasenaUsuario: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.ejecutarLogin();
  }

 private ejecutarLogin() {
  this.usuarioService.login(this.loginForm.value).subscribe({
    next: (response: any) => {
      // Guardar token siempre
      if (response.token && this.usuarioService.isBrowser()) {
        localStorage.setItem('authToken', response.token);
      }

      // Traer usuario y guardarlo
      this.usuarioService.getMiUsuario().subscribe({
        next: (usuario) => {
          if (this.usuarioService.isBrowser()) {
            localStorage.setItem('usuario', JSON.stringify(usuario));
          }

          if (usuario.rolUsuario === 'ADMIN') {
            this.router.navigate(['/admin/usuarios']);
          } else {
            this.router.navigate(['/dashboard']);
          }
        },
        error: (err) => {
          console.error('Error al obtener usuario autenticado:', err);
          this.loginError = 'No se pudo obtener la información del usuario';
        }
      });
    },
    error: (err) => {
      console.error('Error de login:', err);
      this.loginError = this.obtenerMensajeError(err, 'Correo o contraseña incorrectos');
    }
  });
}

  private obtenerMensajeError(err: any, mensajePorDefecto: string): string {
    if (typeof err?.error === 'string' && err.error.trim()) {
      return err.error;
    }

    return err?.error?.message || mensajePorDefecto;
  }
}