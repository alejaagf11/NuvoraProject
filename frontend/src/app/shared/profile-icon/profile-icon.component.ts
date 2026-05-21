import { Component, OnInit } from '@angular/core';
import { ProfilePhotoService } from '../../services/profile-photo.service';

@Component({
  selector: 'app-profile-icon',
  standalone: false,
  templateUrl: './profile-icon.component.html',
  styleUrls: ['./profile-icon.component.css']
})
export class ProfileIconComponent implements OnInit {
  fotoPerfil: string | null = null;

  constructor(private profilePhotoService: ProfilePhotoService) {}

  ngOnInit(): void {
    this.fotoPerfil = this.profilePhotoService.getPhoto();
  }
}