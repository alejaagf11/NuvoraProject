import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { MetasAhorroFormMobileComponent } from './metas-ahorro-form-mobile.component';

describe('MetasAhorroFormMobileComponent', () => {
  let component: MetasAhorroFormMobileComponent;
  let fixture: ComponentFixture<MetasAhorroFormMobileComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ MetasAhorroFormMobileComponent ],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(MetasAhorroFormMobileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
