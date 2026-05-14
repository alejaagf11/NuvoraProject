import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-usuario-login-mobile',
  standalone: false,
  templateUrl: './usuario-login-mobile.component.html',
  styleUrls: ['./usuario-login-mobile.component.css']
})
export class UsuarioLoginMobileComponent {
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

    this.usuarioService.login(this.loginForm.value).subscribe({
      next: () => {
        this.loginError = null;

        this.usuarioService.getMiUsuario().subscribe({
          next: (usuario) => {
            if (usuario.rolUsuario === 'ADMIN') {
              this.router.navigate(['/admin/usuarios']);
            } else {
              this.router.navigate(['/mobile/dashboard']);
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
