import { Component, ElementRef, ViewChild } from '@angular/core';
import { PresupuestoService } from '../services/presupuesto.service';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-presupuesto-mobile',
  standalone: false,
  templateUrl: './presupuesto-mobile.component.html',
  styleUrls: ['./presupuesto-mobile.component.css']
})
export class PresupuestoMobileComponent {
  fotoPerfil: string | null = null;
  inicial = 'U';

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
    private usuarioService: UsuarioService
  ) { }

  ngOnInit(): void {
    this.usuarioService.getMiUsuario().subscribe({
      next: (usuario) => {
        this.fotoPerfil = usuario.fotoPerfil || null;
        this.inicial = usuario.nombreUsuario?.charAt(0).toUpperCase() || 'U';
      }
    });
  }
  @ViewChild('chatBox') chatBox!: ElementRef<HTMLDivElement>;

  private bajarChat(): void {
    setTimeout(() => {
      if (this.chatBox) {
        this.chatBox.nativeElement.scrollTop =
          this.chatBox.nativeElement.scrollHeight;
      }
    }, 50);
  }

  enviarMensaje() {
    if (!this.mensajeChat.trim() || this.cargando) return;

    const texto = this.mensajeChat.trim();

    this.mensajes.push({
      rol: 'user',
      texto
    });

    this.bajarChat();

    this.mensajeChat = '';
    this.cargando = true;

    this.presupuestoService.chat(texto).subscribe({
      next: (respuesta) => {
        this.mensajes.push({
          rol: 'bot',
          texto: respuesta
        });

        this.cargando = false;
        this.bajarChat();
      },
      error: () => {
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
}