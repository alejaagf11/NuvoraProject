import { isPlatformBrowser } from '@angular/common';
import { Inject, Injectable, PLATFORM_ID } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ProfilePhotoService {
  private readonly storageKey = 'nuvoraProfilePhoto';

  constructor(@Inject(PLATFORM_ID) private platformId: object) {}

  getPhoto(): string | null {
    if (!isPlatformBrowser(this.platformId)) {
      return null;
    }

    return localStorage.getItem(this.storageKey);
  }

  savePhoto(photo: string): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem(this.storageKey, photo);
    }
  }

  removePhoto(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem(this.storageKey);
    }
  }
}
