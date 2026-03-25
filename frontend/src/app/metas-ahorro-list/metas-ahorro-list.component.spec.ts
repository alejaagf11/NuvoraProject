import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MetasAhorroListComponent } from './metas-ahorro-list.component';

describe('MetasAhorroListComponent', () => {
  let component: MetasAhorroListComponent;
  let fixture: ComponentFixture<MetasAhorroListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MetasAhorroListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MetasAhorroListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
