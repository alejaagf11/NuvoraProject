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

    console.log('Enviando login con:', this.loginForm.value);

    this.usuarioService.login(this.loginForm.value).subscribe({
      next: (res) => {
        console.log('Respuesta del login:', res);
        console.log('Token guardado:', this.usuarioService.getToken());
        this.loginError = null;
        
        // Redirige después de login
        setTimeout(() => {
          this.router.navigate(['/dashboard']);
        }, 500);
      },
      error: (err) => {
        console.error('Error de login:', err);
        console.error('Status:', err.status);
        console.error('Mensaje:', err.error?.message || err.message);
        this.loginError = 'Correo o contraseña incorrectos';
      }
    });
  }
}
