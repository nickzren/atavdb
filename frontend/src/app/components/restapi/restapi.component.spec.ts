import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RestapiComponent } from './restapi.component';

describe('RestapiComponent', () => {
  let component: RestapiComponent;
  let fixture: ComponentFixture<RestapiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RestapiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RestapiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
