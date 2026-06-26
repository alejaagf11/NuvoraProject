import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileIconMobileComponent } from './profile-icon-mobile.component';

describe('ProfileIconMobileComponent', () => {
  let component: ProfileIconMobileComponent;
  let fixture: ComponentFixture<ProfileIconMobileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProfileIconMobileComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileIconMobileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
