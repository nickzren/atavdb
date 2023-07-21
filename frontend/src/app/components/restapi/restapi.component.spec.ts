import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { RestAPIComponent } from './restapi.component';

describe('RestAPIComponent', () => {
  let component: RestAPIComponent;
  let fixture: ComponentFixture<RestAPIComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ RestAPIComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RestAPIComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
