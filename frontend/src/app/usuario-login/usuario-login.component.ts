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

    if (this.usuarioService.getToken()) {
      this.usuarioService.getMiUsuario().subscribe({
        next: (usuarioActual) => {
          const correoActual = (usuarioActual.correoUsuario || '').toLowerCase();
          const correoIntento = (this.loginForm.value.correoUsuario || '').toLowerCase();

          if (correoActual && correoActual !== correoIntento) {
            this.loginError = `Ya hay una sesion abierta, cierrala antes de entrar con otra cuenta.`;
            return;
          }

          this.ejecutarLogin();
        },
        error: () => {
          this.ejecutarLogin();
        }
      });
      return;
    }

    this.ejecutarLogin();
  }

  private ejecutarLogin() {
    this.usuarioService.login(this.loginForm.value).subscribe({
      next: () => {
        this.loginError = null;

        this.usuarioService.getMiUsuario().subscribe({
          next: (usuario) => {
            if (usuario.rolUsuario === 'ADMIN') {
              this.router.navigate(['/admin/usuarios']);
            } else {
              this.router.navigate(['/dashboard']);
            }
          },
          error: (err) => {
            console.error('Error al obtener usuario autenticado:', err);
            this.loginError = 'No se pudo obtener la informacion del usuario';
          }
        });
      },
      error: (err) => {
        console.error('Error de login:', err);
        this.loginError = 'Correo o contrasena incorrectos';
      }
    });
  }
}
