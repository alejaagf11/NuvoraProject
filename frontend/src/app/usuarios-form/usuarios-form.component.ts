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
      contrasenaUsuario: ['', [Validators.required, Validators.minLength(6)]],
      rolUsuario: ['USER'] // siempre inicializado
    });
  }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id']; // captura usuarioId
    if (this.id) {
      this.usuarioService.getById(this.id).subscribe(data => {
        this.form.patchValue({
          nombreUsuario: data.nombreUsuario,
          correoUsuario: data.correoUsuario,
          contrasenaUsuario: data.contrasenaUsuario,
          rolUsuario: data.rolUsuario || 'USER'
        });
      });
    }
  }

  saveUsuario() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const usuario = this.form.value;

    if (this.id) {
      // editar
      this.usuarioService.update(this.id, usuario).subscribe(() => {
        this.router.navigate(['/usuario-list']);
      });
    } else {
      // crear
      this.usuarioService.create(usuario).subscribe(() => {
        this.router.navigate(['/usuario-list']);
      });
    }
  }

}