import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { Capacitor } from '@capacitor/core';
import { filter } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
    standalone: false,
  styleUrls: ['./app.component.css'], // CORRECTO
})
export class AppComponent {
  title = 'frontend';

  constructor(private router: Router) {
    if (!Capacitor.isNativePlatform()) {
      return;
    }

    this.router.events
      .pipe(filter((event): event is NavigationEnd => event instanceof NavigationEnd))
      .subscribe((event) => {
        if (event.urlAfterRedirects === '/usuario-login') {
          this.router.navigateByUrl('/mobile/usuario-login');
        }
      });
  }
}
