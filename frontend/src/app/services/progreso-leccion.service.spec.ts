import { TestBed } from '@angular/core/testing';

import { ProgresoLeccionService } from './progreso-leccion.service';

describe('ProgresoLeccionService', () => {
  let service: ProgresoLeccionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProgresoLeccionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
