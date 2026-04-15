import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { PresupuestoService } from '../services/presupuesto.service';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-presupuesto',
  standalone: false,
  templateUrl: './presupuesto.component.html',
  styleUrls: ['./presupuesto.component.css']
})
export class PresupuestoComponent {

  mensajes: { rol: 'user' | 'bot'; texto: string }[] = [
    {
      rol: 'bot',
      texto: 'Hola soy tu Nuvy, tu asistente financiera de confianza 😊'
      }
  ];

  mensajeChat = '';
  cargando = false;

  constructor(
    private presupuestoService: PresupuestoService,
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  enviarMensaje() {
    if (!this.mensajeChat.trim() || this.cargando) return;

    const texto = this.mensajeChat.trim();
    this.mensajes.push({ rol: 'user', texto });
    this.mensajeChat = '';
    this.cargando = true;

    this.presupuestoService.chat(texto).subscribe({
      next: (respuesta) => {
        this.mensajes.push({ rol: 'bot', texto: respuesta });
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error en chat IA', err);
        this.mensajes.push({
          rol: 'bot',
          texto: 'No pude procesar tu mensaje en este momento. Intenta de nuevo.'
        });
        this.cargando = false;
      }
    });
  }

  enviarEjemplo(texto: string) {
    this.mensajeChat = texto;
    this.enviarMensaje();
  }

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/usuario-login']);
  }
}
