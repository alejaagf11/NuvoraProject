import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminLeccionesComponent } from './admin-lecciones.component';

describe('AdminLeccionesComponent', () => {
  let component: AdminLeccionesComponent;
  let fixture: ComponentFixture<AdminLeccionesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminLeccionesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminLeccionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
