import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsuarioService } from '../services/usuario.service';
import { Router } from '@angular/router';

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

    this.usuarioService.login(this.loginForm.value).subscribe({
      next: (res) => {
        console.log('Usuario logueado:', res);
        this.loginError = null;

        // Aquí es donde va el token

        this.router.navigate(['/usuarios-list']); // redirige después de login
      },
      error: (err) => {
        console.error('Error de login', err);
        this.loginError = 'Correo o contraseña incorrectos';
      }
    });
  }
}
