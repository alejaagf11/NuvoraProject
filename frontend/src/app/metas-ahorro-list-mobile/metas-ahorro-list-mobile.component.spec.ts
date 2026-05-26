import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { MetasAhorroListMobileComponent } from './metas-ahorro-list-mobile.component';

describe('MetasAhorroListMobileComponent', () => {
  let component: MetasAhorroListMobileComponent;
  let fixture: ComponentFixture<MetasAhorroListMobileComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ MetasAhorroListMobileComponent ],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(MetasAhorroListMobileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
