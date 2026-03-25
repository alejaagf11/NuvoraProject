import { TestBed } from '@angular/core/testing';

import { MetasAhorroService } from './metas-ahorro.service';

describe('MetasAhorroService', () => {
  let service: MetasAhorroService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MetasAhorroService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
