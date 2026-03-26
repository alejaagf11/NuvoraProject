import { Component, OnInit } from '@angular/core';
import { MetasAhorroService } from '../services/metas-ahorro.service';
import { MetasAhorro } from '../models/metas_ahorro';


@Component({
  selector: 'app-metas-ahorro-list',
  standalone: false,
  templateUrl: './metas-ahorro-list.component.html',
  styleUrl: './metas-ahorro-list.component.css'
})
export class MetasAhorroListComponent {

  metas: MetasAhorro[] = [];

  constructor(private metasService: MetasAhorroService) { }

  ngOnInit(): void {
    this.loadMetas();
  }

  loadMetas() {
    this.metasService.getAll().subscribe(data => this.metas = data);
  }

  deleteMeta(id: number) {
    if(confirm("¿Seguro que quieres eliminar esta meta?")) {
      this.metasService.delete(id).subscribe(() => this.loadMetas());
    }
  }
}