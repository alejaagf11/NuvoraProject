import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MetasAhorroFormComponent } from './metas-ahorro-form.component';

describe('MetasAhorroFormComponent', () => {
  let component: MetasAhorroFormComponent;
  let fixture: ComponentFixture<MetasAhorroFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MetasAhorroFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MetasAhorroFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
