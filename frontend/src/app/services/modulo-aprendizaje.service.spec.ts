import { TestBed } from '@angular/core/testing';

import { ModuloAprendizajeService } from './modulo-aprendizaje.service';

describe('ModuloAprendizajeService', () => {
  let service: ModuloAprendizajeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ModuloAprendizajeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
