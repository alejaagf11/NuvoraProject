import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsuarioService } from '../services/usuario.service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-usuarios-form',
  standalone: false,
  templateUrl: './usuarios-form.component.html',
  styleUrls: ['./usuarios-form.component.css']
})
export class UsuariosFormComponent implements OnInit {

  form: FormGroup;
  id: number | null = null;

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.form = this.fb.group({
      nombreUsuario: ['', [Validators.required, Validators.minLength(3)]],
      correoUsuario: ['', [Validators.required, Validators.email]],
      contrasenaUsuario: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id']; // captura usuarioId
    if (this.id) {
      this.usuarioService.getById(this.id).subscribe(data => {
        this.form.patchValue({
          nombreUsuario: data.nombreUsuario,
          correoUsuario: data.correoUsuario,
          contrasenaUsuario: data.contrasenaUsuario
        });
      });
    }
  }

  saveUsuario() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }


    const usuario = {
      nombreUsuario: this.form.value.nombreUsuario,
      correoUsuario: this.form.value.correoUsuario,
      contrasenaUsuario: this.form.value.contrasenaUsuario
    };

    if (this.id) {
      // editar
      this.usuarioService.update(this.id, usuario).subscribe({
        next: () => {
          console.log('Usuario actualizado');
          this.router.navigate(['/metas-ahorro-list']);
        },
        error: (err) => {
          console.error('Error al actualizar usuario', err);
          console.log('Detalles del error:', err.error);
          alert('Error al actualizar la cuenta: ' + (err.error?.message || err.message || 'Error desconocido'));
        }
      });
    } else {
      // crear
      this.usuarioService.create(usuario).subscribe({
        next: () => {
          console.log('Usuario creado, iniciando login automático...');

          this.usuarioService.login({
            correoUsuario: usuario.correoUsuario,
            contrasenaUsuario: usuario.contrasenaUsuario
          }).subscribe({
            next: (res) => {
              console.log('Login automático exitoso:', res);
              console.log('Token guardado después del login:', this.usuarioService.getToken());
              this.router.navigate(['/dashboard']);
            },
            error: (err) => {
              console.error('Usuario creado, pero falló el login automático', err);
              alert('La cuenta se creó, pero no se pudo iniciar sesión automáticamente.');
              this.router.navigate(['/usuario-login']);
            }
          });
        },
        error: (err) => {
          console.error('Error al crear usuario', err);
          console.log('Detalles del error:', err.error);
          alert('Error al crear la cuenta: ' + (err.error?.message || err.message || 'Error desconocido'));
        }
      });

    }
  }

}