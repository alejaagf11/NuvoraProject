import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { PresupuestoService } from '../services/presupuesto.service';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-presupuesto-mobile',
  standalone: false,
  templateUrl: './presupuesto-mobile.component.html',
  styleUrls: ['./presupuesto-mobile.component.css'],
})
export class PresupuestoMobileComponent {
  @ViewChild('chatBox') chatBox!: ElementRef<HTMLDivElement>;

  mensajes: { rol: 'user' | 'bot'; texto: string }[] = [
    {
      rol: 'bot',
      texto: 'Hola, soy Nuvy. Cuentame tus ingresos, deudas, ahorro y gastos para ayudarte con tu presupuesto.'
    }
  ];

  mensajeChat = '';
  cargando = false;

  constructor(
    private presupuestoService: PresupuestoService,
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  private bajarChat(): void {
    setTimeout(() => {
      if (this.chatBox) {
        this.chatBox.nativeElement.scrollTop = this.chatBox.nativeElement.scrollHeight;
      }
    }, 50);
  }

  enviarMensaje() {
    if (!this.mensajeChat.trim() || this.cargando) return;

    const texto = this.mensajeChat.trim();
    this.mensajes.push({ rol: 'user', texto });
    this.mensajeChat = '';
    this.cargando = true;
    this.bajarChat();

    this.presupuestoService.chat(texto).subscribe({
      next: (respuesta) => {
        this.mensajes.push({ rol: 'bot', texto: respuesta });
        this.cargando = false;
        this.bajarChat();
      },
      error: (err) => {
        console.error('Error en chat IA', err);
        this.mensajes.push({
          rol: 'bot',
          texto: 'No pude procesar tu mensaje en este momento. Intenta de nuevo.'
        });
        this.cargando = false;
        this.bajarChat();
      }
    });
  }

  enviarEjemplo(texto: string) {
    this.mensajeChat = texto;
    this.enviarMensaje();
  }

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/mobile/usuario-login']);
  }

}
